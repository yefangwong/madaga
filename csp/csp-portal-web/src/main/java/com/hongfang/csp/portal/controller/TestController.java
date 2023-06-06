/*
 * Copyright 2022 yefangwong(https://github.com/yefangwong)
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

package com.hongfang.csp.portal.controller;

import com.dhf.hrsys.service.EmployeeService;
import com.hongfang.csp.system.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("test")
    public ModelAndView test() {
        ModelAndView mv = new ModelAndView("test");
        //System.out.println("Hello World");
        Employee emp = employeeService.searchById(1);
        mv.addObject("msg", "<h1>Hello World!</h1>");
        mv.addObject("emp", emp);
        return mv;
    }
}
