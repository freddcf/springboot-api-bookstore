package com.fredfonseca.bookstoremanager.publishers.controller;

import com.fredfonseca.bookstoremanager.publishers.dto.PublisherRequestDTO;
import com.fredfonseca.bookstoremanager.publishers.dto.PublisherResponseDTO;
import com.fredfonseca.bookstoremanager.publishers.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/publishers")
@CrossOrigin(origins = "*")
public class PublisherController implements PublisherControllerDocs{

    private PublisherService publisherService;

    @Autowired
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PublisherResponseDTO create(@RequestBody @Valid PublisherRequestDTO publisherRequestDTO) {
        return publisherService.create(publisherRequestDTO);
    }

    @GetMapping("/{id}")
    public PublisherResponseDTO findById(@PathVariable Long id) {
        return publisherService.findById(id);
    }

    @GetMapping
    public List<PublisherResponseDTO> findAll() {
        return publisherService.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        publisherService.delete(id);
    }

    @PutMapping("/{id}")
    public PublisherResponseDTO update(@PathVariable Long id, @RequestBody @Valid PublisherRequestDTO publisherRequestDTO) {
        return publisherService.update(id, publisherRequestDTO);
    }
}