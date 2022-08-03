package com.fredfonseca.bookstoremanager.publishers.service;

import com.fredfonseca.bookstoremanager.author.exception.AuthorNotFoundException;
import com.fredfonseca.bookstoremanager.publishers.dto.PublisherDTO;
import com.fredfonseca.bookstoremanager.publishers.entity.Publisher;
import com.fredfonseca.bookstoremanager.publishers.exception.PublisherAlreadyExistsException;
import com.fredfonseca.bookstoremanager.publishers.exception.PublisherNotFoundException;
import com.fredfonseca.bookstoremanager.publishers.mapper.PublisherMapper;
import com.fredfonseca.bookstoremanager.publishers.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PublisherService {

    private final static PublisherMapper publisherMapper = PublisherMapper.INSTANCE;

    private PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public PublisherDTO create(PublisherDTO publisherDTO) {
        verifyIfExists(publisherDTO.getName());

        Publisher publisherToCreate = publisherMapper.toModel(publisherDTO);
        Publisher createdPublisher = publisherRepository.save(publisherToCreate);
        return publisherMapper.toDTO(createdPublisher);
    }

    public PublisherDTO findById(Long id) {
        return publisherRepository.findById(id)
                .map(publisherMapper::toDTO)
                .orElseThrow(() -> new PublisherNotFoundException(id));
    }

    public List<PublisherDTO> findAll() {
        return publisherRepository.findAll()
                .stream()
                .map(publisherMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        verifyAndGetAuthor(id);
        publisherRepository.deleteById(id);
    }

    public PublisherDTO update(Long id, PublisherDTO publisherToUpdateDTO) {
        Publisher foundPublisher = verifyAndGetAuthor(id);
        publisherToUpdateDTO.setId(foundPublisher.getId());

        verifyIfExists(publisherToUpdateDTO.getId(), publisherToUpdateDTO.getName());

        Publisher publisherToUpdate = publisherMapper.toModel(publisherToUpdateDTO);
        Publisher updatedPublisher = publisherRepository.save(publisherToUpdate);
        return publisherMapper.toDTO(updatedPublisher);
    }

    private void verifyIfExists(Long id, String name) {
        publisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException(id));

        Optional<Publisher> samePublisher = publisherRepository
                .findByName(name);

        if(samePublisher.isPresent() && samePublisher.get().getId() != id)
            throw new PublisherAlreadyExistsException(name);
    }

    private void verifyIfExists(String name) {
        Optional<Publisher> duplicatedPublisher = publisherRepository
                .findByName(name);
        if(duplicatedPublisher.isPresent()) throw new PublisherAlreadyExistsException(name);
    }

    private Publisher verifyAndGetAuthor(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }
}
