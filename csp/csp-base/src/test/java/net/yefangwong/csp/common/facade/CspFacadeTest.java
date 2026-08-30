package net.yefangwong.csp.common.facade;

import common.api.ApiResult;
import junit.framework.TestCase;
import net.yefangwong.csp.common.context.DataPipeline;
import net.yefangwong.csp.common.context.GlobalContext;

public class CspFacadeTest extends TestCase {

    private static class TestFacade extends CspFacade {
        @Override
        @SuppressWarnings("unchecked")
        public <RESP> ApiResult<RESP> execute(DataPipeline pipeline, String actionCode) {
            if ("PING".equals(actionCode)) {
                return (ApiResult<RESP>) ApiResult.success("PONG");
            }
            return ApiResult.failure(400, "Unknown action: " + actionCode);
        }
    }

    public void testCspFacadeExecuteSuccess() {
        CspFacade facade = new TestFacade();
        GlobalContext ctx = new GlobalContext("tester@madaga.com", "repo-1", "127.0.0.1", "t-1", "ten-1");
        DataPipeline pipeline = new DataPipeline(ctx);

        ApiResult<String> result = facade.execute(pipeline, "PING");
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());
        assertEquals("PONG", result.getData());
    }

    public void testCspFacadeExecuteFailure() {
        CspFacade facade = new TestFacade();
        DataPipeline pipeline = new DataPipeline();

        ApiResult<String> result = facade.execute(pipeline, "INVALID");
        assertFalse(result.isSuccess());
        assertEquals(400, result.getCode());
    }
}
