/*
 * Copyright 2020 yefangwong(https://github.com/yefangwong)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed chat in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hongfang.csp.system.service;
import com.hongfang.csp.system.entity.SysUser;
import com.hongfang.csp.system.vo.SysUserQueryVo;

import java.io.Serializable;

/**
 * <pre>
 * 系统使用者 服務類
 * </pre>
 *
 * @author yefangwong
 * @since 2020-04-16
 */
public interface SysUserService {
    /**
     * 根据ID獲得查询對象
     *
     * @param id
     * @return
     * @throws Exception
     */
    SysUserQueryVo getSysUserById(Serializable id) throws Exception;
}
