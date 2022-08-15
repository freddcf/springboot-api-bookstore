package com.fredfonseca.bookstoremanager.publishers.mapper;

import com.fredfonseca.bookstoremanager.publishers.dto.PublisherRequestDTO;
import com.fredfonseca.bookstoremanager.publishers.dto.PublisherResponseDTO;
import com.fredfonseca.bookstoremanager.publishers.entity.Publisher;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PublisherMapper {

    PublisherMapper INSTANCE = Mappers.getMapper(PublisherMapper.class);

    Publisher toModel(PublisherRequestDTO publisherRequestDTO);

    PublisherResponseDTO toDTO(Publisher publisher);
}
