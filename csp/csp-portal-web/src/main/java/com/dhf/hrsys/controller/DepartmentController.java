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

package com.dhf.hrsys.controller;

import com.dhf.hrsys.service.DepartmentService;
import com.hongfang.csp.system.entity.Department;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("manage/department")
@Slf4j
public class DepartmentController {

    private final DepartmentService depService;

    @Autowired
    public DepartmentController(DepartmentService depService) {
        this.depService = depService;
    }

    @RequestMapping(value="show")
    public String show() {
        return "department/show";
    }

    @RequestMapping("search")
    @ResponseBody
    public List<Department> search(Department condition) {
        return depService.search(condition);
    }

    @RequestMapping("showAdd")
    public String showAdd() {
        return "department/add";
    }

    @PostMapping("add")
    @ResponseBody
    public ResponseEntity<String> add(@RequestBody Department dep) {
        boolean success = depService.add(dep);
        if (success) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(500).body("Failed");
        }
    }

    @RequestMapping("showUpdate")
    public ModelAndView showUpdate(Integer id) {
        Department dep = depService.searchById(id);
        ModelAndView mv = new ModelAndView("department/update");
        mv.addObject("dep", dep);
        return mv;
    }

    @PostMapping("update")
    @ResponseBody
    public ResponseEntity<String> update(@RequestBody Department dep) {
        boolean success = depService.update(dep);
        if (success) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(500).body("Failed");
        }
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResponseEntity<String> delete(Integer id) {
        Department dep = new Department();
        dep.setId(id);
        boolean success = depService.delete(dep);
        if (success) {
            return ResponseEntity.ok("Success");
        } else {
            // Deletion failed (most likely because the department has active employees)
            return ResponseEntity.status(400).body("無法刪除：該部門下仍有員工！");
        }
    }
}
