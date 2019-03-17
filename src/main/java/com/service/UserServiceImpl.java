package com.service;

import com.dto.UserDTO;
import com.model.User;
import com.model.enu.RoleEnum;
import com.repository.UserRepository;
import com.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;



    @Override
    public User findUserByUserName(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    public String signUp(UserDTO userDTO) {
        User user = validateUserDTO(userDTO);
        if (user != null){
            String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
            return token;
        }
        return null;
    }

    private boolean checkExistUser(String username) {
        return userRepository.existsUserByUsername(username);
    }

    private User validateUserDTO(UserDTO userDTO) {
        if (userDTO == null || userDTO.getUsername() == null || userDTO.getPassword() == null)
            return null;

        boolean checkExist =  checkExistUser(userDTO.getUsername());
        if (checkExist){
            return null;
        }

        User user = new User();
        List<RoleEnum> roleEnums = Arrays.asList(RoleEnum.ROLE_USER);
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setRoles(roleEnums);
        user = userRepository.save(user);
        return user;
    }


}
