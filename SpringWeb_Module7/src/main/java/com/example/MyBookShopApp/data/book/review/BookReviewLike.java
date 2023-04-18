package com.example.MyBookShopApp.data.book.review;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_review_like")
public class BookReviewLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "INT NOT NULL")
    private int reviewId;

    @Column(columnDefinition = "INT NOT NULL")
    private int userId;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime time;

    @Column(columnDefinition = "SMALLINT NOT NULL")
    private short value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewId", insertable = false, updatable = false)
    private BookReview review;

    public BookReview getReview() {
        return review;
    }

    public void setReview(BookReview review) {
        this.review = review;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
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

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }
}
