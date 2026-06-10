package com.tyut.managesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("batch_submission")
public class BatchSubmission {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long batchId;
    private Long userId;
    private String idPhoto;
    private String lifePhotos;
    private String education;
    private String workList;
    private String studyList;
    private String status;     // reviewing / approved / rejected
    private String rejectReason;
    private LocalDateTime submitTime;
    private LocalDateTime reviewTime;
}