package com.tyut.managesystem.controller;

import com.tyut.managesystem.dto.Result;
import com.tyut.managesystem.entity.CounselorProfile;
import com.tyut.managesystem.entity.User;
import com.tyut.managesystem.service.CounselorProfileService;
import com.tyut.managesystem.service.UserService;
import com.tyut.managesystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/counselors")
public class CounselorListController {

    @Autowired
    private CounselorProfileService profileService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/list")
    public Result<List<CounselorProfile>> listAll(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return Result.ok(profileService.list());
        }
        String t = token.replace("Bearer ", "");
        String role = jwtUtil.getRole(t);
        Long userId = jwtUtil.getUserId(t);

        List<CounselorProfile> all = profileService.list();

        if ("ADMIN".equals(role)) {
            // 管理员：看全校
            return Result.ok(all);
        } else if ("DEPARTMENT".equals(role)) {
            // 学院：只看本学院
            User user = userService.getById(userId);
            String dept = user != null ? user.getDepartment() : "";
            return Result.ok(all.stream()
                    .filter(p -> dept.equals(p.getDepartment()))
                    .collect(Collectors.toList()));
        } else {
            // 辅导员：只看自己
            return Result.ok(all.stream()
                    .filter(p -> userId.equals(p.getUserId()))
                    .collect(Collectors.toList()));
        }
    }
}