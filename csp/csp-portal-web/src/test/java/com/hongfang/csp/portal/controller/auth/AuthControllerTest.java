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

    @Test
    public void testBCrypt() {
        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        String hash = encoder.encode("password123");
        System.out.println("====== BCRYPT HASH GENERATED: " + hash);
        boolean match = encoder.matches("password123", hash);
        System.out.println("====== BCRYPT MATCH: " + match);
        org.junit.jupiter.api.Assertions.assertTrue(match);
    }
}
