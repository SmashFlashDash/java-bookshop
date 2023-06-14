package com.example.MyBookShopApp.dto;

import com.example.MyBookShopApp.data.author.Author;
import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.genre.Genre;
import lombok.Getter;

import java.util.Date;
import java.util.stream.Collectors;

@Getter
public class BookDto {
    private final Integer id;
    private final String description;
    private final short discount;
    private final String image;
    private final short isBestseller;
    private final int price;
    private final Date pubDate;
    private final String slug;
    private final String title;
    private final Integer statInCart;
    private final Integer statBought;
    private final Integer statPostponed;

    private final String author;
    private final String authorSlug;
    private final String genres;

    public BookDto(Book m) {
        this.id = m.getId();
        this.description = m.getDescription();
        this.isBestseller = m.getIsBestseller();
        this.discount = m.getDiscount();
        this.image = m.getImage();
        this.price = m.getPrice();
        this.slug = m.getSlug();
        this.pubDate = m.getPubDate();
        this.title = m.getTitle();
        this.statInCart = m.getStatInCart();
        this.statBought = m.getStatBought();
        this.statPostponed = m.getStatPostponed();

        this.author = m.getAuthor().stream().map(Author::getName).collect(Collectors.joining(", "));
        this.authorSlug = m.getAuthor().stream().map(Author::getSlug).collect(Collectors.joining(", "));
        this.genres = m.getGenre().stream().map(Genre::getName).collect(Collectors.joining(", "));
    }
}
