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

    @Autowired
    public PublisherService(PublisherRepository publisherRepository, BookRepository bookRepository) {
        this.publisherRepository = publisherRepository;
        this.bookRepository = bookRepository;
    }

    public PublisherResponseDTO create(PublisherRequestDTO publisherRequestDTO) {
        verifyIfExists(publisherRequestDTO.getName());

        Publisher publisherToCreate = publisherMapper.toModel(publisherRequestDTO);
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
        Publisher publisherToDelete = verifyAndGetPublisher(id);
        if(bookRepository.findByPublisher(publisherToDelete).isPresent()) throw new DeleteDeniedException();
        publisherRepository.deleteById(id);
    }

    public PublisherResponseDTO update(Long id, PublisherRequestDTO publisherToUpdateDTO) {
        Publisher foundPublisher = verifyAndGetPublisher(id);
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

    private Publisher verifyAndGetPublisher(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException(id));
    }

    public Publisher verifyAndGetIfExists(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException(id));
    }
}
