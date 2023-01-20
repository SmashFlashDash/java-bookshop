package com.example.MyBookShopApp.data.author;

import com.example.MyBookShopApp.data.author.Author;
import com.example.MyBookShopApp.data.author.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author getAuthorBySlug(String slug){
        return authorRepository.findBySlug(slug);
    }

    public Map<String, List<Author>> getAuthorsMap(){
        return authorRepository.findAll().stream().collect(Collectors.groupingBy((Author a)-> a.getName().substring(0,1)));
    }
}
