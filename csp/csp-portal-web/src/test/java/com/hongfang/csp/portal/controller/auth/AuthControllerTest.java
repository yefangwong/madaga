package com.hongfang.csp.portal.controller.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthControllerTest {

    private AuthController authController;

    @BeforeEach
    public void setUp() {
        authController = new AuthController();
    }

    @Test
    public void testSignUpPage() {
        String view = authController.signUpPage();
        assertEquals("auth/signUp", view);
    }
}
