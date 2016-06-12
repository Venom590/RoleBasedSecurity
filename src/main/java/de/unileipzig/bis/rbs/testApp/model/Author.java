package de.unileipzig.bis.rbs.testApp.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Author database entity model.
 *
 * @author Stephan Kemper
 */
@Entity
@Table(name="rbs_authors")
@DiscriminatorValue("rbs_authors")
@PrimaryKeyJoinColumn(name = "id")
public class Author extends DataObject {

    /**
     * Author name
     */
    @Column(name = "name")
    private String name;

    /**
     * Set of roles of children
     */
    @JoinColumn (name="author_id", insertable = false, updatable = false)
//    @OneToMany(mappedBy="author")
    @OneToMany()
    private Set<Book> books = new HashSet<Book>();

    /**
     * Empty constructor as required in JPA
     */
    public Author() { }

    /**
     * Data constructor
     *
     * @param name the name
     */
    public Author(String name) {
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
     * Getter for books
     *
     * @return the books
     */
    public Set<Book> getBooks() { return books; }

    /**
     * Setter for books
     *
     * @param books the books to set
     */
    public void setBooks(Set<Book> books) { this.books = books; }

    @Override
    public String toString() {
        return String.format("Author [id=%d, name=%s]", id, name);
    }
}