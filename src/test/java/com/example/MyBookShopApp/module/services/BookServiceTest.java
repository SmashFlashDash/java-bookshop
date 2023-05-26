package com.example.MyBookShopApp.module.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookServiceTest {
  //  рассчитывающие популярность книги
  //  рейтинг отзыва на книгу
  //  список рекомендуемых пользователю книг.

//  Алгоритм выбора рекомендуемых книг
//  Алгоритм должен быть спроектирован и разработан студентом самостоятельно на основании следующих принципов:
//  Если пользователь не авторизован и не добавлял никакие книги в корзину или в отложенные, то рекомендации
//  должны строиться на основе тех книг, которые имеют наивысший рейтинг на сайте (изначально нужно для 50% книг
//  сгенерировать данные рейтинга), а также недавно появились.
//  Если пользователь авторизован, покупал какие-то книги либо добавлял книги в корзину или в отложенные, то рекомендации
//  должны строиться на основе этих добавлений. В этом случае рекомендуемые книги должны подбираться по тэгам,
//  жанрам и авторам книг, к которым пользователь имеет какое-либо отношение, а также по их новизне. Например,
//  пользователю должна отображаться в рекомендациях новая недавно вышедшая книга автора, книги которого он уже покупал.
//
//  Алгоритм расчета рейтинга книг
//  Рейтинг книги представляет собой число от 1 до 5, которое рассчитывается как среднее значение всех оценок пользователей данной книги, которые тоже могут быть равны от 1 до 5. Среднее значение округляется и получается рейтинг книги.
//    Примеры:
//  Оценки пользователей: 5, 3, 2, 5, 1, 1, 5, 4. Среднее значение равно 26/8 = 3,25, то есть рейтинг равен 3.
//  Оценки пользователей: 5, 2. Среднее значение 7/2 = 3,5, то есть рейтинг равен 4.
//  В случае, если ни один пользователь не оценил книгу, её рейтинг равен 0. Студенту необходимо самостоятельно
//  спроектировать и реализовать таблицу в базе данных для хранения рейтинга книг.
//
//  Алгоритм расчёта рейтинга отзывов
//  Рейтинг отзыва рассчитывается как разница между количеством лайков и дизлайков отзыва.
//  Если нет ни одного лайка и ни одного дизлайка, рейтинг равен 0.

  @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getBooksData() {
    }

    @Test
    void getBooksWithPriceBetween() {
    }

    @Test
    void getBooksByAuthor() {
    }

    @Test
    void getBooksByTitle() {
    }

    @Test
    void getBooksWithMaxPrice() {
    }

    @Test
    void getBestsellers() {
    }

    @Test
    void getPageofRecommendedBooks() {
    }

    @Test
    void getPageOfSearchResultBooks() {
    }

    @Test
    void getPageOfRecommendedBooks() {
    }

    @Test
    void getPageOfNewBooks() {
    }

    @Test
    void getPageOfBestsellersBooks() {
    }

    @Test
    void getPageOfPopularBooks() {
    }

    @Test
    void getPageOfNewBooksDateFrom() {
    }

    @Test
    void getPageOfNewBooksDateTo() {
    }

    @Test
    void getPageOfNewBooksDateBetween() {
    }

    @Test
    void getPageOfBooksByTag() {
    }

    @Test
    void getPageOfBooksByListGenres() {
    }

    @Test
    void getPageOfBooksByAuthor() {
    }
}