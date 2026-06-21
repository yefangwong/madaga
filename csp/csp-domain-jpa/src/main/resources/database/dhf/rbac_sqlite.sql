-- SQLite Database initialization for RBAC schema

CREATE TABLE IF NOT EXISTS sys_role (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(32) NOT NULL,
    code VARCHAR(100),
    type INTEGER,
    state INTEGER DEFAULT 1 NOT NULL,
    remark VARCHAR(200),
    version INTEGER DEFAULT 0 NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_time TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sys_permission (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(32),
    parent_id BIGINT,
    url VARCHAR(200),
    code VARCHAR(100) NOT NULL UNIQUE,
    icon VARCHAR(100),
    type INTEGER NOT NULL,
    level INTEGER NOT NULL,
    state INTEGER DEFAULT 1 NOT NULL,
    sort INTEGER DEFAULT 0 NOT NULL,
    remark VARCHAR(200),
    version INTEGER DEFAULT 0 NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_time TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sys_role_permission (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    state INTEGER DEFAULT 1 NOT NULL,
    remark VARCHAR(200),
    version INTEGER DEFAULT 0 NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_time TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sys_user (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(20) NOT NULL UNIQUE,
    nickname VARCHAR(20),
    password VARCHAR(64) NOT NULL,
    salt VARCHAR(32),
    phone VARCHAR(20) NOT NULL,
    gender INTEGER DEFAULT 1 NOT NULL,
    head VARCHAR(200),
    remark VARCHAR(200),
    state INTEGER DEFAULT 1 NOT NULL,
    department_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    deleted INTEGER DEFAULT 0 NOT NULL,
    version INTEGER DEFAULT 0 NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP
);

-- Delete existing records (if any) to avoid constraint failures on multiple runs
DELETE FROM sys_role;
DELETE FROM sys_user;
DELETE FROM sys_permission;
DELETE FROM sys_role_permission;

-- Insert roles
INSERT INTO sys_role (id, name, code, type, state, remark, version) VALUES (1, '管理者', 'admin', 1, 1, '管理者擁有所有權限', 0);
INSERT INTO sys_role (id, name, code, type, state, remark, version) VALUES (2, '測試人員', 'test', 1, 1, '測試人員擁有部分權限', 0);
INSERT INTO sys_role (id, name, code, type, state, remark, version) VALUES (3, '訪客', 'guest', 1, 1, '訪客無任何權限', 0);

-- Insert users (Password is 'password123' BCrypt encrypted)
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, remark, state, department_id, role_id, deleted, version) 
VALUES (1, 'admin@madaga.com', '系統管理員', '$2a$10$ZxkvUFkCa1lvB6QJVUeX6.8T.OpZiTQLVJmOQMYqNHHDm.nKytf7i', 'salt', '12345678', 1, 'Admin Account', 1, 1, 1, 0, 1);
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, remark, state, department_id, role_id, deleted, version) 
VALUES (2, 'test@madaga.com', '測試用戶', '$2a$10$ZxkvUFkCa1lvB6QJVUeX6.8T.OpZiTQLVJmOQMYqNHHDm.nKytf7i', 'salt', '12345679', 1, 'Tester Account', 1, 1, 2, 0, 1);
INSERT INTO sys_user (id, username, nickname, password, salt, phone, gender, remark, state, department_id, role_id, deleted, version) 
VALUES (3, 'guest@madaga.com', '訪客用戶', '$2a$10$ZxkvUFkCa1lvB6QJVUeX6.8T.OpZiTQLVJmOQMYqNHHDm.nKytf7i', 'salt', '12345680', 1, 'Guest Account', 1, 1, 3, 0, 1);



-- Insert permissions
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version) 
VALUES (1, '系統管理', NULL, NULL, 'system:management', 'el-icon-s-unfold', 1, 1, 1, 0, '系統管理選單', 0);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version) 
VALUES (100, '使用者管理', 1, NULL, 'sys:user:management', 'el-icon-s-unfold', 1, 2, 1, 0, '使用者管理選單', 0);
INSERT INTO sys_permission (id, name, parent_id, url, code, icon, type, level, state, sort, remark, version) 
VALUES (1003, '使用者詳情', 100, NULL, 'sys:user:info', 'el-icon-s-custom', 2, 3, 1, 0, '使用者詳情權限', 0);

-- Insert role permissions
-- Admin has all 3 permissions
INSERT INTO sys_role_permission (role_id, permission_id, state) VALUES (1, 1, 1);
INSERT INTO sys_role_permission (role_id, permission_id, state) VALUES (1, 100, 1);
INSERT INTO sys_role_permission (role_id, permission_id, state) VALUES (1, 1003, 1);

-- Tester only has 'sys:user:info'
INSERT INTO sys_role_permission (role_id, permission_id, state) VALUES (2, 1003, 1);
