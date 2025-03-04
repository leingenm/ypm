package xyz.ypmngr.constant;

public enum Part {
    SNIPPET, STATUS, CONTENT_DETAILS;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
