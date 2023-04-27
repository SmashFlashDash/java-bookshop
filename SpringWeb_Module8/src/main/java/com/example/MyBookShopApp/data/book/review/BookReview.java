package com.example.MyBookShopApp.data.book.review;

import com.example.MyBookShopApp.data.user.UserEntity;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "book_review")
public class BookReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "INT NOT NULL")
    private int bookId;

    // TODO: есть еще поле связи с user OneToMany правильно ли так делать
    //  чтобы не запрашивать обьект user по id из бд
    //  а сразу установить по id
    @Column(columnDefinition = "INT NOT NULL")
    private int userId;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime time;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String text;

//    @OneToOne(optional = false)
//    @JoinColumn(name = "book_rating_id", referencedColumnName = "id")
//    private BookRating bookRating;

    @OneToMany(mappedBy = "review")
    private List<BookReviewLike> bookReviewLike;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private UserEntity user;

    public UserEntity getUser() {
        return user;
    }

//    public BookRating getBookRating() {
//        return bookRating;
//    }
//
//    public void setBookRating(BookRating bookRating) {
//        this.bookRating = bookRating;
//    }

    public List<BookReviewLike> getBookReviewLike() {
        return bookReviewLike;
    }

    public void setBookReviewLike(List<BookReviewLike> bookReviewLike) {
        this.bookReviewLike = bookReviewLike;
    }

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
