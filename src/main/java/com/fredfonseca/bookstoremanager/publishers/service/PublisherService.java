package com.fredfonseca.bookstoremanager.publishers.service;

import com.fredfonseca.bookstoremanager.books.repository.BookRepository;
import com.fredfonseca.bookstoremanager.publishers.dto.PublisherRequestDTO;
import com.fredfonseca.bookstoremanager.publishers.dto.PublisherResponseDTO;
import com.fredfonseca.bookstoremanager.publishers.entity.Publisher;
import com.fredfonseca.bookstoremanager.publishers.exception.DeleteDeniedException;
import com.fredfonseca.bookstoremanager.publishers.exception.PublisherAlreadyExistsException;
import com.fredfonseca.bookstoremanager.publishers.exception.PublisherNotFoundException;
import com.fredfonseca.bookstoremanager.publishers.mapper.PublisherMapper;
import com.fredfonseca.bookstoremanager.publishers.repository.PublisherRepository;
import com.fredfonseca.bookstoremanager.utils.StringPattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PublisherService {

    private final static PublisherMapper publisherMapper = PublisherMapper.INSTANCE;

    private PublisherRepository publisherRepository;

    private BookRepository bookRepository;

    private StringPattern stringPattern;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository, BookRepository bookRepository, StringPattern stringPattern) {
        this.publisherRepository = publisherRepository;
        this.bookRepository = bookRepository;
        this.stringPattern = stringPattern;
    }

    public PublisherResponseDTO create(PublisherRequestDTO publisherRequestDTO) {
        verifyIfExists(publisherRequestDTO.getName());

        Publisher publisherToCreate = publisherMapper.toModel(publisherRequestDTO);
        publisherToCreate.setName(stringPattern.basicPattern(publisherToCreate.getName()));
        publisherToCreate.setCity(stringPattern.onlyWordsPattern(publisherToCreate.getCity()));
        Publisher createdPublisher = publisherRepository.save(publisherToCreate);
        return publisherMapper.toDTO(createdPublisher);
    }

    public PublisherResponseDTO findById(Long id) {
        return publisherRepository.findById(id)
                .map(publisherMapper::toDTO)
                .orElseThrow(() -> new PublisherNotFoundException(id));
    }

    public Page<PublisherResponseDTO> findAll(Pageable pageable) {
        return publisherRepository.findAll(pageable)
                .map(publisherMapper::toDTO);
    }

    public void delete(Long id) {
        Publisher publisherToDelete = verifyAndGetIfExists(id);
        if (!bookRepository.findByPublisher(publisherToDelete).isEmpty()) {
            throw new DeleteDeniedException();
        }
        publisherRepository.deleteById(id);
    }

    public PublisherResponseDTO update(Long id, PublisherRequestDTO publisherToUpdateDTO) {
        Publisher foundPublisher = verifyAndGetIfExists(id);
        publisherToUpdateDTO.setId(foundPublisher.getId());
        verifyIfExists(publisherToUpdateDTO.getId(), publisherToUpdateDTO.getName());

        Publisher publisherToUpdate = publisherMapper.toModel(publisherToUpdateDTO);
        publisherToUpdate.setName(stringPattern.basicPattern(publisherToUpdate.getName()));
        publisherToUpdate.setCity(stringPattern.onlyWordsPattern(publisherToUpdate.getCity()));
        Publisher updatedPublisher = publisherRepository.save(publisherToUpdate);
        return publisherMapper.toDTO(updatedPublisher);
    }

    private void verifyIfExists(Long id, String name) {
        publisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException(id));

        Optional<Publisher> samePublisher = publisherRepository
                .findByName(name);

        if (samePublisher.isPresent() && samePublisher.get().getId() != id) {
            throw new PublisherAlreadyExistsException(name);
        }
    }

    private void verifyIfExists(String name) {
        Optional<Publisher> duplicatedPublisher = publisherRepository
                .findByName(name);
        if (duplicatedPublisher.isPresent()) {
            throw new PublisherAlreadyExistsException(name);
        }
    }

    public Publisher verifyAndGetIfExists(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException(id));
    }
}
