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

package com.hongfang.csp.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 * 系统使用者 查詢结果對象
 * </pre>
 *
 * @author yefangwong
 * @date 2020-04-16
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "SysUserQueryVo對象", description = "系统用户查詢參數")
public class SysUserQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主鍵")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("暱稱")
    private String nickname;

    @ApiModelProperty("手機號碼")
    private String phone;

    @ApiModelProperty("性别，0：女，1：男，預設1")
    private Integer gender;

    @ApiModelProperty("大頭照")
    private String head;

    @ApiModelProperty("remark")
    private String remark;

    @ApiModelProperty("狀態，0：禁用，1：啟用，2：鎖定")
    private Integer state;

    @ApiModelProperty("部門id")
    private Long departmentId;

    @ApiModelProperty("角色id")
    private Long roleId;

    @ApiModelProperty("邏輯删除，0：未删除，1：已删除")
    private Integer deleted;

    @ApiModelProperty("版本")
    private Integer version;

    @ApiModelProperty("建立時間")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("部門名稱")
    private String departmentName;

    @ApiModelProperty("角色名稱")
    private String roleName;
}
