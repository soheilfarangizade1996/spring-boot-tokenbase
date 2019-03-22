package com.service;

import com.dto.UserDTO;
import com.dto.UserResponseDTO;
import com.model.User;
import com.model.enu.RoleEnum;
import com.repository.UserRepository;
import com.security.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends BaseService implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;


    private static final Map<String, List<User>> cacheUser = new ConcurrentHashMap<>();

    private static final String CACHE_USER = "CACHE_USER";




    @Override
    @Scheduled(fixedRate = 2 * 60 * 1000)
    public void cacheUserFromDatabase() {
        logger.info("size CacheMapUser {}", (long) cacheUser.entrySet().size());
        if (cacheUser.containsKey(CACHE_USER)) {
            cacheUser.remove(CACHE_USER);
            cacheUser.put(CACHE_USER, userRepository.findAll());
        } else {
            List<User> userList = userRepository.findAll();
            cacheUser.put(CACHE_USER, userList);
        }
    }

    @Override
    public User findUserByUserName(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    public UserResponseDTO signUp(UserDTO userDTO) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        String token = null;
        User user = validateUserDTO(userDTO);
        if (user.getStatusCode() == 200) {
            token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
            userResponseDTO.setStatusCode(200);
            userResponseDTO.setMessage("Ok");
            userResponseDTO.setToken(token);
            return userResponseDTO;
        }
        userResponseDTO.setStatusCode(user.getStatusCode());
        userResponseDTO.setMessage(user.getMessage());
        userResponseDTO.setToken(token);
        return userResponseDTO;
    }


    @Override
    public UserResponseDTO signIn(UserDTO userDTO) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));
        cacheUser.forEach((key, value) -> System.out.println(value));
        User user = userRepository.findByUsername(userDTO.getUsername());
        if (user != null) {
            String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
            userResponseDTO.setStatusCode(200);
            userResponseDTO.setMessage("Ok");
            userResponseDTO.setToken(token);
            userResponseDTO.setUsername(user.getUsername());
            return userResponseDTO;
        }
        userResponseDTO.setStatusCode(404);
        userResponseDTO.setMessage("Not Found");
        return userResponseDTO;
    }


    @Override
    public UserResponseDTO getUserByToken(HttpServletRequest request) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        User user = userRepository.findByUsername(jwtTokenProvider.getUserNameWithToken(jwtTokenProvider.resolveToken(request)));
        userResponseDTO.setToken(jwtTokenProvider.resolveToken(request));
        userResponseDTO.setStatusCode(200);
        userResponseDTO.setMessage("Ok");
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setMobile(user.getMobile());
        return userResponseDTO;
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
        List<RoleEnum> roleEnums = new ArrayList<>();
        roleEnums.add(RoleEnum.ROLE_ADMIN);
        roleEnums.add(RoleEnum.ROLE_USER);
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setRoles(roleEnums);
        user.setMobile(userDTO.getMobile());
        user = userRepository.save(user);
        user.setStatusCode(200);
        user.setMessage("User Saved...");
        System.out.println(user.getPassword());
        return user;
    }

    @Override
    public List<User> fetchAllUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token.equals("")) {
            return null;
        }
        List<User> userList = new ArrayList<>();
        boolean checkToken = jwtTokenProvider.validateToken(token);
        if (checkToken) {
            userList = userRepository.findAll();
        }
        return userList;
    }
}
