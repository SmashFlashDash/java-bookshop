package com.example.MyBookShopApp.data.user;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.book.file.FileDownloadEntity;
import com.example.MyBookShopApp.data.book.review.BookReviewEntity;
import com.example.MyBookShopApp.data.book.review.BookReviewLikeEntity;
import com.example.MyBookShopApp.data.book.review.MessageEntity;
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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private List<FileDownloadEntity> fileDownload;

    @OneToMany(fetch = FetchType.LAZY)
    private List<BalanceTransactionEntity> BalanceTransaction;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<MessageEntity> message;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private UserContactEntity userContact;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private List<BookReviewEntity> bookReview;

    @OneToMany
    @JoinColumn(name = "userId")
    private List<BookReviewLikeEntity> bookReviewLike;

    public List<MessageEntity> getMessage() {
        return message;
    }

    public void setMessage(List<MessageEntity> message) {
        this.message = message;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<FileDownloadEntity> getFileDownload() {
        return fileDownload;
    }

    public void setFileDownload(List<FileDownloadEntity> fileDownload) {
        this.fileDownload = fileDownload;
    }

    public List<BalanceTransactionEntity> getBalanceTransaction() {
        return BalanceTransaction;
    }

    public void setBalanceTransaction(List<BalanceTransactionEntity> balanceTransaction) {
        BalanceTransaction = balanceTransaction;
    }

//    public List<MessageEntity> getMessage() {
//        return message;
//    }
//
//    public void setMessage(List<MessageEntity> message) {
//        this.message = message;
//    }

    public UserContactEntity getUserContact() {
        return userContact;
    }

    public void setUserContact(UserContactEntity userContact) {
        this.userContact = userContact;
    }

    public List<BookReviewEntity> getBookReview() {
        return bookReview;
    }

    public void setBookReview(List<BookReviewEntity> bookReview) {
        this.bookReview = bookReview;
    }

    public List<BookReviewLikeEntity> getBookReviewLike() {
        return bookReviewLike;
    }

    public void setBookReviewLike(List<BookReviewLikeEntity> bookReviewLike) {
        this.bookReviewLike = bookReviewLike;
    }

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
