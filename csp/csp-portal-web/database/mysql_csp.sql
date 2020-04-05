/**
Copyright [2020-2030] [yefangwong(https://github.com/yefangwong)]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
create database if not exists csp character set utf8 COLLATE utf8_general_ci;

use csp;

create table if not exists foo_bar
(
    id bigint auto_increment comment 'ID'
        primary key,
    name varchar(20) not null comment 'Name',
    foo varchar(20) null comment 'Foo',
    bar varchar(20) not null comment 'Bar',
    remark varchar(200) null comment 'Remark',
    state int default 1 not null comment 'State，0：Disable，1：Enable',
    version int default 0 not null comment 'Version',
    create_time timestamp default CURRENT_TIMESTAMP null comment 'Create Time',
    update_time timestamp null comment 'Update Time'
)
    comment 'FooBar';

create table if not exists example_order
(
    id bigint auto_increment comment '主鍵'
        primary key,
    name varchar(20) not null comment '訂單名稱',
    order_no varchar(100) null comment '訂單编號',
    remark varchar(200) null comment '備註',
    state int default 1 not null comment '狀態，0：禁用，1：啟用',
    version int default 0 not null comment '版本',
    create_time timestamp default CURRENT_TIMESTAMP null comment '建立時間',
    update_time timestamp null comment '修改時間'
)
    comment '訂單範例';

create table if not exists sys_department
(
    id bigint auto_increment comment '主鍵'
        primary key,
    name varchar(32) not null comment '部門名稱',
    parent_id bigint null comment '父id',
    level int null comment '部門層级',
    state int default 1 not null comment '状態，0：禁用，1：啟用',
    sort int default 0 not null comment '排序',
    remark varchar(200) null comment '備註',
    version int default 0 not null comment '版本',
    create_time timestamp default CURRENT_TIMESTAMP null comment '建立時間',
    update_time timestamp null comment '修改時間',
    constraint sys_department_name_uindex
        unique (name)
)
    comment '部門';

create table if not exists sys_permission
(
    id bigint auto_increment comment '主鍵'
        primary key,
    name varchar(32) null comment '權限名稱',
    parent_id bigint null comment '父id',
    url varchar(200) null comment '路徑',
    code varchar(100) not null comment '唯一编碼',
    icon varchar(100) null comment '圖示',
    type int not null comment '類型，1：選單，2：按鈕',
    level int not null comment '層级，1：第一级，2：第二级，N：第N级',
    state int default 1 not null comment '狀態，0：禁用，1：啟用',
    sort int default 0 not null comment '排序',
    remark varchar(200) null comment '備註',
    version int default 0 not null comment '版本',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '建立時間',
    update_time timestamp null comment '修改時間',
    constraint sys_permission_code_uindex
        unique (code)
)
    comment '系统權限';

create table if not exists sys_role
(
    id bigint auto_increment comment '主鍵'
        primary key,
    name varchar(32) not null comment '角色名稱',
    code varchar(100) null comment '角色唯一編碼',
    type int null comment '角色類型',
    state int default 1 not null comment '角色狀態，0：禁用，1：啟用',
    remark varchar(200) null comment '備註',
    version int default 0 not null comment '版本',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '建立時間',
    update_time timestamp null comment '修改時間',
    constraint sys_role_name_uindex
        unique (name)
)
    comment '系统角色';

create table if not exists sys_role_permission
(
    id bigint auto_increment comment '主鍵'
        primary key,
    role_id bigint not null comment '角色id',
    permission_id bigint not null comment '權限id',
    state int default 1 not null comment '狀態，0：禁用，1：啟用',
    remark varchar(200) null comment '備註',
    version int default 0 not null comment '版本',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '建立時間',
    update_time timestamp null comment '修改時間'
)
    comment '角色權限關係';

create index permission_id
    on sys_role_permission (permission_id);

create index role_id
    on sys_role_permission (role_id);

create table if not exists sys_user
(
    id bigint auto_increment comment '主鍵'
        primary key,
    username varchar(20) not null comment '用户名',
    nickname varchar(20) null comment '暱稱',
    password varchar(64) not null comment '密碼',
    salt varchar(32) null comment '鹽值',
    phone varchar(20) not null comment '手機號碼',
    gender int default 1 not null comment '性别，0：女，1：男，預設1',
    head varchar(200) null comment '大頭照',
    remark varchar(200) null comment '備註',
    state int default 1 not null comment '狀態，0：禁用，1：啟用，2：鎖定',
    department_id bigint not null comment '部門id',
    role_id bigint not null comment '角色id',
    deleted int default 0 not null comment '邏輯删除，0：未删除，1：已删除',
    version int default 0 not null comment '版本',
    create_time timestamp default CURRENT_TIMESTAMP null comment '建立時間',
    update_time timestamp null comment '修改時間',
    constraint sys_user_username_uindex
        unique (username)
)
    comment '系统用户';

create index department_id
    on sys_user (department_id);

create index role_id
    on sys_user (role_id);

create table if not exists sys_login_log
(
    id bigint(18) auto_increment comment '主鍵'
        primary key,
    request_id varchar(32) null comment '請求ID',
    username varchar(32) null comment '用户名稱',
    ip varchar(15) null comment 'IP',
    area varchar(45) null comment '地區',
    operator varchar(6) null comment '維運商',
    token varchar(32) null comment 'tokenMd5值',
    type int null comment '1:登錄，2：登出',
    success tinyint(1) default 0 not null comment '是否成功 true:成功/false:失敗',
    code int null comment '響應碼',
    exception_message varchar(300) null comment '失敗消息紀錄',
    user_agent varchar(300) null comment '瀏覽器名稱',
    browser_name varchar(100) null comment '瀏覽器名稱',
    browser_version varchar(100) null comment '瀏覽器版本',
    engine_name varchar(100) null comment '瀏覽器引擎名稱',
    engine_version varchar(100) null comment '瀏覽器引擎版本',
    os_name varchar(100) null comment '系统名稱',
    platform_name varchar(100) null comment '平台名稱',
    mobile tinyint(1) null comment '是否是手機,0:否,1:是',
    device_name varchar(100) null comment '移動端設備名稱',
    device_model varchar(100) null comment '移動端設備型號',
    remark varchar(200) null comment '備註',
    create_time datetime default CURRENT_TIMESTAMP null comment '建立時間',
    update_time datetime null comment '修改時間'
)
    comment '系统登入日志';

create table if not exists sys_operation_log
(
    id bigint(18) auto_increment comment '主鍵'
        primary key,
    request_id varchar(32) null comment '請求ID',
    user_id bigint(18) null comment '用户ID',
    user_name varchar(32) null comment '用户名稱',
    name varchar(200) null comment '日志名稱',
    ip varchar(15) null comment 'IP',
    area varchar(45) null comment '區域',
    operator varchar(6) null comment '運營商',
    path varchar(500) null comment '全路徑',
    module varchar(100) null comment '模組名稱',
    class_name varchar(100) null comment '類名',
    method_name varchar(100) null comment '方法名稱',
    request_method varchar(10) null comment '請求方式，GET/POST',
    content_type varchar(100) null comment '内容类型',
    request_body tinyint(1) null comment '是否是JSON請求映射參數',
    param text null comment '請求参數',
    token varchar(32) null comment 'tokenMd5值',
    type int null comment '0:其它,1:新增,2:修改,3:删除,4:詳情查詢,5:所有列表,6:分頁列表,7:其它查詢,8:上傳文件',
    success tinyint(1) null comment '0:失敗,1:成功',
    code int null comment '響應结果狀態碼',
    message varchar(100) null comment '響應结果消息',
    exception_name varchar(200) null comment '異常類名稱',
    exception_message varchar(300) null comment '異常信息',
    browser_name varchar(100) null comment '瀏覽器名稱',
    browser_version varchar(100) null comment '瀏覽器版本',
    engine_name varchar(100) null comment '瀏覽器引擎名稱',
    engine_version varchar(100) null comment '瀏覽器引擎版本',
    os_name varchar(100) null comment '系统名稱',
    platform_name varchar(100) null comment '平台名稱',
    mobile tinyint(1) null comment '是否是手機,0:否,1:是',
    device_name varchar(100) null comment '移動端設備名稱',
    device_model varchar(100) null comment '移动端設備型號',
    remark varchar(200) null comment '備註',
    create_time datetime default CURRENT_TIMESTAMP null comment '建立時間',
    update_time datetime null comment '修改時間'
)
    comment '系统操作日志';

INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (1, 'admin', '管理者', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889900', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 1, 1, 1, 0, 1, '2020-02-26 00:00:00', '2019-10-27 23:32:29');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (2, 'test', '測試人員1', '34783fb724b259beb71a1279f7cd93bdcfd92a273d566f926419a37825c500df', '087c2e9857f35f1e243367f3b89b81c1', '15888889901', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Tester Account', 1, 1, 2, 0, 1, '2020-02-26 00:00:01', '2020-02-15 19:31:50');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (3, 'admin1', '管理者1', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889902', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 1, 1, 1, 0, 1, '2020-02-26 00:09:09', '2019-10-27 23:32:29');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (4, 'admin2', '管理者2', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889903', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 1, 1, 1, 0, 1, '2020-02-26 16:10:06', '2019-10-27 23:32:29');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (5, 'admin3', '管理者3', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889904', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 1, 1, 1, 0, 1, '2020-02-26 16:10:06', '2019-10-27 23:32:29');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (6, 'admin4', '管理者4', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889905', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 1, 1, 1, 0, 1, '2020-02-26 16:10:06', '2019-10-27 23:32:29');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (7, 'admin5', '管理者5', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889906', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 1, 1, 1, 0, 1, '2020-02-26 23:59:59', '2019-10-27 23:32:29');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (8, 'admin6', '管理者6', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 1, 1, 1, 0, 1, '2020-02-26 23:59:59', '2019-10-27 23:32:29');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (9, 'admin7', '管理者7', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 1, 1, 1, 0, 1, '2020-02-20 23:59:59', '2019-10-27 23:32:29');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (10, 'admin8', '管理者8', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 1, 1, 1, 0, 1, '2020-01-30 22:56:55', '2019-10-27 23:32:29');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (11, 'admin9', '管理者9', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 1, 1, 1, 0, 1, '2019-12-30 00:52:01', '2019-10-27 23:32:29');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (12, 'admin10', '管理者10', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 1, 1, 1, 0, 1, '2019-08-26 00:52:01', '2019-10-27 23:32:29');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (13, 'admin11', '管理者11', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 1, 1, 1, 0, 1, '2019-08-26 00:52:01', '2019-10-27 23:32:29');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (14, 'admin12', '管理者12', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 2, 1, 1, 0, 2, '2019-08-26 00:52:01', '2020-02-27 14:05:40');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (15, 'admin13', '管理者13uuu', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 2, 1, 1, 0, 3, '2019-08-26 00:52:01', '2020-02-27 14:05:18');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (16, 'admin14', '管理者14', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 0, 1, 1, 0, 1, '2019-08-26 00:52:01', '2019-10-27 23:32:29');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (17, 'admin15', '管理者15', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 0, 1, 1, 0, 1, '2019-08-26 00:52:01', '2019-10-27 23:32:29');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (18, 'admin16', '管理者16', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 0, 1, 1, 0, 1, '2019-08-26 00:52:01', '2019-10-27 23:32:29');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (19, 'admin17', '管理者17', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 2, 1, 1, 0, 1, '2019-08-26 00:52:01', '2019-10-27 23:32:29');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (20, 'admin18', '管理者18', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 2, 1, 1, 0, 1, '2019-08-26 00:52:01', '2019-10-27 23:32:29');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (21, 'admin19', '管理者19', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 2, 1, 1, 0, 1, '2019-08-26 00:52:01', '2019-10-27 23:32:29');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (22, 'admin20', '管理者20', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 2, 1, 1, 0, 1, '2019-08-26 00:52:01', '2019-10-27 23:32:29');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (23, 'admin21', '管理者21', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 0, 1, 1, 0, 1, '2019-08-26 00:52:01', '2019-10-27 23:32:29');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (24, 'admin22', '管理者22', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 0, 1, 1, 0, 1, '2019-08-26 00:52:01', '2019-10-27 23:32:29');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (25, 'admin23', '管理者23', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', 2, 1, 1, 0, 3, '2019-08-26 00:52:01', '2020-02-27 14:42:28');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (100, 'dddd', 'ddddd', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', 'aa98a65fa53d198f38d8e3a63f3f5a65', 'ddddddddddd', 1, 'http://localhost:8888/api/resource/201908201013068.png', 'dddddddd', 1, 1, 1, 0, 1, '2020-02-26 14:06:53', '2020-02-27 14:06:52');
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, head, remark, state, department_id, role_id, deleted, version, create_time, update_time) VALUES (101, 'adminx', '111111', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '1faf81180b4a4a78c48d7c31479a0622', '11111111111', 1, 'http://localhost:8888/api/resource/201908201013068.png', '1111111111', 1, 1, 1, 1, 6, '2020-02-26 14:19:57', '2020-03-02 17:33:48');

INSERT INTO sys_department (id, name, parent_id, level, state, sort, remark, version, create_time, update_time) VALUES (1, '技術部', null, 1, 1, 359544077, 'fe8c9cbac0c54395ac411335a31f4888', 15, '2019-10-25 09:46:49', '2019-11-13 19:56:07');
INSERT INTO sys_department (id, name, parent_id, level, state, sort, remark, version, create_time, update_time) VALUES (2, '研發部', null, 1, 1, 0, null, 0, '2019-11-01 20:45:43', null);
INSERT INTO sys_department (id, name, parent_id, level, state, sort, remark, version, create_time, update_time) VALUES (20, '前端開發部', 2, 2, 1, 0, null, 0, '2019-11-01 20:48:38', null);
INSERT INTO sys_department (id, name, parent_id, level, state, sort, remark, version, create_time, update_time) VALUES (21, '後台開發部', 2, 2, 1, 0, null, 0, '2019-11-01 20:48:38', null);
INSERT INTO sys_department (id, name, parent_id, level, state, sort, remark, version, create_time, update_time) VALUES (22, '測試部', 2, 2, 1, 0, null, 0, '2019-11-01 20:48:38', null);
INSERT INTO sys_department (id, name, parent_id, level, state, sort, remark, version, create_time, update_time) VALUES (201, '前端一组', 20, 3, 1, 0, null, 0, '2019-11-01 20:48:38', null);
INSERT INTO sys_department (id, name, parent_id, level, state, sort, remark, version, create_time, update_time) VALUES (202, '前端二组', 20, 3, 1, 0, null, 0, '2019-11-01 20:48:38', null);
INSERT INTO sys_department (id, name, parent_id, level, state, sort, remark, version, create_time, update_time) VALUES (203, '後台一组', 21, 3, 1, 0, null, 0, '2019-11-01 20:48:38', null);
INSERT INTO sys_department (id, name, parent_id, level, state, sort, remark, version, create_time, update_time) VALUES (204, '後台二组', 21, 3, 1, 0, null, 0, '2019-11-01 20:48:38', null);
INSERT INTO sys_department (id, name, parent_id, level, state, sort, remark, version, create_time, update_time) VALUES (205, '測試一组', 22, 3, 1, 0, null, 0, '2019-11-01 20:48:38', null);

INSERT INTO sys_role (id, name, code, type, state, remark, version, create_time, update_time) VALUES (1, '管理者', 'admin', null, 1, '管理者擁有所有權限', 0, '2019-10-25 09:47:21', null);
INSERT INTO sys_role (id, name, code, type, state, remark, version, create_time, update_time) VALUES (2, 'test', 'test', null, 1, '測試人員擁有部分權限', 0, '2019-10-25 09:48:02', null);
INSERT INTO sys_role (id, name, code, type, state, remark, version, create_time, update_time) VALUES (3, '管理者1', 'admin1', null, 1, '管理者擁有所有權限', 0, '2019-10-25 09:47:21', null);
INSERT INTO sys_role (id, name, code, type, state, remark, version, create_time, update_time) VALUES (4, '管理者2', 'admin2', null, 1, '管理者擁有所有權限', 0, '2019-10-25 09:47:21', null);
INSERT INTO sys_role (id, name, code, type, state, remark, version, create_time, update_time) VALUES (5, '管理者3', 'admin3', null, 1, '管理者擁有所有權限', 0, '2019-10-25 09:47:21', null);
INSERT INTO sys_role (id, name, code, type, state, remark, version, create_time, update_time) VALUES (6, '管理者4', 'admin4', null, 1, '管理者擁有所有權限', 0, '2019-10-25 09:47:21', null);
INSERT INTO sys_role (id, name, code, type, state, remark, version, create_time, update_time) VALUES (7, '管理者5', 'admin5', null, 1, '管理者擁有所有權限', 0, '2019-10-25 09:47:21', null);
INSERT INTO sys_role (id, name, code, type, state, remark, version, create_time, update_time) VALUES (8, '管理者6', 'admin6', null, 1, '管理者擁有所有權限', 0, '2019-10-25 09:47:21', null);
INSERT INTO sys_role (id, name, code, type, state, remark, version, create_time, update_time) VALUES (9, '管理者7', 'admin7', null, 1, '管理者擁有所有權限', 0, '2019-10-25 09:47:21', null);
INSERT INTO sys_role (id, name, code, type, state, remark, version, create_time, update_time) VALUES (10, '管理者8', 'admin8', null, 1, '管理者擁有所有權限', 0, '2019-10-25 09:47:21', null);
INSERT INTO sys_role (id, name, code, type, state, remark, version, create_time, update_time) VALUES (11, '管理者9', 'admin9', null, 1, '管理者擁有所有權限', 0, '2019-10-25 09:47:21', null);
INSERT INTO sys_role (id, name, code, type, state, remark, version, create_time, update_time) VALUES (12, '管理者10', 'admin10', null, 1, '管理者擁有所有權限', 0, '2019-10-25 09:47:21', null);
INSERT INTO sys_role (id, name, code, type, state, remark, version, create_time, update_time) VALUES (13, '管理者11', 'admin11', null, 1, '管理者擁有所有權限', 0, '2019-10-25 09:47:21', null);
INSERT INTO sys_role (id, name, code, type, state, remark, version, create_time, update_time) VALUES (14, '管理者12', 'admin12', null, 1, '管理者擁有所有權限', 0, '2019-10-25 09:47:21', null);
INSERT INTO sys_role (id, name, code, type, state, remark, version, create_time, update_time) VALUES (15, '管理者13', 'admin13', null, 1, '管理者擁有所有權限', 0, '2019-10-25 09:47:21', null);
INSERT INTO sys_role (id, name, code, type, state, remark, version, create_time, update_time) VALUES (16, '管理者14', 'admin14', null, 1, '管理者擁有所有權限', 0, '2019-10-25 09:47:21', null);
INSERT INTO sys_role (id, name, code, type, state, remark, version, create_time, update_time) VALUES (17, '管理者15', 'admin15', null, 1, '管理者擁有所有權限', 0, '2019-10-25 09:47:21', null);
INSERT INTO sys_role (id, name, code, type, state, remark, version, create_time, update_time) VALUES (18, '管理者16', 'admin16', null, 1, '管理者擁有所有權限', 0, '2019-10-25 09:47:21', null);
INSERT INTO sys_role (id, name, code, type, state, remark, version, create_time, update_time) VALUES (19, '管理者17', 'admin17', null, 1, '管理者擁有所有權限', 0, '2019-10-25 09:47:21', null);
INSERT INTO sys_role (id, name, code, type, state, remark, version, create_time, update_time) VALUES (20, '管理者18', 'admin18', null, 1, '管理者擁有所有權限', 0, '2019-10-25 09:47:21', null);
INSERT INTO sys_role (id, name, code, type, state, remark, version, create_time, update_time) VALUES (21, '管理者19', 'admin19', null, 1, '管理者擁有所有權限', 0, '2019-10-25 09:47:21', null);
INSERT INTO sys_role (id, name, code, type, state, remark, version, create_time, update_time) VALUES (22, '管理者20', 'admin20', null, 1, '管理者擁有所有權限', 0, '2019-10-25 09:47:21', null);
INSERT INTO sys_role (id, name, code, type, state, remark, version, create_time, update_time) VALUES (23, '管理者21', 'admin21', null, 1, '管理者擁有所有權限', 0, '2019-10-25 09:47:21', null);

INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (1, '系统管理', null, null, 'system:management', 'el-icon-s-unfold', 1, 1, 1, 0, '1權限備註', 0, '2019-10-26 11:12:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (100, '使用者管理', 1, null, 'sys:user:management', 'el-icon-s-unfold', 1, 2, 1, 0, '100權限備註', 0, '2019-10-26 11:15:48', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (200, '角色管理', 1, null, 'sys:role:management', 'el-icon-s-unfold', 1, 2, 1, 0, '200權限備註', 0, '2019-10-26 11:15:48', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (300, '權限管理', 1, null, 'sys:permission:management', 'el-icon-s-unfold', 1, 2, 1, 0, '300權限備註', 0, '2019-10-26 11:15:48', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (400, '部門管理', 1, null, 'sys:department:management', 'el-icon-s-unfold', 1, 2, 1, 0, '400權限備註', 0, '2019-10-26 11:15:48', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (500, '日志管理', 1, null, 'sys:log:manager', 'el-icon-s-custom', 1, 2, 1, 0, '500權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (1000, '使用者新增', 100, null, 'sys:user:add', 'el-icon-s-custom', 2, 3, 1, 0, '1000權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (1001, '使用者修改', 100, null, 'sys:user:update', 'el-icon-s-custom', 2, 3, 1, 0, '1001權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (1002, '使用者删除', 100, null, 'sys:user:delete', 'el-icon-s-custom', 2, 3, 1, 0, '1002權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (1003, '使用者詳情', 100, null, 'sys:user:info', 'el-icon-s-custom', 2, 3, 1, 0, '1003權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (1004, '使用者分頁列表', 100, null, 'sys:user:page', 'el-icon-s-custom', 2, 3, 1, 0, '1004權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (1005, '使用者修改密碼', 100, null, 'sys:user:update:password', 'el-icon-s-custom', 2, 3, 1, 0, '1005權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (1006, '權限備註修改大頭照', 100, null, 'sys:user:update:head', 'el-icon-s-custom', 2, 3, 1, 0, '1006權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (1007, '使用者重置密码', 100, null, 'sys:user:reset:password', 'el-icon-s-custom', 2, 3, 1, 0, '1007權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (2000, '角色新增', 200, null, 'sys:role:add', 'el-icon-s-custom', 2, 3, 1, 0, '2000權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (2001, '角色修改', 200, null, 'sys:role:update', 'el-icon-s-custom', 2, 3, 1, 0, '2001權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (2002, '角色删除', 200, null, 'sys:role:delete', 'el-icon-s-custom', 2, 3, 1, 0, '2002權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (2003, '角色詳情', 200, null, 'sys:role:info', 'el-icon-s-custom', 2, 3, 1, 0, '2003權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (2004, '角色分頁列表', 200, null, 'sys:role:page', 'el-icon-s-custom', 2, 3, 1, 0, '2004權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (2005, '角色列表', 200, null, 'sys:role:list', 'el-icon-s-custom', 2, 3, 1, 0, '2005權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (2006, '角色權限ID列表', 200, null, 'sys:permission:three-ids-by-role-id', 'el-icon-s-custom', 2, 3, 1, 0, '2006權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (3000, '權限新增', 300, null, 'sys:permission:add', 'el-icon-s-custom', 2, 3, 1, 0, '3000權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (3001, '權限修改', 300, null, 'sys:permission:update', 'el-icon-s-custom', 2, 3, 1, 0, '3001權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (3002, '權限删除', 300, null, 'sys:permission:delete', 'el-icon-s-custom', 2, 3, 1, 0, '3002權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (3003, '權限詳情', 300, null, 'sys:permission:info', 'el-icon-s-custom', 2, 3, 1, 0, '3003權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (3004, '權限分頁列表', 300, null, 'sys:permission:page', 'el-icon-s-custom', 2, 3, 1, 0, '3004權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (3005, '權限所有列表', 300, null, 'sys:permission:all:menu:list', 'el-icon-s-custom', 2, 3, 1, 0, '3005權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (3006, '權限所有樹狀列表', 300, null, 'sys:permission:all:menu:tree', 'el-icon-s-custom', 2, 3, 1, 0, '3006權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (3007, '權限使用者列表', 300, null, 'sys:permission:menu:list', 'el-icon-s-custom', 2, 3, 1, 0, '3007權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (3008, '權限使用者樹狀列表', 300, null, 'sys:permission:menu:tree', 'el-icon-s-custom', 2, 3, 1, 0, '3008權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (3009, '權限使用者代碼列表', 300, null, 'sys:permission:codes', 'el-icon-s-custom', 2, 3, 1, 0, '3009權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (3010, '導覽選單', 300, null, 'sys:permission:nav-menu', 'el-icon-s-custom', 2, 3, 1, 0, '3010權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (3011, '角色權限修改', 300, null, 'sys:role-permission:update', 'el-icon-s-custom', 2, 3, 1, 0, '3011權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (4000, '部門新增', 400, null, 'sys:department:add', 'el-icon-s-custom', 2, 3, 1, 0, '4000權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (4001, '部門修改', 400, null, 'sys:department:update', 'el-icon-s-custom', 2, 3, 1, 0, '4001權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (4002, '部門删除', 400, null, 'sys:department:delete', 'el-icon-s-custom', 2, 3, 1, 0, '4002權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (4003, '部門詳情', 400, null, 'sys:department:info', 'el-icon-s-custom', 2, 3, 1, 0, '4003權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (4004, '部門分頁列表', 400, null, 'sys:department:page', 'el-icon-s-custom', 2, 3, 1, 0, '4004權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (4005, '部門列表', 400, null, 'sys:department:list', 'el-icon-s-custom', 2, 3, 1, 0, '4005權限備註', 1, '2019-10-26 11:18:40', '2020-03-09 00:50:13');
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (4006, '部門樹狀列表', 400, null, 'sys:department:all:tree', 'el-icon-s-custom', 2, 3, 1, 0, '4006權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (5001, '操作日志列表', 500, null, 'sys:operation:log:page', 'el-icon-s-custom', 2, 3, 1, 0, '5001權限備註', 0, '2019-10-26 11:18:40', null);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version, create_time, update_time) VALUES (5002, '登入日志列表', 500, null, 'sys:login:log:page', 'el-icon-s-custom', 2, 3, 1, 0, '5002權限備註', 0, '2019-10-26 11:18:40', null);

INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (303, 1, 3008, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (304, 1, 1, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (305, 1, 3009, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (306, 1, 3010, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (307, 1, 3011, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (308, 1, 200, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (309, 1, 5001, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (310, 1, 5002, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (311, 1, 2000, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (312, 1, 400, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (313, 1, 2001, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (314, 1, 2002, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (315, 1, 2003, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (316, 1, 2004, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (317, 1, 2005, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (318, 1, 2006, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (319, 1, 4000, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (320, 1, 4001, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (321, 1, 4002, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (322, 1, 4003, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (323, 1, 100, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (324, 1, 4004, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (325, 1, 4005, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (326, 1, 4006, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (327, 1, 1000, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (328, 1, 1001, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (329, 1, 1002, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (330, 1, 1003, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (331, 1, 1004, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (332, 1, 300, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (333, 1, 1005, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (334, 1, 1006, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (335, 1, 1007, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (336, 1, 500, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (337, 1, 3000, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (338, 1, 3001, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (339, 1, 3002, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (340, 1, 3003, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (341, 1, 3004, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (342, 1, 3005, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (343, 1, 3006, 1, null, 0, '2020-04-01 00:14:36', null);
INSERT INTO sys_role_permission (id, role_id, permission_id, state, remark, version, create_time, update_time) VALUES (344, 1, 3007, 1, null, 0, '2020-04-01 00:14:36', null);

INSERT INTO foo_bar (id, name, foo, bar, remark, state, version, create_time, update_time) VALUES (1, 'test add', 'hello', 'world', '备注', 1, 0, '2020-03-20 11:22:35', null);

INSERT INTO example_order (id, name, order_no, remark, state, version, create_time, update_time) VALUES (1, 'AAA', null, null, 1, 0, '2020-03-12 22:25:35', null);
INSERT INTO example_order (id, name, order_no, remark, state, version, create_time, update_time) VALUES (2, 'BBB', null, null, 1, 0, '2020-03-12 22:25:35', null);
INSERT INTO example_order (id, name, order_no, remark, state, version, create_time, update_time) VALUES (3, 'CCC', null, null, 1, 0, '2020-03-12 22:25:35', null);

INSERT INTO sys_login_log (id, request_id, username, ip, area, operator, token, type, success, code, exception_message, user_agent, browser_name, browser_version, engine_name, engine_version, os_name, platform_name, mobile, device_name, device_model, remark, create_time, update_time) VALUES (1, '1242813712335691777', 'admin', '127.0.0.1', '本機地址', null, 'c87aaffa35dadafb066cf18679eab36e', 1, 1, 200, null, 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36', 'Chrome', '80.0.3987.149', 'Webkit', '537.36', 'OSX', 'Mac', 0, null, null, null, '2020-03-25 22:01:11', null);
INSERT INTO sys_login_log (id, request_id, username, ip, area, operator, token, type, success, code, exception_message, user_agent, browser_name, browser_version, engine_name, engine_version, os_name, platform_name, mobile, device_name, device_model, remark, create_time, update_time) VALUES (2, '1242813887884091393', 'admin', '127.0.0.1', '本機地址', null, 'c87aaffa35dadafb066cf18679eab36e', 2, 1, 200, null, 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36', 'Chrome', '80.0.3987.149', 'Webkit', '537.36', 'OSX', 'Mac', 0, null, null, null, '2020-03-25 22:01:48', null);
INSERT INTO sys_login_log (id, request_id, username, ip, area, operator, token, type, success, code, exception_message, user_agent, browser_name, browser_version, engine_name, engine_version, os_name, platform_name, mobile, device_name, device_model, remark, create_time, update_time) VALUES (3, '1242814069371625474', 'admin', '127.0.0.1', '本機地址', null, null, 1, 0, null, '用户名或密碼錯誤', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36', 'Chrome', '80.0.3987.149', 'Webkit', '537.36', 'OSX', 'Mac', 0, null, null, null, '2020-03-25 22:02:25', null);
INSERT INTO sys_login_log (id, request_id, username, ip, area, operator, token, type, success, code, exception_message, user_agent, browser_name, browser_version, engine_name, engine_version, os_name, platform_name, mobile, device_name, device_model, remark, create_time, update_time) VALUES (4, '1242814192096960513', null, '127.0.0.1', '本機地址', null, null, 2, 0, null, 'token不能為空', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36', 'Chrome', '80.0.3987.149', 'Webkit', '537.36', 'OSX', 'Mac', 0, null, null, null, '2020-03-25 22:02:54', null);

INSERT INTO sys_operation_log (id, request_id, user_id, user_name, name, ip, area, operator, path, module, class_name, method_name, request_method, content_type, request_body, param, token, type, success, code, message, exception_name, exception_message, browser_name, browser_version, engine_name, engine_version, os_name, platform_name, mobile, device_name, device_model, remark, create_time, update_time) VALUES (1, '1242805276474634241', null, null, 'helloWorld', '127.0.0.1', '本機地址', null, '/api/hello/world', null, 'io.geekidea.springbootplus.system.controller.HelloWorldController', 'helloWorld', 'GET', null, 0, null, null, 0, 1, 200, '操作成功', null, null, 'Chrome', '80.0.3987.149', 'Webkit', '537.36', 'OSX', 'Mac', 0, null, null, '', '2020-03-25 21:27:22', null);
INSERT INTO sys_operation_log (id, request_id, user_id, user_name, name, ip, area, operator, path, module, class_name, method_name, request_method, content_type, request_body, param, token, type, success, code, message, exception_name, exception_message, browser_name, browser_version, engine_name, engine_version, os_name, platform_name, mobile, device_name, device_model, remark, create_time, update_time) VALUES (2, '1242820418688049153', null, null, 'FooBar分页列表', '127.0.0.1', '本機地址', null, '/api/fooBar/getPageList', 'foobar', 'com.example.foobar.controller.FooBarController', 'getFooBarPageList', 'POST', 'application/json', 1, '{"pageIndex":1,"pageSize":10}', null, 7, 1, 200, '操作成功', null, null, 'Chrome', '80.0.3987.149', 'Webkit', '537.36', 'OSX', 'Mac', 0, null, null, '', '2020-03-25 22:27:33', null);
