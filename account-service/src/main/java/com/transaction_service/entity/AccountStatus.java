package com.transaction_service.entity;

public enum AccountStatus {
    ACTIVE("Active"),
    SUSPENDED("Suspended"),
    CLOSED("Closed");

    private final String value;

    AccountStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

