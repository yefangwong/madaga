/*
 * Copyright 2022 yefangwong(https://github.com/yefangwong)
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

package com.hongfang.csp.api.controller;
/**
 * 作 業 代 碼 ：解密加密訊息<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：PasswdController.java<br>
 * 描             述 ：<br>
 * 公             司 ：DeHongFang Intelligent Mobile Technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2022/09/04<P>
 */
import com.dehongfang.csp.base.util.EncryptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/passwd")
@RestController
public class PasswdController {
    private static final Logger logger = LoggerFactory.getLogger(PasswdController.class);

    @CrossOrigin
    @PostMapping(value = "/decode/{str}", produces="application/json")
    public String decode(@PathVariable Object str) throws Exception {
        Object result = EncryptUtil.decrypt(str);
        //decrypt
        return (String)result;
    }
}
