package com.dhf.hrsys.controller;

import com.dhf.hrsys.service.IGeneralDAOService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqlControllerTest {

    private SqlController sqlController;

    @Mock
    private IGeneralDAOService generalDaoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sqlController = new SqlController(generalDaoService);
    }

    @Test
    public void testProcessForMyBatis() throws Exception {
        // 使用反射測試私有方法 processForMyBatis
        Method method = SqlController.class.getDeclaredMethod("processForMyBatis", String.class);
        method.setAccessible(true);

        // 測試正常 SQL
        String sql1 = "SELECT * FROM users;";
        String result1 = (String) method.invoke(sqlController, sql1);
        assertEquals("SELECT * FROM users", result1);

        // 測試 null
        String result2 = (String) method.invoke(sqlController, (String) null);
        assertEquals("", result2);

        // 測試多個分號
        String sql3 = "SELECT * FROM users;;;";
        String result3 = (String) method.invoke(sqlController, sql3);
        assertEquals("SELECT * FROM users", result3);

        // 測試沒有分號
        String sql4 = "SELECT * FROM users";
        String result4 = (String) method.invoke(sqlController, sql4);
        assertEquals("SELECT * FROM users", result4);
    }
}
