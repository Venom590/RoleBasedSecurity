package de.unileipzig.bis.rbs.testApp;

/**
 * A simple exception to catch role consistency rule violation
 *
 * @author Lukas Werner
 */
public class RoleObjectConsistencyException extends Exception {

    /**
     * Constructor to set the message hard coded
     */
    public RoleObjectConsistencyException() {
        super("Breaking role consistency rules: A child role may not have more rights than its parent.");
    }

}