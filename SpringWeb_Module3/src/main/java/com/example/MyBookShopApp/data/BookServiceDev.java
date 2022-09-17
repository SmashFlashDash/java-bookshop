package com.example.MyBookShopApp.data;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@Profile("dev")
public class BookServiceDev implements BookServiceInterface {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public BookServiceDev(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        Faker faker = new Faker(new Locale("ru"));
        // получить id авторов из БД
        List<Author> authors = jdbcTemplate.query("SELECT DISTINCT CAST(author_id AS INT) AS author_id FROM books " +
                "ORDER BY author_id", (ResultSet rs, int rownum) -> {
            Author author = new Author();
            author.setId(rs.getInt("author_id"));
            return author;
        });
        // сгенерировать уникальные имена и присвоить обьектам
        List<String> uniqNames = Stream.generate(() ->
                faker.name().firstName().concat(" ").concat(faker.name().lastName()))
                .distinct().limit(authors.size()).collect(Collectors.toList());
        IntStream.range(0, uniqNames.size()).forEach(id -> authors.get(id).setName(uniqNames.get(id)));
        // запись в БД
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(authors.toArray());
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(
                "INSERT INTO authors (id, name) VALUES (:id ,:name)", batch);
//        jdbcTemplate.batchUpdate("INSERT INTO authors (name) VALUES (?)",
//                new BatchPreparedStatementSetter() {
//                    @Override
//                    public void setValues(PreparedStatement ps, int i) throws SQLException {
//                        ps.setString(1, authors.get(i).getName());
//                    }
//                    @Override
//                    public int getBatchSize() {
//                        return authors.size();
//                    }
//                });
    }

    @Override
    public List<Author> getAuthorsData() {
        List<Author> authors = jdbcTemplate.query("SELECT * FROM authors", (ResultSet rs, int rownum) -> {
            Author author = new Author();
            author.setId(rs.getInt("id"));
            author.setName(rs.getString("name"));
            return author;
        });
        return new ArrayList<>(authors);
    }

    @Override
    public List<Book> getBooksData() {
        List<Book> books = jdbcTemplate.query("SELECT * FROM books", (ResultSet rs, int rownum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author_id"));
            book.setTitle(rs.getString("title"));
            book.setPriceOld(rs.getInt("price_old"));
            book.setPrice(rs.getInt("price"));
            return book;
        });
        return new ArrayList<>(books);
    }
}
