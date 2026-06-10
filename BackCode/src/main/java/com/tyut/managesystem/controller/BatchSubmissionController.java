package com.tyut.managesystem.controller;

import com.tyut.managesystem.dto.Result;
import com.tyut.managesystem.entity.BatchSubmission;
import com.tyut.managesystem.service.BatchSubmissionService;
import com.tyut.managesystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/submission")
public class BatchSubmissionController {

    @Autowired
    private BatchSubmissionService submissionService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/submit")
    public Result<?> submit(@RequestHeader("Authorization") String token,
                             @RequestBody BatchSubmission submission) {
        String t = token.replace("Bearer ", "");
        submissionService.submit(jwtUtil.getUserId(t), submission);
        return Result.ok();
    }

    @PostMapping("/revoke/{id}")
    public Result<?> revoke(@PathVariable Long id) {
        submissionService.revoke(id);
        return Result.ok();
    }

    @GetMapping("/my")
    public Result<List<BatchSubmission>> mySubmissions(@RequestHeader("Authorization") String token) {
        String t = token.replace("Bearer ", "");
        return Result.ok(submissionService.listByUser(jwtUtil.getUserId(t)));
    }

    @GetMapping("/detail")
    public Result<BatchSubmission> detail(@RequestParam Long batchId, @RequestParam Long userId) {
        return Result.ok(submissionService.getByBatchAndUser(batchId, userId));
    }
}