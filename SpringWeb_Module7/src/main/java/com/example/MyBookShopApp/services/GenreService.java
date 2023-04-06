package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.dto.GenreDto;
import com.example.MyBookShopApp.data.genre.GenreEntity;
import com.example.MyBookShopApp.data.genre.MapperGenreEntityToGenreDro;
import com.example.MyBookShopApp.data.repositories.GenreRepository;
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

    public GenreEntity getGenreEntityByName(String name) {
        return genreRepository.findByName(name);
    }

    public GenreEntity getGenreEntityBySlug(String slug) {
        return genreRepository.findBySlug(slug);
    }

    public List<GenreEntity> getGenreEntitesBySlugs(List<String> slugs) {
        return genreRepository.findBySlugIn(slugs);
    }

    public GenreDto getAllGenresDto() {
        GenreDto root = new GenreDto();
        MapperGenreEntityToGenreDro.treeOfMap(genreRepository.findAll(), root);
        return root;
    }

    public List<GenreEntity> getGenreNodes(GenreEntity genreEntity) {
        return genreRepository.findAllNodesByGenreEntity(genreEntity.getId());
    }

    public List<GenreEntity> getGenreNodes(String slug) {
        return genreRepository.findAllNodesByGenreEntity(slug);
    }
}
