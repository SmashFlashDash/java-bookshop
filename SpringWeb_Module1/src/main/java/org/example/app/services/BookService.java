package org.example.app.services;

import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final ProjectRepository<Book> bookRepo;

    @Autowired
    public BookService(ProjectRepository<Book> bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepo.retreiveAll();
    }

    public boolean saveBook(Book book) {
        if (book.getAuthor().isEmpty() && book.getTitle().isEmpty() && book.getSize() == null) {
            return false;
        }
        bookRepo.store(book);
        return true;
    }

    public boolean removeBookById(Integer bookIdToRemove) {
        return bookRepo.removeItemById(bookIdToRemove);
    }

    public void removeBookByRegex(String bookRegexToRemove) {
        Pattern re = Pattern.compile("(author|title|id|size)\\s*(==|=|>=|>|<|<=|!=)\\s*(.+)",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = re.matcher(bookRegexToRemove);
        matcher.find();
        String field = matcher.group(1).toLowerCase();
        String operator = matcher.group(2);
        String argument = matcher.group(3);
        if (Arrays.asList(field, operator, argument).contains("")){
            return;
        }

        boolean isEqualSign = Arrays.asList("=", "==").contains(operator);
        if (field.equals("author") && isEqualSign){
            bookRepo.retreiveAll().stream()
                    .filter(x -> x.getAuthor().equals(argument))
                    .forEach(x -> bookRepo.removeItemById(x.getId()));
        } else if (field.equals("title") && isEqualSign) {
            bookRepo.retreiveAll().stream()
                    .filter(x -> x.getTitle().equals(argument))
                    .forEach(x -> bookRepo.removeItemById(x.getId()));
        } else {
            int value;
            try {
                value = Integer.parseInt(argument);
            } catch (NumberFormatException ex) {
                return;
            }
            if (field.equals("size")) {
                bookRepo.retreiveAll().stream()
                        .filter(x -> BookService.regexIntCompare(operator, x.getSize(), value))
                        .forEach(x -> bookRepo.removeItemById(x.getId()));
            } else if (field.equals("id")) {
                bookRepo.retreiveAll().stream()
                        .filter(x -> BookService.regexIntCompare(operator, x.getId(), value))
                        .forEach(x -> bookRepo.removeItemById(x.getId()));
            }
        }
    }

    private static boolean regexIntCompare(String operator, int value, int regexValue){
        if (Arrays.asList("=", "==").contains(operator)){
            return value == regexValue;
        } else if (">=".equals(operator)){
            return value >= regexValue;
        }  else if (">".equals(operator)){
            return value > regexValue;
        }  else if ("<=".equals(operator)){
            return value <= regexValue;
        } else if ("<".equals(operator)){
            return value < regexValue;
        } else if ("!=".equals(operator)){
            return value != regexValue;
        } else {
            System.out.println("Err::: Некорректный типо оператора ".concat(operator));
            return false;
        }
    }
}
