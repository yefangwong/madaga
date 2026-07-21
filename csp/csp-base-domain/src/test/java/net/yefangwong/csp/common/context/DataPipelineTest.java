package net.yefangwong.csp.common.context;

import junit.framework.TestCase;
import java.util.List;

public class DataPipelineTest extends TestCase {

    static class SamplePayload {
        private String payloadName;

        public SamplePayload(String payloadName) {
            this.payloadName = payloadName;
        }

        public String getPayloadName() {
            return payloadName;
        }
    }

    public void testFluentAddAndTypeSafeGet() {
        GlobalContext ctx = GlobalContext.of("operator@company.com", "DHF");
        DataPipeline pipeline = DataPipeline.of(ctx)
                .add("Hello Pipeline")
                .add(100)
                .add(new SamplePayload("SampleData"));

        assertEquals(3, pipeline.size());
        assertFalse(pipeline.isEmpty());

        // 強型別檢索
        String strVal = pipeline.get(String.class);
        assertEquals("Hello Pipeline", strVal);

        Integer intVal = pipeline.get(Integer.class);
        assertEquals(Integer.valueOf(100), intVal);

        SamplePayload payload = pipeline.get(SamplePayload.class);
        assertNotNull(payload);
        assertEquals("SampleData", payload.getPayloadName());

        // Context 比對
        assertNotNull(pipeline.getContext());
        assertEquals("operator@company.com", pipeline.getContext().getOperatorEmail());
    }

    public void testKeyBasedAddAndGet() {
        DataPipeline pipeline = DataPipeline.create()
                .add("userReq", new SamplePayload("NamedReq"))
                .add("systemConfig", "ConfigString");

        assertTrue(pipeline.has("userReq"));
        assertTrue(pipeline.has("systemConfig"));

        SamplePayload req = pipeline.get("userReq", SamplePayload.class);
        assertNotNull(req);
        assertEquals("NamedReq", req.getPayloadName());

        String cfg = pipeline.get("systemConfig", String.class);
        assertEquals("ConfigString", cfg);
    }

    public void testHasAndClear() {
        DataPipeline pipeline = DataPipeline.create()
                .add("TestItem")
                .add("itemKey", 999);

        assertTrue(pipeline.has(String.class));
        assertTrue(pipeline.has(Integer.class));
        assertTrue(pipeline.has("itemKey"));

        pipeline.clear();

        assertTrue(pipeline.isEmpty());
        assertEquals(0, pipeline.size());
        assertNull(pipeline.get(String.class));
    }
}
