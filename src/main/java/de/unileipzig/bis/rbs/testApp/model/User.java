package de.unileipzig.bis.rbs.testApp.model;

import javax.persistence.*;

/**
 * User database entity model.
 *
 * @author Lukas Werner
 */
@Entity
@Table(name="rbs_users")
public class User {

    /**
     * Database id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * User name
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Password
     */
    @Column(nullable = false)
    private String password;

    /**
     * Name
     */
    @Column
    private String name;

    /**
     * Empty constructor as required in JPA
     */
    public User() { }

    /**
     * Data constructor
     *
     * @param username the username
     * @param password the password
     * @param name the name
     */
    public User(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("User [id=%d, username=%s, password=???, name=%s]", id, username, name);
    }
}