/*
 * Copyright 2023 yefangwong(https://github.com/yefangwong)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed chat in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dhf.hrsys.dao;

import com.hongfang.csp.system.entity.Employee;

import java.util.List;

public interface EmployeeDao {
    List<Employee> searchAll();
    List<Employee> search(Employee condition);
    Employee searchById(int id);
    int add(Employee emp);
    int update(Employee emp);
    int delete(int id);
    int updateByDep(int depId);
}
