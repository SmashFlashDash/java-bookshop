package com.example.MyBookShopApp.data.book;

import com.example.MyBookShopApp.data.author.Author;
import com.example.MyBookShopApp.data.book.file.FileDownloadEntity;
import com.example.MyBookShopApp.data.book.review.BookReviewEntity;
import com.example.MyBookShopApp.data.genre.GenreEntity;
import com.example.MyBookShopApp.data.payments.BalanceTransactionEntity;
import com.example.MyBookShopApp.data.user.UserEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String description;

    @Column(columnDefinition = "SMALLINT NOT NULL")
    private short discount;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String image;

    @Column(columnDefinition = "SMALLINT NOT NULL")
    private short is_bestseller;

    @Column(columnDefinition = "INT NOT NULL")
    private int price;

    @Column(columnDefinition = "DATE NOT NULL")
    @Temporal(TemporalType.DATE)
    private Date pub_date;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "book2author",
            joinColumns = {@JoinColumn(name = "bookId")},
            inverseJoinColumns = {@JoinColumn(name = "authorId")})
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "book2genre",
            joinColumns = {@JoinColumn(name = "bookId")},
            inverseJoinColumns = {@JoinColumn(name = "genreId")})
    private GenreEntity genre;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "book2user",
            joinColumns = {@JoinColumn(name = "bookId")},
            inverseJoinColumns = {@JoinColumn(name = "userId")})
    private List<UserEntity> users;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "file_download",
            joinColumns = @JoinColumn(name = "bookId", referencedColumnName = "Id"),
            inverseJoinColumns = @JoinColumn(name = "userId"))
    private List<FileDownloadEntity> fileDownload;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "balance_transaction",
//            joinColumns = @JoinColumn(name = "bookId"),
//            inverseJoinColumns = @JoinColumn(name = "userId"))
//    private List<BalanceTransactionEntity> balanceTransaction;
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "book_review",
//            joinColumns = {@JoinColumn(name = "bookId")},
//            inverseJoinColumns = {@JoinColumn(name = "userId")})
//    private List<BookReviewEntity> bookReview;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public GenreEntity getGenre() {
        return genre;
    }

    public void setGenre(GenreEntity genre) {
        this.genre = genre;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }

    public List<FileDownloadEntity> getFileDownload() {
        return fileDownload;
    }

    public void setFileDownload(List<FileDownloadEntity> fileDownloadEntities) {
        this.fileDownload = fileDownloadEntities;
    }

//    public List<BalanceTransactionEntity> getBalanceTransaction() {
//        return balanceTransaction;
//    }
//
//    public void setBalanceTransaction(List<BalanceTransactionEntity> balanceTransactionEntity) {
//        this.balanceTransaction = balanceTransactionEntity;
//    }
//
//    public List<BookReviewEntity> getBookReview() {
//        return bookReview;
//    }
//
//    public void setBookReview(List<BookReviewEntity> bookReviewEntity) {
//        this.bookReview = bookReviewEntity;
//    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public short getDiscount() {
        return discount;
    }

    public void setDiscount(short discount) {
        this.discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public short getIs_bestseller() {
        return is_bestseller;
    }

    public void setIs_bestseller(short is_bestseller) {
        this.is_bestseller = is_bestseller;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getPub_date() {
        return pub_date;
    }

    public void setPub_date(Date pub_date) {
        this.pub_date = pub_date;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author=" + author +
                ", title='" + title + '\'' +
                ", price=" + price +
                '}';
    }
}
