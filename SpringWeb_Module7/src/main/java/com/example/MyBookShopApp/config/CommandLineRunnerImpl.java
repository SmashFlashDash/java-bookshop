package com.example.MyBookShopApp.config;

import com.example.MyBookShopApp.data.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandLineRunnerImpl implements CommandLineRunner {

    BookRepository bookRepository;

    @Autowired
    public CommandLineRunnerImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        deleteTestEntityById(0L);
    }

    private void deleteTestEntityById(Long id) {
        System.out.println("method");
    }
}
