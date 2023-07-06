/*
 * Copyright 2023 yefangwong(https://github.com/yefangwong)
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

package com.dhf.hrsys.annotation;

import java.lang.annotation.*;

/**
 * 樣式裝飾器 註解 在 Controller 中註解
 * Mark
 * @date 20230613
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Layout {
    /**
     * no layout will be used
     */
    String none = "none";
    /**
     * default layout will be used
     */
    String defaultLayout = "/layout/default";

    String value() default defaultLayout;
}
