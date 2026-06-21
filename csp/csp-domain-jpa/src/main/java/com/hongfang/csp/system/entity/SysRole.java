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

package com.hongfang.csp.system.entity;

import com.hongfang.csp.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

/**
 * 系統角色
 * @author yefangwong
 * @since 2026-06-21
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysRole對象", description = "系統角色")
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主鍵")
    @NotNull(message = "ID不能為空")
    private Long id;

    @ApiModelProperty("角色名稱")
    @NotNull(message = "角色名稱不能為空")
    private String name;

    @ApiModelProperty("角色唯一編碼")
    private String code;

    @ApiModelProperty("角色類型")
    private Integer type;

    @ApiModelProperty("角色狀態，0：禁用，1：啟用")
    private Integer state;

    @ApiModelProperty("備註")
    private String remark;

    @ApiModelProperty("版本")
    @Null(message = "版本不用傳")
    private Integer version;

    @ApiModelProperty("建立時間")
    @Null(message = "建立時間不用傳")
    private Date createTime;

    @ApiModelProperty("修改時間")
    @Null(message = "修改時間不用傳")
    private Date updateTime;
}
