package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.author.Author;
import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.genre.GenreEntity;
import com.example.MyBookShopApp.data.repositories.BookRepository;
import com.example.MyBookShopApp.data.tag.TagEntity;
import com.example.MyBookShopApp.errs.BookstoreApiWrongParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BookService {

    BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooksData() {
        return bookRepository.findAll();
    }

    public List<Book> getBooksWithPriceBetween(Integer min, Integer max) {
        return bookRepository.findAllByPriceBetween(min, max);
    }

    public List<Book> getBooksByAuthor(String authorName) {
        return bookRepository.findBooksByAuthorNameContaining(authorName);
    }

    public List<Book> getBooksByTitle(String title) throws BookstoreApiWrongParameterException {
        if (title.equals("") || title.length() <= 1) {
            throw new BookstoreApiWrongParameterException("Wrong values passed to one or more parameters");
        } else {
            List<Book> data = bookRepository.findBooksByTitleContaining(title);
            if (data.size() > 0) {
                return data;
            } else {
                throw new BookstoreApiWrongParameterException("No data found with specified parameters...");
            }
        }
    }

    public List<Book> getBooksWithMaxPrice() {
        return bookRepository.getBooksWithMaxDiscount();
    }

    public List<Book> getBestsellers() {
        return bookRepository.getBestsellers();
    }

    public Page<Book> getPageofRecommendedBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAll(nextPage);
    }

    public Page<Book> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit) {
        return bookRepository.findBookByTitleContaining(searchWord, PageRequest.of(offset, limit));
    }

    public Page<Book> getPageOfRecommendedBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
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
        if (tagEntity == null) {
            return Page.empty();
        } else {
            return bookRepository.findAllByTagsContainingOrderByPubDateDesc(tagEntity, PageRequest.of(offset, limit));
        }

    }

    public Page<Book> getPageOfBooksByListGenres(List<GenreEntity> genres, Integer offset, Integer limit) {
        if (genres == null || genres.isEmpty()) {
            return Page.empty();
        } else {
            return bookRepository.findAllByGenreInOrderByPubDateDesc(genres, PageRequest.of(offset, limit));
        }
    }

    public Page<Book> getPageOfBooksByAuthor(Author author, Integer offset, Integer limit) {
        return bookRepository.findAllByAuthorOrderByPubDateDesc(author, PageRequest.of(offset, limit));
    }

}