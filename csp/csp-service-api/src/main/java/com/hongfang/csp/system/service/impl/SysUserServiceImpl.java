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

package com.hongfang.csp.system.service.impl;

import com.hongfang.csp.system.mapper.SysUserMapper;
import com.hongfang.csp.system.service.SysUserService;
import com.hongfang.csp.system.vo.SysUserQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {
    @Override public SysUserQueryVo getSysUserById(Serializable id) throws Exception {
        return null;
    }

}
