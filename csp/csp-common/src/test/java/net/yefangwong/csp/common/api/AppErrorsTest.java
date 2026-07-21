package net.yefangwong.csp.common.api;

import junit.framework.TestCase;

public class AppErrorsTest extends TestCase {

    public void testFluentAddAndAccessors() {
        AppErrors errors = AppErrors.create()
                .add("E1", "Error One")
                .addValidation("username", "Required")
                .add(AppError.of("E3", "Error Three", "age"));

        assertTrue(errors.hasErrors());
        assertFalse(errors.isEmpty());
        assertEquals(3, errors.size());
        assertEquals("Error One", errors.getFirstMessage());
    }

    public void testAddAll() {
        AppErrors e1 = AppErrors.create().add("A1", "Msg 1");
        AppErrors e2 = AppErrors.create().add("A2", "Msg 2");

        e1.addAll(e2);
        assertEquals(2, e1.size());
    }

    public void testIteration() {
        AppErrors errors = AppErrors.of(
                AppError.of("E1", "Msg1"),
                AppError.of("E2", "Msg2")
        );

        int count = 0;
        for (AppError err : errors) {
            assertNotNull(err.getCode());
            count++;
        }
        assertEquals(2, count);
    }

    public void testClear() {
        AppErrors errors = AppErrors.create().add("E1", "Msg1");
        assertTrue(errors.hasErrors());

        errors.clear();
        assertFalse(errors.hasErrors());
        assertEquals(0, errors.size());
        assertNull(errors.getFirstMessage());
    }
}
