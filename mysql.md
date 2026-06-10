辅导员信息管理系统数据库架构说明
一、数据库基本信息
数据库名称：tyut_manage

字符集：utf8mb4

排序规则：utf8mb4_general_ci

主要用途：存储和管理辅导员基本信息、信息采集批次、审核流程等数据，支持多角色（辅导员、院系、管理员）协同工作。

二、数据表结构及作用
1. sys_user（系统用户表）
作用：存储系统登录用户的基本账号信息，区分不同角色权限。
字段名	类型	说明
id	BIGINT	用户主键ID，自增
username	VARCHAR(50)	登录用户名，唯一
password	VARCHAR(100)	登录密码
nickname	VARCHAR(50)	昵称/姓名
role	VARCHAR(20)	角色：COUNSELOR / DEPARTMENT / ADMIN
employee_no	VARCHAR(20)	工号
department	VARCHAR(50)	所属部门/学院
phone	VARCHAR(20)	联系电话
create_time / update_time	DATETIME	创建/更新时间

2. counselor_profile（辅导员个人信息表）
作用：存储辅导员的详细个人信息，支持照片、工作/学习经历等结构化数据。
字段名	类型	说明
id	BIGINT	主键ID
user_id	BIGINT	关联 sys_user.id
name	VARCHAR(50)	姓名
gender	VARCHAR(10)	性别
birth	VARCHAR(20)	出生日期
political_status	VARCHAR(20)	政治面貌
employee_no	VARCHAR(20)	工号
department	VARCHAR(50)	所在学院
type	VARCHAR(20)	辅导员类型（专职/兼职/专任教师）
position	VARCHAR(50)	职务
campus	VARCHAR(20)	校区
office	VARCHAR(50)	办公室
phone	VARCHAR(20)	电话
education	VARCHAR(20)	最高学历
degree	VARCHAR(20)	最后学位
id_photo	TEXT	证件照（Base64）
life_photos	TEXT	生活照（JSON数组）
work_list / study_list	TEXT	工作/学习经历（JSON）
status	VARCHAR(20)	审核状态（normal/reviewing/rejected）
reject_reason	VARCHAR(500)	驳回原因
update_time / create_time	DATETIME	更新时间/创建时间

3. collection_batch（采集批次表）
作用：管理信息采集任务批次，控制每个批次的起止时间与发布者。
字段名	类型	说明
id	BIGINT	批次主键ID
name	VARCHAR(100)	批次名称
start_time / end_time	DATE	采集起止日期
duration	VARCHAR(50)	持续时长（备用字段）
publisher_id	BIGINT	发布者ID（关联 sys_user.id）
publish_time	DATETIME	发布时间

4. batch_submission（批次提交记录表）
作用：记录辅导员在某个批次中提交的信息内容及审核状态。
字段名	类型	说明
id	BIGINT	主键ID
batch_id	BIGINT	关联 collection_batch.id
user_id	BIGINT	提交人ID
id_photo / life_photos	TEXT	证件照/生活照
education	VARCHAR(20)	学历
work_list / study_list	TEXT	工作/学习经历
status	VARCHAR(20)	审核状态（reviewing/approved/rejected）
reject_reason	VARCHAR(500)	驳回原因
submit_time / review_time	DATETIME	提交时间/审核时间

5. application_record（审核申请记录表）
作用：统一管理所有需要审核的申请（如信息变更、批次提交），支持多类型审核流程。
字段名	类型	说明
id	BIGINT	主键ID
user_id	BIGINT	申请人ID
user_name / employee_no / department	VARCHAR	冗余字段，便于展示
type	VARCHAR(20)	申请类型（profile_change / batch_submit）
update_type	VARCHAR(20)	变更类型（自主变更/采集填报）
content	TEXT	变更内容（JSON格式）
status	VARCHAR(20)	申请状态（pending/approved/rejected）
reject_reason	VARCHAR(500)	驳回原因
reviewer_id	BIGINT	审核人ID
apply_time / review_time	DATETIME	申请时间/审核时间
三、默认用户初始化
脚本预置了三个演示用户，方便测试不同角色：

用户名	密码	角色	工号	部门
counselor	123456	辅导员（COUNSELOR）	11001	信息工程学院
college	123456	院系（DEPARTMENT）	11002	信息工程学院
admin	123456	管理员（ADMIN）	11003	NULL

最终审核通过并归档数据