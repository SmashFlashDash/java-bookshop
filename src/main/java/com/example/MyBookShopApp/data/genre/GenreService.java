package com.example.MyBookShopApp.data.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public GenreDto getAllGenresDto(){
        GenreDto root = new GenreDto();
        root.treeOfMap(genreRepository.findAll());
        // TODO: вынести триггер в БД при добаавлении в book2genre изменять поле countBooks в genreEntity
        // но тогда и у жанров наслужемых изменяется количество
        // можно обьеденить два метода с рекурсиями
        // TODO: TreeSet не сортирует при update значений
        // root.calculateCountBooks();
        // root.calculateDepth();
        return root;
    }

    public List<GenreEntity> getGenresBySlugsArray(List<String> slugs) {
        return genreRepository.findBySlugIn(slugs);
    }
}
