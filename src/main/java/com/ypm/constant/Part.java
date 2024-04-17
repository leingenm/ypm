package com.ypm.constant;

public enum Part {
    SNIPPET, STATUS;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
