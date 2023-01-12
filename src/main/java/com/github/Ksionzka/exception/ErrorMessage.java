package com.github.Ksionzka.exception;

public enum ErrorMessage {
    BAD_REQUEST,
    NOT_FOUND;

    public String readableString() {
        return this.name().replace("_", " ").toLowerCase();
    }
}
