package de.unileipzig.bis.rbs.testApp.controllers;

public class HintMessage {

    enum HintStatus {
        info,
        success,
        warning,
        danger
    }

    private HintStatus status;
    private String message;

    public HintMessage(HintStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HintStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}