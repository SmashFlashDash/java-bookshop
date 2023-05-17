package com.example.MyBookShopApp.data.book.links;

import javax.persistence.*;

@Entity
@Table(name = "book2tag", uniqueConstraints =
@UniqueConstraint(columnNames = {"bookId", "tagId"}))
public class Book2Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "INT NOT NULL")
    private int bookId;
    @Column(columnDefinition = "INT NOT NULL")
    private int tagId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
}
