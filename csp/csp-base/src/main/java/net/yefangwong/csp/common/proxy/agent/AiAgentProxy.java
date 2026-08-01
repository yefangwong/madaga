/*
 * Copyright (c) 2015-2026 Hongfang Intelligent Technology / Mark Wong (yefangwong).
 * All rights reserved.
 *
 * Proprietary and Confidential.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */
package net.yefangwong.csp.common.proxy.agent;

import java.io.Serializable;
import java.util.List;

/**
 * AiAgentProxy - 特種 AI Agent 代理中樞
 */
public class AiAgentProxy {

    private final GraphifyMapService graphifyService = new GraphifyMapService();
    private final MctsSearchEngine mctsEngine = new MctsSearchEngine();

    public AgentReachabilityReport evaluateReachability(String repoPath, String sourceNode, String sinkNode) {
        String graph = graphifyService.buildCodePropertyGraph(repoPath);
        MctsSearchEngine.MctsSearchResult result = mctsEngine.searchOptimalPath(graph, sourceNode, sinkNode);

        return new AgentReachabilityReport(
            result.isReachable(),
            result.getHops(),
            result.getReward(),
            result.getPathNodes()
        );
    }

    public static class AgentReachabilityReport implements Serializable {
        private static final long serialVersionUID = 1L;

        private final boolean reachable;
        private final int pathHops;
        private final double rewardScore;
        private final List<String> pathNodes;

        public AgentReachabilityReport(boolean reachable, int pathHops, double rewardScore, List<String> pathNodes) {
            this.reachable = reachable;
            this.pathHops = pathHops;
            this.rewardScore = rewardScore;
            this.pathNodes = pathNodes;
        }

        public boolean isReachable() { return reachable; }
        public int getPathHops() { return pathHops; }
        public double getRewardScore() { return rewardScore; }
        public List<String> getPathNodes() { return pathNodes; }
    }
}
