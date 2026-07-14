package com.dhf.hrsys.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("department")
@Slf4j
public class DepartmentController {
    @RequestMapping(value="show")
    public String show() {
        return "department/show";
    }
}
