package de.unileipzig.bis.rbs.testApp;

public class RoleObjectConsistencyException extends Exception {

    public RoleObjectConsistencyException() {
        super("Breaking role consistency rules: A child role may not have more rights than its parent.");
    }

}