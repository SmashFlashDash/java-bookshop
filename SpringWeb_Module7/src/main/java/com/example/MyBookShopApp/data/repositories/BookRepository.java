package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.author.Author;
import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.genre.GenreEntity;
import com.example.MyBookShopApp.data.tag.TagEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

//    List<Book> findBooksByAuthor_Name(String name);
//
//    @Query("from Book")
//    List<Book> customFindAllBooks();
//
//    //NEW BOOK REST REPOSITORY
//
//    List<Book> findBooksByAuthorNameContaining(String authorsFirstName);
//
//    List<Book> findBooksByTitleContaining(String bookTitle);
//
//    List<Book> findAllByPriceBetween(Integer min, Integer max);
//
//    @Query("from Book where isBestseller = 1")
//    List<Book> getBestsellers();
//
//    @Query(value = "SELECT * FROM books WHERE discount = (SELECT MAX(discount) FROM books", nativeQuery = true)
//    List<Book> getBooksWithMaxDiscount();
//
//    Page<Book> findBookByTitleContaining(String bookTitle, Pageable nextPage);
//

//


//    List<Book> findBookByAuthor_Name(String name);
//    @Query("from Book")
//    List<Book> customFindAllBooks();
//
//    //NEW BOOK REST REPOSITORY COMMANDS
//    List<Book> findBookByAuthorNameContaining(String authorFirstName);
//    List<Book> findBookByTitleContaining(String bookTitle);
//    List<Book> findBooksByPriceBetween(Integer min, Integer max);
//    List<Book> findBooksByPriceIs(Integer price);

    Book findBookBySlug(String slug);

    List<Book> findBooksBySlugIn(String[] slugs);

    @Query("from Book where isBestseller=1")
    List<Book> getBestsellers();

    @Query(value = "SELECT * FROM books WHERE discount = (SELECT MAX(discount) FROM books)", nativeQuery = true)
    List<Book> getBooksWithMaxDiscount();

    Page<Book> findBookByTitleContaining(String bookTitle, Pageable nextPage);

    Page<Book> findAllByTagsContainingOrderByPubDateDesc(TagEntity tag, Pageable nexPage);

    Page<Book> findAllByGenreOrderByPubDateDesc(GenreEntity genre, Pageable nexPage);

    Page<Book> findAllByGenreInOrderByPubDateDesc(List<GenreEntity> genre, Pageable nexPage);

    @Query("FROM Book b ORDER BY b.statBought + 0.7 * b.statInCart + 0.4 * b.statPostponed DESC")
    Page<Book> findAllByOrderByPopular(Pageable nextPage);

    // @Query("from Book book WHERE book.is_bestseller = 1 ORDER BY book.pub_date DESC ")
    Page<Book> findBookByIsBestsellerEqualsOrderByPubDateDesc(Short num, Pageable nextPage);

    Page<Book> findAllByOrderByPubDateDesc(Pageable nextPage);

    Page<Book> findAllByPubDateAfterOrderByPubDateDesc(Date from, Pageable nextPage);

    Page<Book> findAllByPubDateBeforeOrderByPubDateDesc(Date to, Pageable nextPage);

    Page<Book> findAllByPubDateBetweenOrderByPubDateDesc(Date from, Date to, Pageable nextPage);

    Page<Book> findAllByAuthorOrderByPubDateDesc(Author authorBySlug, Pageable nextPage);

    List<Book> findBooksByAuthorNameContaining(String authorName);

    List<Book> findAllByPriceBetween(Integer min, Integer max);

    List<Book> findBooksByTitleContaining(String title);
}
