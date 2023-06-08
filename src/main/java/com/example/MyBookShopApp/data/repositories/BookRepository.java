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

    List<Book> findBooksBySlugIn(Collection<String> titles);

    @Query("SELECT b from Book b ORDER BY b.raiting DESC NULLS LAST, b.pubDate DESC")
    Page<Book> findRecommendedBooks(Pageable nextPage);

    @Query(value = "SELECT b from Book b " +
            "LEFT JOIN b.genre g LEFT JOIN b.tags t LEFT JOIN b.author a " +
            "where g in :genres OR t in :tags OR a in :authors " +
            "ORDER BY b.raiting DESC NULLS LAST")
    Page<Book> findBooksByGenreInOrTagsInOrAuthorIn(@Param("genres") Collection<Genre> genres,
                                                    @Param("tags") Collection<TagEntity> tags,
                                                    @Param("authors") Collection<Author> authors,
                                                    Pageable nexPage);
}
