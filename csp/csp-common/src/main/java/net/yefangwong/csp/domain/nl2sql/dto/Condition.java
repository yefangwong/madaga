package net.yefangwong.csp.domain.nl2sql.dto;

public class Condition {
    private String field;
    private String op;
    private String value;

    public Condition(String field, String op, String value) {
        this.field = field;
        this.op = op;
        this.value = value;
    }

    public String getField() { return field; }
    public void setField(String field) { this.field = field; }

    public String getOp() { return op; }
    public void setOp(String op) { this.op = op; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
