package net.yefangwong.csp.domain.nl2sql.dto;

import java.util.List;

public class CompilationResultDTO {
    private String status;
    private List<Token> tokens;
    private AstNode ast;
    private String generatedSql;
    private String errorNode;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<Token> getTokens() { return tokens; }
    public void setTokens(List<Token> tokens) { this.tokens = tokens; }

    public AstNode getAst() { return ast; }
    public void setAst(AstNode ast) { this.ast = ast; }

    public String getGeneratedSql() { return generatedSql; }
    public void setGeneratedSql(String generatedSql) { this.generatedSql = generatedSql; }

    public String getErrorNode() { return errorNode; }
    public void setErrorNode(String errorNode) { this.errorNode = errorNode; }
}
