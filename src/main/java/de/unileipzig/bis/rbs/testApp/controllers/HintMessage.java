package de.unileipzig.bis.rbs.testApp.controllers;

/**
 * Simple hint message class.
 *
 * @author Lukas Werner
 */
public class HintMessage {

    /**
     * The hint status (bootstrap status)
     */
    enum HintStatus {
        info,
        success,
        warning,
        danger
    }

    /**
     * The hint status
     */
    private HintStatus status;
    /**
     * The hint message
     */
    private String message;

    /**
     * Data constructor.
     *
     * @param status the hint status
     * @param message the hint message
     */
    public HintMessage(HintStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Getter for status
     *
     * @return the status
     */
    public HintStatus getStatus() {
        return status;
    }

    /**
     * Getter for message
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}