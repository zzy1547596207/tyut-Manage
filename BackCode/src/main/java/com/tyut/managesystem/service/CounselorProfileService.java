package com.tyut.managesystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tyut.managesystem.entity.CounselorProfile;

public interface CounselorProfileService extends IService<CounselorProfile> {
    CounselorProfile getByUserId(Long userId);
    void submitReview(Long userId, CounselorProfile profile);
    void revokeReview(Long userId);
}