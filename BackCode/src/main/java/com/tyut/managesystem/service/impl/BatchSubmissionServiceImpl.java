package com.tyut.managesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tyut.managesystem.entity.ApplicationRecord;
import com.tyut.managesystem.entity.BatchSubmission;
import com.tyut.managesystem.entity.User;
import com.tyut.managesystem.mapper.ApplicationRecordMapper;
import com.tyut.managesystem.mapper.BatchSubmissionMapper;
import com.tyut.managesystem.mapper.UserMapper;
import com.tyut.managesystem.service.BatchSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class BatchSubmissionServiceImpl extends ServiceImpl<BatchSubmissionMapper, BatchSubmission> implements BatchSubmissionService {

    @Autowired
    private ApplicationRecordMapper recordMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public void submit(Long userId, BatchSubmission submission) {
        submission.setUserId(userId);
        submission.setStatus("reviewing");
        submission.setSubmitTime(LocalDateTime.now());
        save(submission);

        User user = userMapper.selectById(userId);

        ApplicationRecord record = new ApplicationRecord();
        record.setUserId(userId);
        record.setUserName(user != null ? user.getNickname() : "");
        record.setEmployeeNo(user != null ? user.getEmployeeNo() : "");
        record.setDepartment(user != null ? user.getDepartment() : "");
        record.setType("batch_submit");
        record.setUpdateType("閲囬泦濉姤");
        record.setBatchId(submission.getBatchId());
        record.setStatus("pending");
        record.setApplyTime(LocalDateTime.now());

        // 鏋勫缓鍐呭鎽樿
        Map<String, Object> contentMap = new LinkedHashMap<>();
        contentMap.put("education", submission.getEducation());
        contentMap.put("workList", submission.getWorkList());
        contentMap.put("studyList", submission.getStudyList());
        contentMap.put("hasIdPhoto", submission.getIdPhoto() != null && !submission.getIdPhoto().isEmpty());
        contentMap.put("hasLifePhotos", submission.getLifePhotos() != null && !submission.getLifePhotos().equals("[]"));
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            record.setContent(mapper.writeValueAsString(contentMap));
        } catch (Exception e) {
            record.setContent("{}");
        }

        recordMapper.insert(record);
    }

    @Override
    @Transactional
    public void revoke(Long submissionId) {
        BatchSubmission sub = getById(submissionId);
        if (sub != null) {
            removeById(submissionId);
            recordMapper.delete(new LambdaQueryWrapper<ApplicationRecord>()
                    .eq(ApplicationRecord::getUserId, sub.getUserId())
                    .eq(ApplicationRecord::getType, "batch_submit")
                    .eq(ApplicationRecord::getStatus, "pending"));
        }
    }

    @Override
    public BatchSubmission getByBatchAndUser(Long batchId, Long userId) {
        LambdaQueryWrapper<BatchSubmission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BatchSubmission::getBatchId, batchId)
               .eq(BatchSubmission::getUserId, userId)
               .orderByDesc(BatchSubmission::getSubmitTime)
               .last("limit 1");
        List<BatchSubmission> list = list(wrapper);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<BatchSubmission> listByUser(Long userId) {
        return list(new LambdaQueryWrapper<BatchSubmission>().eq(BatchSubmission::getUserId, userId));
    }
}