package com.example.MyBookShopApp.data.user;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.book.file.FileDownloadEntity;
import com.example.MyBookShopApp.data.book.review.BookReviewEntity;
import com.example.MyBookShopApp.data.payments.BalanceTransactionEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String hash;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime regTime;

    @Column(columnDefinition = "INT NOT NULL")
    private int balance;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private List<Book> books;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "file_download",
//            joinColumns = @JoinColumn(name = "userId"),
//            inverseJoinColumns = @JoinColumn(name = "bookId"))
//    private List<FileDownloadEntity> fileDownload;
//
//    // TODO: не правильно мапит по UserId
//    //  всегда получает обьект по balance_transaction.id=1
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "balance_transaction",
//            joinColumns = @JoinColumn(name = "userId"),
//            inverseJoinColumns = @JoinColumn(name = "bookId"))
//    private List<BalanceTransactionEntity> BalanceTransaction;
//
//
//    // TODO: не правильно мапит по UserId
//    //  всегда получает обьект по book_review.id=1
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "book_review",
//            joinColumns = @JoinColumn(name = "userId"),
//            inverseJoinColumns = @JoinColumn(name = "bookId"))
//    private List<BookReviewEntity> bookReview;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "message",
//            joinColumns = {@JoinColumn(name = "bookId")},
//            inverseJoinColumns = {@JoinColumn(name = "userId")})
//    private List<BookReviewEntity> bookReviewEntity;
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "balance_transaction",
//            joinColumns = {@JoinColumn(name = "bookId")},
//            inverseJoinColumns = {@JoinColumn(name = "userId")})
//    private List<BookReviewEntity> bookReviewEntity;
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "file_download",
//            joinColumns = {@JoinColumn(name = "bookId")},
//            inverseJoinColumns = {@JoinColumn(name = "userId")})
//    private List<BookReviewEntity> bookReviewEntity;
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "book2user",
//            joinColumns = {@JoinColumn(name = "bookId")},
//            inverseJoinColumns = {@JoinColumn(name = "userId")})
//    private List<BookReviewEntity> bookReviewEntity;
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "user_contact",
//            joinColumns = {@JoinColumn(name = "bookId")},
//            inverseJoinColumns = {@JoinColumn(name = "userId")})
//    private List<BookReviewEntity> bookReviewEntity;
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "book_review_like",
//            joinColumns = {@JoinColumn(name = "bookId")},
//            inverseJoinColumns = {@JoinColumn(name = "userId")})
//    private List<BookReviewEntity> bookReviewEntity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public LocalDateTime getRegTime() {
        return regTime;
    }

    public void setRegTime(LocalDateTime regTime) {
        this.regTime = regTime;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
