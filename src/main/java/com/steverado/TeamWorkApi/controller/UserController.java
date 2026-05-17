package com.steverado.TeamWorkApi.controller;

import com.steverado.TeamWorkApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
}
