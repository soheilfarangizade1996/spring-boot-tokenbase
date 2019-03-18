package com.service;

import com.dto.UserDTO;
import com.model.User;

import java.util.Map;

public interface UserService {

    User findUserByUserName(String username);

    Map<User, String> signUp(UserDTO userDTO);
}
