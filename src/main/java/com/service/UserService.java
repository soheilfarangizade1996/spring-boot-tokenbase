package com.service;

import com.dto.UserDTO;
import com.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface UserService {

    User findUserByUserName(String username);

    Map<User, String> signUp(UserDTO userDTO);

    String signIn(UserDTO userDTO);

    User getUserByToken(HttpServletRequest request);
}
