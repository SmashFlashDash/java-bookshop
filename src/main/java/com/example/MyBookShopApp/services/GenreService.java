package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.genre.Genre;
import com.example.MyBookShopApp.data.genre.MapperGenreEntityToGenreDro;
import com.example.MyBookShopApp.data.repositories.GenreRepository;
import com.example.MyBookShopApp.dto.GenreDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {
    GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Genre getGenreByName(String name) {
        return genreRepository.findByName(name);
    }

    public Genre getGenreBySlug(String slug) {
        return genreRepository.findBySlug(slug);
    }

    public List<Genre> getGenreEntitesBySlugs(List<String> slugs) {
        return genreRepository.findBySlugIn(slugs);
    }

    public GenreDto getAllGenresDto() {
        GenreDto root = new GenreDto();
        MapperGenreEntityToGenreDro.treeOfMap(genreRepository.findAll(), root);
        return root;
    }

    public List<Genre> getGenreNodes(Genre genreEntity) {
        return genreRepository.findAllNodesByGenreEntity(genreEntity.getId());
    }

    public List<Genre> getGenreNodes(String slug) {
        return genreRepository.findAllNodesByGenreEntity(slug);
    }
}
