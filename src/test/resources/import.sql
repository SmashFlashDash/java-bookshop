CREATE OR REPLACE FUNCTION keep_book_stat() RETURNS TRIGGER AS E'BEGIN
IF (TG_OP = \'INSERT\') THEN
    IF (NEW.type_id = 1) THEN
        UPDATE book SET stat_bought = stat_bought + 1 WHERE id = NEW.book_id;
    ELSEIF (NEW.type_id = 2) THEN
        UPDATE book SET stat_bought = stat_in_cart + 1 WHERE id = NEW.book_id;
    ELSE
        UPDATE book SET stat_bought = stat_postponed + 1 WHERE id = NEW.book_id;
    END IF;
	RETURN NEW;
ELSIF (TG_OP = \'UPDATE\') THEN
	IF (OLD.type_id = NEW.type_id) THEN
		RETURN NULL;
	END IF;
    IF (OLD.type_id = 1) THEN
        UPDATE book SET stat_bought = stat_bought - 1 WHERE id = OLD.book_id;
    ELSEIF (OLD.type_id = 2) THEN
        UPDATE book SET stat_in_cart = stat_in_cart - 1 WHERE id = OLD.book_id;
    ELSE
        UPDATE book SET stat_postponed = stat_postponed - 1 WHERE id = OLD.book_id;
    END IF;
    IF (NEW.type_id = 1) THEN
        UPDATE book SET stat_bought = stat_bought + 1 WHERE id = OLD.book_id;
    ELSEIF (NEW.type_id = 2) THEN
        UPDATE book SET stat_in_cart = stat_in_cart + 1 WHERE id = OLD.book_id;
    ELSE
        UPDATE book SET stat_postponed = stat_postponed + 1 WHERE id = OLD.book_id;
    END IF;
    RETURN NEW;
END IF;
END;' LANGUAGE plpgsql;

CREATE TRIGGER book2user AFTER INSERT OR UPDATE ON book2user FOR EACH ROW EXECUTE PROCEDURE keep_book_stat();

insert into author (name, photo, slug, little_biography, extend_biography) values ('Jase Petrozzi', 'http://dummyimage.com/219x100.png/ff4444/ffffff', 'jpetrozzi0@instagram.com', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas augue tortor, congue aliquam eros a, consectetur euismod nibh. In ornare aliquet risus quis tincidunt. Proin efficitur purus enim, et efficitur ante ornare ut. Nullam facilisis diam ut bibendum mattis. Aenean vitae quam felis. Integer magna elit, cursus vitae feugiat eu, commodo ac augue. Pellentesque efficitur felis quis ipsum convallis ultricies a eget ipsum. Phasellus tristique orci a porttitor semper. Nulla non tincidunt diam, quis cursus enim.', 'Suspendisse leo ligula, condimentum in commodo vel, malesuada quis augue. Pellentesque gravida lobortis metus sed dignissim. Integer ut nisi volutpat, dictum eros pulvinar, porttitor nulla. Donec congue ante ac eros volutpat rhoncus. Curabitur non elit nulla. Nullam vulputate, mi non maximus suscipit, diam velit ultrices augue, vulputate tempus lectus lorem eget magna. Maecenas euismod laoreet sollicitudin. Donec tristique dolor sem. Maecenas dictum in enim at porta. Proin vel dignissim augue, sit amet tristique elit. Fusce euismod eros feugiat turpis dictum, efficitur maximus erat sagittis. Suspendisse vestibulum magna hendrerit dolor tempor, ut pharetra felis imperdiet.</p><p>Nullam sed elementum felis. Maecenas sollicitudin, metus ut laoreet sagittis, lectus risus feugiat enim, at cursus ante odio at nisl. Mauris elementum augue mauris, nec hendrerit nisi vehicula et. Nunc porttitor justo et risus fermentum sodales.');
insert into genre (name, parent_id, slug) values ('Documentary', null, 'documentary');

-- тестовый пользователь password = test-password
insert into "user" (password, balance, hash, name, reg_time) values ('$2a$10$kekAsZqa8AKAUzGH09rw9u3EfqP6ADmsQPWXYQ0WJoro4O2PzRqn.', 649, '10fb670b93848837ad8f4a7b8fee8f7687d4b827', 'test-user', '2022-08-09 03:21:10');
insert into user_contact (user_id, type, approved, code, code_trails, code_time, contact) values (1, 'EMAIL', 1, 'code', 0, '2023-05-06 14:04:58.428116', 'test-contact');
insert into "user" (password, balance, hash, name, reg_time) values ('$2a$10$XzXiuJ1jQZciIVzGhp6uluDXAjrvsUW/W.wvP7n/XnWH9SnIcK7L6', 649, '0000000000000000000000', 'Test User', '2022-08-09 03:21:10');
insert into user_contact (user_id, type, approved, code, code_trails, code_time, contact) values (2, 'EMAIL', 1, 'code', 0, '2023-05-06 14:04:58.428116', 'test@test.test');
insert into user_contact (user_id, type, approved, code, code_trails, code_time, contact) values (2, 'PHONE', 1, 'code', 0, '2023-05-06 14:04:58.428116', '+7 (222) 222 2222');

-- тестовые книги с тэгами
insert into tag (tag, slug, description) values ('test', 'test', 'test');
insert into tag (tag, slug, description) values ('challenge', 'challenge', 'nunc');
insert into book (is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values
(0, '2022-06-02 05:10:52', 800, 669, 'Number1', 'number1', 'http://dummyimage.com/122x100.png/dddddd/000000', 'justo etiam pretium iaculis justo in hac habitasse platea dictumst etiam faucibus cursus', 1, 1, 1),
(0, '2022-06-02 05:10:52', 800, 669, 'Number2', 'number2', 'http://dummyimage.com/122x100.png/dddddd/000000', 'justo etiam pretium iaculis justo in hac habitasse platea dictumst etiam faucibus cursus', 1, 1, 0),
(0, '2022-06-02 05:10:52', 800, 669, 'Number3', 'number3', 'http://dummyimage.com/122x100.png/dddddd/000000', 'justo etiam pretium iaculis justo in hac habitasse platea dictumst etiam faucibus cursus', 1, 0, 0),
(0, '2022-06-02 05:10:52', 800, 669, 'Number4', 'number4', 'http://dummyimage.com/122x100.png/dddddd/000000', 'justo etiam pretium iaculis justo in hac habitasse platea dictumst etiam faucibus cursus', 0, 0, 0),
(0, '2022-06-02 05:10:52', 800, 669, 'Number5', 'number5', 'http://dummyimage.com/122x100.png/dddddd/000000', 'justo etiam pretium iaculis justo in hac habitasse platea dictumst etiam faucibus cursus', 3, 3, 3),
(0, '2022-06-02 05:10:52', 800, 669, 'Number6', 'number6', 'http://dummyimage.com/122x100.png/dddddd/000000', 'justo etiam pretium iaculis justo in hac habitasse platea dictumst etiam faucibus cursus', 0, 0, 3),
(0, '2022-06-02 05:10:52', 800, 669, 'Number7', 'number7', 'http://dummyimage.com/122x100.png/dddddd/000000', 'justo etiam pretium iaculis justo in hac habitasse platea dictumst etiam faucibus cursus', 0, 3, 0),
(0, '2022-06-02 05:10:52', 800, 669, 'Number8', 'number8', 'http://dummyimage.com/122x100.png/dddddd/000000', 'justo etiam pretium iaculis justo in hac habitasse platea dictumst etiam faucibus cursus', 3, 0, 0),
(0, '2022-06-02 05:10:52', 800, 669, 'Number9', 'number9', 'http://dummyimage.com/122x100.png/dddddd/000000', 'justo etiam pretium iaculis justo in hac habitasse platea dictumst etiam faucibus cursus', 3, 3, 3);

-- тестовые книги поиск по сайту
insert into book (is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values
(0, '2022-06-02 05:10:52', 800, 669, 'Snowriders', 'number9', 'http://dummyimage.com/122x100.png/dddddd/000000', 'justo etiam pretium iaculis justo in hac habitasse platea dictumst etiam faucibus cursus', 3, 3, 3);


insert into book2tag (book_id, tag_id) values (1, 1);
insert into book2tag (book_id, tag_id) values (2, 1);
insert into book2tag (book_id, tag_id) values (3, 2);
insert into book2tag (book_id, tag_id) values (4, 2);
insert into book2tag (book_id, tag_id) values (5, 2);
insert into book2tag (book_id, tag_id) values (6, 2);
insert into book2tag (book_id, tag_id) values (7, 2);
insert into book2tag (book_id, tag_id) values (8, 2);
insert into book2tag (book_id, tag_id) values (9, 2);

insert into book_rating (value, book_id) values (5, 1);
insert into book_rating (value, book_id) values (5, 1);
insert into book_rating (value, book_id) values (1, 1);
insert into book_rating (value, book_id) values (1, 1);
insert into book_rating (value, book_id) values (1, 1);



insert into balance_transaction (book_id, user_id, description, time, value) values (1, 1, 'lobortis convallis', '2022-11-08 19:51:48', 878);
insert into book2author (author_id, book_id) values (1, 1);
insert into book2genre (book_id, genre_id) values (1, 1);
insert into book2user (book_id, time, type_id, user_id) values (1, '2022-12-06 16:45:25', 1, 1);
insert into book2user_type (code, name) values (1, 'Куплена');
insert into book_file (hash, path, type_id) values ('97cdf444fa1e2212c7d27226ed9d0b87c94d01aw', 'EgetNuncDonec.avi', 1);
insert into book_file_type (description, name) values ('eleifend luctus', 'pdf');
insert into document (slug, sort_index, text, title) values ('feugiat', 11, 'Nam ultrices, libero non mattis pulvinar, nulla pede ullamcorper augue, a suscipit nulla elit ac nulla.', 'volutpat');
insert into faq (answer, question, sort_index) values ('vivamus', 'at', 2);
insert into file_download (book_id, count, user_id) values (1, 8, 1);
insert into message (email, name, subject, text, time, user_id) values ('criordan0@alexa.com', 'Costanza Riordan', 'nulla suspendisse', 'Pellentesque ultrices mattis odio.', '2022-06-04 16:29:33', 1);

insert into book_review (book_id, text, time, user_id) values (1, 'In blandit ultrices enim.', '2022-09-06 17:58:42', 1);
insert into book_review_like (review_id, time, user_id, value) values (1, '2022-11-18 03:42:54', 1, 1);
