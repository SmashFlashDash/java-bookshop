package com.example.MyBookShopApp.data.user;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.book.file.FileDownloadEntity;
import com.example.MyBookShopApp.data.book.review.BookReview;
import com.example.MyBookShopApp.data.book.review.BookReviewLike;
import com.example.MyBookShopApp.data.book.review.MessageEntity;
import com.example.MyBookShopApp.data.payments.BalanceTransactionEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "`user`")
public class User {
//public class User implements UserDetails {

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

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String email;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String password;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String phone;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private List<Book> books;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private List<FileDownloadEntity> fileDownload;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private List<BalanceTransactionEntity> BalanceTransaction;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<MessageEntity> message;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserContact userContact;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<BookReview> bookReview;

    @OneToMany
    @JoinColumn(name = "userId")
    private List<BookReviewLike> bookReviewLike;


//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
//    }
//
//    @Override
//    public String getUsername() {
//        return getEmail();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
}
