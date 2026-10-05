package net.yefangwong.csp.domain.nl2sql.service;

import net.yefangwong.csp.domain.nl2sql.dto.AstNode;
import net.yefangwong.csp.domain.nl2sql.dto.CompilationResultDTO;
import net.yefangwong.csp.domain.nl2sql.dto.Condition;
import net.yefangwong.csp.domain.nl2sql.dto.Token;

import java.util.ArrayList;
import java.util.List;

public class Nlp2SqlCompilerService {

    public CompilationResultDTO compile(String prompt, int modelType) {
        CompilationResultDTO result = new CompilationResultDTO();
        
        if (modelType != 3) {
            result.setStatus("UNSUPPORTED_MODEL");
            return result;
        }

        try {
            // 1. Lexical Analysis
            List<Token> tokens = scan(prompt);
            
            // 2. Syntax Analysis
            AstNode ast = parse(tokens);
            
            // 3. Semantic Analysis
            validate(ast, tokens);
            
            // 4. Code Generation
            String sql = emit(ast);
            
            result.setStatus("SUCCESS");
            result.setTokens(tokens);
            result.setAst(ast);
            result.setGeneratedSql(sql);
            
        } catch (CompilerException e) {
            result.setStatus(e.getStatus());
            result.setErrorNode(e.getErrorNode());
        }

        return result;
    }

    private List<Token> scan(String prompt) {
        List<Token> tokens = new ArrayList<>();
        // Very basic dictionary for POC
        // prompt: "查詢財務部有沒有一位吳華瑄的小姐"
        // prompt: "查詢財務部有沒有一公斤的吳華瑄"
        // prompt: "查詢研發部有沒有..."
        
        String remaining = prompt;
        
        while (!remaining.isEmpty()) {
            if (remaining.startsWith("查詢")) {
                tokens.add(new Token("查詢", "ACTION_SELECT", "blue"));
                remaining = remaining.substring(2);
            } else if (remaining.startsWith("財務部")) {
                tokens.add(new Token("財務部", "ENTITY_DEPT", "green"));
                remaining = remaining.substring(3);
            } else if (remaining.startsWith("有沒有")) {
                tokens.add(new Token("有沒有", "INTENT_EXISTS", "purple"));
                remaining = remaining.substring(3);
            } else if (remaining.startsWith("一位")) {
                tokens.add(new Token("一位", "QUANTIFIER", "gray"));
                remaining = remaining.substring(2);
            } else if (remaining.startsWith("吳華瑄")) {
                tokens.add(new Token("吳華瑄", "ENTITY_NAME", "green"));
                remaining = remaining.substring(3);
            } else if (remaining.startsWith("的")) {
                tokens.add(new Token("的", "PARTICLE_LINK", "gray"));
                remaining = remaining.substring(1);
            } else if (remaining.startsWith("小姐")) {
                tokens.add(new Token("小姐", "ENTITY_GENDER", "green"));
                remaining = remaining.substring(2);
            } else if (remaining.startsWith("一公斤")) {
                tokens.add(new Token("一公斤", "QUANTIFIER_WEIGHT", "red"));
                remaining = remaining.substring(3);
            } else if (remaining.startsWith("研發部")) {
                throw new CompilerException("UNKNOWN_TOKEN", "研發部");
            } else {
                throw new CompilerException("UNKNOWN_TOKEN", remaining.substring(0, 1));
            }
        }
        
        return tokens;
    }

    private AstNode parse(List<Token> tokens) {
        AstNode ast = new AstNode();
        ast.setNodeType("QueryStatement");
        ast.setTargetTable("employee");
        List<Condition> conditions = new ArrayList<>();
        
        for (Token token : tokens) {
            if ("ACTION_SELECT".equals(token.getType()) || "INTENT_EXISTS".equals(token.getType())) {
                ast.setAction("EXISTS");
            } else if ("ENTITY_DEPT".equals(token.getType())) {
                conditions.add(new Condition("department", "EQ", token.getWord()));
            } else if ("ENTITY_NAME".equals(token.getType())) {
                conditions.add(new Condition("name", "EQ", token.getWord()));
            } else if ("ENTITY_GENDER".equals(token.getType())) {
                String genderCode = "小姐".equals(token.getWord()) ? "F" : "M";
                conditions.add(new Condition("gender", "EQ", genderCode));
            }
        }
        
        ast.setConditions(conditions);
        return ast;
    }

    private void validate(AstNode ast, List<Token> tokens) {
        // Mock semantic validation
        for (Token token : tokens) {
            if ("QUANTIFIER_WEIGHT".equals(token.getType())) {
                throw new CompilerException("TYPE_ERROR", token.getWord());
            }
        }
    }

    private String emit(AstNode ast) {
        StringBuilder sql = new StringBuilder();
        if ("EXISTS".equals(ast.getAction())) {
            sql.append("SELECT EXISTS(SELECT 1 FROM ").append(ast.getTargetTable());
            
            if (ast.getConditions() != null && !ast.getConditions().isEmpty()) {
                sql.append(" WHERE ");
                for (int i = 0; i < ast.getConditions().size(); i++) {
                    Condition cond = ast.getConditions().get(i);
                    sql.append(cond.getField()).append(" = '").append(cond.getValue()).append("'");
                    if (i < ast.getConditions().size() - 1) {
                        sql.append(" AND ");
                    }
                }
            }
            sql.append(") AS is_exist;");
        }
        return sql.toString();
    }
    
    public static class CompilerException extends RuntimeException {
        private final String status;
        private final String errorNode;
        
        public CompilerException(String status, String errorNode) {
            this.status = status;
            this.errorNode = errorNode;
        }
        
        public String getStatus() { return status; }
        public String getErrorNode() { return errorNode; }
    }
}
