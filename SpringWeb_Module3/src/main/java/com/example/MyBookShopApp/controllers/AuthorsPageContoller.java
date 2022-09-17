package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Author;
import com.example.MyBookShopApp.data.BookServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/authors")
public class AuthorsPageContoller {
    private final LinkedHashMap<String, String> alphabetLink = new LinkedHashMap<String, String>() {{
        put("а", "a");
        put("б", "b");
        put("в", "v");
        put("г", "g");
        put("д", "d");
        put("е", "e");
        put("ё", "e");
        put("ж", "ge");
        put("з", "z");
        put("и", "i");
        put("й", "ik");
        put("к", "k");
        put("л", "L");
        put("м", "m");
        put("н", "n");
        put("о", "o");
        put("п", "p");
        put("р", "r");
        put("с", "s");
        put("т", "t");
        put("у", "u");
        put("ф", "f");
        put("х", "h");
        put("ц", "c");
        put("ч", "ch");
        put("ш", "sh");
        put("щ", "shh");
//        put("Ъ", "?");
//        put("Ы", "?");
//        put("Ь", "?");
        put("э", "ye");
        put("ю", "yu");
        put("я", "ya");
    }};

    BookServiceInterface bookService;

    @Autowired
    public AuthorsPageContoller(BookServiceInterface bookService) {
        this.bookService = bookService;
    }

    @GetMapping("")
    public String authorsPage(Model model) {
        List<Author> authors = bookService.getAuthorsData();
        authors.sort(Comparator.comparing(Author::getName));
        // генерим список обьектов с полями id, symb, List<String> authornames
        ArrayList<AuthorAlphabet> authorsNavigate = new ArrayList<>();
        List<String> authorsByChar;
        int authorIndex = 0;
        for (Map.Entry<String, String> item : alphabetLink.entrySet()) {
            authorsByChar = new ArrayList<>();
            authorsNavigate.add(new AuthorAlphabet(item.getValue(), item.getKey().toUpperCase(), authorsByChar));
            while (authorIndex < authors.size() && authors.get(authorIndex).getName().substring(0, 1).toLowerCase().equals(item.getKey())) {
                System.out.println("Добавить автора: " + authorIndex);
                authorsByChar.add(authors.get(authorIndex).getName());
                authorIndex++;
            }
        }
        model.addAttribute("authors", authorsNavigate);
        return "/authors/index";
    }
}


class AuthorAlphabet {
    /**
     * Обьект для th
     */
    private String id;
    private String symb;
    private List<String> authorsList;

    public AuthorAlphabet(String id, String symb, List<String> authorsList) {
        this.id = id;
        this.symb = symb;
        this.authorsList = authorsList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymb() {
        return symb;
    }

    public void setSymb(String symb) {
        this.symb = symb;
    }

    public List<String> getAuthorsList() {
        return authorsList;
    }

    public void setAuthorsList(List<String> authorsList) {
        this.authorsList = authorsList;
    }
}
