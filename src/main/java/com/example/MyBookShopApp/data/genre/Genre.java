package com.example.MyBookShopApp.data.genre;

import com.example.MyBookShopApp.data.book.Book;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "genre")
@Data
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "INT")
    private Integer parentId;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL", unique = true)
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL", unique = true)
    private String name;

    @ManyToMany(mappedBy = "genre")
    private List<Book> books;
}
