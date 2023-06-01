package com.dhf.hrsys.controller;

import com.hongfang.csp.system.entity.Department;
import com.hongfang.csp.system.entity.Employee;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("emp")
public class EmployeeController {

    private static List<Department> depList = new ArrayList<Department>();

    static {
        Department dep = new Department();
        dep.setId(1);
        dep.setNumber(1369);
        dep.setName("資訊部");
        depList.add(dep);
        dep = new Department();
        dep.setId(2);
        dep.setNumber(2369);
        dep.setName("財務部");
        depList.add(dep);
    }

    @RequestMapping("search")
    public ModelAndView search() {
       ModelAndView mv = new ModelAndView("emp/show");
       List<Employee> list = getEmployeeList();
       List<Department> depList = new ArrayList<Department>();
       mv.addObject("list", list);
       mv.addObject("depList", depList);
       return mv;
    }

    private List<Employee> getEmployeeList() {
        List<Employee> list = new ArrayList<Employee>();
        Employee e = new Employee();
        e.setId(1);
        e.setName("翁Ｘ芳");
        e.setAge(18);
        e.setGender("男");
        e.setDep(depList.get(0));
        list.add(e);
        e = new Employee();
        e.setId(2);
        e.setName("劉Ｘ華");
        e.setAge(18);
        e.setGender("女");
        e.setDep(depList.get(1));
        list.add(e);
        return list;
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
    }

    private void add(HttpServletRequest request, HttpServletResponse response) {
    }

    private void search(HttpServletRequest request, HttpServletResponse response) {
    }
}
