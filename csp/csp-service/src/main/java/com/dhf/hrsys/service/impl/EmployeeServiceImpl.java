package com.dhf.hrsys.service.impl;

import com.dhf.hrsys.dao.impl.EmployeeDao;
import com.dhf.hrsys.service.EmployeeService;
import com.hongfang.csp.system.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    EmployeeDao empDao;

    public List<Employee> searchAll() {
        return empDao.searchAll();
    }

    @Override
    public List<Employee> search(Employee condition) {
        List<Employee> list = empDao.search(condition);
        return list;
    }

    @Override
    public Employee searchById(int id) {

//        Employee emp = new Employee();
//        emp.setId(1);
//        emp.setName("mark");
//        emp.setGender("男");
//        emp.setAge(18);
        return empDao.searchById(id);
    }

    @Override
    public boolean add(Employee emp) {
        return empDao.add(emp) == 1 ? true : false;
    }

    @Override
    public boolean update(Employee emp) {
        return empDao.update(emp) == 1 ? true : false;
    }

    @Override
    public boolean delete(Employee emp) {
        return  empDao.delete(emp.getId()) == 1 ? true : false;
    }
}
