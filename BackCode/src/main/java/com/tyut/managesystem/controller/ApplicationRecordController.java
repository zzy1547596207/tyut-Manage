package com.tyut.managesystem.controller;

import com.tyut.managesystem.dto.Result;
import com.tyut.managesystem.entity.ApplicationRecord;
import com.tyut.managesystem.entity.User;
import com.tyut.managesystem.service.ApplicationRecordService;
import com.tyut.managesystem.service.UserService;
import com.tyut.managesystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/review")
public class ApplicationRecordController {

    @Autowired
    private ApplicationRecordService recordService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/pending")
    public Result<List<ApplicationRecord>> listPending(@RequestHeader("Authorization") String token) {
        String t = token.replace("Bearer ", "");
        String role = jwtUtil.getRole(t);
        Long userId = jwtUtil.getUserId(t);

        String department = null;
        if ("DEPARTMENT".equals(role)) {
            User user = userService.getById(userId);
            department = user != null ? user.getDepartment() : "";
        }

        return Result.ok(recordService.listPending(department));
    }

    @PostMapping("/approve/{id}")
    public Result<?> approve(@PathVariable Long id,
                              @RequestHeader("Authorization") String token) {
        String t = token.replace("Bearer ", "");
        recordService.approve(id, jwtUtil.getUserId(t));
        return Result.ok();
    }

    @PostMapping("/reject/{id}")
    public Result<?> reject(@PathVariable Long id,
                             @RequestBody Map<String, String> body,
                             @RequestHeader("Authorization") String token) {
        String t = token.replace("Bearer ", "");
        recordService.reject(id, jwtUtil.getUserId(t), body.get("reason"));
        return Result.ok();
    }
}