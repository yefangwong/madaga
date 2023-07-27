package com.dhf.hrsys.service;

import com.hongfang.csp.system.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> searchAll();
    List<Employee> search(Employee condition);
    Employee searchById(int id);
    boolean add(Employee emp);
    boolean update(Employee emp);
    boolean delete(Employee emp);
}
