package com.tyut.managesystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tyut.managesystem.entity.ApplicationRecord;
import java.util.List;

public interface ApplicationRecordService extends IService<ApplicationRecord> {
    List<ApplicationRecord> listPending(String department);
    void approve(Long id, Long reviewerId);
    void reject(Long id, Long reviewerId, String reason);
}