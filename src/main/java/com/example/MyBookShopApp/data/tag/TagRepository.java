package com.example.MyBookShopApp.data.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<TagEntity, Integer> {
    @Query("FROM TagEntity AS t ORDER BY size(t.books) DESC, t.tag ASC")
    List<TagEntity> findAllTagsSortedByBookCount();

    TagEntity findByTag(String tag);
}
