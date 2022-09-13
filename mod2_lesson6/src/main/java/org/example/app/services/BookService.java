package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BookService {
    private final ProjectRepository<Book> bookRepo;
    private final Logger logger = Logger.getLogger(BookService.class);

    @Autowired
    public BookService(ProjectRepository<Book> bookRepo) {
        this.bookRepo = bookRepo;
    }

    private static boolean regexCompare(String operator, String value, String regexValue) {
        if (Arrays.asList("=", "==").contains(operator)) {
            return value.equals(regexValue);
        } else if (operator.equals("!=")) {
            return !value.equals(regexValue);
        } else {
            throw new IllegalArgumentException("Error operator argument");
        }
    }

    private static boolean regexCompare(String operator, int value, int regexValue) {
        if (Arrays.asList("=", "==").contains(operator)) {
            return value == regexValue;
        } else if (">=".equals(operator)) {
            return value >= regexValue;
        } else if (">".equals(operator)) {
            return value > regexValue;
        } else if ("<=".equals(operator)) {
            return value <= regexValue;
        } else if ("<".equals(operator)) {
            return value < regexValue;
        } else if ("!=".equals(operator)) {
            return value != regexValue;
        } else {
            throw new IllegalArgumentException("Error operator argument");
        }
    }

    public List<Book> getAllBooks() {
        return bookRepo.retreiveAll();
    }

    public void saveBook(Book book) {
        bookRepo.store(book);
    }

    public boolean removeBookById(Integer bookIdToRemove) {
        return bookRepo.removeItemById(bookIdToRemove);
    }

    public void removeBookByRegex(String type, String operator, String value) {
        bookRepo.retreiveAll().stream().forEach(book -> {
            boolean isToRemove;
            switch (type) {
                case "id":
                    isToRemove = regexCompare(operator, book.getId(), Integer.parseInt(value));
                    break;
                case "size":
                    isToRemove = regexCompare(operator, book.getSize(), Integer.parseInt(value));
                    break;
                case "author":
                    isToRemove = regexCompare(operator, book.getAuthor(), value);
                    break;
                case "title":
                    isToRemove = regexCompare(operator, book.getTitle(), value);
                    break;
                default:
                    throw new IllegalArgumentException("Error: unhandable type value");
            }
            if (isToRemove) {
                bookRepo.removeItemById(book.getId());
            }
        });
    }

    public void defaultInit() {
        logger.info("default INIT in book service");
    }

    public void defaultDestroy() {
        logger.info("default DESTROY in book service");
    }
}
