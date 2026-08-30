package net.yefangwong.csp.common.proxy;

import junit.framework.TestCase;
import net.yefangwong.csp.common.proxy.agent.AiAgentProxy;

public class AiAgentProxyTest extends TestCase {

    public void testEvaluateReachability() {
        AiAgentProxy proxy = new AiAgentProxy();
        AiAgentProxy.AgentReachabilityReport report = proxy.evaluateReachability(
            "yefangwong/madaga",
            "ControllerEndpoint",
            "VulnerableSink"
        );

        assertNotNull(report);
        assertTrue(report.isReachable());
        assertEquals(2, report.getPathHops());
        assertTrue(report.getRewardScore() > 0.0);
        assertEquals(3, report.getPathNodes().size());
        assertEquals("ControllerEndpoint", report.getPathNodes().get(0));
        assertEquals("VulnerableSink", report.getPathNodes().get(2));
    }
}
