package com.tyut.managesystem.controller;

import com.tyut.managesystem.dto.Result;
import com.tyut.managesystem.entity.User;
import com.tyut.managesystem.service.UserService;
import com.tyut.managesystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private DataSource dataSource;

    @GetMapping("/users")
    public Result<?> listUsers() {
        List<User> users = userService.list();
        return Result.ok(users);
    }

    @PutMapping("/users/{id}")
    public Result<?> updateUser(@PathVariable Long id, @RequestBody Map<String, String> body) {
        User user = userService.getById(id);
        if (user == null) return Result.fail("用户不存在");
        if (body.containsKey("nickname")) user.setNickname(body.get("nickname"));
        if (body.containsKey("department")) user.setDepartment(body.get("department"));
        if (body.containsKey("employeeNo")) user.setEmployeeNo(body.get("employeeNo"));
        userService.updateById(user);
        return Result.ok(user);
    }

    @PostMapping("/users")
    public Result<?> createUser(@RequestBody Map<String, String> body) {
        User user = new User();
        user.setUsername(body.get("username"));
        user.setPassword(body.get("password"));
        user.setNickname(body.get("nickname"));
        user.setRole(body.get("role"));
        user.setDepartment(body.getOrDefault("department", ""));
        user.setEmployeeNo(body.getOrDefault("employeeNo", ""));
        userService.save(user);
        return Result.ok(user);
    }

    @PostMapping("/db/alter")
    public Result<?> alterTable(@RequestBody Map<String, String> body) {
        String sql = body.get("sql");
        if (sql == null || sql.isEmpty()) return Result.fail("sql required");
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            return Result.ok("ok");
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }
}