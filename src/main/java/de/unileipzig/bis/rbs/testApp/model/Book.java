package de.unileipzig.bis.rbs.testApp.model;

import javax.persistence.*;

/**
 * Author database entity model.
 *
 * @author Stephan Kemper
 */
@Entity
@Table(name="rbs_books")
@PrimaryKeyJoinColumn(name = "id")
public class Book extends DataObject {

    /**
     * Book isbn
     */
    @Column(name = "isbn")
    private String isbn;

    /**
     * Book title
     */
    @Column(name = "title")
    private String title;

    /**
     * Book author
     */
    @ManyToOne
    @JoinColumn(name="author_id")
    private Author author;

    /**
     * Empty constructor as required in JPA
     */
    public Book() { }

    /**
     * Data constructor
     *
     * @param isbn the isbn number
     * @param title the title
     * @param author the author
     */
    public Book(String isbn, String title, Author author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }

    /**
     * Getter for isbn
     *
     * @return the isbn
     */
    public String getIsbn() { return isbn; }

    /**
     * Setter for isbn
     *
     * @param isbn the isbn to set
     */
    public void setIsbn(String isbn) { this.isbn = isbn; }

    /**
     * Getter for title
     *
     * @return the title
     */
    public String getTitle() { return title; }

    /**
     * Setter for title
     *
     * @param title the title to set
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Getter for author
     *
     * @return the author
     */
    public Author getAuthor() { return author; }

    /**
     * Setter for author
     *
     * @param author the author to set
     */
    public void setAuthor(Author author) { this.author = author; }

    @Override
    public String toString() {
        return String.format("Book [id=%d, isbn=%s, title=%s, author=%s]",
          id, isbn, title, author == null? "Null" : author.getName());
    }
}
