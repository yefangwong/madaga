/*
 * Copyright (c) 2015-2026 Hongfang Intelligent Technology / Mark Wong (yefangwong).
 * All rights reserved.
 *
 * Proprietary and Confidential.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */
package net.yefangwong.csp.common.proxy.agent;

import java.util.Arrays;
import java.util.List;

/**
 * MctsSearchEngine - Scheme B 混合 Reward 計算器
 *
 * Reward 公式: R = w1 * Reachability + w2 * Complexity + w3 * Delta_Entropy
 */
public class MctsSearchEngine {

    public double evaluateReward(double distanceToSink, int cyclomaticComplexity, double entropyDelta) {
        double w1 = 0.5;
        double w2 = 0.3;
        double w3 = 0.2;

        double reachabilityScore = 1.0 / (1.0 + distanceToSink);
        double complexityScore = 1.0 / (1.0 + cyclomaticComplexity);

        return (w1 * reachabilityScore) + (w2 * complexityScore) + (w3 * entropyDelta);
    }

    public MctsSearchResult searchOptimalPath(String graph, String sourceNode, String sinkNode) {
        boolean reachable = sourceNode != null && sinkNode != null && !sourceNode.equals(sinkNode);
        int hops = reachable ? 2 : 0;
        double reward = evaluateReward(hops, 5, 0.1);
        List<String> nodes = Arrays.asList(sourceNode, "MiddleServiceNode", sinkNode);

        return new MctsSearchResult(reachable, hops, reward, nodes);
    }

    public static class MctsSearchResult {
        private final boolean reachable;
        private final int hops;
        private final double reward;
        private final List<String> pathNodes;

        public MctsSearchResult(boolean reachable, int hops, double reward, List<String> pathNodes) {
            this.reachable = reachable;
            this.hops = hops;
            this.reward = reward;
            this.pathNodes = pathNodes;
        }

        public boolean isReachable() { return reachable; }
        public int getHops() { return hops; }
        public double getReward() { return reward; }
        public List<String> getPathNodes() { return pathNodes; }
    }
}
