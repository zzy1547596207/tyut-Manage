package com.tyut.managesystem.controller;

import com.tyut.managesystem.dto.LoginDTO;
import com.tyut.managesystem.dto.LoginResult;
import com.tyut.managesystem.dto.Result;
import com.tyut.managesystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<LoginResult> login(@Valid @RequestBody LoginDTO dto) {
        try {
            return Result.ok(userService.login(dto));
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }
}