package com.hongfang.csp.portal.controller;

import com.hongfang.csp.system.service.SysUserService;
import com.hongfang.csp.system.vo.SysUserQueryVo;
import com.hongfang.csp.webframeworx.common.api.ApiResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class SysUserControllerTest {

    @InjectMocks
    private SysUserController sysUserController;

    @Mock
    private SysUserService sysUserService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetSysUser() throws Exception {
        Long userId = 1L;
        SysUserQueryVo expectedUser = new SysUserQueryVo();
        expectedUser.setId(userId);
        expectedUser.setUsername("testUser");

        when(sysUserService.getSysUserById(userId)).thenReturn(expectedUser);

        ApiResult<SysUserQueryVo> result = sysUserController.getSysUser(userId);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals("testUser", result.getData().getUsername());
    }
}
