package com.nataliatsi.mavis.mapper;

import com.nataliatsi.mavis.dto.UserRegisterDto;
import com.nataliatsi.mavis.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    User toUser(UserRegisterDto userRegister);
}
