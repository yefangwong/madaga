package net.yefangwong.csp.common.bl;

import common.api.ApiResult;
import junit.framework.TestCase;

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

    static class TestBL extends BaseBL<SampleRequest, SampleResponse> {
        private boolean throwException = false;

        public void setThrowException(boolean throwException) {
            this.throwException = throwException;
        }

        @Override
        protected boolean validateInput(SampleRequest request) {
            if (request == null || request.getName() == null || request.getName().trim().isEmpty()) {
                errors.addValidation("name", "Name cannot be empty");
                return false;
            }
            return true;
        }

        @Override
        protected boolean verifyAuthority(SampleRequest request, String operatorEmail) {
            if (!"ADMIN".equals(request.getRole())) {
                errors.add("E403", "Only ADMIN can execute");
                return false;
            }
            return true;
        }

        @Override
        protected SampleResponse executeBusiness(SampleRequest request) throws Exception {
            if (throwException) {
                throw new RuntimeException("DB Connection Timeout");
            }
            return new SampleResponse("Processed: " + request.getName());
        }
    }

    public void testValidateInputFailure() {
        TestBL bl = new TestBL();
        SampleRequest req = new SampleRequest("", "ADMIN");

        ApiResult<SampleResponse> res = bl.process(req, "admin@test.com", "TEST_ACTION", "repo1");

        assertEquals(400, res.getCode());
        assertFalse(res.isSuccess());
        assertEquals("Name cannot be empty", res.getMessage());
        assertNull(res.getData());
    }

    public void testVerifyAuthorityFailure() {
        TestBL bl = new TestBL();
        SampleRequest req = new SampleRequest("ValidName", "USER");

        ApiResult<SampleResponse> res = bl.process(req, "user@test.com", "TEST_ACTION", "repo1");

        assertEquals(403, res.getCode());
        assertFalse(res.isSuccess());
        assertEquals("Only ADMIN can execute", res.getMessage());
    }

    public void testProcessSuccess() {
        TestBL bl = new TestBL();
        SampleRequest req = new SampleRequest("ValidName", "ADMIN");

        ApiResult<SampleResponse> res = bl.process(req, "admin@test.com", "TEST_ACTION", "repo1");

        assertEquals(200, res.getCode());
        assertTrue(res.isSuccess());
        assertNotNull(res.getData());
        assertEquals("Processed: ValidName", res.getData().getResultMessage());
    }

    public void testProcessException() {
        TestBL bl = new TestBL();
        bl.setThrowException(true);
        SampleRequest req = new SampleRequest("ValidName", "ADMIN");

        ApiResult<SampleResponse> res = bl.process(req, "admin@test.com", "TEST_ACTION", "repo1");

        assertEquals(500, res.getCode());
        assertFalse(res.isSuccess());
        assertTrue(res.getMessage().contains("DB Connection Timeout"));
    }
}
