package com.service;

import com.dto.UserDTO;
import com.model.User;

public interface UserService {

    User findUserByUserName(String username);

    String signUp(UserDTO userDTO);
}
