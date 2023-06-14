package com.example.MyBookShopApp.data.author;

import com.example.MyBookShopApp.data.book.Book;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String photo;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String slug;

    @Column(columnDefinition = "VARCHAR(1000) NOT NULL")
    private String littleBiography;

    @Column(columnDefinition = "TEXT")
    private String extendBiography;

    @ManyToMany(mappedBy = "author")
    private List<Book> bookList = new ArrayList<>();

    public String getLittleBiography() {
        return littleBiography;
    }

    public void setLittleBiography(String littleBiography) {
        this.littleBiography = littleBiography;
    }

    public String getExtendBiography() {
        return extendBiography;
    }

    public void setExtendBiography(String extendBiography) {
        this.extendBiography = extendBiography;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", photo='" + photo + '\'' +
                ", slug='" + slug + '\'' +
                '}';
    }
}
