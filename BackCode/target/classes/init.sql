-- 辅导员基本信息管理系统 - 数据库初始化脚本

CREATE DATABASE IF NOT EXISTS tyut_manage DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE tyut_manage;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50),
    role VARCHAR(20) NOT NULL COMMENT 'COUNSELOR/DEPARTMENT/ADMIN',
    employee_no VARCHAR(20),
    department VARCHAR(50),
    phone VARCHAR(20),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '系统用户表';

-- 辅导员个人信息表
CREATE TABLE IF NOT EXISTS counselor_profile (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    name VARCHAR(50),
    gender VARCHAR(10),
    birth VARCHAR(20),
    political_status VARCHAR(20) COMMENT '政治面貌',
    employee_no VARCHAR(20),
    department VARCHAR(50),
    type VARCHAR(20) COMMENT '专职辅导员/兼职辅导员/专任教师',
    position VARCHAR(50) COMMENT '职务',
    campus VARCHAR(20),
    office VARCHAR(50),
    phone VARCHAR(20),
    education VARCHAR(20) COMMENT '最高学历',
    degree VARCHAR(20) COMMENT '最后学位',
    id_photo TEXT COMMENT '证件照base64',
    life_photos TEXT COMMENT '生活照JSON数组',
    work_list TEXT COMMENT '工作经历JSON',
    study_list TEXT COMMENT '学习经历JSON',
    status VARCHAR(20) DEFAULT 'normal' COMMENT 'normal/reviewing/rejected',
    reject_reason VARCHAR(500),
    update_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '辅导员个人信息表';

-- 采集批次表
CREATE TABLE IF NOT EXISTS collection_batch (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    start_time DATE,
    end_time DATE,
    duration VARCHAR(50),
    publisher_id BIGINT,
    publish_time DATETIME
) COMMENT '信息采集批次表';

-- 批次提交表
CREATE TABLE IF NOT EXISTS batch_submission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    batch_id BIGINT,
    user_id BIGINT,
    id_photo TEXT,
    life_photos TEXT,
    education VARCHAR(20),
    work_list TEXT,
    study_list TEXT,
    status VARCHAR(20) DEFAULT 'reviewing' COMMENT 'reviewing/approved/rejected',
    reject_reason VARCHAR(500),
    submit_time DATETIME,
    review_time DATETIME
) COMMENT '批次提交记录表';

-- 审核申请表
CREATE TABLE IF NOT EXISTS application_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    user_name VARCHAR(50),
    employee_no VARCHAR(20),
    department VARCHAR(50),
    type VARCHAR(20) COMMENT 'profile_change/batch_submit',
    update_type VARCHAR(20) COMMENT '自主变更/采集填报',
    content TEXT COMMENT '变更内容JSON',
    status VARCHAR(20) DEFAULT 'pending' COMMENT 'pending/approved/rejected',
    reject_reason VARCHAR(500),
    reviewer_id BIGINT,
    apply_time DATETIME,
    review_time DATETIME
) COMMENT '审核申请记录表';

-- 插入默认用户
INSERT INTO sys_user (username, password, nickname, role, employee_no, department) VALUES
('counselor', '123456', '辅导员演示', 'COUNSELOR', '11001', '信息工程学院'),
('college', '123456', '学院演示', 'DEPARTMENT', '11002', '信息工程学院'),
('admin', '123456', '管理员演示', 'ADMIN', '11003', NULL);
'