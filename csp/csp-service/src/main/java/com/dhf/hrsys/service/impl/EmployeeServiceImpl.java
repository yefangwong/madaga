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
        log.debug("service search executed!");
        List<Employee> list = empDao.search(condition);
        log.debug("service size:" + list.size());
        return list;
    }

    @Override
    public Employee searchById(int id) {
        return null;
    }

    @Override
    public boolean add(Employee emp) {
        return false;
    }

    @Override
    public boolean update(Employee emp) {
        return false;
    }

    @Override
    public boolean delete(Employee emp) {
        return false;
    }
}
