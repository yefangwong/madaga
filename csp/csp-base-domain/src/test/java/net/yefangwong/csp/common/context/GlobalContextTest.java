package net.yefangwong.csp.common.context;

import junit.framework.TestCase;

public class GlobalContextTest extends TestCase {

    public void testGlobalContextFactory() {
        GlobalContext ctx = GlobalContext.of("admin@company.com", "DHF");

        assertEquals("admin@company.com", ctx.getOperatorEmail());
        assertEquals("DHF", ctx.getComCode());
        assertEquals("127.0.0.1", ctx.getClientIp());
        assertEquals("USER", ctx.getRole());
        assertNotNull(ctx.getTraceId());
        assertFalse(ctx.getTraceId().isEmpty());
    }

    public void testGlobalContextBuilder() {
        GlobalContext ctx = GlobalContext.builder()
                .operatorEmail("auditor@company.com")
                .comCode("HQ")
                .clientIp("192.168.1.100")
                .role("ADMIN")
                .traceId("trace123")
                .build();

        assertEquals("auditor@company.com", ctx.getOperatorEmail());
        assertEquals("HQ", ctx.getComCode());
        assertEquals("192.168.1.100", ctx.getClientIp());
        assertEquals("ADMIN", ctx.getRole());
        assertEquals("trace123", ctx.getTraceId());
    }

    public void testGlobalContextEqualsAndHashCode() {
        GlobalContext ctx1 = GlobalContext.builder()
                .operatorEmail("user@test.com")
                .comCode("CODE")
                .traceId("t1")
                .build();

        GlobalContext ctx2 = GlobalContext.builder()
                .operatorEmail("user@test.com")
                .comCode("CODE")
                .traceId("t1")
                .build();

        assertEquals(ctx1, ctx2);
        assertEquals(ctx1.hashCode(), ctx2.hashCode());
    }
}
