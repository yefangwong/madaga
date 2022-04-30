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

package com.hongfang.csp.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.hongfang.csp.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

/**
 * <pre>
 *     系統使用者
 * </pre>
 *
 * @author yefangwong
 * @since 2020-04-05
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysUser對象", description = "系統使用者")
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主鍵")
    @NotNull(message = "ID不能為空")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("使用者姓名")
    @NotNull(message = "使用者姓名不能為空")
    private String username;

    @ApiModelProperty("暱稱")
    private String nickname;

    @ApiModelProperty("密碼")
    private String password;

    @ApiModelProperty("鹽值")
    private String salt;

    @ApiModelProperty("手機號碼")
    @NotBlank(message = "手機號碼不能為空")
    private String phone;

    @ApiModelProperty("性别，0：女，1：男，預設1")
    private Integer gender;

    @ApiModelProperty("大頭照")
    private String head;

    @ApiModelProperty("備註")
    private String remark;

    @ApiModelProperty("狀態，0：禁用，1：啟用，2：鎖定")
    private Integer state;

    @ApiModelProperty("部門id")
    @NotNull(message = "部門id不能為空")
    private Long departmentId;

    @ApiModelProperty("角色id")
    @NotNull(message = "角色id不能為空")
    private Long roleId;

    @ApiModelProperty("邏輯删除，0：未删除，1：已删除")
    @Null(message = "邏輯删除不用傳")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty("版本")
    @Null(message = "版本不用傳")
    @Version
    private Integer version;

    @ApiModelProperty("建立時間")
    @Null(message = "建立時間不用傳")
    private Date createTime;

    @ApiModelProperty("修改時間")
    @Null(message = "修改時間不用傳")
    private Date updateTime;
}
