package net.yefangwong.csp.common.api;

import junit.framework.TestCase;

public class AppErrorTest extends TestCase {

    public void testAppErrorCreation() {
        AppError err = AppError.of("E404", "Not Found", "userId", AppError.CATEGORY_BUSINESS);

        assertEquals("E404", err.getCode());
        assertEquals("Not Found", err.getMessage());
        assertEquals("userId", err.getField());
        assertEquals(AppError.CATEGORY_BUSINESS, err.getCategory());
    }

    public void testAppErrorValidation() {
        AppError err = AppError.validation("email", "Email format invalid");

        assertEquals("INVALID_INPUT", err.getCode());
        assertEquals("Email format invalid", err.getMessage());
        assertEquals("email", err.getField());
        assertEquals(AppError.CATEGORY_VALIDATION, err.getCategory());
    }

    public void testAppErrorBuilder() {
        AppError err = AppError.builder()
                .code("E500")
                .message("Internal Error")
                .category(AppError.CATEGORY_SYSTEM)
                .build();

        assertEquals("E500", err.getCode());
        assertEquals("Internal Error", err.getMessage());
        assertNull(err.getField());
        assertEquals(AppError.CATEGORY_SYSTEM, err.getCategory());
    }

    public void testAppErrorEqualsAndHashCode() {
        AppError err1 = AppError.of("E100", "Msg", "fieldA");
        AppError err2 = AppError.of("E100", "Msg", "fieldA");

        assertEquals(err1, err2);
        assertEquals(err1.hashCode(), err2.hashCode());
    }
}
