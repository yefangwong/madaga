package com.dhf.hrsys.service.impl;

import com.dhf.hrsys.service.EmployeeService;
import com.hongfang.csp.system.entity.Employee;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Override
    public Employee searchById(int id) {

        Employee emp = new Employee();
        emp.setId(1);
        emp.setName("mark");
        emp.setGender("男");
        emp.setAge(18);
        return emp;
    }
}
