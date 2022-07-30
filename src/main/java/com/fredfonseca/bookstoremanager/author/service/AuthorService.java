package com.fredfonseca.bookstoremanager.author.service;

import com.fredfonseca.bookstoremanager.author.dto.AuthorDTO;
import com.fredfonseca.bookstoremanager.author.entity.Author;
import com.fredfonseca.bookstoremanager.author.exception.AuthorAlreadyExistsException;
import com.fredfonseca.bookstoremanager.author.exception.AuthorNotFoundException;
import com.fredfonseca.bookstoremanager.author.mapper.AuthorMapper;
import com.fredfonseca.bookstoremanager.author.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final static AuthorMapper authorMapper = AuthorMapper.INSTANCE;

    private AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public AuthorDTO create(AuthorDTO authorDTO) {
        verifyExists(authorDTO.getName());
        Author authorToCreate = authorMapper.toModel(authorDTO);
        Author createdAuthor = authorRepository.save(authorToCreate);
        return authorMapper.toDTO(createdAuthor);
    }

    public AuthorDTO findById(Long id) {
        Author foundAuthor = verifyAndGetAuthor(id);
        return authorMapper.toDTO(foundAuthor);
    }

    private void verifyExists(String authorName) {
        authorRepository.findByName(authorName)
                .ifPresent(author -> { throw new AuthorAlreadyExistsException(authorName); });
    }

    public List<AuthorDTO> findAll() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        verifyAndGetAuthor(id);
        authorRepository.deleteById(id);
    }

    public AuthorDTO update(AuthorDTO authorDTO) {
        Author authorToCreate = authorMapper.toModel(authorDTO);
        Author createdAuthor = authorRepository.save(authorToCreate);
        return authorMapper.toDTO(createdAuthor);
    }

    private Author verifyAndGetAuthor(Long id) {
        Author foundAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
        return foundAuthor;
    }
}
