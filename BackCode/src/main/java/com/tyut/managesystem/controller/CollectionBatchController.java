package com.tyut.managesystem.controller;

import com.tyut.managesystem.dto.Result;
import com.tyut.managesystem.entity.CollectionBatch;
import com.tyut.managesystem.entity.User;
import com.tyut.managesystem.service.CollectionBatchService;
import com.tyut.managesystem.service.UserService;
import com.tyut.managesystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/batch")
public class CollectionBatchController {

    @Autowired
    private CollectionBatchService batchService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/list")
    public Result<List<CollectionBatch>> list(@RequestHeader("Authorization") String token) {
        String t = token.replace("Bearer ", "");
        String role = jwtUtil.getRole(t);
        Long userId = jwtUtil.getUserId(t);

        if ("ADMIN".equals(role)) {
            return Result.ok(batchService.listAll());
        } else {
            User user = userService.getById(userId);
            String dept = user != null ? user.getDepartment() : "";
            return Result.ok(batchService.listByDepartment(dept));
        }
    }

    @PostMapping("/publish")
    public Result<?> publish(@RequestHeader("Authorization") String token,
                              @RequestBody CollectionBatch batch) {
        String t = token.replace("Bearer ", "");
        Long userId = jwtUtil.getUserId(t);
        User user = userService.getById(userId);
        if (user != null && user.getDepartment() != null) {
            batch.setDepartment(user.getDepartment());
        }
        batchService.publish(batch, userId);
        return Result.ok();
    }
}