package de.unileipzig.bis.rbs.testApp.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Stephan on 21.05.2016.
 */
@Entity
@DiscriminatorValue("rbs_authors")
@Table(name="rbs_authors")
@PrimaryKeyJoinColumn(name = "id")
public class Author extends Object{

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
     * @return the id
     */
    public Long getId() {
        return id;
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

    public Set<Book> getBooks() { return books; }

    public void setBooks(Set<Book> books) { this.books = books; }

    @Override
    public String toString() {
        return String.format("Author [id=%d, name=%s]", id, name);
    }
}
