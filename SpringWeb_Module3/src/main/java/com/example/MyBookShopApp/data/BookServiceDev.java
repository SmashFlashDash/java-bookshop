package com.example.MyBookShopApp.data;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@Profile("dev")
public class BookServiceDev extends BookService {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public BookServiceDev(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void generateAuthorData() {
        Faker faker = new Faker(new Locale("ru"));
        // получить id авторов из БД
        List<Author> authors = this.jdbcTemplate.query("SELECT DISTINCT CAST(author_id AS INT) AS author_id FROM books " +
                "ORDER BY author_id", (ResultSet rs, int rownum) -> {
            Author author = new Author();
            author.setId(rs.getInt("author_id"));
            return author;
        });
        // сгенерировать уникальные имена
        List<String> uniqNames = Stream.generate(() ->
                faker.name().firstName().concat(" ").concat(faker.name().lastName()))
                .distinct().limit(authors.size()).collect(Collectors.toList());
        //присвоить id
        IntStream.range(0, uniqNames.size()).forEach(id -> authors.get(id).setName(uniqNames.get(id)));
        // запись в БД
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(authors.toArray());
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(
                "INSERT INTO authors (id, name) VALUES (:id ,:name)", batch);
    }
}
