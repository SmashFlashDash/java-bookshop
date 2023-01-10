package com.example.MyBookShopApp.data.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenreRepository extends JpaRepository<GenreEntity, Integer> {

    public GenreEntity findByName(String name);
    public GenreEntity findBySlug(String slug);
}
