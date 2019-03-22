package com.service;

import com.dto.UserDTO;
import com.dto.UserResponseDTO;
import com.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface UserService {

    User findUserByUserName(String username);

    UserResponseDTO signUp(UserDTO userDTO);

    UserResponseDTO signIn(UserDTO userDTO);

    UserResponseDTO getUserByToken(HttpServletRequest request);

    List<User> fetchAllUser(HttpServletRequest request);

    void cacheUserFromDatabase();

}
