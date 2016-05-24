package de.unileipzig.bis.rbs.testApp.model;

import javax.persistence.*;

/**
 * Created by Stephan on 21.05.2016.
 */

@Entity
@DiscriminatorValue("rbs_books")
@Table(name="rbs_books")
@PrimaryKeyJoinColumn(name = "id")


public class Book extends Object{

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

    public String getIsbn() { return isbn; }

    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public Author getAuthor() { return author; }

    public void setAuthor(Author author) { this.author = author; }

    @Override
    public String toString() {
        return String.format("Book [id=%d, isbn=%s, title=%s, author=%s]", id, isbn, title, author.getName());
    }
}
