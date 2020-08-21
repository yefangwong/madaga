/*
 * Copyright 2020 yefangwong(https://github.com/yefangwong)
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

package com.hongfang.csp.system.mapper;import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hongfang.csp.system.entity.SysUser;
import com.hongfang.csp.system.vo.SysUserQueryVo;

import java.io.Serializable;

/**
 * <pre>
 * 系统使用者 Mapper 介面
 * </pre>
 *
 * @author yefangwong
 * @since 2020-04-16
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 根据ID查詢使用者資訊
     *
     * @param id
     * @return
     */
    SysUserQueryVo getSysUserById(Serializable id);

}
