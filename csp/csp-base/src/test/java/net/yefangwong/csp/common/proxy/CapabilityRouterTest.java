package net.yefangwong.csp.common.proxy;

import junit.framework.TestCase;
import net.yefangwong.csp.common.proxy.compute.CapabilityRouter;
import net.yefangwong.csp.common.proxy.compute.ComputeResult;
import net.yefangwong.csp.common.proxy.compute.ComputeTaskData;

public class CapabilityRouterTest extends TestCase {

    public void testForbiddenVectorInterceptor() {
        CapabilityRouter router = new CapabilityRouter();
        double[] forbiddenVec = new double[]{-1.0, 0.5, 0.2};
        ComputeTaskData task = new ComputeTaskData("t1", forbiddenVec, 8, "test prompt");

        ComputeResult res = router.submitInferenceTask(task);
        assertFalse(res.isSuccess());
        assertTrue(res.isBlocked());
        assertEquals("Pre-Flight Interceptor", res.getDriverName());
    }

    public void testLocalDriverRouting() {
        CapabilityRouter router = new CapabilityRouter();
        double[] safeVec = new double[]{0.8, 0.5, 0.2};
        ComputeTaskData task = new ComputeTaskData("t2", safeVec, 8, "test prompt local");

        ComputeResult res = router.submitInferenceTask(task);
        assertTrue(res.isSuccess());
        assertFalse(res.isBlocked());
        assertTrue(res.getDriverName().contains("LocalHeterogeneousDriver"));
    }

    public void testDgxSparkDriverRouting() {
        CapabilityRouter router = new CapabilityRouter();
        double[] safeVec = new double[]{0.8, 0.5, 0.2};
        ComputeTaskData task = new ComputeTaskData("t3", safeVec, 32, "test prompt dgx");

        ComputeResult res = router.submitInferenceTask(task);
        assertTrue(res.isSuccess());
        assertFalse(res.isBlocked());
        assertTrue(res.getDriverName().contains("NvidiaDgxSparkDriver"));
    }
}
