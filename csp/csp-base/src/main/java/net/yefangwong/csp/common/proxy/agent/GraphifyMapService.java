/*
 * Copyright (c) 2015-2026 Hongfang Intelligent Technology / Mark Wong (yefangwong).
 * All rights reserved.
 *
 * Proprietary and Confidential.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */
package net.yefangwong.csp.common.proxy.agent;

/**
 * GraphifyMapService - CPG (Code Property Graph) AST 拓撲建構器
 */
public class GraphifyMapService {

    public String buildCodePropertyGraph(String repoPath) {
        return "CPG_GRAPH_FOR_" + (repoPath != null ? repoPath : "DEFAULT_REPO");
    }
}
