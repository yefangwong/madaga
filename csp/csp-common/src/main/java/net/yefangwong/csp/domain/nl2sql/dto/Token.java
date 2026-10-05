package net.yefangwong.csp.domain.nl2sql.dto;

public class Token {
    private String word;
    private String type;
    private String color;

    public Token(String word, String type, String color) {
        this.word = word;
        this.type = type;
        this.color = color;
    }

    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
