package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.author.Author;
import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.genre.Genre;
import com.example.MyBookShopApp.data.tag.TagEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    Book findBookBySlug(String slug);

    List<Book> findBooksBySlugIn(String[] slugs);

    @Query("from Book where isBestseller=1")
    List<Book> getBestsellers();

    @Query(value = "SELECT * FROM books WHERE discount = (SELECT MAX(discount) FROM books)", nativeQuery = true)
    List<Book> getBooksWithMaxDiscount();

    Page<Book> findBookByTitleContaining(String bookTitle, Pageable nextPage);

    Page<Book> findAllByTagsContainingOrderByPubDateDesc(TagEntity tag, Pageable nexPage);

    Page<Book> findByGenreOrderByPubDateDesc(Genre genre, Pageable nexPage);

    Page<Book> findAllByGenreInOrderByPubDateDesc(Collection<Genre> genre, Pageable nexPage);

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

    List<Book> findBooksByTitleIn(Collection<String> titles);

    @Query(value = "SELECT b.* FROM book b LEFT JOIN " +
            "(SELECT book_id, AVG(value) AS avg  FROM book_rating AS br GROUP BY book_id) avg " +
            "ON b.id = avg.book_id " +
            "ORDER BY avg DESC NULLS LAST, pub_date DESC", nativeQuery = true)
    Page<Book> findRecommendedBooks(Pageable nextPage);

    @Query(value = "SELECT b.* from book b " +
            "LEFT JOIN (SELECT * FROM book2genre b2g INNER JOIN genre g ON b2g.genre_id = g.id " +
            "     WHERE g.name in :genres) b2g " +
            "ON b2g.book_id = b.id " +
//            "LEFT JOIN (SELECT * FROM book2tag b2t INNER JOIN tag t ON b2t.tag_id = t.id" +
//            "     WHERE t.tag in :tags) b2t " +
//            "ON b2t.book_id = b.id " +
//            "LEFT JOIN (SELECT * FROM book2author b2a INNER JOIN author a ON b2a.author_id = a.id" +
//            "     WHERE a.name in :authors) b2a " +
//            "ON b2a.book_id = b.id " +
            "LEFT JOIN (SELECT book_id, AVG(value) avg FROM book_rating br GROUP BY book_id) avg " +
            "ON b.id = avg.book_id " +
            "ORDER BY avg.avg DESC NULLS LAST, pub_date DESC", nativeQuery = true)
//            "ORDER BY avg.avg DESC NULLS LAST, b2t.tag, b2g.name, pub_date DESC", nativeQuery = true)
    Page<Book> findBooksByGenreInOrTagsInOrAuthorIn(@Param("genres") Collection<Genre> genres,
//                                                    @Param("tags") Collection<TagEntity> tags,
//                                                    @Param("authors") Collection<Author> authors,
                                                    Pageable nexPage);
}
