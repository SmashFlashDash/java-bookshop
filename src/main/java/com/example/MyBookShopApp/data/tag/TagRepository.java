package com.example.MyBookShopApp.data.tag;

import com.example.MyBookShopApp.data.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<TagEntity, Integer> {
    // SQL
    // @Query(value = "SELECT * FROM tag RIGHT JOIN " +
    //         "(SELECT tag_id, COUNT(book_id) book_count FROM book2tag GROUP BY tag_id) as tg " +
    //         "ON tg.tag_id = tag.id ORDER BY book_count DESC, tag ASC;", nativeQuery = true)
    // HQL
    @Query("FROM TagEntity AS t ORDER BY size(t.books) DESC, t.tag ASC")
    List<TagEntity> findAllTagsSortedByBookCount();

    TagEntity findByTag(String tag);
}
