package com.example.MyBookShopApp.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private AuthorService(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public Map<String, List<Author>> getAuthorsMap() {
        List<Author> authors = jdbcTemplate.query("SELECT * FROM authors", (ResultSet rs, int rownum) -> {
            Author author = new Author();
            author.setId(rs.getInt("id"));
            author.setName(rs.getString("name"));
            return author;
        });
        authors.sort(Comparator.comparing(Author::getName));
        return authors.stream().collect(Collectors.groupingBy((Author a) -> {return a.getName().substring(0,1);},
                LinkedHashMap::new, Collectors.toList()));
    }
}
