package org.example.web.dto;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Book {
    private Integer id;
    @NotEmpty(message = "Author can't be empty")
    private String author;
    @NotEmpty(message = "Title can't be empty")
    private String title;
    @NotNull(message = "Size can't be empty")
    @Digits(integer = 4, fraction = 0, message = "Size must be integer digit")
    @Range(min = 0, max = 100, message = "Size must be greater 0 and less than 1000")
    private Integer size;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setSize(String size) {
        this.size = Integer.parseInt(size);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", size=" + size +
                '}';
    }
}
