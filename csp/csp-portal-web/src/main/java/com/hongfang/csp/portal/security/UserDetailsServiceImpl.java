package com.hongfang.csp.portal.security;

import com.hongfang.csp.system.entity.SysUser;
import com.hongfang.csp.system.mapper.SysPermissionMapper;
import com.hongfang.csp.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserMapper sysUserMapper;
    private final SysPermissionMapper sysPermissionMapper;

    @Autowired
    public UserDetailsServiceImpl(SysUserMapper sysUserMapper, SysPermissionMapper sysPermissionMapper) {
        this.sysUserMapper = sysUserMapper;
        this.sysPermissionMapper = sysPermissionMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 查詢使用者
        SysUser sysUser = sysUserMapper.getSysUserByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }




        // 2. 判斷使用者狀態 (0：禁用，1：啟用，2：鎖定)
        boolean enabled = sysUser.getState() == 1;
        boolean accountNonLocked = sysUser.getState() != 2;

        // 3. 獲取使用者權限編碼列表
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (sysUser.getRoleId() != null) {
            List<String> permissionCodes = sysPermissionMapper.getPermissionCodesByRoleId(sysUser.getRoleId());
            for (String code : permissionCodes) {
                if (code != null && !code.trim().isEmpty()) {
                    authorities.add(new SimpleGrantedAuthority(code));
                }
            }
        }

        // 4. 回傳 UserDetails 對象
        return new User(
            sysUser.getUsername(),
            sysUser.getPassword(),
            enabled,
            true, // accountNonExpired
            true, // credentialsNonExpired
            accountNonLocked,
            authorities
        );
    }
}
