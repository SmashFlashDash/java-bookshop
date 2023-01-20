package com.example.MyBookShopApp.data.book;

import com.example.MyBookShopApp.data.author.Author;
import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.book.BookRepository;
import com.example.MyBookShopApp.data.genre.GenreEntity;
import com.example.MyBookShopApp.data.genre.GenreRepository;
import com.example.MyBookShopApp.data.tag.TagEntity;
import com.example.MyBookShopApp.data.tag.TagRepository;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.net.ContentHandler;
import java.util.*;

@Service
public class BookService {
    BookRepository bookRepository;
    TagRepository tagRepository;
    GenreRepository genreRepository;
    @Autowired
    public BookService(BookRepository bookRepository, TagRepository tagRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.tagRepository = tagRepository;
        this.genreRepository = genreRepository;
    }

    public List<Book> getBooksData() {
        return bookRepository.findAll();
    }

    //NEW BOOK SEVICE METHODS
//    public List<Book> getBooksByAuthor(String authorName){
//        return bookRepository.findBookByAuthorNameContaining(authorName);
//    }
//    public List<Book> getBooksByTitle(String title){
//        return bookRepository.findBookByTitleContaining(title);
//    }
//    public List<Book> getBooksWithPriceBetween(Integer min, Integer max){
//        return bookRepository.findBooksByPriceBetween(min,max);
//    }
//    public List<Book> getBooksWithPrice(Integer price){
//        return bookRepository.findBooksByPriceIs(price);
//    }
//    public List<Book> getBooksWithMaxPrice(){
//        return bookRepository.getBooksWithMaxDiscount();
//    }
//    public List<Book> getBestsellers(){
//        return bookRepository.getBestsellers();
//    }

    public Page<Book> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit){
        return bookRepository.findBookByTitleContaining(searchWord, PageRequest.of(offset,limit));
    }
    public Page<Book> getPageOfRecommendedBooks(Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset,limit);
        return bookRepository.findAll(nextPage);
    }
    public Page<Book> getPageOfNewBooks(Integer offset, Integer limit) {
        return bookRepository.findAllByOrderByPubDateDesc(PageRequest.of(offset, limit));
//        return bookRepository.findAll(PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "pubDate")));
    }
    public Page<Book> getPageOfBestsellersBooks(Integer offset, Integer limit) {
        return bookRepository.findBookByIsBestsellerEqualsOrderByPubDateDesc((short) 1, PageRequest.of(offset, limit));
    }
    public Page<Book> getPageOfPopularBooks(Integer offset, Integer limit) {
        return bookRepository.findAllByOrderByPopular(PageRequest.of(offset, limit));
    }
    public Page<Book> getPageOfNewBooksDateFrom(Date from, Integer offset, Integer limit) {
        return bookRepository.findAllByPubDateAfterOrderByPubDateDesc(from, PageRequest.of(offset, limit));
    }
    public Page<Book> getPageOfNewBooksDateTo(Date to, Integer offset, Integer limit) {
        return bookRepository.findAllByPubDateBeforeOrderByPubDateDesc(to, PageRequest.of(offset, limit));
    }
    public Page<Book> getPageOfNewBooksDateBetween(Date from, Date to, Integer offset, Integer limit) {
        return bookRepository.findAllByPubDateBetweenOrderByPubDateDesc(from, to, PageRequest.of(offset, limit));
    }
    public Page<Book> getPageOfBooksByTag(TagEntity tagEntity, Integer offset, Integer limit) {
        if (tagEntity == null){
            return Page.empty();
        } else {
            return bookRepository.findAllByTagsContainingOrderByPubDateDesc(tagEntity, PageRequest.of(offset, limit));
        }

    }

    public Page<Book> getPageOfBooksByListGenres(List<GenreEntity> genres, Integer offset, Integer limit){
        if (genres == null || genres.isEmpty()){
            return Page.empty();
        } else {
            return bookRepository.findAllByGenreInOrderByPubDateDesc(genres, PageRequest.of(offset, limit));
        }
    }

    public Page<Book> getPageOfBooksByAuthor(Author author, Integer offset, Integer limit) {
        return bookRepository.findAllByAuthorOrderByPubDateDesc(author, PageRequest.of(offset, limit));
    }
}
