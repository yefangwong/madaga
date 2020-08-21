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

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hongfang.csp.system.entity.SysUser;
import com.hongfang.csp.system.mapper.SysUserMapper;
import com.hongfang.csp.system.service.SysUserService;
import com.hongfang.csp.system.vo.SysUserQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override public SysUserQueryVo getSysUserById(Serializable id) throws Exception {
        return sysUserMapper.getSysUserById(id);
    }

    @Override public boolean saveBatch(Collection<SysUser> entityList, int batchSize) {
        return false;
    }

    @Override public boolean saveOrUpdateBatch(Collection<SysUser> entityList, int batchSize) {
        return false;
    }

    @Override public boolean updateBatchById(Collection<SysUser> entityList, int batchSize) {
        return false;
    }

    @Override public boolean saveOrUpdate(SysUser entity) {
        return false;
    }

    @Override public SysUser getOne(Wrapper<SysUser> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override public Map<String, Object> getMap(Wrapper<SysUser> queryWrapper) {
        return null;
    }

    @Override public <V> V getObj(Wrapper<SysUser> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override public BaseMapper<SysUser> getBaseMapper() {
        return null;
    }
}
