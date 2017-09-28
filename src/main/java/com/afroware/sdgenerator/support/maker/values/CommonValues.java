package com.afroware.sdgenerator.support.maker.values;

/**
 * Created by lamallam on 28/09/17.
 */
public enum CommonValues {

    SPACE(" "),
    NEWLINE("\r\n"),
    TAB("\t"),
    COMA("," + SPACE),
    SEMICOLON(";" + NEWLINE),
    KEY_START(SPACE + "{" + NEWLINE),
    KEY_END("}" + NEWLINE),
    PARENTHESIS("("),
    PARENTHESIS_END(")"),
    COMMENT_START("/**" + NEWLINE),
    COMMENT_BODY("*" + SPACE),
    COMMENT_END("*/" + NEWLINE),
    COMMENT_SINGLE("//" + SPACE),
    DIAMOND_START("<"),
    DIAMOND_END(">");

    private String value;

    CommonValues(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
