package com.Controller;

import com.config.PasswordEncoder;
import com.dto.UserDTO;
import com.dto.UserResponseDTO;
import com.model.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public UserResponseDTO singUp(@RequestBody UserDTO userDTO) {
        return userService.signUp(userDTO);
    }


    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public UserResponseDTO signin(@RequestBody UserDTO userDTO) {
        return userService.signIn(userDTO);
    }


    @RequestMapping(value = "/whoMi", method = RequestMethod.POST)
    public UserResponseDTO getUser(HttpServletRequest request) {
        return userService.getUserByToken(request);
    }

    @RequestMapping(value = "fetchAllUser", method = RequestMethod.POST)
    public List<User> fetchAllUser(HttpServletRequest request) {
        return userService.fetchAllUser(request);
    }


    @RequestMapping(value = "test", method = RequestMethod.POST)
    public String test(@RequestBody UserDTO userDTO) {
        String pass = passwordEncoder.encodePassSHA1(userDTO.getPassword());
        System.out.println(pass);
        return pass;
    }

}
