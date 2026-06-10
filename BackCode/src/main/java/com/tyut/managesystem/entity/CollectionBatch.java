package com.tyut.managesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("collection_batch")
public class CollectionBatch {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private LocalDate startTime;
    private LocalDate endTime;
    private String duration;
    private String department;
    private Long publisherId;
    private LocalDateTime publishTime;
}