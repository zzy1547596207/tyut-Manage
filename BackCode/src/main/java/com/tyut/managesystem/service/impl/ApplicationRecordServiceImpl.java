package com.tyut.managesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tyut.managesystem.entity.ApplicationRecord;
import com.tyut.managesystem.entity.BatchSubmission;
import com.tyut.managesystem.entity.CounselorProfile;
import com.tyut.managesystem.entity.User;
import com.tyut.managesystem.mapper.ApplicationRecordMapper;
import com.tyut.managesystem.mapper.BatchSubmissionMapper;
import com.tyut.managesystem.mapper.CounselorProfileMapper;
import com.tyut.managesystem.mapper.UserMapper;
import com.tyut.managesystem.service.ApplicationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationRecordServiceImpl extends ServiceImpl<ApplicationRecordMapper, ApplicationRecord> implements ApplicationRecordService {

    @Autowired
    private CounselorProfileMapper profileMapper;
    @Autowired
    private BatchSubmissionMapper submissionMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<ApplicationRecord> listPending(String department) {
        LambdaQueryWrapper<ApplicationRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ApplicationRecord::getStatus, "pending");
        if (department != null && !department.isEmpty()) {
            wrapper.eq(ApplicationRecord::getDepartment, department);
        }
        wrapper.orderByDesc(ApplicationRecord::getApplyTime);
        return list(wrapper);
    }

    @Override
    @Transactional
    public void approve(Long id, Long reviewerId) {
        ApplicationRecord record = getById(id);
        if (record == null) return;
        record.setStatus("approved");
        record.setReviewerId(reviewerId);
        record.setReviewTime(LocalDateTime.now());
        updateById(record);

        if ("profile_change".equals(record.getType())) {
            CounselorProfile profile = profileMapper.selectOne(
                    new LambdaQueryWrapper<CounselorProfile>().eq(CounselorProfile::getUserId, record.getUserId()));
            if (profile != null) {
                profile.setStatus("normal");
                profile.setUpdateTime(LocalDateTime.now());
                profileMapper.updateById(profile);

                // 同步 sys_user 表
                User user = userMapper.selectById(record.getUserId());
                if (user != null && profile.getName() != null) {
                    user.setNickname(profile.getName());
                    if (profile.getEmployeeNo() != null) user.setEmployeeNo(profile.getEmployeeNo());
                    if (profile.getDepartment() != null) user.setDepartment(profile.getDepartment());
                    if (profile.getPhone() != null) user.setPhone(profile.getPhone());
                    userMapper.updateById(user);
                }
            }
        } else if ("batch_submit".equals(record.getType())) {
            List<BatchSubmission> subs = submissionMapper.selectList(
                    new LambdaQueryWrapper<BatchSubmission>()
                            .eq(BatchSubmission::getUserId, record.getUserId())
                            .eq(BatchSubmission::getStatus, "reviewing")
                            .orderByDesc(BatchSubmission::getSubmitTime)
                            .last("limit 1"));
            if (!subs.isEmpty()) {
                BatchSubmission sub = subs.get(0);
                sub.setStatus("approved");
                sub.setReviewTime(LocalDateTime.now());
                submissionMapper.updateById(sub);
            }
        }
    }

    @Override
    @Transactional
    public void reject(Long id, Long reviewerId, String reason) {
        ApplicationRecord record = getById(id);
        if (record == null) return;
        record.setStatus("rejected");
        record.setRejectReason(reason);
        record.setReviewerId(reviewerId);
        record.setReviewTime(LocalDateTime.now());
        updateById(record);

        if ("profile_change".equals(record.getType())) {
            CounselorProfile profile = profileMapper.selectOne(
                    new LambdaQueryWrapper<CounselorProfile>().eq(CounselorProfile::getUserId, record.getUserId()));
            if (profile != null) {
                profile.setStatus("rejected");
                profile.setRejectReason(reason);
                profileMapper.updateById(profile);
            }
        } else if ("batch_submit".equals(record.getType())) {
            List<BatchSubmission> subs = submissionMapper.selectList(
                    new LambdaQueryWrapper<BatchSubmission>()
                            .eq(BatchSubmission::getUserId, record.getUserId())
                            .eq(BatchSubmission::getStatus, "reviewing")
                            .orderByDesc(BatchSubmission::getSubmitTime)
                            .last("limit 1"));
            if (!subs.isEmpty()) {
                BatchSubmission sub = subs.get(0);
                sub.setStatus("rejected");
                sub.setRejectReason(reason);
                submissionMapper.updateById(sub);
            }
        }
    }
}