package com.tyut.managesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("application_record")
public class ApplicationRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String userName;
    private String employeeNo;
    private String department;
    private String type;       // profile_change / batch_submit
    private String updateType; // 自主变更 / 采集填报
    private String content;    // JSON
    private Long batchId;      // 关联的采集批次ID
    private String status;     // pending / approved / rejected
    private String rejectReason;
    private Long reviewerId;
    private LocalDateTime applyTime;
    private LocalDateTime reviewTime;
}