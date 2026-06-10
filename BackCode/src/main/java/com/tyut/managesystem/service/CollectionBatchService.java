package com.tyut.managesystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tyut.managesystem.entity.CollectionBatch;
import java.util.List;

public interface CollectionBatchService extends IService<CollectionBatch> {
    List<CollectionBatch> listAll();
    List<CollectionBatch> listByDepartment(String department);
    void publish(CollectionBatch batch, Long publisherId);
}