package com.example.MyBookShopApp.data.book.review;

import com.example.MyBookShopApp.data.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "book_review")
@Data
public class BookReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "INT NOT NULL")
    private int bookId;

    @Column(columnDefinition = "INT NOT NULL")
    private int userId;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String text;

    @OneToMany(mappedBy = "review")
    private List<BookReviewLike> bookReviewLike;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    public long getDislikesCount() {
        return getBookReviewLike().stream().filter(f -> f.getValue() < 0).count();
    }

    public long getLikesCount() {
        return getBookReviewLike().stream().filter(f -> f.getValue() > 0).count();
    }
}
