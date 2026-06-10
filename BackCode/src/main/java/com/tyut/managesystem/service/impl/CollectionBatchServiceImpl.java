package com.tyut.managesystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tyut.managesystem.entity.CollectionBatch;
import com.tyut.managesystem.mapper.CollectionBatchMapper;
import com.tyut.managesystem.service.CollectionBatchService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CollectionBatchServiceImpl extends ServiceImpl<CollectionBatchMapper, CollectionBatch> implements CollectionBatchService {

    @Override
    public List<CollectionBatch> listAll() {
        return list();
    }

    @Override
    public List<CollectionBatch> listByDepartment(String department) {
        if (department == null || department.isEmpty()) {
            return list();
        }
        LambdaQueryWrapper<CollectionBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CollectionBatch::getDepartment, department)
               .orderByDesc(CollectionBatch::getPublishTime);
        return list(wrapper);
    }

    @Override
    public void publish(CollectionBatch batch, Long publisherId) {
        batch.setPublisherId(publisherId);
        batch.setPublishTime(LocalDateTime.now());
        save(batch);
    }
}