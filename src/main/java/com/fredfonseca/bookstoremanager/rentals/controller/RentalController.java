package com.fredfonseca.bookstoremanager.rentals.controller;

import com.fredfonseca.bookstoremanager.rentals.dto.RentalRequestDTO;
import com.fredfonseca.bookstoremanager.rentals.dto.RentalResponseDTO;
import com.fredfonseca.bookstoremanager.rentals.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/rentals")
public class RentalController implements RentalControllerDocs{

    private RentalService rentalService;

    @Autowired
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RentalResponseDTO create(@RequestBody @Valid RentalRequestDTO rentalRequestDTO) {
        return rentalService.create(rentalRequestDTO);
    }

    @GetMapping("/{id}")
    public RentalResponseDTO findById(@PathVariable Long id) {
        return rentalService.findById(id);
    }

    @GetMapping
    public List<RentalResponseDTO> findAll() {
        return rentalService.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        rentalService.delete(id);
    }

    @PutMapping("/{id}")
    public RentalResponseDTO update(@PathVariable Long id, @RequestBody @Valid RentalRequestDTO rentalRequestDTO) {
        return rentalService.update(id, rentalRequestDTO);
    }
}
