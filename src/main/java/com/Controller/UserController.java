package com.Controller;

import com.dto.UserDTO;
import com.model.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {


    @Autowired
    private UserService userService;

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public Map<User, String> singUp(@RequestBody UserDTO userDTO){
        return userService.signUp(userDTO);
    }


    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public String signin(@RequestBody UserDTO userDTO){
        return userService.signIn(userDTO);
    }
}
