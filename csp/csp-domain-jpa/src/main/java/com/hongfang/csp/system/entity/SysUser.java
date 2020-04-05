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
import com.hongfang.csp.base.entity.BaseEntity;
import io.geekidea.springbootplus.framework.core.validator.groups.Add;
import io.geekidea.springbootplus.framework.core.validator.groups.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

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
    @NotNull(message = "ID不能為空", groups = {Update.class})
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("使用者姓名")
    @NotNull(message = "使用者姓名不能為空", groups = {Add.class})
    private String username;

    @ApiModelProperty("暱稱")
    private String nickname;

    @ApiModelProperty("密碼")
    private String password;

    @ApiModelProperty("鹽值")
    private String salt;
}
