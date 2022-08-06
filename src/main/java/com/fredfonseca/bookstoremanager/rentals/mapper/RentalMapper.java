package com.fredfonseca.bookstoremanager.rentals.mapper;

import com.fredfonseca.bookstoremanager.rentals.dto.RentalRequestDTO;
import com.fredfonseca.bookstoremanager.rentals.dto.RentalResponseDTO;
import com.fredfonseca.bookstoremanager.rentals.entity.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RentalMapper {

    RentalMapper INSTANCE = Mappers.getMapper(RentalMapper.class);

    Rental toModel(RentalRequestDTO rentalRequestDTO);

    Rental toModel(RentalResponseDTO rentalResponseDTO);

    RentalResponseDTO toDTO(Rental rental);
}
