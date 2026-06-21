/*
 * Copyright 2026 yefangwong(https://github.com/yefangwong)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hongfang.csp.system.mapper;

import java.util.List;

/**
 * 系統權限 Mapper 介面
 * @author yefangwong
 * @since 2026-06-21
 */
public interface SysPermissionMapper {
    /**
     * 根據角色ID查詢該角色擁有的權限編碼列表
     * @param roleId
     * @return
     */
    List<String> getPermissionCodesByRoleId(Long roleId);
}
