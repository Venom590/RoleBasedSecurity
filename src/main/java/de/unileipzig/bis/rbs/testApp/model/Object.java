package de.unileipzig.bis.rbs.testApp.model;

import javax.persistence.*;

/**
 * Object database entity model.
 *
 * @author Stephan Kemper
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="table_name")
@Table(name="rbs_objects")
public class Object {

    /**
     * Database id
     */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    /**
     * Object name
     */
    @Column(name = "table_object_id")
    private Long tableObjectId;

//    @Column(name = "table_name")
//    private String tableName;

    /**
     * Empty constructor as required in JPA
     */
    public Object() { }

    /**
     * Data constructor
     *
     * @param tableObjectId the name
     */
    public Object(Long tableObjectId) {
        this.tableObjectId = tableObjectId;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    public Long getTableObjectId() {
        return tableObjectId;
    }

    public void setTableObjectId(Long tableObjectId) {
        this.tableObjectId = tableObjectId;
    }

    @Override
    public String toString() {
        return String.format("Object [id=%d, tableObjectId=%s]", id, tableObjectId);
    }
}