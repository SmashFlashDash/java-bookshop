package com.example.MyBookShopApp.data.genre;

import org.apache.logging.log4j.util.PropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GenreService {
    GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public GenreEntity getGenreEntityByName(String name){
        return genreRepository.findByName(name);
    }

    public GenreEntity getGenreEntityBySlug(String slug){
        return genreRepository.findBySlug(slug);
    }

    public List<GenreEntity> getGenreEntitesBySlugs(List<String> slugs) {
        return genreRepository.findBySlugIn(slugs);
    }

    public GenreDto getAllGenresDto(){
        // TODO: вынести триггер в БД при добаавлении в book2genre изменять поле countBooks в genreEntity
        // но тогда и у жанров наслужемых изменяется количество
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
