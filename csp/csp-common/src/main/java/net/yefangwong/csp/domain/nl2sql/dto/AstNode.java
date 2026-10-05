package net.yefangwong.csp.domain.nl2sql.dto;

import java.util.List;

public class AstNode {
    private String nodeType;
    private String action;
    private String targetTable;
    private List<Condition> conditions;

    public String getNodeType() { return nodeType; }
    public void setNodeType(String nodeType) { this.nodeType = nodeType; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getTargetTable() { return targetTable; }
    public void setTargetTable(String targetTable) { this.targetTable = targetTable; }

    public List<Condition> getConditions() { return conditions; }
    public void setConditions(List<Condition> conditions) { this.conditions = conditions; }
}
