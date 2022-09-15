package com.softserve.edu.sporthubujp.service;

import java.util.InvalidPropertiesFormatException;

import com.softserve.edu.sporthubujp.dto.UserDTO;
import com.softserve.edu.sporthubujp.dto.UserSavePasswordDTO;
import com.softserve.edu.sporthubujp.dto.UserSaveProfileDTO;
import com.softserve.edu.sporthubujp.entity.User;

public interface UserService {

    String signUpUser(UserDTO userDTO);

    int enableUser(String email);

    User findUserByEmail(String email);

    UserDTO updateUser(User oldUser, UserSaveProfileDTO newUser);

    UserDTO updatePassword(User oldUser, UserSavePasswordDTO newUser) throws InvalidPropertiesFormatException;
}