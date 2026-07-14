/*
 * Copyright 2026 yefangwong(https://github.com/yefangwong)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dhf.hrsys.service.impl;

import com.dhf.hrsys.dao.DepartmentDao;
import com.dhf.hrsys.dao.EmployeeDao;
import com.dhf.hrsys.service.DepartmentService;
import com.hongfang.csp.system.entity.Department;
import com.hongfang.csp.system.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentDao depDao;

    @Autowired
    private EmployeeDao empDao;

    @Override
    public List<Department> search(Department condition) {
        return depDao.search(condition);
    }

    @Override
    public Department searchById(int id) {
        return depDao.searchById(id);
    }

    @Override
    public boolean add(Department dep) {
        int result = depDao.add(dep);
        return result > 0;
    }

    @Override
    public boolean update(Department dep) {
        int result = depDao.update(dep);
        return result > 0;
    }

    @Override
    public boolean delete(Department dep) {
        // Business logic check: prevent delete if department is assigned to active employees
        Employee condition = new Employee();
        Department depCond = new Department();
        depCond.setId(dep.getId());
        condition.setDep(depCond);
        
        List<Employee> employees = empDao.search(condition);
        if (employees != null && !employees.isEmpty()) {
            // Department is currently associated with employees; prevent deletion
            return false;
        }
        
        int result = depDao.delete(dep.getId());
        return result > 0;
    }
}
