package com.dhf.hrsys.controller;

import com.dhf.hrsys.service.DepartmentService;
import com.dhf.hrsys.service.EmployeeService;
import com.dhf.hrsys.service.IGeneralDAOService;
import com.hongfang.csp.system.entity.Department;
import com.hongfang.csp.system.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class EmployeeControllerTest {

    private EmployeeController employeeController;

    @Mock
    private IGeneralDAOService generalDaoService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private DepartmentService departmentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        employeeController = new EmployeeController(generalDaoService, employeeService, departmentService);
    }

    @Test
    public void testGetDepList() {
        List<Department> mockList = new ArrayList<>();
        Department d1 = new Department(); d1.setName("資訊部");
        Department d2 = new Department(); d2.setName("財務部");
        mockList.add(d1); mockList.add(d2);
        when(departmentService.search(null)).thenReturn(mockList);

        List<Department> depList = employeeController.getDepList();
        assertNotNull(depList);
        assertEquals(2, depList.size());
        assertEquals("資訊部", depList.get(0).getName());
        assertEquals("財務部", depList.get(1).getName());
    }

    @Test
    public void testSearch() throws Exception {
        Employee condition = new Employee();
        List<Employee> expectedList = new ArrayList<>();
        expectedList.add(new Employee());
        
        when(employeeService.search(condition)).thenReturn(expectedList);

        List<Employee> actualList = employeeController.search(condition);
        
        assertNotNull(actualList);
        assertEquals(1, actualList.size());
    }

    @Test
    public void testShow() {
        assertEquals("emp/show", employeeController.show());
    }

    @Test
    public void testShowAdd() {
        org.springframework.web.servlet.ModelAndView mv = employeeController.showAdd();
        assertNotNull(mv);
        assertEquals("emp/add", mv.getViewName());
    }
}
