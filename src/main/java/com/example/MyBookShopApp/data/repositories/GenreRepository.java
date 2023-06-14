package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.genre.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

    Genre findByName(String name);

    Genre findBySlug(String slug);

    List<Genre> findBySlugIn(List<String> slug);

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
    @Query(value = "WITH RECURSIVE r AS ( " +
            "SELECT id, name, parent_id, slug, 1 AS depth FROM genre " +
            "WHERE id = :id " +
            "UNION ALL " +
            "SELECT g.id, g.name, g.parent_id, g.slug, depth + 1 FROM genre AS g " +
            "JOIN r ON g.parent_id = r.id" +
            ") " +
            "SELECT * FROM r;", nativeQuery = true)
    List<Genre> findAllNodesByGenreEntity(@Param("id") Integer id);

    @Query(value = "WITH RECURSIVE r AS ( " +
            "SELECT id, name, parent_id, slug, 1 AS depth FROM genre " +
            "WHERE slug = :slug " +
            "UNION ALL " +
            "SELECT g.id, g.name, g.parent_id, g.slug, depth + 1 FROM genre AS g " +
            "JOIN r ON g.parent_id = r.id" +
            ") " +
            "SELECT * FROM r;", nativeQuery = true)
    List<Genre> findAllNodesByGenreEntity(@Param("slug") String slug);
}
