package net.yefangwong.csp.common.bl;

import common.api.ApiResult;
import junit.framework.TestCase;
import net.yefangwong.csp.common.context.DataPipeline;
import net.yefangwong.csp.common.context.GlobalContext;

public class BaseBLTest extends TestCase {

    static class SampleRequest {
        private String name;
        private String role;

        public SampleRequest(String name, String role) {
            this.name = name;
            this.role = role;
        }

        public String getName() { return name; }
        public String getRole() { return role; }
    }

    static class SampleResponse {
        private String resultMessage;

        public SampleResponse(String resultMessage) {
            this.resultMessage = resultMessage;
        }

        public String getResultMessage() { return resultMessage; }
    }

    static class TestBL extends BaseBL<SampleResponse> {
        private boolean throwException = false;

        public void setThrowException(boolean throwException) {
            this.throwException = throwException;
        }

        @Override
        protected boolean validateInput(DataPipeline pipeline) {
            SampleRequest req = pipeline.get(SampleRequest.class);
            if (req == null || req.getName() == null || req.getName().trim().isEmpty()) {
                errors.addValidation("name", "Name cannot be empty");
                return false;
            }
            return true;
        }

        @Override
        protected boolean verifyAuthority(DataPipeline pipeline) {
            SampleRequest req = pipeline.get(SampleRequest.class);
            if (req == null || !"ADMIN".equals(req.getRole())) {
                errors.add("E403", "Only ADMIN can execute");
                return false;
            }
            return true;
        }

        @Override
        protected SampleResponse executeBusiness(DataPipeline pipeline) throws Exception {
            if (throwException) {
                throw new RuntimeException("DB Connection Timeout");
            }
            SampleRequest req = pipeline.get(SampleRequest.class);
            return new SampleResponse("Processed: " + req.getName());
        }
    }

    public void testValidateInputFailure() {
        TestBL bl = new TestBL();
        GlobalContext ctx = new GlobalContext("admin@test.com", "repo1", "127.0.0.1", "t1", "ten1");
        DataPipeline pipeline = new DataPipeline(ctx);
        pipeline.put(SampleRequest.class, new SampleRequest("", "ADMIN"));

        ApiResult<SampleResponse> res = bl.process(pipeline, "TEST_ACTION");

        assertEquals(400, res.getCode());
        assertFalse(res.isSuccess());
        assertEquals("Name cannot be empty", res.getMessage());
        assertNull(res.getData());
    }

    public void testVerifyAuthorityFailure() {
        TestBL bl = new TestBL();
        GlobalContext ctx = new GlobalContext("user@test.com", "repo1", "127.0.0.1", "t1", "ten1");
        DataPipeline pipeline = new DataPipeline(ctx);
        pipeline.put(SampleRequest.class, new SampleRequest("ValidName", "USER"));

        ApiResult<SampleResponse> res = bl.process(pipeline, "TEST_ACTION");

        assertEquals(403, res.getCode());
        assertFalse(res.isSuccess());
        assertEquals("Only ADMIN can execute", res.getMessage());
    }

    public void testProcessSuccess() {
        TestBL bl = new TestBL();
        GlobalContext ctx = new GlobalContext("admin@test.com", "repo1", "127.0.0.1", "t1", "ten1");
        DataPipeline pipeline = new DataPipeline(ctx);
        pipeline.put(SampleRequest.class, new SampleRequest("ValidName", "ADMIN"));

        ApiResult<SampleResponse> res = bl.process(pipeline, "TEST_ACTION");

        assertEquals(200, res.getCode());
        assertTrue(res.isSuccess());
        assertNotNull(res.getData());
        assertEquals("Processed: ValidName", res.getData().getResultMessage());
    }

    public void testProcessException() {
        TestBL bl = new TestBL();
        bl.setThrowException(true);
        GlobalContext ctx = new GlobalContext("admin@test.com", "repo1", "127.0.0.1", "t1", "ten1");
        DataPipeline pipeline = new DataPipeline(ctx);
        pipeline.put(SampleRequest.class, new SampleRequest("ValidName", "ADMIN"));

        ApiResult<SampleResponse> res = bl.process(pipeline, "TEST_ACTION");

        assertEquals(500, res.getCode());
        assertFalse(res.isSuccess());
        assertTrue(res.getMessage().contains("DB Connection Timeout"));
    }
}
