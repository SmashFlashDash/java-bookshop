package com.example.MyBookShopApp.data.book.review;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
public class BookRating {

    @Id
    // TODO: hibenrate секвенс не синхронизировался с вставленными в бд записями
    //  org.postgresql.util.PSQLException: ERROR: duplicate key value violates unique constraint "book_rating_pkey"
    //  Подробности: Key (id)=(1) already exists.
    @GenericGenerator(name="generator", strategy="increment")
    @GeneratedValue(generator="generator")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    @Max(5)
    @Column(columnDefinition = "SMALLINT NOT NULL")
    private Short value;

    @Column(columnDefinition = "INT NOT NULL")
    private Integer bookId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getValue() {
        return value;
    }

    public void setValue(Short value) {
        this.value = value;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }
}
