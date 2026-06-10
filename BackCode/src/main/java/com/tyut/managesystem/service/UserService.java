package com.tyut.managesystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tyut.managesystem.dto.LoginDTO;
import com.tyut.managesystem.dto.LoginResult;
import com.tyut.managesystem.entity.User;

public interface UserService extends IService<User> {
    LoginResult login(LoginDTO dto);
}