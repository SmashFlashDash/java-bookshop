package com.example.MyBookShopApp.data.payments;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "balance_transaction")
@Data
//@JsonIgnoreProperties({"book", "user"})
public class BalanceTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "INT NOT NULL")
    private Integer userId;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime time;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private Integer value;

     @Column(name = "bookId",columnDefinition = "INT NOT NULL")
     private Integer bookId;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookId", insertable = false, updatable = false)
    private Book book;

     @ManyToOne
     @JoinColumn(name = "userId", insertable = false, updatable = false)
     private User user;
}
