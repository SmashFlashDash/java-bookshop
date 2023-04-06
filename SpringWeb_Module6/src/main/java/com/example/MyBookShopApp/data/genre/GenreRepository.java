package com.example.MyBookShopApp.data.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface GenreRepository extends JpaRepository<GenreEntity, Integer> {

    GenreEntity findByName(String name);
    GenreEntity findBySlug(String slug);
    List<GenreEntity> findBySlugIn(List<String> slug);

//    WITH RECURSIVE r AS (
//            SELECT id, name, parent_id, slug, 1 AS depth FROM genre
//            WHERE id = 2
//
//            UNION ALL
//
//            SELECT g.id, g.name, g.parent_id, g.slug, depth + 1 FROM genre AS g, r AS re
//                    WHERE g.parent_id = re.id
//    второй вирант
//    SELECT g.id, g.name, g.parent_id, g.slug, depth + 1 FROM genre AS g
//    JOIN r ON g.parent_id = r.id
//    )
//    SELECT * FROM r;
    @Query(value="WITH RECURSIVE r AS ( " +
            "SELECT id, name, parent_id, slug, 1 AS depth FROM genre " +
            "WHERE id = :id " +
            "UNION ALL " +
            "SELECT g.id, g.name, g.parent_id, g.slug, depth + 1 FROM genre AS g " +
            "JOIN r ON g.parent_id = r.id" +
            ") " +
            "SELECT * FROM r;", nativeQuery = true)
    List<GenreEntity> findAllNodesByGenreEntity(@Param("id") Integer id);

    @Query(value="WITH RECURSIVE r AS ( " +
            "SELECT id, name, parent_id, slug, 1 AS depth FROM genre " +
            "WHERE slug = :slug " +
            "UNION ALL " +
            "SELECT g.id, g.name, g.parent_id, g.slug, depth + 1 FROM genre AS g " +
            "JOIN r ON g.parent_id = r.id" +
            ") " +
            "SELECT * FROM r;", nativeQuery = true)
    List<GenreEntity> findAllNodesByGenreEntity(@Param("slug") String slug);
}
