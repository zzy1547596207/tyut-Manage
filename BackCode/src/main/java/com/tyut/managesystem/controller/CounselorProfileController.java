package com.tyut.managesystem.controller;

import com.tyut.managesystem.dto.Result;
import com.tyut.managesystem.entity.CounselorProfile;
import com.tyut.managesystem.service.CounselorProfileService;
import com.tyut.managesystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class CounselorProfileController {

    @Autowired
    private CounselorProfileService profileService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/my")
    public Result<CounselorProfile> getMyProfile(@RequestHeader("Authorization") String token) {
        String t = token.replace("Bearer ", "");
        return Result.ok(profileService.getByUserId(jwtUtil.getUserId(t)));
    }

    @PostMapping("/submit")
    public Result<?> submitReview(@RequestHeader("Authorization") String token,
                                   @RequestBody CounselorProfile profile) {
        String t = token.replace("Bearer ", "");
        profileService.submitReview(jwtUtil.getUserId(t), profile);
        return Result.ok();
    }

    @PostMapping("/revoke")
    public Result<?> revokeReview(@RequestHeader("Authorization") String token) {
        String t = token.replace("Bearer ", "");
        profileService.revokeReview(jwtUtil.getUserId(t));
        return Result.ok();
    }
}