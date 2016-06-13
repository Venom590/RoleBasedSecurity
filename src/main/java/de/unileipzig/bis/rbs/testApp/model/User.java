package de.unileipzig.bis.rbs.testApp.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * User database entity model.
 *
 * @author Lukas Werner
 */
@Entity
@Table(name="rbs_users")
public class User implements UserDetails {

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
     * Roles
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "rbs_users_roles", joinColumns = {
            @JoinColumn(name = "user_id", nullable = false)
    }, inverseJoinColumns = {
            @JoinColumn(name = "role_id", nullable = false)
    })
    private Set<Role> roles = new HashSet<>(0);

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
     * Getter for id
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Getter for username
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Setter for username
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for password
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for password
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for roles
     *
     * @return the roles
     */
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Setter for roles
     *
     * @param roles the roles to set
     */
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    /**
     * Check if this user can read a given object
     *
     * @param object the given object
     * @return true if the user can read
     */
    public boolean canRead(DataObject object) {
        for (Role role: roles) {
            if (role.canRead(object)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if this user can write a given object
     *
     * @param object the given object
     * @return true if the user can write
     */
    public boolean canWrite(DataObject object) {
        for (Role role: roles) {
            if (role.canWrite(object)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if this user can delete a given object
     *
     * @param object the given object
     * @return true if the user can delete
     */
    public boolean canDelete(DataObject object) {
        for (Role role: roles) {
            if (role.canDelete(object)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get GrantedAuthorities for user
     *
     * @return granted authorities
     */
    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (final Role r: roles) {
            grantedAuthorities.add((GrantedAuthority) r::getName);
        }
        return grantedAuthorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id != null ? id.equals(user.id) : user.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("User [id=%d, username=%s, password=???, name=%s]", id, username, name);
    }
}