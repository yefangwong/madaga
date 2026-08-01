package net.yefangwong.csp.common.context;

import junit.framework.TestCase;

public class DataPipelineTest extends TestCase {

    public void testDataPipelineContext() {
        GlobalContext ctx = new GlobalContext("user@test.com", "madaga", "127.0.0.1", "trace-123", "tenant-1");
        DataPipeline pipeline = new DataPipeline(ctx);

        assertNotNull(pipeline.getContext());
        assertEquals("user@test.com", pipeline.getContext().getOperatorEmail());
        assertEquals("madaga", pipeline.getContext().getComCode());
        assertEquals("127.0.0.1", pipeline.getContext().getClientIp());
        assertEquals("trace-123", pipeline.getContext().getTraceId());
        assertEquals("tenant-1", pipeline.getContext().getTenantId());
    }

    public void testDataPipelinePayloadMap() {
        DataPipeline pipeline = new DataPipeline();
        String sampleReq = "sample-request-payload";
        pipeline.put(String.class, sampleReq);

        assertTrue(pipeline.hasPayload(String.class));
        assertEquals("sample-request-payload", pipeline.get(String.class));
    }
}
