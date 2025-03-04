package xyz.ypmngr.constant;

public enum PrivacyStatus {
    PRIVATE, PUBLIC, UNLISTED;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
