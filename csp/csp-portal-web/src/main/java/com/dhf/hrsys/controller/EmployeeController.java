package com.dhf.hrsys.controller;

import com.dhf.hrsys.service.EmployeeService;
import com.dhf.hrsys.service.IGeneralDAOService;
import com.dhf.util.JXLExcelBuilder;
import com.hongfang.csp.system.entity.Department;
import com.hongfang.csp.system.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("manage/emp")
@Slf4j
public class EmployeeController {
    final IGeneralDAOService generalDaoService;
    private static List<Department> depList = new ArrayList<Department>();
    EmployeeService empService;

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

    public EmployeeController(IGeneralDAOService generalDaoService,
                              EmployeeService empService) {
        this.generalDaoService = generalDaoService;
        this.empService = empService;
    }

    @RequestMapping(value="show")
    public String show() {
        return "emp/show";
    }

    @RequestMapping(value="showAdd")
    public ModelAndView showAdd() {
        ModelAndView mv = new ModelAndView("emp/add");
        mv.addObject("depList", depList);
        return mv;
    }

    @RequestMapping("showUpdate")
    public ModelAndView showUpdate(Integer id) {
        Employee emp = empService.searchById(id);
        ModelAndView mv = new ModelAndView("emp/update");
        mv.addObject("emp", emp);
        mv.addObject("depList", depList);
        return mv;
    }

    @RequestMapping("search")
    @ResponseBody
    public List<Employee> search(Employee condition) throws Exception {
        List<Employee> list = empService.search(condition);
        return list;
    }

    @RequestMapping("getDepList")
    @ResponseBody
    public List<Department> getDepList() {
        return depList;
    }

    @PostMapping("export")
    public ModelAndView export(HttpServletRequest req,
        HttpServletResponse res) throws Exception {
        ModelAndView mv = null;
        Condition condition = new Condition();
        List<HashMap> dataList = getEmployeeList(condition);

        res.setContentType("application/vnd.ms-excel");
        res.setHeader("Content-disposition", "attachment;filename=Report.xls");

        JXLExcelBuilder excelBuilder = new JXLExcelBuilder(res.getOutputStream());
        String titles[] = new String[] {
            "編號",
            "姓名",
            "姓別",
            "年齡",
            "部門"
        };

        String colsMapTitles[] = new String[] {
            "empId",
            "empName",
            "gender",
            "age",
            "depName"
        };

        excelBuilder.buildTitle(titles, colsMapTitles).buildBody(dataList);
        excelBuilder.build();
        
        return mv;
    }

    private List<HashMap> getEmployeeList(Condition condition) throws Exception {
        return generalDaoService.queryForList("CUST_EMPLOYEE.SQL1");
    }

    @RequestMapping("delete")
    @ResponseBody
    public org.springframework.http.ResponseEntity<String> delete(Integer id) {
        Employee emp = new Employee();
        emp.setId(id);
        boolean success = empService.delete(emp);
        if (success) {
            return org.springframework.http.ResponseEntity.ok("Success");
        } else {
            return org.springframework.http.ResponseEntity.status(500).body("Failed");
        }
    }

    @RequestMapping(value = "update")
    @ResponseBody
    public org.springframework.http.ResponseEntity<String> update(@RequestBody Employee emp) {
        boolean success = empService.update(emp);
        if (success) {
            return org.springframework.http.ResponseEntity.ok("Success");
        } else {
            return org.springframework.http.ResponseEntity.status(500).body("Failed");
        }
    }

    @PostMapping("add")
    @ResponseBody
    public org.springframework.http.ResponseEntity<String> add(@org.springframework.web.bind.annotation.RequestBody Employee emp) {
        boolean success = empService.add(emp);
        if (success) {
            return org.springframework.http.ResponseEntity.ok("Success");
        } else {
            return org.springframework.http.ResponseEntity.status(500).body("Failed");
        }
    }

    private void search(HttpServletRequest request, HttpServletResponse response) {
    }
}
