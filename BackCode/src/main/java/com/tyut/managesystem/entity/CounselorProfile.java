package com.tyut.managesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("counselor_profile")
public class CounselorProfile {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String name;
    private String gender;
    private String birth;
    private String politicalStatus;
    private String employeeNo;
    private String department;
    private String type;       // 专职辅导员 / 兼职辅导员 / 专任教师
    private String position;
    private String campus;
    private String office;
    private String phone;
    private String education;
    private String degree;
    private String idPhoto;
    private String lifePhotos;
    private String workList;   // JSON
    private String studyList;  // JSON
    private String status;     // normal / reviewing / rejected
    private String rejectReason;
    private LocalDateTime updateTime;
    private LocalDateTime createTime;
}