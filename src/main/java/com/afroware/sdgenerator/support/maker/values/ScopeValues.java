package com.afroware.sdgenerator.support.maker.values;

import static com.afroware.sdgenerator.support.maker.values.CommonValues.SPACE;

/**
 * Created by lamallam on 28/09/17.
 */
public enum ScopeValues {

    PUBLIC("public" + SPACE),
    PRIVATE("private" + SPACE);

    private String value;

    ScopeValues(String value) {
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
