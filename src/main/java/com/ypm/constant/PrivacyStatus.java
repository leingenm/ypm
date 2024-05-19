package com.ypm.constant;

public enum PrivacyStatus {
    PRIVATE, PUBLIC, UNLISTED;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
