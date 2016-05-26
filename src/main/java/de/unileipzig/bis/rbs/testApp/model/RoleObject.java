package de.unileipzig.bis.rbs.testApp.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rbs_roles_objects")
@AssociationOverrides({
        @AssociationOverride(name = "id.role", joinColumns = @JoinColumn(name = "role_id")),
        @AssociationOverride(name = "id.object", joinColumns = @JoinColumn(name = "object_id"))
})
public class RoleObject implements Serializable {

    @EmbeddedId
    private RoleObjectId id = new RoleObjectId();
    @Column(name = "can_read", nullable = false)
    private boolean canRead;
    @Column(name = "can_write", nullable = false)
    private boolean canWrite;
    @Column(name = "can_delete", nullable = false)
    private boolean canDelete;

    public RoleObject() {

    }

    public RoleObjectId getId() {
        return id;
    }

    public void setId(RoleObjectId id) {
        this.id = id;
    }

    @Transient
    public Role getRole() {
        return id.getRole();
    }

    public void setRole(Role role) {
        id.setRole(role);
    }

    @Transient
    public DataObject getObject() {
        return id.getObject();
    }

    public void setObject(DataObject object) {
        id.setObject(object);
    }

    public boolean getCanRead() {
        return canRead;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    public boolean getCanWrite() {
        return canWrite;
    }

    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }

    public boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
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