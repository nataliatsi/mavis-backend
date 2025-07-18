package com.nataliatsi.mavis.mapper;

import com.nataliatsi.mavis.dto.AddressRequestDTO;
import com.nataliatsi.mavis.dto.UserRequestDTO;
import com.nataliatsi.mavis.entities.Address;
import com.nataliatsi.mavis.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(UserRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    Address toAddress(AddressRequestDTO dto);
}
