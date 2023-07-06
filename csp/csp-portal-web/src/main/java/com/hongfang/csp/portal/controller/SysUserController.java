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

package com.hongfang.csp.portal.controller;

import com.hongfang.csp.system.service.SysUserService;
import com.hongfang.csp.system.vo.SysUserQueryVo;
import com.hongfang.csp.webframeworx.common.api.ApiResult;
import com.hongfang.csp.webframeworx.common.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 * 系統使用者 前端控制器
 * </pre>
 *
 * @author yefangwong
 * @since 2020-04-17
 */
@Slf4j
@RestController
@RequestMapping("/sysUser")
@Api(value = "系统使用者API", tags = {"系統使用者"})
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 根据id取得系统使用者
     */
    @GetMapping("/info/{id}")
    public ApiResult<SysUserQueryVo> getSysUser(@PathVariable("id") Long id) throws Exception {
        SysUserQueryVo sysUserQueryVo = sysUserService.getSysUserById(id);
        return ApiResult.ok(sysUserQueryVo);
    }
}
