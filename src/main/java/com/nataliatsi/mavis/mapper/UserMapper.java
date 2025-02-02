package com.nataliatsi.mavis.mapper;

import com.nataliatsi.mavis.dto.UserRegisterDto;
import com.nataliatsi.mavis.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", ignore = true)
    User toUser(UserRegisterDto userRegister);
}
