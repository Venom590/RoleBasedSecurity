package de.unileipzig.bis.rbs.testApp.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * RoleObject database entity model.
 *
 * @author Lukas Werner
 */
@Entity
@Table(name = "rbs_roles_objects")
@AssociationOverrides({
        @AssociationOverride(name = "id.role", joinColumns = @JoinColumn(name = "role_id")),
        @AssociationOverride(name = "id.object", joinColumns = @JoinColumn(name = "object_id"))
})
public class RoleObject implements Serializable {

    /**
     * The composed key for role object association
     */
    @EmbeddedId
    private RoleObjectId id = new RoleObjectId();
    /**
     * Can read property
     */
    @Column(name = "can_read", nullable = false)
    private boolean canRead;
    /**
     * Can write property
     */
    @Column(name = "can_write", nullable = false)
    private boolean canWrite;
    /**
     * Can delete property
     */
    @Column(name = "can_delete", nullable = false)
    private boolean canDelete;

    /**
     * Empty constructor as required in JPA
     */
    public RoleObject() { }

    /**
     * Getter for id
     *
     * @return the id
     */
    public RoleObjectId getId() {
        return id;
    }

    /**
     * Setter for id
     *
     * @param id the id to set
     */
    public void setId(RoleObjectId id) {
        this.id = id;
    }

    /**
     * Getter for role (from id)
     *
     * @return the role
     */
    @Transient
    public Role getRole() {
        return id.getRole();
    }

    /**
     * Setter for the role (to id)
     *
     * @param role the role to set
     */
    public void setRole(Role role) {
        id.setRole(role);
    }

    /**
     * Getter for the object (from id)
     *
     * @return the object
     */
    @Transient
    public DataObject getObject() {
        return id.getObject();
    }

    /**
     * Setter for the object (to id)
     *
     * @param object the object to set
     */
    public void setObject(DataObject object) {
        id.setObject(object);
    }

    /**
     * Getter for canRead
     *
     * @return canRead
     */
    public boolean getCanRead() {
        return canRead;
    }

    /**
     * Setter for canRead
     *
     * @param canRead new value
     */
    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    /**
     * Getter for canWrite
     *
     * @return canWrite
     */
    public boolean getCanWrite() {
        return canWrite;
    }

    /**
     * Setter for canWrite
     *
     * @param canWrite new value
     */
    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }

    /**
     * Getter for canDelete
     *
     * @return canDelete
     */
    public boolean getCanDelete() {
        return canDelete;
    }

    /**
     * Setter for canDelete
     *
     * @param canDelete new value
     */
    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    /**
     * Check if this roleObject has more rights than another roleObject
     *
     * @param roleObject the other roleObject
     * @return true if more rights
     */
    public boolean hasMoreRightsThan(RoleObject roleObject) {
        return (getCanRead() && !roleObject.getCanRead())
                || (getCanWrite() && !roleObject.getCanWrite())
                || (getCanDelete() && !roleObject.getCanDelete());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        RoleObject other = (RoleObject)obj;

        if (id != null ? !id.equals(other.id) : other.id != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return id.toString() + String.format(" can_read=%b, can_write=%b, can_delete=%b", canRead, canWrite, canDelete);
    }
}