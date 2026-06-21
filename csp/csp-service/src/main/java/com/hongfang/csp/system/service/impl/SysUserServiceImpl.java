package com.hongfang.csp.system.service.impl;

import com.hongfang.csp.system.mapper.SysUserMapper;
import com.hongfang.csp.system.service.SysUserService;
import com.hongfang.csp.system.vo.SysUserQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override 
    public SysUserQueryVo getSysUserById(Serializable id) throws Exception {
        return sysUserMapper.getSysUserById(id);
    }
}
