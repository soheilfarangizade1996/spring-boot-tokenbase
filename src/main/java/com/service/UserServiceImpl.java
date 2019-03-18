package com.service;

import com.dto.UserDTO;
import com.model.User;
import com.model.enu.RoleEnum;
import com.repository.UserRepository;
import com.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public User findUserByUserName(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    public Map<User, String> signUp(UserDTO userDTO) {
        Map<User, String> map = new HashMap<>();
        User user = validateUserDTO(userDTO);
        if (user.getStatusCode() == 200) {
            String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
            map.put(user, "token : " + token);
            return map;
        }
        map.put(user, "token : " + null);
        return map;
    }


    @Override
    public String signIn(UserDTO userDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));
        User user = userRepository.findByUsername(userDTO.getUsername());
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
        User userResult = new User();
        if (userDTO == null || userDTO.getUsername() == null || userDTO.getPassword() == null) {
            userResult.setMessage("User Is Null");
            userResult.setStatusCode(401);
            return userResult;
        }

        boolean checkExist = checkExistUser(userDTO.getUsername());
        if (checkExist) {
            userResult.setMessage("User Exist");
            userResult.setStatusCode(302);
            return userResult;
        }

        User user = new User();
        List<RoleEnum> roleEnums = Arrays.asList(RoleEnum.ROLE_USER);
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setRoles(roleEnums);
        user = userRepository.save(user);
        user.setStatusCode(200);
        user.setMessage("User Saved...");
        return user;
    }


}
