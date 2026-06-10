package com.tyut.managesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tyut.managesystem.entity.ApplicationRecord;
import com.tyut.managesystem.entity.CounselorProfile;
import com.tyut.managesystem.entity.User;
import com.tyut.managesystem.mapper.ApplicationRecordMapper;
import com.tyut.managesystem.mapper.CounselorProfileMapper;
import com.tyut.managesystem.mapper.UserMapper;
import com.tyut.managesystem.service.CounselorProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class CounselorProfileServiceImpl extends ServiceImpl<CounselorProfileMapper, CounselorProfile> implements CounselorProfileService {

    @Autowired
    private ApplicationRecordMapper recordMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public CounselorProfile getByUserId(Long userId) {
        return getOne(new LambdaQueryWrapper<CounselorProfile>().eq(CounselorProfile::getUserId, userId));
    }

    @Override
    @Transactional
    public void submitReview(Long userId, CounselorProfile profile) {
        CounselorProfile exist = getByUserId(userId);
        profile.setUserId(userId);
        profile.setStatus("reviewing");
        profile.setUpdateTime(LocalDateTime.now());
        if (exist != null) {
            profile.setId(exist.getId());
            updateById(profile);
        } else {
            save(profile);
        }

        User user = userMapper.selectById(userId);

        ApplicationRecord record = new ApplicationRecord();
        record.setUserId(userId);
        record.setUserName(user != null ? user.getNickname() : "");
        record.setEmployeeNo(user != null ? user.getEmployeeNo() : "");
        record.setDepartment(user != null ? user.getDepartment() : "");
        record.setType("profile_change");
        record.setUpdateType("自主变更");
        record.setStatus("pending");
        record.setApplyTime(LocalDateTime.now());
        recordMapper.insert(record);
    }

    @Override
    @Transactional
    public void revokeReview(Long userId) {
        CounselorProfile profile = getByUserId(userId);
        if (profile != null && "reviewing".equals(profile.getStatus())) {
            profile.setStatus("normal");
            updateById(profile);
            recordMapper.delete(new LambdaQueryWrapper<ApplicationRecord>()
                    .eq(ApplicationRecord::getUserId, userId)
                    .eq(ApplicationRecord::getType, "profile_change")
                    .eq(ApplicationRecord::getStatus, "pending"));
        }
    }
}