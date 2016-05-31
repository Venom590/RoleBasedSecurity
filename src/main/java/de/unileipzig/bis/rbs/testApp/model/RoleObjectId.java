package de.unileipzig.bis.rbs.testApp.model;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * The composed key for role objects
 *
 * @author Lukas Werner
 */
@Embeddable
public class RoleObjectId implements Serializable {

    /**
     * The role of this association
     */
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    /**
     * The object of this association
     */
    @ManyToOne
    @JoinColumn(name = "object_id")
    private DataObject object;

    /**
     * Getter for role
     *
     * @return the role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Setter for role
     *
     * @param role the role to set
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Getter for object
     *
     * @return the object
     */
    public DataObject getObject() {
        return object;
    }

    /**
     * Setter for object
     *
     * @param object the object to set
     */
    public void setObject(DataObject object) {
        this.object = object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoleObjectId that = (RoleObjectId) o;

        if (role != null ? !role.equals(that.role) : that.role != null) return false;
        return object != null ? object.equals(that.object) : that.object == null;
    }

    @Override
    public int hashCode() {
        int result = role != null ? role.hashCode() : 0;
        result = 31 * result + (object != null ? object.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("id[%s, %s]", role, object);
    }
}