package com.dhf.hrsys.controller;

import com.dhf.hrsys.service.DepartmentService;
import com.dhf.hrsys.service.EmployeeService;
import com.dhf.hrsys.service.GeneralDAOServiceFacade;
import com.dhf.util.JXLExcelBuilder;
import com.hongfang.csp.system.entity.Department;
import com.hongfang.csp.system.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("emp")
@Slf4j
public class EmployeeController {
    final GeneralDAOServiceFacade serviceFacade;
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

    public EmployeeController(GeneralDAOServiceFacade serviceFacade) {
        this.serviceFacade = serviceFacade;
    }

    @RequestMapping("search")
    public ModelAndView search() throws Exception {
       ModelAndView mv = new ModelAndView("emp/show");
       Condition condition = new Condition();
       List<HashMap> list = getEmployeeList(condition);
       log.info("emp size:{}", list.size());
       List<Department> depList = new ArrayList<Department>();
       mv.addObject("c", condition);
       mv.addObject("list", list);
       mv.addObject("depList", depList);
       return mv;
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
        return serviceFacade.queryForList("CUST_EMPLOYEE.SQL1");
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
