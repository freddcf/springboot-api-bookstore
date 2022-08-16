package com.fredfonseca.bookstoremanager.rentals.controller;

import com.fredfonseca.bookstoremanager.rentals.dto.RentalRequestDTO;
import com.fredfonseca.bookstoremanager.rentals.dto.RentalRequestUpdateDTO;
import com.fredfonseca.bookstoremanager.rentals.dto.RentalResponseDTO;
import com.fredfonseca.bookstoremanager.rentals.service.RentalService;
import com.fredfonseca.bookstoremanager.users.dto.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public RentalResponseDTO create(@AuthenticationPrincipal AuthenticatedUser authenticatedUser, @RequestBody @Valid RentalRequestDTO rentalRequestDTO) {
        return rentalService.create(authenticatedUser, rentalRequestDTO);
    }

    @GetMapping("/{id}")
    public RentalResponseDTO findById(@PathVariable Long id, @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return rentalService.findById(id, authenticatedUser);
    }

    @GetMapping
    public Page<RentalResponseDTO> findAll(@AuthenticationPrincipal AuthenticatedUser authenticatedUser, Pageable pageable) {
        return rentalService.findAll(authenticatedUser, pageable);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        rentalService.delete(id, authenticatedUser);
    }

    @PutMapping("/{id}")
    public RentalResponseDTO update(@PathVariable Long id, @AuthenticationPrincipal AuthenticatedUser authenticatedUser, @RequestBody @Valid RentalRequestUpdateDTO rentalRequestUpdateDTO) {
        return rentalService.update(id, authenticatedUser, rentalRequestUpdateDTO);
    }
}
