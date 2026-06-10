package com.tyut.managesystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tyut.managesystem.entity.BatchSubmission;
import java.util.List;

public interface BatchSubmissionService extends IService<BatchSubmission> {
    void submit(Long userId, BatchSubmission submission);
    void revoke(Long submissionId);
    List<BatchSubmission> listByUser(Long userId);
    BatchSubmission getByBatchAndUser(Long batchId, Long userId);
}