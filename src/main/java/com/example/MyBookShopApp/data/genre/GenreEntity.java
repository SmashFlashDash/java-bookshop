package com.example.MyBookShopApp.data.genre;

import com.example.MyBookShopApp.data.book.Book;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "genre")
public class GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "INT")
    private Integer parentId;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL", unique = true)
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL", unique = true)
    private String name;

    @OneToMany(mappedBy = "genre")
    private List<Book> books;

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
