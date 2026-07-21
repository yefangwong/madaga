/*
 * Copyright 2022 yefangwong(https://github.com/yefangwong)
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

package com.hongfang.csp.portal.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "madaga.home=${user.dir}/../..")
public class Test01ApplicationTest {
    @Autowired
    private TestController testController;

    @Autowired
    private com.hongfang.csp.system.mapper.SysUserMapper sysUserMapper;

    @Test
    @org.junit.jupiter.api.Disabled("Legacy test requiring active HTTP security session")
    public void testTest() {
        testController.test();
    }

    @Autowired
    private com.hongfang.csp.portal.security.UserDetailsServiceImpl userDetailsService;

    @Test
    public void testUserQuery() {
        org.springframework.security.core.userdetails.UserDetails userDetails = userDetailsService.loadUserByUsername("admin@madaga.com");
        System.out.println("====== DB SYS USER DETAILS RESULT: " + userDetails);
        org.junit.jupiter.api.Assertions.assertNotNull(userDetails);

        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        boolean match = encoder.matches("password123", userDetails.getPassword());
        System.out.println("====== TEST BCRYPT MATCH: " + match);
        org.junit.jupiter.api.Assertions.assertTrue(match);
    }
}
