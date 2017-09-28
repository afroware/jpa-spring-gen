package com.afroware.sdgenerator.support.maker.values;

import static com.afroware.sdgenerator.support.maker.values.CommonValues.SPACE;

/**
 * Created by lamallam on 28/09/17.
 */
public enum ObjectValues {
    PACKAGE("package" + SPACE),
    IMPORT("import" + SPACE),
    IMPLEMENTS("implements" + SPACE),
    THIS("this."),
    EXTENDS("extends" + SPACE),
    RETURN("return" + SPACE);

    private String value;

    ObjectValues(String value) {
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
