package com.softserve.edu.sporthubujp.mapper;

import com.softserve.edu.sporthubujp.dto.UserDTO;
import com.softserve.edu.sporthubujp.dto.UserSaveProfileDTO;
import com.softserve.edu.sporthubujp.entity.User;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {
    User dtoToEntity(UserDTO userDTO);
    UserDTO entityToDto(User user);
    User updateUser(@MappingTarget User userFromDb,  UserSaveProfileDTO newUser);
    UserDTO dtoToSaveDto(UserSaveProfileDTO userSaveProfileDTO);
    UserSaveProfileDTO saveDtoToDto(UserDTO userDTO);
}