package com.example.MyBookShopApp.controllerAdvice;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.dto.ApiResponse;
import com.example.MyBookShopApp.errs.BookstoreApiWrongParameterException;
import com.example.MyBookShopApp.errs.EmptySearchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.security.sasl.AuthenticationException;
import java.util.logging.Logger;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(EmptySearchException.class)
    public String handleEmptySearchException(EmptySearchException e, RedirectAttributes redirectAttributes) {
        Logger.getLogger(this.getClass().getSimpleName()).warning(e.getLocalizedMessage());
        redirectAttributes.addFlashAttribute("searchError", e);
        return "redirect:/";
    }

    @ExceptionHandler(BookstoreApiWrongParameterException.class)
    public ResponseEntity<ApiResponse<Book>> handleBookstoreApiWrongParameterException(Exception exception) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Bad parameter value...", exception)
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Book>> handleMissingServletRequestParameterException(Exception exception) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Missing required parameters",
                exception), HttpStatus.BAD_REQUEST);
    }

    //@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Необходимо авторизоваться")
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> acessDenied(Exception exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Необходимо авторизоваться");
    }

    @ExceptionHandler(AuthenticationException.class)
    public boolean handleAuthentificationException(Exception exception) {
        // TODO: хорошо обьект вернуть но нужно изменить sript.min.js
        // ContactConfirmationResponse response = new ContactConfirmationResponse();
        // response.setResult(exception.getMessage());
        // return response;
        return false;
    }
}
