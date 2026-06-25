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

import com.hongfang.csp.system.entity.SysRole;

/**
 * 系統角色 Mapper 介面
 * @author yefangwong
 * @since 2026-06-21
 */
public interface SysRoleMapper {
    /**
     * 根據ID查詢角色資訊
     * @param id
     * @return
     */
    SysRole getSysRoleById(Long id);
}
