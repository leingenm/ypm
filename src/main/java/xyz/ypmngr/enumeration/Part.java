package xyz.ypmngr.enumeration;

public enum Part {
    SNIPPET, STATUS, CONTENT_DETAILS;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
