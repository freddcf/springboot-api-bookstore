package com.fredfonseca.bookstoremanager.users.mapper;

import com.fredfonseca.bookstoremanager.users.dto.AdminDTO;
import com.fredfonseca.bookstoremanager.users.dto.UserDTO;
import com.fredfonseca.bookstoremanager.users.dto.UserResponseDTO;
import com.fredfonseca.bookstoremanager.users.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    Users toModel(UserDTO userDTO);
    Users toModel(AdminDTO adminDTO);

    UserResponseDTO toDTO(Users users);
}
