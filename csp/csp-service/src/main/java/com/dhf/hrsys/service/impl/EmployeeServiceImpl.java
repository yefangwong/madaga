package com.dhf.hrsys.service.impl;

import com.dhf.hrsys.dao.EmployeeDao;
import com.dhf.hrsys.service.EmployeeService;
import com.hongfang.csp.system.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    EmployeeDao empDao;
    public EmployeeServiceImpl(EmployeeDao empDao) {
       this.empDao = empDao;
    }

    public List<Employee> searchAll() {
        return null;
    }

    @Override
    public List<Employee> search(Employee condition) {
        List<Employee> list = empDao.search(condition);
        return list;
    }

    @Override
    public Employee searchById(int id) {
        return empDao.searchById(id);
    }

    @Override
    public boolean add(Employee emp) {
        int result = empDao.add(emp);
        return result > 0;
    }

    @Override
    public boolean update(Employee emp) {
        int result = empDao.update(emp);
        return result > 0;
    }

    @Override
    public boolean delete(Employee emp) {
        int result = empDao.delete(emp.getId());
        return result > 0;
    }
}
