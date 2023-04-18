-- view для триггера
-- CREATE OR REPLACE VIEW book_stat_view AS
--     SELECT b.id book_id, b2u.user_id, b2u.type_id, b.stat_bought, b.stat_in_cart, b.stat_postponed
--     FROM book b LEFT JOIN book2user b2u ON b2u.book_id = b.id;


-- -- создаем функцию которая возвращает триггер
-- -- в hibernate при ините на парсится $$, это выражение для pgAdmin
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

-- -- Тест работы триггера
-- -- сбросить таблицу
-- UPDATE book2user SET type_id = 1;
-- UPDATE book SET stat_bought = (SELECT COUNT(*) FROM book2user AS b2a WHERE b2a.book_id = book.id AND b2a.type_id = 1);
-- UPDATE book SET stat_in_cart = (SELECT COUNT(*) FROM book2user AS b2a WHERE b2a.book_id = book.id AND b2a.type_id = 2);
-- UPDATE book SET stat_postponed = (SELECT COUNT(*) FROM book2user AS b2a WHERE b2a.book_id = book.id AND b2a.type_id = 3);
-- -- показать таблицы
-- SELECT * FROM public.book2user WHERE book_id in (1) ORDER BY id ASC LIMIT 100;
-- SELECT id, title, stat_bought, stat_in_cart, stat_postponed FROM public.book ORDER BY id ASC LIMIT 20;
-- -- поменяеть на тип 2 и показать таблицу
-- UPDATE book2user SET type_id = 2 WHERE book_id in (1);
-- SELECT id, title, stat_bought, stat_in_cart, stat_postponed FROM public.book ORDER BY id ASC LIMIT 20;
-- -- поменяеть на тип 1 и показать таблицу
-- UPDATE book2user SET type_id = 1 WHERE book_id in (1);
-- SELECT id, title, stat_bought, stat_in_cart, stat_postponed FROM public.book ORDER BY id ASC LIMIT 20;


insert into author (id, name, photo, slug, little_biography, extend_biography) values (1, 'Jase Petrozzi', 'http://dummyimage.com/219x100.png/ff4444/ffffff', 'jpetrozzi0@instagram.com', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas augue tortor, congue aliquam eros a, consectetur euismod nibh. In ornare aliquet risus quis tincidunt. Proin efficitur purus enim, et efficitur ante ornare ut. Nullam facilisis diam ut bibendum mattis. Aenean vitae quam felis. Integer magna elit, cursus vitae feugiat eu, commodo ac augue. Pellentesque efficitur felis quis ipsum convallis ultricies a eget ipsum. Phasellus tristique orci a porttitor semper. Nulla non tincidunt diam, quis cursus enim.', 'Suspendisse leo ligula, condimentum in commodo vel, malesuada quis augue. Pellentesque gravida lobortis metus sed dignissim. Integer ut nisi volutpat, dictum eros pulvinar, porttitor nulla. Donec congue ante ac eros volutpat rhoncus. Curabitur non elit nulla. Nullam vulputate, mi non maximus suscipit, diam velit ultrices augue, vulputate tempus lectus lorem eget magna. Maecenas euismod laoreet sollicitudin. Donec tristique dolor sem. Maecenas dictum in enim at porta. Proin vel dignissim augue, sit amet tristique elit. Fusce euismod eros feugiat turpis dictum, efficitur maximus erat sagittis. Suspendisse vestibulum magna hendrerit dolor tempor, ut pharetra felis imperdiet.</p><p>Nullam sed elementum felis. Maecenas sollicitudin, metus ut laoreet sagittis, lectus risus feugiat enim, at cursus ante odio at nisl. Mauris elementum augue mauris, nec hendrerit nisi vehicula et. Nunc porttitor justo et risus fermentum sodales.');
insert into author (id, name, photo, slug, little_biography, extend_biography) values (2, 'Dena Pourvoieur', 'http://dummyimage.com/221x100.png/cc0000/ffffff', 'dpourvoieur1@1und1.de', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas augue tortor, congue aliquam eros a, consectetur euismod nibh. In ornare aliquet risus quis tincidunt. Proin efficitur purus enim, et efficitur ante ornare ut. Nullam facilisis diam ut bibendum mattis. Aenean vitae quam felis. Integer magna elit, cursus vitae feugiat eu, commodo ac augue. Pellentesque efficitur felis quis ipsum convallis ultricies a eget ipsum. Phasellus tristique orci a porttitor semper. Nulla non tincidunt diam, quis cursus enim.', 'Suspendisse leo ligula, condimentum in commodo vel, malesuada quis augue. Pellentesque gravida lobortis metus sed dignissim. Integer ut nisi volutpat, dictum eros pulvinar, porttitor nulla. Donec congue ante ac eros volutpat rhoncus. Curabitur non elit nulla. Nullam vulputate, mi non maximus suscipit, diam velit ultrices augue, vulputate tempus lectus lorem eget magna. Maecenas euismod laoreet sollicitudin. Donec tristique dolor sem. Maecenas dictum in enim at porta. Proin vel dignissim augue, sit amet tristique elit. Fusce euismod eros feugiat turpis dictum, efficitur maximus erat sagittis. Suspendisse vestibulum magna hendrerit dolor tempor, ut pharetra felis imperdiet.</p><p>Nullam sed elementum felis. Maecenas sollicitudin, metus ut laoreet sagittis, lectus risus feugiat enim, at cursus ante odio at nisl. Mauris elementum augue mauris, nec hendrerit nisi vehicula et. Nunc porttitor justo et risus fermentum sodales.');
insert into author (id, name, photo, slug, little_biography, extend_biography) values (3, 'Gregoor Harwin', 'http://dummyimage.com/224x100.png/dddddd/000000', 'gharwin2@vk.com', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas augue tortor, congue aliquam eros a, consectetur euismod nibh. In ornare aliquet risus quis tincidunt. Proin efficitur purus enim, et efficitur ante ornare ut. Nullam facilisis diam ut bibendum mattis. Aenean vitae quam felis. Integer magna elit, cursus vitae feugiat eu, commodo ac augue. Pellentesque efficitur felis quis ipsum convallis ultricies a eget ipsum. Phasellus tristique orci a porttitor semper. Nulla non tincidunt diam, quis cursus enim.', 'Suspendisse leo ligula, condimentum in commodo vel, malesuada quis augue. Pellentesque gravida lobortis metus sed dignissim. Integer ut nisi volutpat, dictum eros pulvinar, porttitor nulla. Donec congue ante ac eros volutpat rhoncus. Curabitur non elit nulla. Nullam vulputate, mi non maximus suscipit, diam velit ultrices augue, vulputate tempus lectus lorem eget magna. Maecenas euismod laoreet sollicitudin. Donec tristique dolor sem. Maecenas dictum in enim at porta. Proin vel dignissim augue, sit amet tristique elit. Fusce euismod eros feugiat turpis dictum, efficitur maximus erat sagittis. Suspendisse vestibulum magna hendrerit dolor tempor, ut pharetra felis imperdiet.</p><p>Nullam sed elementum felis. Maecenas sollicitudin, metus ut laoreet sagittis, lectus risus feugiat enim, at cursus ante odio at nisl. Mauris elementum augue mauris, nec hendrerit nisi vehicula et. Nunc porttitor justo et risus fermentum sodales.');
insert into author (id, name, photo, slug, little_biography, extend_biography) values (4, 'Luigi Passler', 'http://dummyimage.com/146x100.png/ff4444/ffffff', 'lpassler3@cnn.com', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas augue tortor, congue aliquam eros a, consectetur euismod nibh. In ornare aliquet risus quis tincidunt. Proin efficitur purus enim, et efficitur ante ornare ut. Nullam facilisis diam ut bibendum mattis. Aenean vitae quam felis. Integer magna elit, cursus vitae feugiat eu, commodo ac augue. Pellentesque efficitur felis quis ipsum convallis ultricies a eget ipsum. Phasellus tristique orci a porttitor semper. Nulla non tincidunt diam, quis cursus enim.', 'Suspendisse leo ligula, condimentum in commodo vel, malesuada quis augue. Pellentesque gravida lobortis metus sed dignissim. Integer ut nisi volutpat, dictum eros pulvinar, porttitor nulla. Donec congue ante ac eros volutpat rhoncus. Curabitur non elit nulla. Nullam vulputate, mi non maximus suscipit, diam velit ultrices augue, vulputate tempus lectus lorem eget magna. Maecenas euismod laoreet sollicitudin. Donec tristique dolor sem. Maecenas dictum in enim at porta. Proin vel dignissim augue, sit amet tristique elit. Fusce euismod eros feugiat turpis dictum, efficitur maximus erat sagittis. Suspendisse vestibulum magna hendrerit dolor tempor, ut pharetra felis imperdiet.</p><p>Nullam sed elementum felis. Maecenas sollicitudin, metus ut laoreet sagittis, lectus risus feugiat enim, at cursus ante odio at nisl. Mauris elementum augue mauris, nec hendrerit nisi vehicula et. Nunc porttitor justo et risus fermentum sodales.');
insert into author (id, name, photo, slug, little_biography, extend_biography) values (5, 'Yasmeen Itzkin', 'http://dummyimage.com/110x100.png/dddddd/000000', 'yitzkin4@diigo.com', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas augue tortor, congue aliquam eros a, consectetur euismod nibh. In ornare aliquet risus quis tincidunt. Proin efficitur purus enim, et efficitur ante ornare ut. Nullam facilisis diam ut bibendum mattis. Aenean vitae quam felis. Integer magna elit, cursus vitae feugiat eu, commodo ac augue. Pellentesque efficitur felis quis ipsum convallis ultricies a eget ipsum. Phasellus tristique orci a porttitor semper. Nulla non tincidunt diam, quis cursus enim.', 'Suspendisse leo ligula, condimentum in commodo vel, malesuada quis augue. Pellentesque gravida lobortis metus sed dignissim. Integer ut nisi volutpat, dictum eros pulvinar, porttitor nulla. Donec congue ante ac eros volutpat rhoncus. Curabitur non elit nulla. Nullam vulputate, mi non maximus suscipit, diam velit ultrices augue, vulputate tempus lectus lorem eget magna. Maecenas euismod laoreet sollicitudin. Donec tristique dolor sem. Maecenas dictum in enim at porta. Proin vel dignissim augue, sit amet tristique elit. Fusce euismod eros feugiat turpis dictum, efficitur maximus erat sagittis. Suspendisse vestibulum magna hendrerit dolor tempor, ut pharetra felis imperdiet.</p><p>Nullam sed elementum felis. Maecenas sollicitudin, metus ut laoreet sagittis, lectus risus feugiat enim, at cursus ante odio at nisl. Mauris elementum augue mauris, nec hendrerit nisi vehicula et. Nunc porttitor justo et risus fermentum sodales.');
insert into author (id, name, photo, slug, little_biography, extend_biography) values (6, 'Shalom Hearnden', 'http://dummyimage.com/186x100.png/dddddd/000000', 'shearnden5@sina.com.cn', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas augue tortor, congue aliquam eros a, consectetur euismod nibh. In ornare aliquet risus quis tincidunt. Proin efficitur purus enim, et efficitur ante ornare ut. Nullam facilisis diam ut bibendum mattis. Aenean vitae quam felis. Integer magna elit, cursus vitae feugiat eu, commodo ac augue. Pellentesque efficitur felis quis ipsum convallis ultricies a eget ipsum. Phasellus tristique orci a porttitor semper. Nulla non tincidunt diam, quis cursus enim.', 'Suspendisse leo ligula, condimentum in commodo vel, malesuada quis augue. Pellentesque gravida lobortis metus sed dignissim. Integer ut nisi volutpat, dictum eros pulvinar, porttitor nulla. Donec congue ante ac eros volutpat rhoncus. Curabitur non elit nulla. Nullam vulputate, mi non maximus suscipit, diam velit ultrices augue, vulputate tempus lectus lorem eget magna. Maecenas euismod laoreet sollicitudin. Donec tristique dolor sem. Maecenas dictum in enim at porta. Proin vel dignissim augue, sit amet tristique elit. Fusce euismod eros feugiat turpis dictum, efficitur maximus erat sagittis. Suspendisse vestibulum magna hendrerit dolor tempor, ut pharetra felis imperdiet.</p><p>Nullam sed elementum felis. Maecenas sollicitudin, metus ut laoreet sagittis, lectus risus feugiat enim, at cursus ante odio at nisl. Mauris elementum augue mauris, nec hendrerit nisi vehicula et. Nunc porttitor justo et risus fermentum sodales.');
insert into author (id, name, photo, slug, little_biography, extend_biography) values (7, 'Mayor Bettesworth', 'http://dummyimage.com/204x100.png/dddddd/000000', 'mbettesworth6@w3.org', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas augue tortor, congue aliquam eros a, consectetur euismod nibh. In ornare aliquet risus quis tincidunt. Proin efficitur purus enim, et efficitur ante ornare ut. Nullam facilisis diam ut bibendum mattis. Aenean vitae quam felis. Integer magna elit, cursus vitae feugiat eu, commodo ac augue. Pellentesque efficitur felis quis ipsum convallis ultricies a eget ipsum. Phasellus tristique orci a porttitor semper. Nulla non tincidunt diam, quis cursus enim.', 'Suspendisse leo ligula, condimentum in commodo vel, malesuada quis augue. Pellentesque gravida lobortis metus sed dignissim. Integer ut nisi volutpat, dictum eros pulvinar, porttitor nulla. Donec congue ante ac eros volutpat rhoncus. Curabitur non elit nulla. Nullam vulputate, mi non maximus suscipit, diam velit ultrices augue, vulputate tempus lectus lorem eget magna. Maecenas euismod laoreet sollicitudin. Donec tristique dolor sem. Maecenas dictum in enim at porta. Proin vel dignissim augue, sit amet tristique elit. Fusce euismod eros feugiat turpis dictum, efficitur maximus erat sagittis. Suspendisse vestibulum magna hendrerit dolor tempor, ut pharetra felis imperdiet.</p><p>Nullam sed elementum felis. Maecenas sollicitudin, metus ut laoreet sagittis, lectus risus feugiat enim, at cursus ante odio at nisl. Mauris elementum augue mauris, nec hendrerit nisi vehicula et. Nunc porttitor justo et risus fermentum sodales.');
insert into author (id, name, photo, slug, little_biography, extend_biography) values (8, 'Nessi Dingwall', 'http://dummyimage.com/108x100.png/dddddd/000000', 'ndingwall7@netscape.com', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas augue tortor, congue aliquam eros a, consectetur euismod nibh. In ornare aliquet risus quis tincidunt. Proin efficitur purus enim, et efficitur ante ornare ut. Nullam facilisis diam ut bibendum mattis. Aenean vitae quam felis. Integer magna elit, cursus vitae feugiat eu, commodo ac augue. Pellentesque efficitur felis quis ipsum convallis ultricies a eget ipsum. Phasellus tristique orci a porttitor semper. Nulla non tincidunt diam, quis cursus enim.', 'Suspendisse leo ligula, condimentum in commodo vel, malesuada quis augue. Pellentesque gravida lobortis metus sed dignissim. Integer ut nisi volutpat, dictum eros pulvinar, porttitor nulla. Donec congue ante ac eros volutpat rhoncus. Curabitur non elit nulla. Nullam vulputate, mi non maximus suscipit, diam velit ultrices augue, vulputate tempus lectus lorem eget magna. Maecenas euismod laoreet sollicitudin. Donec tristique dolor sem. Maecenas dictum in enim at porta. Proin vel dignissim augue, sit amet tristique elit. Fusce euismod eros feugiat turpis dictum, efficitur maximus erat sagittis. Suspendisse vestibulum magna hendrerit dolor tempor, ut pharetra felis imperdiet.</p><p>Nullam sed elementum felis. Maecenas sollicitudin, metus ut laoreet sagittis, lectus risus feugiat enim, at cursus ante odio at nisl. Mauris elementum augue mauris, nec hendrerit nisi vehicula et. Nunc porttitor justo et risus fermentum sodales.');
insert into author (id, name, photo, slug, little_biography, extend_biography) values (9, 'Maye Foggarty', 'http://dummyimage.com/144x100.png/ff4444/ffffff', 'mfoggarty8@indiegogo.com', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas augue tortor, congue aliquam eros a, consectetur euismod nibh. In ornare aliquet risus quis tincidunt. Proin efficitur purus enim, et efficitur ante ornare ut. Nullam facilisis diam ut bibendum mattis. Aenean vitae quam felis. Integer magna elit, cursus vitae feugiat eu, commodo ac augue. Pellentesque efficitur felis quis ipsum convallis ultricies a eget ipsum. Phasellus tristique orci a porttitor semper. Nulla non tincidunt diam, quis cursus enim.', 'Suspendisse leo ligula, condimentum in commodo vel, malesuada quis augue. Pellentesque gravida lobortis metus sed dignissim. Integer ut nisi volutpat, dictum eros pulvinar, porttitor nulla. Donec congue ante ac eros volutpat rhoncus. Curabitur non elit nulla. Nullam vulputate, mi non maximus suscipit, diam velit ultrices augue, vulputate tempus lectus lorem eget magna. Maecenas euismod laoreet sollicitudin. Donec tristique dolor sem. Maecenas dictum in enim at porta. Proin vel dignissim augue, sit amet tristique elit. Fusce euismod eros feugiat turpis dictum, efficitur maximus erat sagittis. Suspendisse vestibulum magna hendrerit dolor tempor, ut pharetra felis imperdiet.</p><p>Nullam sed elementum felis. Maecenas sollicitudin, metus ut laoreet sagittis, lectus risus feugiat enim, at cursus ante odio at nisl. Mauris elementum augue mauris, nec hendrerit nisi vehicula et. Nunc porttitor justo et risus fermentum sodales.');
insert into author (id, name, photo, slug, little_biography, extend_biography) values (10, 'Elsinore Flack', 'http://dummyimage.com/233x100.png/5fa2dd/ffffff', 'eflack9@arstechnica.com', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas augue tortor, congue aliquam eros a, consectetur euismod nibh. In ornare aliquet risus quis tincidunt. Proin efficitur purus enim, et efficitur ante ornare ut. Nullam facilisis diam ut bibendum mattis. Aenean vitae quam felis. Integer magna elit, cursus vitae feugiat eu, commodo ac augue. Pellentesque efficitur felis quis ipsum convallis ultricies a eget ipsum. Phasellus tristique orci a porttitor semper. Nulla non tincidunt diam, quis cursus enim.', 'Suspendisse leo ligula, condimentum in commodo vel, malesuada quis augue. Pellentesque gravida lobortis metus sed dignissim. Integer ut nisi volutpat, dictum eros pulvinar, porttitor nulla. Donec congue ante ac eros volutpat rhoncus. Curabitur non elit nulla. Nullam vulputate, mi non maximus suscipit, diam velit ultrices augue, vulputate tempus lectus lorem eget magna. Maecenas euismod laoreet sollicitudin. Donec tristique dolor sem. Maecenas dictum in enim at porta. Proin vel dignissim augue, sit amet tristique elit. Fusce euismod eros feugiat turpis dictum, efficitur maximus erat sagittis. Suspendisse vestibulum magna hendrerit dolor tempor, ut pharetra felis imperdiet.</p><p>Nullam sed elementum felis. Maecenas sollicitudin, metus ut laoreet sagittis, lectus risus feugiat enim, at cursus ante odio at nisl. Mauris elementum augue mauris, nec hendrerit nisi vehicula et. Nunc porttitor justo et risus fermentum sodales.');
insert into author (id, name, photo, slug, little_biography, extend_biography) values (11, 'Derk Semken', 'http://dummyimage.com/109x100.png/5fa2dd/ffffff', 'dsemkena@ocn.ne.jp', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas augue tortor, congue aliquam eros a, consectetur euismod nibh. In ornare aliquet risus quis tincidunt. Proin efficitur purus enim, et efficitur ante ornare ut. Nullam facilisis diam ut bibendum mattis. Aenean vitae quam felis. Integer magna elit, cursus vitae feugiat eu, commodo ac augue. Pellentesque efficitur felis quis ipsum convallis ultricies a eget ipsum. Phasellus tristique orci a porttitor semper. Nulla non tincidunt diam, quis cursus enim.', null);
insert into author (id, name, photo, slug, little_biography, extend_biography) values (12, 'Julianne Reeds', 'http://dummyimage.com/120x100.png/5fa2dd/ffffff', 'jreedsb@storify.com', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas augue tortor, congue aliquam eros a, consectetur euismod nibh. In ornare aliquet risus quis tincidunt. Proin efficitur purus enim, et efficitur ante ornare ut. Nullam facilisis diam ut bibendum mattis. Aenean vitae quam felis. Integer magna elit, cursus vitae feugiat eu, commodo ac augue. Pellentesque efficitur felis quis ipsum convallis ultricies a eget ipsum. Phasellus tristique orci a porttitor semper. Nulla non tincidunt diam, quis cursus enim.', null);
insert into author (id, name, photo, slug, little_biography, extend_biography) values (13, 'Miller Lavery', 'http://dummyimage.com/183x100.png/dddddd/000000', 'mlaveryc@com.com', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas augue tortor, congue aliquam eros a, consectetur euismod nibh. In ornare aliquet risus quis tincidunt. Proin efficitur purus enim, et efficitur ante ornare ut. Nullam facilisis diam ut bibendum mattis. Aenean vitae quam felis. Integer magna elit, cursus vitae feugiat eu, commodo ac augue. Pellentesque efficitur felis quis ipsum convallis ultricies a eget ipsum. Phasellus tristique orci a porttitor semper. Nulla non tincidunt diam, quis cursus enim.', null);
insert into author (id, name, photo, slug, little_biography, extend_biography) values (14, 'Tarrance Longstreet', 'http://dummyimage.com/210x100.png/5fa2dd/ffffff', 'tlongstreetd@angelfire.com', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas augue tortor, congue aliquam eros a, consectetur euismod nibh. In ornare aliquet risus quis tincidunt. Proin efficitur purus enim, et efficitur ante ornare ut. Nullam facilisis diam ut bibendum mattis. Aenean vitae quam felis. Integer magna elit, cursus vitae feugiat eu, commodo ac augue. Pellentesque efficitur felis quis ipsum convallis ultricies a eget ipsum. Phasellus tristique orci a porttitor semper. Nulla non tincidunt diam, quis cursus enim.', null);
insert into author (id, name, photo, slug, little_biography, extend_biography) values (15, 'Colas Barnwell', 'http://dummyimage.com/231x100.png/cc0000/ffffff', 'cbarnwelle@nba.com', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas augue tortor, congue aliquam eros a, consectetur euismod nibh. In ornare aliquet risus quis tincidunt. Proin efficitur purus enim, et efficitur ante ornare ut. Nullam facilisis diam ut bibendum mattis. Aenean vitae quam felis. Integer magna elit, cursus vitae feugiat eu, commodo ac augue. Pellentesque efficitur felis quis ipsum convallis ultricies a eget ipsum. Phasellus tristique orci a porttitor semper. Nulla non tincidunt diam, quis cursus enim.', null);
insert into author (id, name, photo, slug, little_biography, extend_biography) values (16, 'Talbert Gall', 'http://dummyimage.com/240x100.png/ff4444/ffffff', 'tgallf@delicious.com', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas augue tortor, congue aliquam eros a, consectetur euismod nibh. In ornare aliquet risus quis tincidunt. Proin efficitur purus enim, et efficitur ante ornare ut. Nullam facilisis diam ut bibendum mattis. Aenean vitae quam felis. Integer magna elit, cursus vitae feugiat eu, commodo ac augue. Pellentesque efficitur felis quis ipsum convallis ultricies a eget ipsum. Phasellus tristique orci a porttitor semper. Nulla non tincidunt diam, quis cursus enim.', null);
insert into author (id, name, photo, slug, little_biography, extend_biography) values (17, 'Saxe Giacubbo', 'http://dummyimage.com/119x100.png/cc0000/ffffff', 'sgiacubbog@hp.com', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas augue tortor, congue aliquam eros a, consectetur euismod nibh. In ornare aliquet risus quis tincidunt. Proin efficitur purus enim, et efficitur ante ornare ut. Nullam facilisis diam ut bibendum mattis. Aenean vitae quam felis. Integer magna elit, cursus vitae feugiat eu, commodo ac augue. Pellentesque efficitur felis quis ipsum convallis ultricies a eget ipsum. Phasellus tristique orci a porttitor semper. Nulla non tincidunt diam, quis cursus enim.', null);
insert into author (id, name, photo, slug, little_biography, extend_biography) values (18, 'Katrina Forkan', 'http://dummyimage.com/157x100.png/ff4444/ffffff', 'kforkanh@cloudflare.com', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas augue tortor, congue aliquam eros a, consectetur euismod nibh. In ornare aliquet risus quis tincidunt. Proin efficitur purus enim, et efficitur ante ornare ut. Nullam facilisis diam ut bibendum mattis. Aenean vitae quam felis. Integer magna elit, cursus vitae feugiat eu, commodo ac augue. Pellentesque efficitur felis quis ipsum convallis ultricies a eget ipsum. Phasellus tristique orci a porttitor semper. Nulla non tincidunt diam, quis cursus enim.', null);
insert into author (id, name, photo, slug, little_biography, extend_biography) values (19, 'Melita Lopez', 'http://dummyimage.com/239x100.png/cc0000/ffffff', 'mlopezi@prnewswire.com', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas augue tortor, congue aliquam eros a, consectetur euismod nibh. In ornare aliquet risus quis tincidunt. Proin efficitur purus enim, et efficitur ante ornare ut. Nullam facilisis diam ut bibendum mattis. Aenean vitae quam felis. Integer magna elit, cursus vitae feugiat eu, commodo ac augue. Pellentesque efficitur felis quis ipsum convallis ultricies a eget ipsum. Phasellus tristique orci a porttitor semper. Nulla non tincidunt diam, quis cursus enim.', null);
insert into author (id, name, photo, slug, little_biography, extend_biography) values (20, 'Mannie Kochel', 'http://dummyimage.com/178x100.png/ff4444/ffffff', 'mkochelj@booking.com', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas augue tortor, congue aliquam eros a, consectetur euismod nibh. In ornare aliquet risus quis tincidunt. Proin efficitur purus enim, et efficitur ante ornare ut. Nullam facilisis diam ut bibendum mattis. Aenean vitae quam felis. Integer magna elit, cursus vitae feugiat eu, commodo ac augue. Pellentesque efficitur felis quis ipsum convallis ultricies a eget ipsum. Phasellus tristique orci a porttitor semper. Nulla non tincidunt diam, quis cursus enim.', null);
insert into author (id, name, photo, slug, little_biography, extend_biography) values (21, 'Vite Bellon', 'http://dummyimage.com/159x100.png/cc0000/ffffff', 'vbellonk@wunderground.com', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas augue tortor, congue aliquam eros a, consectetur euismod nibh. In ornare aliquet risus quis tincidunt. Proin efficitur purus enim, et efficitur ante ornare ut. Nullam facilisis diam ut bibendum mattis. Aenean vitae quam felis. Integer magna elit, cursus vitae feugiat eu, commodo ac augue. Pellentesque efficitur felis quis ipsum convallis ultricies a eget ipsum. Phasellus tristique orci a porttitor semper. Nulla non tincidunt diam, quis cursus enim.', null);


insert into genre (id, name, parent_id, slug) values (1, 'Documentary', null, 'documentary');
insert into genre (id, name, parent_id, slug) values (2, 'Sci-Fi', null, 'sci_fi');
insert into genre (id, name, parent_id, slug) values (3, 'Musical', null, 'musical');
insert into genre (id, name, parent_id, slug) values (4, 'Crime', 1, 'crime');
insert into genre (id, name, parent_id, slug) values (5, 'Comedy|Romance', 1, 'comedy_romance');
insert into genre (id, name, parent_id, slug) values (6, 'Drama|Musical', 1, 'drama_musical');
insert into genre (id, name, parent_id, slug) values (7, 'Comedy', 2, 'comedy');
insert into genre (id, name, parent_id, slug) values (8, 'Crime|Drama|Thriller', 2, 'crime_drama_thriller');
insert into genre (id, name, parent_id, slug) values (9, 'Action|Adventure', 2, 'action_adventure');
insert into genre (id, name, parent_id, slug) values (10, 'Action|Adventure|Comedy', 2, 'action_adventure_comedy');
insert into genre (id, name, parent_id, slug) values (11, 'Action|Comedy', 3, 'action_comedy');
insert into genre (id, name, parent_id, slug) values (12, 'Action|Sci-Fi', 3, 'action_sci_fi');
insert into genre (id, name, parent_id, slug) values (13, 'Action', 3, 'action');
insert into genre (id, name, parent_id, slug) values (14, 'Crime|Thriller', 3, 'crime_thriller');
insert into genre (id, name, parent_id, slug) values (15, 'Thriller', 3, 'thriller');
insert into genre (id, name, parent_id, slug) values (16, 'Drama|Action', 3, 'drama_action');
insert into genre (id, name, parent_id, slug) values (17, 'Action|Comedy|Musical', 3, 'action_comedy_musical');
insert into genre (id, name, parent_id, slug) values (18, 'Comedy|Fomedy', 3, 'comedy_fomedy');
insert into genre (id, name, parent_id, slug) values (19, 'Action|Romance', 15, 'action_romance');
insert into genre (id, name, parent_id, slug) values (20, 'Action|Crime|Thriller', 15, 'action_crime_thriller');
insert into genre (id, name, parent_id, slug) values (21, 'Western', 15, 'western');


insert into "user" (id, balance, hash, name, reg_time) values (1, 649, '10fb670b93848837ad8f4a7b8fee8f7687d4b827', 'Edin Giraudot', '2022-08-09 03:21:10');
insert into "user" (id, balance, hash, name, reg_time) values (2, 954, '5dc5fa6ddfce0ba827becda1e58485f0e5fbb993', 'Eustace Seavers', '2022-10-07 00:59:24');
insert into "user" (id, balance, hash, name, reg_time) values (3, 936, '96078bf1cb5874040d3c780f21f1cd21cced4c38', 'Rebekkah Heater', '2022-03-29 18:00:57');
insert into "user" (id, balance, hash, name, reg_time) values (4, 609, '0b430e96037a222ce6419bced9d712c5ce90a435', 'Shandeigh Fogel', '2022-09-10 22:23:28');
insert into "user" (id, balance, hash, name, reg_time) values (5, 848, 'ab686dd8bbb0a2be276cb2053533c29e5780b109', 'Osbourne Finey', '2022-12-12 21:51:02');
insert into "user" (id, balance, hash, name, reg_time) values (6, 795, 'b6061a27bbfef41338c14bf272a13c9137ec5211', 'Joleen Barke', '2022-09-11 15:28:56');
insert into "user" (id, balance, hash, name, reg_time) values (7, 968, '50f4ca27f38c136d60b8150c599405df024024af', 'Catina Ulster', '2022-10-10 15:22:09');
insert into "user" (id, balance, hash, name, reg_time) values (8, 615, '072658ee4c154388e924c2ef5146ff702882d752', 'Caspar Ainger', '2022-07-30 13:54:42');
insert into "user" (id, balance, hash, name, reg_time) values (9, 580, 'e9f269e057798bd4bb9e89b04e09dd590f1cff09', 'Rycca Dovey', '2022-02-14 10:45:22');
insert into "user" (id, balance, hash, name, reg_time) values (10, 839, 'c2672ff834a441c513ff63cc2ba2fdab739921b2', 'Rhianna Jaumet', '2022-06-06 14:12:43');
insert into "user" (id, balance, hash, name, reg_time) values (11, 852, 'f54241094ebee96df9376b7e4d62c6b3214e9cf5', 'Andeee Eim', '2022-09-28 01:28:31');
insert into "user" (id, balance, hash, name, reg_time) values (12, 751, '5246b34a2a7a50a8b2177b091c5d4f5b43471589', 'Tonie Mocker', '2022-06-23 07:24:12');
insert into "user" (id, balance, hash, name, reg_time) values (13, 783, 'c36f8b3bbca31289c6d9ef8d8d81a0e9ea5c2e75', 'Vasili Bridges', '2022-11-30 16:57:49');
insert into "user" (id, balance, hash, name, reg_time) values (14, 589, '4623fdee97e4559a02ee55e94034a10a198c09ca', 'Val Levings', '2022-09-22 16:59:33');
insert into "user" (id, balance, hash, name, reg_time) values (15, 946, '0e1d7cd465353e0672a974003032ccba3b0defe2', 'Marylynne Hendrickx', '2022-11-20 08:37:04');
insert into "user" (id, balance, hash, name, reg_time) values (16, 976, '711de7362d8c60ee278c64e8ea4617a0aa9208a8', 'Chic Pethybridge', '2022-04-29 19:44:49');
insert into "user" (id, balance, hash, name, reg_time) values (17, 645, '3335d6fbb2d7fb8f340d9860e58c52f6afcb80e1', 'Osborne Minister', '2022-05-08 13:47:32');
insert into "user" (id, balance, hash, name, reg_time) values (18, 798, 'db275a1daf1d76a428b8562f2057067c5f9d1748', 'Noak Pilmore', '2022-03-17 23:55:56');
insert into "user" (id, balance, hash, name, reg_time) values (19, 689, 'aee5ae51dcd798c863f31e85d1f899b365e5e799', 'Mord Hymer', '2022-04-25 00:17:04');
insert into "user" (id, balance, hash, name, reg_time) values (20, 971, 'b21b13ced3cf39ba1031097cf199e88fc4d0b862', 'Rockwell Fendley', '2022-09-08 11:18:06');
insert into "user" (id, balance, hash, name, reg_time) values (21, 746, 'eb9daae84fa2e97c3a99ab7948737aa8b88cda91', 'Merrily Yurkov', '2022-02-19 20:57:18');
insert into "user" (id, balance, hash, name, reg_time) values (22, 583, '012c00baa4b476e6f95c49d6c200cf98878002fb', 'Guilbert McGreal', '2022-08-31 02:19:19');
insert into "user" (id, balance, hash, name, reg_time) values (23, 952, 'afcabd87619e4e231403074d6ba2bbaaf55c5011', 'Frants Astling', '2022-05-06 13:49:05');
insert into "user" (id, balance, hash, name, reg_time) values (24, 617, 'cae66efc44b6b99751c7f07d3e177f3d363467e4', 'Trudy Muress', '2022-08-10 06:06:33');
insert into "user" (id, balance, hash, name, reg_time) values (25, 720, '6919dd79acbd0d10171a92b38d77e7c03e028011', 'Amandy Boyen', '2022-01-17 17:55:20');
insert into "user" (id, balance, hash, name, reg_time) values (26, 804, '90beeb1d5a44df7bd363508586b81635ef1a138d', 'Elias Sartin', '2022-11-19 04:49:19');
insert into "user" (id, balance, hash, name, reg_time) values (27, 696, 'c577d2fae0310b8ecab996c43b8362e95d2f4a36', 'Kelila Kefford', '2022-12-02 07:43:12');
insert into "user" (id, balance, hash, name, reg_time) values (28, 523, '012d80a2f70776475751d2db1ee45ab2e47a4bb9', 'Paddie Handy', '2021-12-29 19:00:39');
insert into "user" (id, balance, hash, name, reg_time) values (29, 624, '94d2f7f0e977ae9afa40332a135d1cc701499162', 'Gretel Marron', '2022-11-15 11:20:19');
insert into "user" (id, balance, hash, name, reg_time) values (30, 630, 'f39df63b0a19a943791b0f81034971679ee63282', 'Waylen Broadhead', '2022-06-07 23:28:25');
insert into "user" (id, balance, hash, name, reg_time) values (31, 536, '2f6a80a54a6bd3b5e28341c3cacabd9044ac131d', 'Lindi Arnecke', '2022-11-04 05:02:27');
insert into "user" (id, balance, hash, name, reg_time) values (32, 864, 'f3304a84864d6a3aa5bacf09b72a773b5093672f', 'Muriel Fearby', '2022-01-25 10:21:00');
insert into "user" (id, balance, hash, name, reg_time) values (33, 901, 'bc3ef5c01e359a549008b4cdc718897866530b94', 'Brody Baylay', '2022-07-14 08:47:05');
insert into "user" (id, balance, hash, name, reg_time) values (34, 795, '0952a0f47ba93037b8e90d0de6529ef23296cfb1', 'Joy Davys', '2022-04-26 20:48:51');
insert into "user" (id, balance, hash, name, reg_time) values (35, 537, 'cc901b7a72829df0b472bbd85196674eefc46cb9', 'Berry Sperski', '2022-01-11 18:30:48');
insert into "user" (id, balance, hash, name, reg_time) values (36, 859, '57ea017dbdcfce81ae29198982147319c762fdbf', 'Konstanze Pascow', '2022-09-05 02:01:33');
insert into "user" (id, balance, hash, name, reg_time) values (37, 732, '094fdf6a2fba294dc8fd119a65149385927c7a51', 'Wallis Tearney', '2022-09-14 05:53:06');
insert into "user" (id, balance, hash, name, reg_time) values (38, 698, 'd74321674beedd25224ece5022adbbe9cf002158', 'Arleyne Behling', '2022-07-10 15:37:36');
insert into "user" (id, balance, hash, name, reg_time) values (39, 521, '24908dc2c87912b1cadf9e9ba23e206c11a2dbf7', 'Des Rickaert', '2022-06-19 02:45:48');
insert into "user" (id, balance, hash, name, reg_time) values (40, 945, '98dc0e314198eb06ff35ad84d8ad7031d41093b3', 'Diane Drane', '2022-07-09 22:34:23');
insert into "user" (id, balance, hash, name, reg_time) values (41, 925, 'd56f11e250be48e28daf9a58e18f2f754e5dce3f', 'Crin Tison', '2022-04-25 02:17:38');
insert into "user" (id, balance, hash, name, reg_time) values (42, 976, 'b34170dffc0c737acf1c24c8b062fe06aec41c65', 'Van Hoyles', '2022-02-09 04:45:09');
insert into "user" (id, balance, hash, name, reg_time) values (43, 910, 'b2c2de8f4c7cde1cc893c66149b86bd9d083b60d', 'Quinn Darwent', '2022-01-31 15:25:22');
insert into "user" (id, balance, hash, name, reg_time) values (44, 953, 'e4a05b52489c63886d407bb6df34d1b8afd0e570', 'Leelah Parnall', '2022-08-31 15:52:55');
insert into "user" (id, balance, hash, name, reg_time) values (45, 951, 'ae423a94f8260dad8f322818ad0d2b072e97734d', 'Page Derkes', '2022-11-19 16:07:46');
insert into "user" (id, balance, hash, name, reg_time) values (46, 975, '453ff562f438b6bbec854bdc173abfe5dcc4deea', 'Marthena Whithalgh', '2022-05-17 11:25:48');
insert into "user" (id, balance, hash, name, reg_time) values (47, 919, '948c4d11845d7e15739a693a24151c1690b410db', 'Adan Aberdalgy', '2022-02-10 19:45:58');
insert into "user" (id, balance, hash, name, reg_time) values (48, 584, '3da203a29a2ed3720f48540393d1eac1b0912b71', 'Courtnay McGrill', '2022-03-10 17:58:31');
insert into "user" (id, balance, hash, name, reg_time) values (49, 642, '55db8b737245383288552a3c484107741f4c6b64', 'Cull Ferns', '2022-02-24 09:50:00');
insert into "user" (id, balance, hash, name, reg_time) values (50, 994, 'ddf52fdc3182df7f6d74b056b673a5b2294030c6', 'Candra Grevile', '2022-05-04 20:30:11');
insert into "user" (id, balance, hash, name, reg_time) values (51, 569, '82eaefa41ab4d58353b93e54c6bf8a11eabaf82b', 'Humfrid Pestell', '2022-11-11 22:39:21');
insert into "user" (id, balance, hash, name, reg_time) values (52, 683, '3c62bc3a3b0952f872c5ac1894e3d0e1638f9117', 'Grantley Mabbe', '2022-07-06 01:28:49');
insert into "user" (id, balance, hash, name, reg_time) values (53, 619, 'a529d48c5ac844d681324466b576a50159c4b75d', 'Hilarius Pummell', '2022-10-25 05:02:12');
insert into "user" (id, balance, hash, name, reg_time) values (54, 712, '562cd48cebe1efa3b1376b7558ece87b3c9f2421', 'Merrill Salliss', '2022-12-21 17:50:39');
insert into "user" (id, balance, hash, name, reg_time) values (55, 649, '92192f245d8bedba5bf6f98bb292e09b351538c0', 'Pamelina Errol', '2022-07-20 15:06:33');
insert into "user" (id, balance, hash, name, reg_time) values (56, 821, '69d70c3251d31dfdb9cc982160760fceed086c7a', 'Chester Brinkler', '2022-04-28 17:19:22');
insert into "user" (id, balance, hash, name, reg_time) values (57, 789, 'a8fc224f210c05f93a75a182371ae2879411b762', 'Roddy Mattam', '2022-09-27 02:33:22');
insert into "user" (id, balance, hash, name, reg_time) values (58, 760, '5323698d8da7b374cf0fa53c40d7a4b5ab603e68', 'Jillana Mancell', '2022-12-09 02:11:29');
insert into "user" (id, balance, hash, name, reg_time) values (59, 951, 'c4b284b4f921885e562142b4a4b34bf51601e585', 'Rosalinde Vicent', '2022-01-19 01:41:43');
insert into "user" (id, balance, hash, name, reg_time) values (60, 858, '1e3a0f1b779e8a7cd444a406720b9789d9153c39', 'Gerhardine Warnes', '2022-05-24 01:44:48');
insert into "user" (id, balance, hash, name, reg_time) values (61, 752, '33435657053f4e3691b1549b1ddcb9324a1153d4', 'Elinor Beirne', '2022-10-27 07:42:30');
insert into "user" (id, balance, hash, name, reg_time) values (62, 892, '3ccaec7155c460fd748ca72370b10f42f8f6ceb2', 'Bret McLafferty', '2022-10-19 23:34:38');
insert into "user" (id, balance, hash, name, reg_time) values (63, 943, 'bd49334abdf5010b7d65c8af8a655a84b0c9e6f8', 'Vicky Figure', '2022-03-12 18:55:34');
insert into "user" (id, balance, hash, name, reg_time) values (64, 644, '75706696203e1c5938d1408de52bda6fa7bbadc2', 'Kristopher Sole', '2022-02-13 09:32:36');
insert into "user" (id, balance, hash, name, reg_time) values (65, 571, 'a2d6af017a7f801cc936c600201f673095218631', 'Royall Valencia', '2022-10-09 11:52:21');
insert into "user" (id, balance, hash, name, reg_time) values (66, 786, '5e949b11d0ebb3b0f6248fbac69ff3cee5f8f2f7', 'Annabal Slatten', '2021-12-24 23:16:18');
insert into "user" (id, balance, hash, name, reg_time) values (67, 985, '385a2dfb5f304ab5a34fba734a0b4d68667077d6', 'Jasmina Raymond', '2022-09-09 09:55:22');
insert into "user" (id, balance, hash, name, reg_time) values (68, 700, 'fca9fa6b9a3129debfafcf0cf67742a6e0e526e1', 'Harmonia Heak', '2022-10-31 17:19:11');
insert into "user" (id, balance, hash, name, reg_time) values (69, 928, '7a6ecb40d0ab04d4f2e198bfa25d0456e05d8ab2', 'Steve Ell', '2022-07-10 12:48:20');
insert into "user" (id, balance, hash, name, reg_time) values (70, 619, '9203db6364142810c288c483f0a055e05b4a53ad', 'Abbe Nabbs', '2022-05-15 17:32:32');
insert into "user" (id, balance, hash, name, reg_time) values (71, 791, '0dccbbb3412bed4086142941084f174aae707a43', 'Timofei Merrikin', '2022-01-21 19:26:32');
insert into "user" (id, balance, hash, name, reg_time) values (72, 888, 'dd7dd19b6f6cc865c9991edd0dd5908aac870608', 'Baron Elderton', '2022-07-18 23:57:53');
insert into "user" (id, balance, hash, name, reg_time) values (73, 594, '6dcc1599cbd2bf139f6a9ece5d1e7d7c2c33041d', 'Constantine Janjusevic', '2022-06-26 11:30:15');
insert into "user" (id, balance, hash, name, reg_time) values (74, 728, '94f3e820e7f65f2bc458c40425e1f74ea7c98343', 'Katrinka Ruegg', '2022-02-15 16:24:58');
insert into "user" (id, balance, hash, name, reg_time) values (75, 694, 'f836d95127c3f59b6e7902a91c428785796657c0', 'Diena Durdy', '2022-01-18 03:47:42');
insert into "user" (id, balance, hash, name, reg_time) values (76, 732, 'db8f4f90cd6b2e2b8c8f9d3552bb544d4bf7290c', 'Bartie Skains', '2022-03-26 12:53:54');
insert into "user" (id, balance, hash, name, reg_time) values (77, 602, '8fa48f3b9abd2a06fcd032eb42caac042b581552', 'Nolly Rosini', '2022-02-28 16:26:02');
insert into "user" (id, balance, hash, name, reg_time) values (78, 807, 'd7aa26333737e7e9c3ba2a999b14be54896adb9c', 'Doll Benaine', '2022-08-18 15:43:33');
insert into "user" (id, balance, hash, name, reg_time) values (79, 550, '197941ddaae4318a34565eea73cf27c4b59a9477', 'Ag de la Tremoille', '2022-11-19 08:07:27');
insert into "user" (id, balance, hash, name, reg_time) values (80, 626, '73298a6c2830386d2a2482f8149468b417cac5c6', 'Gabbie Patek', '2022-10-29 22:39:59');
insert into "user" (id, balance, hash, name, reg_time) values (81, 819, '64f5fd3a7e3e7f8b13a66ba4c5a26c2f75a87d0f', 'Moses Rocco', '2022-11-29 10:11:21');
insert into "user" (id, balance, hash, name, reg_time) values (82, 802, '43120c3c8aad918b7bc1393bdcbb1da501cdac94', 'Calla Loughrey', '2022-06-16 21:41:30');
insert into "user" (id, balance, hash, name, reg_time) values (83, 678, '26c409adca8e17b8482535420cc718ff29f1878f', 'Aura Haselgrove', '2022-08-15 00:00:30');
insert into "user" (id, balance, hash, name, reg_time) values (84, 686, '634ddc7303efc0aa839cbf3600670a84b401f45c', 'Dana Monelli', '2022-12-04 01:07:45');
insert into "user" (id, balance, hash, name, reg_time) values (85, 733, '22ae740acf49facba24d583dc7e522356addb715', 'Kimball Kearney', '2021-12-22 01:34:26');
insert into "user" (id, balance, hash, name, reg_time) values (86, 782, 'e122d82037d5dfb9b02290667d72e446e58f00d6', 'Reilly Moen', '2022-10-18 19:22:43');
insert into "user" (id, balance, hash, name, reg_time) values (87, 749, 'fb0d567df790fb26c4063c2cf335d73942bc4805', 'Tynan McElane', '2022-01-24 21:08:47');
insert into "user" (id, balance, hash, name, reg_time) values (88, 823, '0b853d083659e43f7a9aa141130438aa4bd74809', 'Solly Haire', '2022-03-22 01:13:53');
insert into "user" (id, balance, hash, name, reg_time) values (89, 516, '8f56b4f63d64d92e4754c0cb45e71a3dd1a270c3', 'Alisa Gouldie', '2022-08-09 00:42:51');
insert into "user" (id, balance, hash, name, reg_time) values (90, 848, 'f67de19f91e8251b6e0cc964947c1874b3a6d3c0', 'Ottilie Wagerfield', '2022-06-22 02:59:27');
insert into "user" (id, balance, hash, name, reg_time) values (91, 758, '98a5e43e14f1eba15a099a92a378040ad2266eb8', 'Katey Speirs', '2022-05-01 11:35:05');
insert into "user" (id, balance, hash, name, reg_time) values (92, 652, '929461c5be208e6c5b6adf0948b2aa45db3800fd', 'Bil Tunesi', '2022-02-24 01:57:20');
insert into "user" (id, balance, hash, name, reg_time) values (93, 980, '5315f029a89353d6fad4bccf59326a69ba6a0668', 'Caresa Coulson', '2022-05-16 06:31:32');
insert into "user" (id, balance, hash, name, reg_time) values (94, 690, '3448629109374f7ba66c569d1b4939de9aab871c', 'Saunder Squire', '2022-07-12 05:19:32');
insert into "user" (id, balance, hash, name, reg_time) values (95, 823, '88b7bf65e843293afced86650af6dd692ba1af69', 'Atlanta Ducastel', '2022-01-01 03:08:58');
insert into "user" (id, balance, hash, name, reg_time) values (96, 724, 'f94b795dd37e41153083c358bc05b2c7eab8fd04', 'Lonnard Copeman', '2022-05-17 18:20:56');
insert into "user" (id, balance, hash, name, reg_time) values (97, 645, '2aab80408c45a2374f53b56caa660b34823d0375', 'Camala Bidwell', '2022-01-26 13:15:16');
insert into "user" (id, balance, hash, name, reg_time) values (98, 753, '49aa052fdd554f209231b4e834e7f5f83f1d2955', 'Hamel Jephcote', '2022-05-20 20:54:26');
insert into "user" (id, balance, hash, name, reg_time) values (99, 867, 'b4399bb4f9e850834ce4a542492f66a16fc80f5a', 'Barny Clulee', '2022-09-27 10:51:15');
insert into "user" (id, balance, hash, name, reg_time) values (100, 943, '627248f64e785a5f7bdc2f31a23a7d82369e3f6f', 'Pam Quaif', '2022-03-06 01:58:36');


insert into tag (id, tag, slug, description) values (1, 'challenge', 'challenge', 'nunc');
insert into tag (id, tag, slug, description) values (2, 'sit_amet', 'sit_amet', 'convallis');
insert into tag (id, tag, slug, description) values (3, 'Grass-roots', 'Grass-roots', 'consequat in');
insert into tag (id, tag, slug, description) values (4, 'algorithm', 'algorithm', 'elementum');
insert into tag (id, tag, slug, description) values (5, 'solution-oriented', 'solution-oriented', 'aliquet');
insert into tag (id, tag, slug, description) values (6, 'Synchronised', 'synchronised', 'curae');
insert into tag (id, tag, slug, description) values (7, 'mi', 'mi', 'nibh quisque');
insert into tag (id, tag, slug, description) values (8, 'open-source', 'open-source', 'lectus');
insert into tag (id, tag, slug, description) values (9, 'upgradable', 'upgradable', 'in');
insert into tag (id, tag, slug, description) values (10, 'responsive', 'responsive', 'porta');
insert into tag (id, tag, slug, description) values (11, 'stand-alone', 'stand-alone', 'metus sapien');
insert into tag (id, tag, slug, description) values (12, 'self-enabling', 'self-enabling', 'ante');
insert into tag (id, tag, slug, description) values (13, 'zero_tolerance', 'zero_tolerance', 'quisque');
insert into tag (id, tag, slug, description) values (14, 'in_eleifend', 'in_eleifend', 'lectus');
insert into tag (id, tag, slug, description) values (15, 'local_area_network', 'local_area_network', 'maecenas');
insert into tag (id, tag, slug, description) values (16, 'future_proofed', 'future_proofed', 'non lectus');
insert into tag (id, tag, slug, description) values (17, 'vel_ipsum', 'vel_ipsum', 'sem');
insert into tag (id, tag, slug, description) values (18, 'in_tempus', 'in_tempus', 'morbi');
insert into tag (id, tag, slug, description) values (19, 'habitasse', 'habitasse', 'donec');
insert into tag (id, tag, slug, description) values (20, 'sapien_dignissim', 'sapien_dignissim', 'sapien dignissim');
insert into tag (id, tag, slug, description) values (21, 'metus_sapien', 'metus_sapien', 'augue');


insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (1, 0, '2022-06-02 05:10:52', 800, 669, 'Playboys, The', 'playboys_the', 'http://dummyimage.com/122x100.png/dddddd/000000', 'justo etiam pretium iaculis justo in hac habitasse platea dictumst etiam faucibus cursus', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (2, 1, '2022-10-01 01:03:21', 693, 647, 'Alcina', 'alcina', 'http://dummyimage.com/246x100.png/dddddd/000000', 'facilisi cras non velit nec nisi vulputate nonummy maecenas tincidunt', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (3, 1, '2022-08-27 11:50:34', 955, 958, 'Tales from the Script', 'tales_from_the_script', 'http://dummyimage.com/103x100.png/ff4444/ffffff', 'vivamus in felis eu sapien cursus vestibulum proin eu mi nulla ac enim in', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (4, 0, '2022-09-25 00:08:12', 757, 987, 'Express, The', 'express_the', 'http://dummyimage.com/111x100.png/dddddd/000000', 'fusce lacus purus aliquet at feugiat non pretium quis lectus suspendisse', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (5, 1, '2022-06-27 23:57:54', 637, 654, 'Escapist, The', 'escapist_the', 'http://dummyimage.com/112x100.png/ff4444/ffffff', 'sem sed sagittis nam congue risus semper porta volutpat quam pede lobortis ligula sit amet eleifend pede libero quis orci', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (6, 0, '2022-10-15 00:44:18', 550, 814, 'Hideous Kinky', 'hideous_kinky, Deadly Night', 'http://dummyimage.com/250x100.png/cc0000/ffffff', 'elementum pellentesque quisque porta volutpat erat quisque erat eros viverra eget congue eget semper rutrum', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (7, 0, '2022-12-02 17:29:39', 742, 977, 'Happy Poet, The', 'happy_poet_the', 'http://dummyimage.com/131x100.png/dddddd/000000', 'mi pede malesuada in imperdiet et commodo vulputate justo in blandit', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (8, 0, '2022-04-21 19:55:43', 747, 647, 'Free The Mind', 'free_the_mind', 'http://dummyimage.com/150x100.png/ff4444/ffffff', 'quam sollicitudin vitae consectetuer eget rutrum at lorem integer tincidunt ante vel ipsum', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (9, 1, '2022-07-09 14:45:52', 675, 707, 'Sovereign''s Company', 'sovereigns_company', 'http://dummyimage.com/158x100.png/dddddd/000000', 'sapien non mi integer ac neque duis bibendum morbi non quam nec dui luctus rutrum nulla tellus', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (10, 1, '2022-02-01 08:41:43', 604, 618, 'Queen Sized', 'queen_sized', 'http://dummyimage.com/187x100.png/5fa2dd/ffffff', 'est lacinia nisi venenatis tristique fusce congue diam id ornare imperdiet sapien urna pretium nisl ut volutpat sapien', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (11, 1, '2022-04-11 19:23:15', 562, 750, 'Iron Eagle IV', 'iron_eagle_iv', 'http://dummyimage.com/171x100.png/cc0000/ffffff', 'vel est donec odio justo sollicitudin ut suscipit a feugiat et eros vestibulum ac est lacinia', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (12, 0, '2022-02-10 07:22:13', 637, 961, 'One-Armed Swordsman, The (Dubei dao)', 'one_armed_swordsman_the_dubei_dao', 'http://dummyimage.com/140x100.png/dddddd/000000', 'sed tincidunt eu felis fusce posuere felis sed lacus morbi sem mauris laoreet', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (13, 1, '2022-01-20 02:32:40', 995, 771, 'Barnyard: The Original Party Animals', 'barnyard_the_original_party_animals', 'http://dummyimage.com/150x100.png/cc0000/ffffff', 'dictumst etiam faucibus cursus urna ut tellus nulla ut erat id mauris vulputate', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (14, 1, '2022-12-16 08:00:06', 828, 647, 'Hannibal', 'hannibal', 'http://dummyimage.com/239x100.png/cc0000/ffffff', 'dui vel nisl duis ac nibh fusce lacus purus aliquet at feugiat non pretium', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (15, 1, '2022-07-09 16:41:37', 1000, 994, 'Hyenas (Hyènes)', 'hyenas_hyenes', 'http://dummyimage.com/179x100.png/cc0000/ffffff', 'nunc viverra dapibus nulla suscipit ligula in lacus curabitur at ipsum ac tellus semper interdum mauris', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (16, 1, '2022-06-18 12:03:07', 714, 913, 'Snowriders', 'snowriders', 'http://dummyimage.com/185x100.png/dddddd/000000', 'aliquet pulvinar sed nisl nunc rhoncus dui vel sem sed', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (17, 0, '2022-07-21 13:38:34', 840, 957, '55 Days at Peking', '55_days_at_peking', 'http://dummyimage.com/144x100.png/5fa2dd/ffffff', 'dapibus duis at velit eu est congue elementum in hac habitasse platea dictumst morbi vestibulum velit id pretium iaculis diam', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (18, 0, '2022-05-25 12:50:18', 976, 829, 'In China They Eat Dogs (I Kina spiser de hunde)', 'in_china_they_eat_dogs_i_kina_spiser_de_hunde', 'http://dummyimage.com/145x100.png/ff4444/ffffff', 'ante vivamus tortor duis mattis egestas metus aenean fermentum donec ut mauris eget massa', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (19, 0, '2022-07-26 05:18:10', 695, 689, 'Klimt', 'klimt', 'http://dummyimage.com/117x100.png/cc0000/ffffff', 'pellentesque quisque porta volutpat erat quisque erat eros viverra eget congue', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (20, 1, '2022-01-08 10:42:20', 837, 909, 'Broken Arrow', 'broken_arrow', 'http://dummyimage.com/206x100.png/dddddd/000000', 'mattis pulvinar nulla pede ullamcorper augue a suscipit nulla elit ac nulla sed vel enim sit', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (21, 0, '2022-06-26 18:07:04', 928, 954, 'Reflections in a Golden Eye', 'reflections_in_a_golden_eye', 'http://dummyimage.com/128x100.png/dddddd/000000', 'dictumst maecenas ut massa quis augue luctus tincidunt nulla mollis', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (22, 0, '2022-03-02 12:03:38', 990, 606, 'Beauty and the Beast (Belle et la bête, La)', 'beauty_and_the_beast_belle_et_la_bete_la', 'http://dummyimage.com/169x100.png/cc0000/ffffff', 'dapibus augue vel accumsan tellus nisi eu orci mauris lacinia sapien quis libero nullam sit', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (23, 1, '2022-06-10 22:00:35', 598, 1000, 'Dumbo', 'dumbo', 'http://dummyimage.com/123x100.png/5fa2dd/ffffff', 'in leo maecenas pulvinar lobortis est phasellus sit amet erat nulla tempus vivamus in felis eu sapien cursus vestibulum', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (24, 0, '2021-12-26 19:15:22', 758, 591, 'Chak De India!', 'chak_de_india', 'http://dummyimage.com/125x100.png/dddddd/000000', 'mauris laoreet ut rhoncus aliquet pulvinar sed nisl nunc rhoncus dui vel sem sed sagittis nam congue risus semper porta', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (25, 0, '2022-09-14 01:02:16', 972, 881, 'Dark Horse (Voksne mennesker)', 'dark_horse_voksne_mennesker', 'http://dummyimage.com/171x100.png/cc0000/ffffff', 'volutpat in congue etiam justo etiam pretium iaculis justo in hac habitasse platea dictumst etiam faucibus', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (26, 0, '2022-08-27 21:39:37', 617, 726, 'She''s Out of My League', 'shes_out_of_my_league', 'http://dummyimage.com/100x100.png/dddddd/000000', 'vel sem sed sagittis nam congue risus semper porta volutpat quam pede lobortis ligula sit amet eleifend pede libero quis', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (27, 0, '2022-04-29 07:58:09', 989, 597, 'Great Waltz, The', 'great_waltz_the', 'http://dummyimage.com/137x100.png/dddddd/000000', 'integer aliquet massa id lobortis convallis tortor risus dapibus augue', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (28, 1, '2021-12-23 02:49:47', 854, 584, 'Killers from Space', 'killers_from_space', 'http://dummyimage.com/208x100.png/dddddd/000000', 'dictumst etiam faucibus cursus urna ut tellus nulla ut erat id mauris vulputate elementum nullam varius nulla facilisi cras non', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (29, 1, '2022-09-02 00:48:30', 527, 853, 'Mysterious Skin', 'mysterious_skin', 'http://dummyimage.com/124x100.png/ff4444/ffffff', 'luctus cum sociis natoque penatibus et magnis dis parturient montes nascetur ridiculus mus vivamus vestibulum', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (30, 0, '2022-11-22 13:48:12', 819, 745, 'Flowers of War, The (Jin líng shí san chai)', 'flowers_of_war_the_jin_ling_shi_san_chai', 'http://dummyimage.com/179x100.png/cc0000/ffffff', 'pede venenatis non sodales sed tincidunt eu felis fusce posuere felis sed', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (31, 0, '2022-07-02 03:43:43', 749, 828, 'Fish Called Wanda, A', 'fish_called_wanda_a', 'http://dummyimage.com/150x100.png/5fa2dd/ffffff', 'ultrices posuere cubilia curae duis faucibus accumsan odio curabitur convallis duis consequat dui nec nisi volutpat eleifend donec', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (32, 0, '2022-04-30 11:56:23', 940, 603, 'Manson Family, The', 'manson_family_the', 'http://dummyimage.com/233x100.png/5fa2dd/ffffff', 'scelerisque mauris sit amet eros suspendisse accumsan tortor quis turpis sed ante vivamus tortor', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (33, 0, '2022-10-27 15:59:30', 923, 826, 'Europa Report', 'europa_report', 'http://dummyimage.com/223x100.png/dddddd/000000', 'pede malesuada in imperdiet et commodo vulputate justo in blandit ultrices enim lorem ipsum dolor sit amet', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (34, 0, '2022-04-05 21:21:39', 638, 538, 'Project A (''A'' gai waak)', 'project_a_a_gai_waak', 'http://dummyimage.com/208x100.png/dddddd/000000', 'rutrum neque aenean auctor gravida sem praesent id massa id nisl venenatis lacinia aenean', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (35, 1, '2022-08-31 17:22:29', 876, 735, '40-Year-Old Virgin, The', '40_year_old_virgin_the', 'http://dummyimage.com/185x100.png/ff4444/ffffff', 'curae mauris viverra diam vitae quam suspendisse potenti nullam porttitor lacus at turpis donec', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (36, 0, '2022-08-05 17:23:53', 930, 620, 'About Face: Supermodels Then and Now', 'about_face_supermodels_then_and_now', 'http://dummyimage.com/238x100.png/5fa2dd/ffffff', 'felis sed interdum venenatis turpis enim blandit mi in porttitor pede justo', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (37, 0, '2022-07-21 02:44:35', 914, 749, '40-Year-Old Virgin, The', '40_year_old_virgin_the', 'http://dummyimage.com/138x100.png/ff4444/ffffff', 'consectetuer adipiscing elit proin interdum mauris non ligula pellentesque ultrices phasellus id sapien in sapien iaculis', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (38, 1, '2022-02-24 16:32:17', 710, 962, 'Alive', 'alive', 'http://dummyimage.com/216x100.png/5fa2dd/ffffff', 'vulputate vitae nisl aenean lectus pellentesque eget nunc donec quis', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (39, 1, '2022-03-18 15:06:02', 907, 738, 'North West Frontier', 'north_west_frontier', 'http://dummyimage.com/100x100.png/5fa2dd/ffffff', 'id nisl venenatis lacinia aenean sit amet justo morbi ut odio cras mi pede malesuada in', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (40, 0, '2022-02-20 14:30:16', 652, 612, 'Samson and Delilah', 'samson_and_delilah', 'http://dummyimage.com/143x100.png/dddddd/000000', 'ultrices posuere cubilia curae mauris viverra diam vitae quam suspendisse potenti', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (41, 1, '2022-04-23 16:42:47', 860, 571, 'Bitter Creek', 'bitter_creek', 'http://dummyimage.com/143x100.png/ff4444/ffffff', 'quis turpis sed ante vivamus tortor duis mattis egestas metus aenean fermentum donec ut mauris eget massa tempor', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (42, 0, '2022-03-16 06:28:26', 875, 905, 'Barnens ö', 'barnens_o', 'http://dummyimage.com/228x100.png/5fa2dd/ffffff', 'nulla mollis molestie lorem quisque ut erat curabitur gravida nisi at nibh', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (43, 0, '2022-09-24 20:40:37', 787, 548, 'Lancelot of the Lake (Lancelot du Lac)', 'lancelot_of_the_lake_lancelot_du_lac', 'http://dummyimage.com/225x100.png/cc0000/ffffff', 'adipiscing molestie hendrerit at vulputate vitae nisl aenean lectus pellentesque eget nunc donec quis orci', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (44, 1, '2022-11-06 14:34:33', 560, 851, 'Thunderball', 'thunderball', 'http://dummyimage.com/207x100.png/ff4444/ffffff', 'in porttitor pede justo eu massa donec dapibus duis at velit eu est congue elementum in hac habitasse', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (45, 0, '2022-07-27 18:51:37', 596, 825, 'Finding Joy', 'finding_joy', 'http://dummyimage.com/172x100.png/ff4444/ffffff', 'mi sit amet lobortis sapien sapien non mi integer ac neque duis bibendum morbi non quam nec dui luctus', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (46, 0, '2022-02-14 17:48:46', 983, 778, 'Company of Heroes', 'company_of_heroes', 'http://dummyimage.com/220x100.png/cc0000/ffffff', 'luctus et ultrices posuere cubilia curae donec pharetra magna vestibulum aliquet ultrices erat tortor sollicitudin mi sit amet lobortis sapien', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (47, 0, '2022-01-13 17:23:11', 816, 651, 'Wagon Master', 'wagon_master', 'http://dummyimage.com/140x100.png/dddddd/000000', 'ante vel ipsum praesent blandit lacinia erat vestibulum sed magna at nunc commodo placerat praesent blandit', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (48, 1, '2021-12-26 11:13:23', 718, 775, 'Queen of the Mountains', 'queen_of_the_mountains', 'http://dummyimage.com/123x100.png/cc0000/ffffff', 'montes nascetur ridiculus mus vivamus vestibulum sagittis sapien cum sociis natoque penatibus et magnis', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (49, 0, '2022-09-11 18:49:29', 870, 728, 'Frida', 'frida', 'http://dummyimage.com/162x100.png/cc0000/ffffff', 'volutpat eleifend donec ut dolor morbi vel lectus in quam fringilla rhoncus mauris enim', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (50, 0, '2022-06-17 01:14:40', 526, 718, 'Mutiny on the Bounty', 'mutiny_on_the_bounty', 'http://dummyimage.com/102x100.png/ff4444/ffffff', 'nunc commodo placerat praesent blandit nam nulla integer pede justo', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (51, 1, '2022-10-22 12:49:21', 827, 709, 'Wishmaster 2: Evil Never Dies', 'wishmaster_2_evil_never_dies', 'http://dummyimage.com/147x100.png/dddddd/000000', 'lacinia eget tincidunt eget tempus vel pede morbi porttitor lorem id ligula', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (52, 0, '2022-11-23 12:58:06', 776, 722, 'Braveheart', 'braveheart', 'http://dummyimage.com/212x100.png/5fa2dd/ffffff', 'nisi volutpat eleifend donec ut dolor morbi vel lectus in quam fringilla rhoncus mauris', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (53, 0, '2022-12-09 21:26:40', 920, 589, 'Murphy''s War', 'murphys_war', 'http://dummyimage.com/173x100.png/cc0000/ffffff', 'vehicula condimentum curabitur in libero ut massa volutpat convallis morbi odio odio elementum eu', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (54, 1, '2022-06-02 19:24:23', 558, 976, 'The Radio Pirates', 'the_radio_pirates', 'http://dummyimage.com/249x100.png/5fa2dd/ffffff', 'convallis tortor risus dapibus augue vel accumsan tellus nisi eu orci mauris lacinia sapien quis libero nullam sit amet turpis', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (55, 1, '2022-01-11 05:27:05', 785, 599, 'Bay, The', 'bay_the', 'http://dummyimage.com/239x100.png/5fa2dd/ffffff', 'vestibulum rutrum rutrum neque aenean auctor gravida sem praesent id massa id nisl venenatis lacinia aenean sit', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (56, 1, '2022-10-26 14:26:02', 727, 777, 'Adventures of the Wilderness Family, The', 'adventures_of_the_wilderness_family_the', 'http://dummyimage.com/186x100.png/ff4444/ffffff', 'dui vel sem sed sagittis nam congue risus semper porta volutpat', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (57, 1, '2022-07-04 19:27:34', 701, 981, 'Five Obstructions, The (Fem benspænd, De)', 'five_obstructions_the_fem_benspaend_de', 'http://dummyimage.com/133x100.png/cc0000/ffffff', 'eget congue eget semper rutrum nulla nunc purus phasellus in felis donec semper sapien a libero', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (58, 1, '2022-11-25 07:06:42', 762, 996, 'Zombie Honeymoon', 'zombie_honeymoon', 'http://dummyimage.com/153x100.png/dddddd/000000', 'sapien a libero nam dui proin leo odio porttitor id consequat in consequat ut nulla sed accumsan felis ut at', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (59, 0, '2022-04-15 15:44:25', 609, 597, 'Fuzz', 'fuzz', 'http://dummyimage.com/118x100.png/cc0000/ffffff', 'felis fusce posuere felis sed lacus morbi sem mauris laoreet ut rhoncus aliquet pulvinar', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (60, 1, '2022-02-08 22:54:21', 544, 839, 'I''m Going Home (Je rentre à la maison)', 'im_going_home_je_rentre_a_la_maison', 'http://dummyimage.com/160x100.png/dddddd/000000', 'montes nascetur ridiculus mus vivamus vestibulum sagittis sapien cum sociis natoque penatibus et magnis dis parturient montes', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (61, 1, '2022-05-17 15:44:57', 910, 547, 'Carcasses', 'carcasses', 'http://dummyimage.com/123x100.png/5fa2dd/ffffff', 'libero convallis eget eleifend luctus ultricies eu nibh quisque id justo sit amet sapien', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (62, 0, '2022-06-03 08:17:32', 561, 840, 'Beethoven', 'beethoven', 'http://dummyimage.com/162x100.png/cc0000/ffffff', 'vivamus tortor duis mattis egestas metus aenean fermentum donec ut mauris eget', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (63, 0, '2022-02-23 17:50:18', 546, 881, 'Bottle Shock', 'bottle_shock', 'http://dummyimage.com/160x100.png/5fa2dd/ffffff', 'tincidunt in leo maecenas pulvinar lobortis est phasellus sit amet erat nulla', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (64, 0, '2022-03-17 22:08:42', 815, 508, 'Fallen Idol, The', 'fallen_idol_the', 'http://dummyimage.com/208x100.png/ff4444/ffffff', 'sagittis nam congue risus semper porta volutpat quam pede lobortis ligula sit amet eleifend pede libero quis orci', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (65, 0, '2022-07-25 21:07:58', 627, 982, 'Wild, The', 'wild_the', 'http://dummyimage.com/122x100.png/dddddd/000000', 'pulvinar nulla pede ullamcorper augue a suscipit nulla elit ac nulla sed vel enim sit amet nunc viverra', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (66, 0, '2022-02-21 07:29:35', 515, 862, 'Break-in', 'break_in', 'http://dummyimage.com/247x100.png/ff4444/ffffff', 'diam id ornare imperdiet sapien urna pretium nisl ut volutpat sapien arcu sed augue aliquam erat', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (67, 1, '2022-10-18 08:23:00', 787, 960, 'Love Affair, or the Case of the Missing Switchboard Operator (Ljubavni slucaj ili tragedija sluzbenice P.T.T.)', 'love_affair_or_the_case_of_the_missing_switchboard_operator_ljubavni_slucaj_ili_tragedija_sluzbenice_ptt', 'http://dummyimage.com/167x100.png/dddddd/000000', 'curae duis faucibus accumsan odio curabitur convallis duis consequat dui nec nisi', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (68, 1, '2022-04-18 14:13:03', 616, 637, 'Exiled (Fong juk)', 'exiled_fong_juk', 'http://dummyimage.com/193x100.png/cc0000/ffffff', 'metus aenean fermentum donec ut mauris eget massa tempor convallis', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (69, 1, '2022-07-06 13:59:04', 502, 627, 'Treasure Island', 'treasure_island', 'http://dummyimage.com/120x100.png/cc0000/ffffff', 'et magnis dis parturient montes nascetur ridiculus mus etiam vel augue vestibulum rutrum rutrum neque', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (70, 1, '2022-06-21 17:58:53', 720, 592, 'Judas Kiss', 'Your Highness', 'http://dummyimage.com/226x100.png/dddddd/000000', 'consectetuer adipiscing elit proin risus praesent lectus vestibulum quam sapien varius ut blandit non interdum in ante vestibulum ante ipsum', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (71, 1, '2022-04-23 12:41:31', 501, 959, 'Special Day, A (Giornata particolare, Una)', 'Rasputin', 'http://dummyimage.com/236x100.png/ff4444/ffffff', 'interdum eu tincidunt in leo maecenas pulvinar lobortis est phasellus sit amet', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (72, 1, '2022-02-17 20:10:29', 990, 897, 'Road House', 'Beaches of Agnes, The (Plages d''Agnès, Les)', 'http://dummyimage.com/223x100.png/ff4444/ffffff', 'sit amet justo morbi ut odio cras mi pede malesuada in', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (73, 0, '2022-10-25 13:38:10', 704, 736, 'Youth Without Youth', 'Rear Window', 'http://dummyimage.com/154x100.png/5fa2dd/ffffff', 'vivamus tortor duis mattis egestas metus aenean fermentum donec ut mauris eget massa tempor convallis nulla', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (74, 0, '2022-04-08 23:57:34', 937, 750, 'Joyless Street, The (Die freudlose Gasse)', 'Tooth Fairy', 'http://dummyimage.com/144x100.png/5fa2dd/ffffff', 'vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae duis faucibus accumsan odio curabitur convallis duis', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (75, 1, '2022-08-12 00:32:55', 926, 968, 'Ginger Snaps: Unleashed', 'Magic of Belle Isle, The', 'http://dummyimage.com/109x100.png/ff4444/ffffff', 'sociis natoque penatibus et magnis dis parturient montes nascetur ridiculus mus vivamus vestibulum sagittis', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (76, 0, '2022-03-20 09:42:31', 878, 670, 'Mother, The', 'Switch', 'http://dummyimage.com/248x100.png/ff4444/ffffff', 'pede ac diam cras pellentesque volutpat dui maecenas tristique est', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (77, 1, '2022-09-30 23:53:23', 936, 584, 'Focus', 'Deep Blue', 'http://dummyimage.com/212x100.png/cc0000/ffffff', 'eros vestibulum ac est lacinia nisi venenatis tristique fusce congue diam id ornare imperdiet sapien urna pretium nisl ut volutpat', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (78, 0, '2022-07-20 19:40:13', 524, 582, 'National Lampoon''s Senior Trip', 'Objectified', 'http://dummyimage.com/180x100.png/cc0000/ffffff', 'rhoncus aliquam lacus morbi quis tortor id nulla ultrices aliquet', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (79, 1, '2022-11-06 13:06:44', 984, 936, 'Majority of One, A', 'Dinner Guest, The (L''invité)', 'http://dummyimage.com/131x100.png/cc0000/ffffff', 'maecenas leo odio condimentum id luctus nec molestie sed justo pellentesque viverra pede ac', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (80, 0, '2022-04-18 04:12:20', 885, 779, 'New York: A Documentary Film', 'In Two Minds', 'http://dummyimage.com/161x100.png/dddddd/000000', 'penatibus et magnis dis parturient montes nascetur ridiculus mus etiam', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (81, 0, '2022-09-10 04:40:18', 857, 745, 'Execution of P, The (Kinatay)', 'Wetlands (Feuchtgebiete)', 'http://dummyimage.com/152x100.png/5fa2dd/ffffff', 'ultrices posuere cubilia curae nulla dapibus dolor vel est donec odio justo sollicitudin ut suscipit a feugiat et eros', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (82, 0, '2022-11-19 10:15:45', 535, 927, 'Midnight Run', 'Breaking Point, The', 'http://dummyimage.com/184x100.png/ff4444/ffffff', 'faucibus orci luctus et ultrices posuere cubilia curae duis faucibus accumsan odio curabitur convallis duis consequat', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (83, 0, '2022-01-24 11:41:15', 831, 796, 'Soldier, The', 'Private Resort', 'http://dummyimage.com/193x100.png/dddddd/000000', 'vel est donec odio justo sollicitudin ut suscipit a feugiat et', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (84, 0, '2022-04-01 16:07:57', 752, 678, 'My Father and the Man in Black', 'Tormented', 'http://dummyimage.com/177x100.png/ff4444/ffffff', 'integer aliquet massa id lobortis convallis tortor risus dapibus augue vel', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (85, 0, '2022-01-05 21:24:39', 547, 796, 'My Geisha', 'Tomb, The', 'http://dummyimage.com/194x100.png/5fa2dd/ffffff', 'rhoncus dui vel sem sed sagittis nam congue risus semper porta volutpat quam pede lobortis', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (86, 1, '2022-11-16 09:08:13', 751, 513, 'C.R.A.Z.Y.', 'Two Can Play That Game', 'http://dummyimage.com/205x100.png/cc0000/ffffff', 'eleifend quam a odio in hac habitasse platea dictumst maecenas ut massa quis augue luctus tincidunt', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (87, 1, '2022-08-13 18:57:16', 992, 823, 'Daniel', 'Thief and the Cobbler, The (a.k.a. Arabian Knight)', 'http://dummyimage.com/180x100.png/cc0000/ffffff', 'in consequat ut nulla sed accumsan felis ut at dolor quis odio consequat varius integer ac leo', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (88, 0, '2022-03-22 16:15:55', 905, 939, 'Wilde', 'Tom and Jerry: A Nutcracker Tale', 'http://dummyimage.com/172x100.png/ff4444/ffffff', 'congue diam id ornare imperdiet sapien urna pretium nisl ut volutpat sapien arcu sed augue aliquam', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (89, 0, '2022-07-11 20:45:18', 951, 835, 'Udaan', 'Forever Hardcore: The Documentary', 'http://dummyimage.com/142x100.png/5fa2dd/ffffff', 'donec pharetra magna vestibulum aliquet ultrices erat tortor sollicitudin mi sit amet lobortis sapien sapien', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (90, 1, '2022-05-20 04:25:25', 607, 968, 'Expert, The', 'Seventh Heaven', 'http://dummyimage.com/143x100.png/dddddd/000000', 'quis odio consequat varius integer ac leo pellentesque ultrices mattis odio donec', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (91, 0, '2022-10-27 17:54:38', 566, 677, 'Roxanne', 'You May Not Kiss the Bride', 'http://dummyimage.com/150x100.png/dddddd/000000', 'platea dictumst morbi vestibulum velit id pretium iaculis diam erat', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (92, 0, '2022-01-07 09:53:28', 787, 865, 'Boy', 'Grandmother', 'http://dummyimage.com/158x100.png/cc0000/ffffff', 'elit proin interdum mauris non ligula pellentesque ultrices phasellus id sapien in sapien iaculis congue vivamus', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (93, 1, '2022-09-25 03:16:17', 717, 809, 'Rosa Luxemburg', 'Children of the Corn', 'http://dummyimage.com/114x100.png/cc0000/ffffff', 'a libero nam dui proin leo odio porttitor id consequat', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (94, 0, '2022-08-25 03:52:04', 948, 852, 'Strawberry Wine', 'Choose Me', 'http://dummyimage.com/241x100.png/5fa2dd/ffffff', 'sit amet consectetuer adipiscing elit proin interdum mauris non ligula pellentesque', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (95, 0, '2022-05-07 20:24:03', 745, 815, 'Faithful', 'Hearts and Minds', 'http://dummyimage.com/113x100.png/5fa2dd/ffffff', 'lorem quisque ut erat curabitur gravida nisi at nibh in', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (96, 1, '2022-10-19 07:23:58', 845, 570, 'Men Cry Bullets', 'W.C. Fields and Me', 'http://dummyimage.com/156x100.png/dddddd/000000', 'mauris non ligula pellentesque ultrices phasellus id sapien in sapien iaculis congue vivamus metus arcu', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (97, 1, '2022-03-29 14:34:44', 710, 836, 'Film ist.', 'Go West', 'http://dummyimage.com/125x100.png/cc0000/ffffff', 'suspendisse potenti in eleifend quam a odio in hac habitasse platea dictumst maecenas ut massa quis', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (98, 0, '2022-11-25 08:02:24', 744, 584, 'Down Argentine Way', 'Rain', 'http://dummyimage.com/219x100.png/ff4444/ffffff', 'cras mi pede malesuada in imperdiet et commodo vulputate justo in blandit ultrices', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (99, 0, '2022-02-14 10:36:26', 669, 632, 'Attack of the 5 Ft. 2 Women (National Lampoon''s Attack of the 5 Ft 2 Woman)', 'Harold & Kumar Escape from Guantanamo Bay', 'http://dummyimage.com/245x100.png/cc0000/ffffff', 'ac enim in tempor turpis nec euismod scelerisque quam turpis adipiscing lorem vitae mattis', 0, 0, 0);
insert into book (id, is_bestseller, pub_date, price, discount, title, slug, image, description, stat_in_cart, stat_bought, stat_postponed) values (100, 1, '2022-05-06 04:48:42', 579, 890, 'Wild in the Country', 'Storm Over Asia (Potomok Chingis-Khana)', 'http://dummyimage.com/207x100.png/ff4444/ffffff', 'tempus semper est quam pharetra magna ac consequat metus sapien', 0, 0, 0);


insert into book2tag (id, book_id, tag_id) values (1, 1, 16);
insert into book2tag (id, book_id, tag_id) values (2, 2, 17);
insert into book2tag (id, book_id, tag_id) values (3, 3, 20);
insert into book2tag (id, book_id, tag_id) values (4, 4, 15);
insert into book2tag (id, book_id, tag_id) values (5, 5, 9);
insert into book2tag (id, book_id, tag_id) values (6, 6, 21);
insert into book2tag (id, book_id, tag_id) values (7, 7, 5);
insert into book2tag (id, book_id, tag_id) values (8, 8, 18);
insert into book2tag (id, book_id, tag_id) values (9, 9, 3);
insert into book2tag (id, book_id, tag_id) values (10, 10, 2);
insert into book2tag (id, book_id, tag_id) values (11, 11, 20);
insert into book2tag (id, book_id, tag_id) values (12, 12, 4);
insert into book2tag (id, book_id, tag_id) values (13, 13, 13);
insert into book2tag (id, book_id, tag_id) values (14, 14, 14);
insert into book2tag (id, book_id, tag_id) values (15, 15, 1);
insert into book2tag (id, book_id, tag_id) values (16, 16, 15);
insert into book2tag (id, book_id, tag_id) values (17, 17, 16);
insert into book2tag (id, book_id, tag_id) values (18, 18, 6);
insert into book2tag (id, book_id, tag_id) values (19, 19, 12);
insert into book2tag (id, book_id, tag_id) values (20, 20, 13);
insert into book2tag (id, book_id, tag_id) values (21, 21, 9);
insert into book2tag (id, book_id, tag_id) values (22, 22, 9);
insert into book2tag (id, book_id, tag_id) values (23, 23, 14);
insert into book2tag (id, book_id, tag_id) values (24, 24, 5);
insert into book2tag (id, book_id, tag_id) values (25, 25, 7);
insert into book2tag (id, book_id, tag_id) values (26, 26, 8);
insert into book2tag (id, book_id, tag_id) values (27, 27, 18);
insert into book2tag (id, book_id, tag_id) values (28, 28, 2);
insert into book2tag (id, book_id, tag_id) values (29, 29, 5);
insert into book2tag (id, book_id, tag_id) values (30, 30, 2);
insert into book2tag (id, book_id, tag_id) values (31, 31, 15);
insert into book2tag (id, book_id, tag_id) values (32, 32, 8);
insert into book2tag (id, book_id, tag_id) values (33, 33, 4);
insert into book2tag (id, book_id, tag_id) values (34, 34, 15);
insert into book2tag (id, book_id, tag_id) values (35, 35, 21);
insert into book2tag (id, book_id, tag_id) values (36, 36, 17);
insert into book2tag (id, book_id, tag_id) values (37, 37, 10);
insert into book2tag (id, book_id, tag_id) values (38, 38, 1);
insert into book2tag (id, book_id, tag_id) values (39, 39, 9);
insert into book2tag (id, book_id, tag_id) values (40, 40, 18);
insert into book2tag (id, book_id, tag_id) values (41, 41, 2);
insert into book2tag (id, book_id, tag_id) values (42, 42, 17);
insert into book2tag (id, book_id, tag_id) values (43, 43, 12);
insert into book2tag (id, book_id, tag_id) values (44, 44, 19);
insert into book2tag (id, book_id, tag_id) values (45, 45, 21);
insert into book2tag (id, book_id, tag_id) values (46, 46, 19);
insert into book2tag (id, book_id, tag_id) values (47, 47, 19);
insert into book2tag (id, book_id, tag_id) values (48, 48, 6);
insert into book2tag (id, book_id, tag_id) values (49, 49, 18);
insert into book2tag (id, book_id, tag_id) values (50, 50, 4);
insert into book2tag (id, book_id, tag_id) values (51, 51, 10);
insert into book2tag (id, book_id, tag_id) values (52, 52, 16);
insert into book2tag (id, book_id, tag_id) values (53, 53, 1);
insert into book2tag (id, book_id, tag_id) values (54, 54, 21);
insert into book2tag (id, book_id, tag_id) values (55, 55, 5);
insert into book2tag (id, book_id, tag_id) values (56, 56, 21);
insert into book2tag (id, book_id, tag_id) values (57, 57, 19);
insert into book2tag (id, book_id, tag_id) values (58, 58, 11);
insert into book2tag (id, book_id, tag_id) values (59, 59, 14);
insert into book2tag (id, book_id, tag_id) values (60, 60, 18);
insert into book2tag (id, book_id, tag_id) values (61, 61, 1);
insert into book2tag (id, book_id, tag_id) values (62, 62, 5);
insert into book2tag (id, book_id, tag_id) values (63, 63, 4);
insert into book2tag (id, book_id, tag_id) values (64, 64, 20);
insert into book2tag (id, book_id, tag_id) values (65, 65, 11);
insert into book2tag (id, book_id, tag_id) values (66, 66, 17);
insert into book2tag (id, book_id, tag_id) values (67, 67, 13);
insert into book2tag (id, book_id, tag_id) values (68, 68, 10);
insert into book2tag (id, book_id, tag_id) values (69, 69, 11);
insert into book2tag (id, book_id, tag_id) values (70, 70, 7);
insert into book2tag (id, book_id, tag_id) values (71, 71, 18);
insert into book2tag (id, book_id, tag_id) values (72, 72, 9);
insert into book2tag (id, book_id, tag_id) values (73, 73, 4);
insert into book2tag (id, book_id, tag_id) values (74, 74, 15);
insert into book2tag (id, book_id, tag_id) values (75, 75, 13);
insert into book2tag (id, book_id, tag_id) values (76, 76, 16);
insert into book2tag (id, book_id, tag_id) values (77, 77, 7);
insert into book2tag (id, book_id, tag_id) values (78, 78, 17);
insert into book2tag (id, book_id, tag_id) values (79, 79, 6);
insert into book2tag (id, book_id, tag_id) values (80, 80, 5);
insert into book2tag (id, book_id, tag_id) values (81, 81, 21);
insert into book2tag (id, book_id, tag_id) values (82, 82, 18);
insert into book2tag (id, book_id, tag_id) values (83, 83, 9);
insert into book2tag (id, book_id, tag_id) values (84, 84, 5);
insert into book2tag (id, book_id, tag_id) values (85, 85, 21);
insert into book2tag (id, book_id, tag_id) values (86, 86, 15);
insert into book2tag (id, book_id, tag_id) values (87, 87, 20);
insert into book2tag (id, book_id, tag_id) values (88, 88, 9);
insert into book2tag (id, book_id, tag_id) values (89, 89, 2);
insert into book2tag (id, book_id, tag_id) values (90, 90, 8);
insert into book2tag (id, book_id, tag_id) values (91, 91, 8);
insert into book2tag (id, book_id, tag_id) values (92, 92, 16);
insert into book2tag (id, book_id, tag_id) values (93, 93, 14);
insert into book2tag (id, book_id, tag_id) values (94, 94, 12);
insert into book2tag (id, book_id, tag_id) values (95, 95, 14);
insert into book2tag (id, book_id, tag_id) values (96, 96, 20);
insert into book2tag (id, book_id, tag_id) values (97, 97, 8);
insert into book2tag (id, book_id, tag_id) values (98, 98, 13);
insert into book2tag (id, book_id, tag_id) values (99, 99, 2);
insert into book2tag (id, book_id, tag_id) values (100, 100, 4);


insert into balance_transaction (id, book_id, user_id, description, time, value) values (1, 85, 12, 'lobortis convallis', '2022-11-08 19:51:48', 878);
insert into balance_transaction (id, book_id, user_id, description, time, value) values (2, 97, 1, 'luctus cum', '2022-05-14 16:03:10', 565);
insert into balance_transaction (id, book_id, user_id, description, time, value) values (3, 26, 3, 'neque duis', '2022-02-24 00:18:55', 597);
insert into balance_transaction (id, book_id, user_id, description, time, value) values (4, 68, 14, 'nulla tempus', '2022-05-16 14:53:30', 884);
insert into balance_transaction (id, book_id, user_id, description, time, value) values (5, 39, 8, 'nulla ac', '2022-03-30 21:38:47', 933);
insert into balance_transaction (id, book_id, user_id, description, time, value) values (6, 27, 14, 'vel nulla', '2022-11-19 01:34:27', 725);
insert into balance_transaction (id, book_id, user_id, description, time, value) values (7, 27, 15, 'vel ipsum', '2022-07-17 00:56:23', 935);
insert into balance_transaction (id, book_id, user_id, description, time, value) values (8, 72, 19, 'varius nulla', '2022-08-06 05:07:09', 816);
insert into balance_transaction (id, book_id, user_id, description, time, value) values (9, 66, 20, 'eu nibh', '2022-08-27 05:03:43', 907);
insert into balance_transaction (id, book_id, user_id, description, time, value) values (10, 08, 4, 'ullamcorper augue', '2022-01-15 10:58:17', 527);
insert into balance_transaction (id, book_id, user_id, description, time, value) values (11, 18, 19, 'ac nibh', '2022-03-19 12:13:51', 802);
insert into balance_transaction (id, book_id, user_id, description, time, value) values (12, 20, 3, 'nisl aenean', '2022-03-08 02:54:44', 913);
insert into balance_transaction (id, book_id, user_id, description, time, value) values (13, 24, 14, 'morbi vestibulum', '2022-06-14 06:24:42', 912);
insert into balance_transaction (id, book_id, user_id, description, time, value) values (14, 66, 6, 'nisl aenean', '2022-08-08 20:28:00', 865);
insert into balance_transaction (id, book_id, user_id, description, time, value) values (15, 46, 6, 'blandit mi', '2022-02-13 10:33:23', 768);
insert into balance_transaction (id, book_id, user_id, description, time, value) values (16, 69, 14, 'platea dictumst', '2022-10-01 16:35:59', 669);
insert into balance_transaction (id, book_id, user_id, description, time, value) values (17, 71, 13, 'erat fermentum', '2022-06-08 09:54:27', 701);
insert into balance_transaction (id, book_id, user_id, description, time, value) values (18, 9, 15, 'pulvinar sed', '2022-10-23 19:55:49', 608);
insert into balance_transaction (id, book_id, user_id, description, time, value) values (19, 34, 10, 'non pretium', '2022-11-11 10:40:57', 992);
insert into balance_transaction (id, book_id, user_id, description, time, value) values (20, 92, 9, 'hendrerit at', '2022-03-23 12:58:16', 644);
insert into balance_transaction (id, book_id, user_id, description, time, value) values (21, 77, 17, 'suspendisse potenti', '2022-10-08 21:02:45', 826);


insert into book2author (id, author_id, book_id) values (1, 1, 1);
insert into book2author (id, author_id, book_id) values (2, 7, 2);
insert into book2author (id, author_id, book_id) values (3, 4, 3);
insert into book2author (id, author_id, book_id) values (4, 16, 4);
insert into book2author (id, author_id, book_id) values (5, 7, 5);
insert into book2author (id, author_id, book_id) values (6, 2, 6);
insert into book2author (id, author_id, book_id) values (7, 14, 7);
insert into book2author (id, author_id, book_id) values (8, 10, 8);
insert into book2author (id, author_id, book_id) values (9, 3, 9);
insert into book2author (id, author_id, book_id) values (10, 5, 10);
insert into book2author (id, author_id, book_id) values (11, 5, 11);
insert into book2author (id, author_id, book_id) values (12, 5, 12);
insert into book2author (id, author_id, book_id) values (13, 5, 13);
insert into book2author (id, author_id, book_id) values (14, 5, 14);
insert into book2author (id, author_id, book_id) values (15, 5, 15);
insert into book2author (id, author_id, book_id) values (16, 5, 16);
insert into book2author (id, author_id, book_id) values (17, 5, 17);
insert into book2author (id, author_id, book_id) values (18, 5, 18);
insert into book2author (id, author_id, book_id) values (19, 5, 19);
insert into book2author (id, author_id, book_id) values (20, 12, 20);
insert into book2author (id, author_id, book_id) values (21, 12, 21);
insert into book2author (id, author_id, book_id) values (22, 14, 22);
insert into book2author (id, author_id, book_id) values (23, 3, 23);
insert into book2author (id, author_id, book_id) values (24, 20, 24);
insert into book2author (id, author_id, book_id) values (25, 12, 25);
insert into book2author (id, author_id, book_id) values (26, 12, 26);
insert into book2author (id, author_id, book_id) values (27, 12, 27);
insert into book2author (id, author_id, book_id) values (28, 12, 28);
insert into book2author (id, author_id, book_id) values (29, 12, 29);
insert into book2author (id, author_id, book_id) values (30, 12, 30);
insert into book2author (id, author_id, book_id) values (31, 12, 31);
insert into book2author (id, author_id, book_id) values (32, 12, 32);
insert into book2author (id, author_id, book_id) values (33, 12, 33);
insert into book2author (id, author_id, book_id) values (34, 18, 34);
insert into book2author (id, author_id, book_id) values (35, 18, 35);
insert into book2author (id, author_id, book_id) values (36, 18, 36);
insert into book2author (id, author_id, book_id) values (37, 18, 37);
insert into book2author (id, author_id, book_id) values (38, 18, 38);
insert into book2author (id, author_id, book_id) values (39, 18, 39);
insert into book2author (id, author_id, book_id) values (40, 18, 40);
insert into book2author (id, author_id, book_id) values (41, 18, 41);
insert into book2author (id, author_id, book_id) values (42, 18, 42);
insert into book2author (id, author_id, book_id) values (43, 17, 43);
insert into book2author (id, author_id, book_id) values (44, 17, 44);
insert into book2author (id, author_id, book_id) values (45, 17, 45);
insert into book2author (id, author_id, book_id) values (46, 17, 46);
insert into book2author (id, author_id, book_id) values (47, 17, 47);
insert into book2author (id, author_id, book_id) values (48, 17, 48);
insert into book2author (id, author_id, book_id) values (49, 17, 49);
insert into book2author (id, author_id, book_id) values (50, 17, 50);
insert into book2author (id, author_id, book_id) values (51, 17, 51);
insert into book2author (id, author_id, book_id) values (52, 12, 52);
insert into book2author (id, author_id, book_id) values (53, 12, 53);
insert into book2author (id, author_id, book_id) values (54, 20, 54);
insert into book2author (id, author_id, book_id) values (55, 20, 55);
insert into book2author (id, author_id, book_id) values (56, 20, 56);
insert into book2author (id, author_id, book_id) values (57, 20, 57);
insert into book2author (id, author_id, book_id) values (58, 16, 58);
insert into book2author (id, author_id, book_id) values (59, 16, 59);
insert into book2author (id, author_id, book_id) values (60, 16, 60);
insert into book2author (id, author_id, book_id) values (61, 16, 61);
insert into book2author (id, author_id, book_id) values (62, 15, 62);
insert into book2author (id, author_id, book_id) values (63, 16, 63);
insert into book2author (id, author_id, book_id) values (64, 10, 64);
insert into book2author (id, author_id, book_id) values (65, 13, 65);
insert into book2author (id, author_id, book_id) values (66, 16, 66);
insert into book2author (id, author_id, book_id) values (67, 6, 67);
insert into book2author (id, author_id, book_id) values (68, 14, 68);
insert into book2author (id, author_id, book_id) values (69, 1, 69);
insert into book2author (id, author_id, book_id) values (70, 10, 70);
insert into book2author (id, author_id, book_id) values (71, 19, 71);
insert into book2author (id, author_id, book_id) values (72, 13, 72);
insert into book2author (id, author_id, book_id) values (73, 12, 73);
insert into book2author (id, author_id, book_id) values (74, 15, 74);
insert into book2author (id, author_id, book_id) values (75, 8, 75);
insert into book2author (id, author_id, book_id) values (76, 14, 76);
insert into book2author (id, author_id, book_id) values (77, 12, 77);
insert into book2author (id, author_id, book_id) values (78, 2, 78);
insert into book2author (id, author_id, book_id) values (79, 14, 79);
insert into book2author (id, author_id, book_id) values (80, 7, 80);
insert into book2author (id, author_id, book_id) values (81, 19, 81);
insert into book2author (id, author_id, book_id) values (82, 8, 82);
insert into book2author (id, author_id, book_id) values (83, 12, 83);
insert into book2author (id, author_id, book_id) values (84, 14, 84);
insert into book2author (id, author_id, book_id) values (85, 12, 85);
insert into book2author (id, author_id, book_id) values (86, 19, 86);
insert into book2author (id, author_id, book_id) values (87, 2, 87);
insert into book2author (id, author_id, book_id) values (88, 17, 88);
insert into book2author (id, author_id, book_id) values (89, 20, 89);
insert into book2author (id, author_id, book_id) values (90, 18, 90);
insert into book2author (id, author_id, book_id) values (91, 8, 91);
insert into book2author (id, author_id, book_id) values (92, 3, 92);
insert into book2author (id, author_id, book_id) values (93, 20, 93);
insert into book2author (id, author_id, book_id) values (94, 8, 94);
insert into book2author (id, author_id, book_id) values (95, 5, 95);
insert into book2author (id, author_id, book_id) values (96, 2, 96);
insert into book2author (id, author_id, book_id) values (97, 15, 97);
insert into book2author (id, author_id, book_id) values (98, 11, 98);
insert into book2author (id, author_id, book_id) values (99, 12, 99);
insert into book2author (id, author_id, book_id) values (100, 7, 100);


insert into book2genre (id, book_id, genre_id) values (1, 1, 14);
insert into book2genre (id, book_id, genre_id) values (2, 2, 6);
insert into book2genre (id, book_id, genre_id) values (3, 3, 13);
insert into book2genre (id, book_id, genre_id) values (4, 4, 8);
insert into book2genre (id, book_id, genre_id) values (5, 5, 2);
insert into book2genre (id, book_id, genre_id) values (6, 6, 12);
insert into book2genre (id, book_id, genre_id) values (7, 7, 14);
insert into book2genre (id, book_id, genre_id) values (8, 8, 20);
insert into book2genre (id, book_id, genre_id) values (9, 9, 10);
insert into book2genre (id, book_id, genre_id) values (10, 10, 16);
insert into book2genre (id, book_id, genre_id) values (11, 11, 5);
insert into book2genre (id, book_id, genre_id) values (12, 12, 7);
insert into book2genre (id, book_id, genre_id) values (13, 13, 12);
insert into book2genre (id, book_id, genre_id) values (14, 14, 17);
insert into book2genre (id, book_id, genre_id) values (15, 15, 11);
insert into book2genre (id, book_id, genre_id) values (16, 16, 9);
insert into book2genre (id, book_id, genre_id) values (17, 17, 16);
insert into book2genre (id, book_id, genre_id) values (18, 18, 20);
insert into book2genre (id, book_id, genre_id) values (19, 19, 6);
insert into book2genre (id, book_id, genre_id) values (20, 20, 4);
insert into book2genre (id, book_id, genre_id) values (21, 21, 17);


insert into book2user (id, book_id, time, type_id, user_id) values (1, 8, '2022-12-06 16:45:25', 1, 11);
insert into book2user (id, book_id, time, type_id, user_id) values (2, 4, '2022-01-29 15:02:02', 1, 17);
insert into book2user (id, book_id, time, type_id, user_id) values (3, 6, '2022-09-05 03:46:28', 1, 11);
insert into book2user (id, book_id, time, type_id, user_id) values (4, 15, '2022-08-18 17:57:59', 2, 19);
insert into book2user (id, book_id, time, type_id, user_id) values (5, 11, '2022-02-10 03:40:46', 1, 16);
insert into book2user (id, book_id, time, type_id, user_id) values (6, 1, '2022-02-16 07:35:02', 1, 8);
insert into book2user (id, book_id, time, type_id, user_id) values (7, 5, '2022-01-08 09:01:13', 2, 9);
insert into book2user (id, book_id, time, type_id, user_id) values (8, 19, '2022-08-27 20:09:36', 2, 9);
insert into book2user (id, book_id, time, type_id, user_id) values (9, 17, '2022-02-20 04:54:17', 2, 3);
insert into book2user (id, book_id, time, type_id, user_id) values (10, 15, '2022-04-07 02:14:53', 2, 4);
insert into book2user (id, book_id, time, type_id, user_id) values (11, 17, '2022-10-15 07:39:22', 2, 5);
insert into book2user (id, book_id, time, type_id, user_id) values (12, 13, '2022-10-09 03:31:59', 2, 1);
insert into book2user (id, book_id, time, type_id, user_id) values (13, 11, '2022-03-06 18:03:15', 2, 17);
insert into book2user (id, book_id, time, type_id, user_id) values (14, 6, '2022-07-21 04:43:22', 1, 6);
insert into book2user (id, book_id, time, type_id, user_id) values (15, 13, '2022-11-18 16:26:16', 1, 12);
insert into book2user (id, book_id, time, type_id, user_id) values (16, 10, '2022-07-18 10:19:18', 2, 2);
insert into book2user (id, book_id, time, type_id, user_id) values (17, 16, '2022-05-26 23:56:21', 1, 6);
insert into book2user (id, book_id, time, type_id, user_id) values (18, 2, '2022-05-09 17:38:08', 2, 4);
insert into book2user (id, book_id, time, type_id, user_id) values (19, 8, '2022-06-20 04:32:24', 1, 17);
insert into book2user (id, book_id, time, type_id, user_id) values (20, 13, '2022-05-02 11:43:00', 1, 2);
insert into book2user (id, book_id, time, type_id, user_id) values (21, 7, '2022-11-04 22:36:24', 2, 18);
insert into book2user (id, book_id, time, type_id, user_id) values (22, 12, '2022-10-31 14:53:04', 2, 5);
insert into book2user (id, book_id, time, type_id, user_id) values (23, 7, '2022-12-18 23:12:14', 1, 4);
insert into book2user (id, book_id, time, type_id, user_id) values (24, 9, '2022-09-10 19:18:45', 2, 14);
insert into book2user (id, book_id, time, type_id, user_id) values (25, 15, '2022-01-17 15:32:22', 1, 12);
insert into book2user (id, book_id, time, type_id, user_id) values (26, 2, '2022-10-08 22:02:21', 2, 5);
insert into book2user (id, book_id, time, type_id, user_id) values (27, 12, '2022-08-16 02:53:23', 2, 6);
insert into book2user (id, book_id, time, type_id, user_id) values (28, 3, '2022-09-16 10:49:40', 2, 6);
insert into book2user (id, book_id, time, type_id, user_id) values (29, 9, '2022-12-09 22:27:54', 1, 11);
insert into book2user (id, book_id, time, type_id, user_id) values (30, 9, '2022-09-27 18:49:24', 2, 18);
insert into book2user (id, book_id, time, type_id, user_id) values (31, 15, '2022-05-06 19:45:03', 1, 1);
insert into book2user (id, book_id, time, type_id, user_id) values (32, 11, '2022-02-12 05:03:00', 1, 10);
insert into book2user (id, book_id, time, type_id, user_id) values (33, 4, '2022-05-10 20:01:52', 1, 13);
insert into book2user (id, book_id, time, type_id, user_id) values (34, 2, '2022-05-15 08:03:48', 2, 9);
insert into book2user (id, book_id, time, type_id, user_id) values (35, 15, '2022-03-08 11:44:41', 2, 20);
insert into book2user (id, book_id, time, type_id, user_id) values (36, 16, '2022-09-16 23:53:17', 2, 15);
insert into book2user (id, book_id, time, type_id, user_id) values (37, 2, '2022-09-25 10:10:50', 2, 8);
insert into book2user (id, book_id, time, type_id, user_id) values (38, 17, '2022-07-13 18:29:51', 1, 20);
insert into book2user (id, book_id, time, type_id, user_id) values (39, 11, '2022-11-23 06:02:17', 2, 8);
insert into book2user (id, book_id, time, type_id, user_id) values (40, 19, '2022-10-21 16:27:48', 2, 5);
insert into book2user (id, book_id, time, type_id, user_id) values (41, 15, '2022-10-18 21:01:32', 1, 16);
insert into book2user (id, book_id, time, type_id, user_id) values (42, 4, '2022-07-24 03:22:00', 2, 19);
insert into book2user (id, book_id, time, type_id, user_id) values (43, 19, '2022-04-04 12:07:48', 2, 6);
insert into book2user (id, book_id, time, type_id, user_id) values (44, 7, '2022-07-05 00:52:29', 2, 5);
insert into book2user (id, book_id, time, type_id, user_id) values (45, 11, '2022-02-24 05:36:26', 2, 9);
insert into book2user (id, book_id, time, type_id, user_id) values (46, 14, '2022-02-20 09:20:40', 1, 4);
insert into book2user (id, book_id, time, type_id, user_id) values (47, 7, '2022-10-07 23:17:23', 2, 15);
insert into book2user (id, book_id, time, type_id, user_id) values (48, 9, '2022-08-08 08:08:35', 2, 7);
insert into book2user (id, book_id, time, type_id, user_id) values (49, 20, '2022-09-23 15:19:08', 1, 14);
insert into book2user (id, book_id, time, type_id, user_id) values (50, 14, '2022-10-16 06:37:25', 1, 17);
insert into book2user (id, book_id, time, type_id, user_id) values (51, 13, '2022-09-15 08:18:28', 1, 7);
insert into book2user (id, book_id, time, type_id, user_id) values (52, 1, '2022-05-18 00:24:14', 2, 13);
insert into book2user (id, book_id, time, type_id, user_id) values (53, 18, '2022-03-05 06:33:01', 1, 6);
insert into book2user (id, book_id, time, type_id, user_id) values (54, 14, '2022-10-03 02:51:47', 2, 14);
insert into book2user (id, book_id, time, type_id, user_id) values (55, 3, '2022-02-02 19:23:24', 1, 17);
insert into book2user (id, book_id, time, type_id, user_id) values (56, 16, '2022-09-13 07:06:24', 2, 10);
insert into book2user (id, book_id, time, type_id, user_id) values (57, 4, '2022-08-31 15:36:44', 1, 11);
insert into book2user (id, book_id, time, type_id, user_id) values (58, 14, '2022-10-19 09:52:52', 2, 11);
insert into book2user (id, book_id, time, type_id, user_id) values (59, 18, '2022-01-10 14:01:17', 1, 5);
insert into book2user (id, book_id, time, type_id, user_id) values (60, 13, '2022-05-03 08:25:06', 2, 14);
insert into book2user (id, book_id, time, type_id, user_id) values (61, 1, '2022-10-31 00:42:08', 2, 16);
insert into book2user (id, book_id, time, type_id, user_id) values (62, 14, '2022-11-28 01:07:18', 2, 20);
insert into book2user (id, book_id, time, type_id, user_id) values (63, 4, '2022-10-01 00:13:17', 1, 18);
insert into book2user (id, book_id, time, type_id, user_id) values (64, 14, '2022-04-30 15:40:41', 1, 9);
insert into book2user (id, book_id, time, type_id, user_id) values (65, 6, '2022-04-19 11:19:25', 1, 10);
insert into book2user (id, book_id, time, type_id, user_id) values (66, 17, '2022-09-24 13:30:00', 2, 7);
insert into book2user (id, book_id, time, type_id, user_id) values (67, 18, '2022-02-19 12:41:51', 2, 4);
insert into book2user (id, book_id, time, type_id, user_id) values (68, 13, '2022-10-14 07:19:39', 2, 8);
insert into book2user (id, book_id, time, type_id, user_id) values (69, 10, '2022-08-13 06:29:59', 2, 9);
insert into book2user (id, book_id, time, type_id, user_id) values (70, 14, '2022-12-19 08:22:06', 1, 18);
insert into book2user (id, book_id, time, type_id, user_id) values (71, 10, '2022-03-01 16:00:29', 2, 20);
insert into book2user (id, book_id, time, type_id, user_id) values (72, 12, '2022-04-08 08:20:19', 1, 3);
insert into book2user (id, book_id, time, type_id, user_id) values (73, 10, '2021-12-29 21:17:49', 1, 4);
insert into book2user (id, book_id, time, type_id, user_id) values (74, 2, '2022-02-14 15:37:59', 1, 7);
insert into book2user (id, book_id, time, type_id, user_id) values (75, 7, '2022-05-21 07:20:55', 1, 13);
insert into book2user (id, book_id, time, type_id, user_id) values (76, 16, '2022-04-27 17:39:25', 1, 8);
insert into book2user (id, book_id, time, type_id, user_id) values (77, 3, '2022-09-21 03:49:47', 2, 1);
insert into book2user (id, book_id, time, type_id, user_id) values (78, 2, '2022-07-06 18:02:17', 1, 3);
insert into book2user (id, book_id, time, type_id, user_id) values (79, 15, '2022-12-07 22:42:21', 1, 15);
insert into book2user (id, book_id, time, type_id, user_id) values (80, 6, '2022-04-14 08:00:38', 1, 19);
insert into book2user (id, book_id, time, type_id, user_id) values (81, 2, '2022-08-06 00:29:55', 1, 1);
insert into book2user (id, book_id, time, type_id, user_id) values (82, 5, '2022-11-30 02:30:20', 2, 17);
insert into book2user (id, book_id, time, type_id, user_id) values (83, 8, '2022-05-21 06:24:06', 2, 20);
insert into book2user (id, book_id, time, type_id, user_id) values (84, 17, '2022-05-13 23:50:26', 2, 1);
insert into book2user (id, book_id, time, type_id, user_id) values (85, 7, '2022-05-08 02:45:41', 1, 14);
insert into book2user (id, book_id, time, type_id, user_id) values (86, 19, '2022-10-22 06:34:56', 3, 8);
insert into book2user (id, book_id, time, type_id, user_id) values (87, 18, '2021-12-27 17:23:06', 3, 19);
insert into book2user (id, book_id, time, type_id, user_id) values (88, 14, '2022-05-13 16:21:30', 3, 5);
insert into book2user (id, book_id, time, type_id, user_id) values (89, 12, '2022-03-28 18:45:54', 3, 2);
insert into book2user (id, book_id, time, type_id, user_id) values (90, 16, '2022-04-16 00:18:54', 3, 3);
insert into book2user (id, book_id, time, type_id, user_id) values (91, 10, '2022-12-18 15:08:20', 3, 10);
insert into book2user (id, book_id, time, type_id, user_id) values (92, 13, '2022-01-20 01:49:22', 3, 4);
insert into book2user (id, book_id, time, type_id, user_id) values (93, 4, '2022-08-01 20:49:37', 3, 10);
insert into book2user (id, book_id, time, type_id, user_id) values (94, 19, '2022-01-27 07:00:10', 3, 7);
insert into book2user (id, book_id, time, type_id, user_id) values (95, 6, '2022-04-27 07:04:29', 3, 7);
insert into book2user (id, book_id, time, type_id, user_id) values (96, 17, '2022-09-14 00:54:55', 3, 4);
insert into book2user (id, book_id, time, type_id, user_id) values (97, 8, '2022-07-22 22:16:15', 3, 10);
insert into book2user (id, book_id, time, type_id, user_id) values (98, 4, '2022-10-10 07:26:28', 3, 4);
insert into book2user (id, book_id, time, type_id, user_id) values (99, 11, '2022-09-25 14:40:42', 3, 14);
insert into book2user (id, book_id, time, type_id, user_id) values (100, 14, '2022-12-08 23:17:46', 3, 16);


insert into book2user_type (id, code, name) values (1, 1, 'Куплена');
insert into book2user_type (id, code, name) values (2, 2, 'В коризне');
insert into book2user_type (id, code, name) values (3, 3, 'Отложена');


insert into book_file (id, hash, path, type_id) values (1, '97cdf444fa1e2212c7d27226ed9d0b87c94d01a1', 'EgetNuncDonec.avi', 1);
insert into book_file (id, hash, path, type_id) values (2, 'e184b22d729d9cc9e5f2d1940ae04f60740947b2', 'NullaPede.mp3', 2);
insert into book_file (id, hash, path, type_id) values (3, '6bbeff03f0940923ef657b83fae2d8aa46b3eaeb', 'DiamNeque.ppt', 1);
insert into book_file (id, hash, path, type_id) values (4, '6fa8ad0a8f4e97e0ee1453be7632c95221f46cd3', 'TurpisElementumLigula.ppt', 2);
insert into book_file (id, hash, path, type_id) values (5, 'c8c2d3ba2f93cc9f501fb563016d9937656bfc87', 'Pulvinar.mpeg', 2);
insert into book_file (id, hash, path, type_id) values (6, '80564da4e2f1e4a5c57cb924ad3c80637d0b4cbe', 'Volutpat.gif', 1);
insert into book_file (id, hash, path, type_id) values (7, 'a1509921456c073ef198cf9dfcb797fc5f189870', 'SitAmetEros.xls', 1);
insert into book_file (id, hash, path, type_id) values (8, '3c474fa5c6f9f0641dd561fb6a879fdd9ccc1e9e', 'HabitassePlateaDictumst.mpeg', 1);
insert into book_file (id, hash, path, type_id) values (9, '9f82307efff843b8b533685c41019543a864580a', 'Morbi.tiff', 1);
insert into book_file (id, hash, path, type_id) values (10, 'e499add72fc3fedf00eaeefdff3c155ccdbb01a8', 'RisusSemper.mp3', 2);


insert into book_file_type (id, description, name) values (1, 'eleifend luctus', 'pdf');
insert into book_file_type (id, description, name) values (2, 'amet eros', 'epub');
insert into book_file_type (id, description, name) values (3, 'augue vel', 'fb2');


insert into document (id, slug, sort_index, text, title) values (1, 'feugiat', 11, 'Nam ultrices, libero non mattis pulvinar, nulla pede ullamcorper augue, a suscipit nulla elit ac nulla.', 'volutpat');
insert into document (id, slug, sort_index, text, title) values (2, 'sit', 8, 'Nunc nisl.', 'montes nascetur');
insert into document (id, slug, sort_index, text, title) values (3, 'dapibus', 9, 'Vivamus vel nulla eget eros elementum pellentesque.', 'erat');
insert into document (id, slug, sort_index, text, title) values (4, 'dolor', 7, 'Cras in purus eu magna vulputate luctus.', 'justo');
insert into document (id, slug, sort_index, text, title) values (5, 'non', 20, 'Proin risus.', 'facilisi');
insert into document (id, slug, sort_index, text, title) values (6, 'purus', 13, 'Morbi sem mauris, laoreet ut, rhoncus aliquet, pulvinar sed, nisl.', 'porttitor');
insert into document (id, slug, sort_index, text, title) values (7, 'nulla', 16, 'Phasellus sit amet erat.', 'vulputate');
insert into document (id, slug, sort_index, text, title) values (8, 'rhoncus', 16, 'Vivamus vel nulla eget eros elementum pellentesque.', 'ut');
insert into document (id, slug, sort_index, text, title) values (9, 'lacinia', 18, 'Nunc purus.', 'risus praesent');
insert into document (id, slug, sort_index, text, title) values (10, 'leo', 6, 'Quisque porta volutpat erat.', 'vulputate justo');
insert into document (id, slug, sort_index, text, title) values (11, 'odio', 12, 'Donec quis orci eget orci vehicula condimentum.', 'maecenas');
insert into document (id, slug, sort_index, text, title) values (12, 'dictumst', 1, 'Donec vitae nisi.', 'vel');
insert into document (id, slug, sort_index, text, title) values (13, 'blandit', 20, 'Ut at dolor quis odio consequat varius.', 'nullam orci');
insert into document (id, slug, sort_index, text, title) values (14, 'dolor', 20, 'Sed accumsan felis.', 'sed');
insert into document (id, slug, sort_index, text, title) values (15, 'nulla', 21, 'Vivamus metus arcu, adipiscing molestie, hendrerit at, vulputate vitae, nisl.', 'lacinia nisi');
insert into document (id, slug, sort_index, text, title) values (16, 'ipsum', 17, 'Aliquam non mauris.', 'sed justo');
insert into document (id, slug, sort_index, text, title) values (17, 'habitasse', 8, 'Suspendisse potenti.', 'id mauris');
insert into document (id, slug, sort_index, text, title) values (18, 'semper', 18, 'Nam congue, risus semper porta volutpat, quam pede lobortis ligula, sit amet eleifend pede libero quis orci.', 'vel augue');
insert into document (id, slug, sort_index, text, title) values (19, 'odio', 19, 'Phasellus in felis.', 'tellus semper');
insert into document (id, slug, sort_index, text, title) values (20, 'vel', 2, 'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Mauris viverra diam vitae quam.', 'orci');
insert into document (id, slug, sort_index, text, title) values (21, 'mauris', 6, 'Quisque arcu libero, rutrum ac, lobortis vel, dapibus at, diam.', 'fermentum justo');


insert into faq (id, answer, question, sort_index) values (1, 'vivamus', 'at', 2);
insert into faq (id, answer, question, sort_index) values (2, 'ac tellus', 'vestibulum ante', 1);
insert into faq (id, answer, question, sort_index) values (3, 'laoreet ut', 'sollicitudin', 2);
insert into faq (id, answer, question, sort_index) values (4, 'gravida nisi', 'ipsum', 1);
insert into faq (id, answer, question, sort_index) values (5, 'justo sit', 'elementum', 1);
insert into faq (id, answer, question, sort_index) values (6, 'nisl', 'dictumst', 2);
insert into faq (id, answer, question, sort_index) values (7, 'sed', 'vestibulum', 1);
insert into faq (id, answer, question, sort_index) values (8, 'ante', 'in', 2);
insert into faq (id, answer, question, sort_index) values (9, 'dapibus nulla', 'nam ultrices', 2);
insert into faq (id, answer, question, sort_index) values (10, 'lectus aliquam', 'convallis', 1);
insert into faq (id, answer, question, sort_index) values (11, 'volutpat', 'aliquam', 1);
insert into faq (id, answer, question, sort_index) values (12, 'vestibulum', 'ipsum', 2);
insert into faq (id, answer, question, sort_index) values (13, 'ac leo', 'porttitor id', 2);
insert into faq (id, answer, question, sort_index) values (14, 'fermentum', 'odio consequat', 1);
insert into faq (id, answer, question, sort_index) values (15, 'tortor sollicitudin', 'porttitor id', 2);
insert into faq (id, answer, question, sort_index) values (16, 'congue', 'venenatis', 2);
insert into faq (id, answer, question, sort_index) values (17, 'pretium', 'hac', 2);
insert into faq (id, answer, question, sort_index) values (18, 'sodales', 'proin risus', 1);
insert into faq (id, answer, question, sort_index) values (19, 'vitae', 'ultrices posuere', 1);
insert into faq (id, answer, question, sort_index) values (20, 'quam', 'nunc', 2);
insert into faq (id, answer, question, sort_index) values (21, 'ante nulla', 'at velit', 2);


insert into file_download (id, book_id, count, user_id) values (1, 4, 8, 1);
insert into file_download (id, book_id, count, user_id) values (2, 18, 17, 7);
insert into file_download (id, book_id, count, user_id) values (3, 21, 5, 9);
insert into file_download (id, book_id, count, user_id) values (4, 16, 14, 16);
insert into file_download (id, book_id, count, user_id) values (5, 9, 12, 15);
insert into file_download (id, book_id, count, user_id) values (6, 4, 1, 14);
insert into file_download (id, book_id, count, user_id) values (7, 7, 1, 15);
insert into file_download (id, book_id, count, user_id) values (8, 17, 6, 11);
insert into file_download (id, book_id, count, user_id) values (9, 16, 17, 20);
insert into file_download (id, book_id, count, user_id) values (10, 2, 18, 10);
insert into file_download (id, book_id, count, user_id) values (11, 7, 9, 19);
insert into file_download (id, book_id, count, user_id) values (12, 13, 20, 4);
insert into file_download (id, book_id, count, user_id) values (13, 1, 21, 2);
insert into file_download (id, book_id, count, user_id) values (14, 14, 19, 1);
insert into file_download (id, book_id, count, user_id) values (15, 9, 1, 7);
insert into file_download (id, book_id, count, user_id) values (16, 21, 13, 15);
insert into file_download (id, book_id, count, user_id) values (17, 2, 9, 9);
insert into file_download (id, book_id, count, user_id) values (18, 13, 20, 1);
insert into file_download (id, book_id, count, user_id) values (19, 7, 17, 19);
insert into file_download (id, book_id, count, user_id) values (20, 14, 10, 6);
insert into file_download (id, book_id, count, user_id) values (21, 20, 17, 6);


insert into message (id, email, name, subject, text, time, user_id) values (1, 'criordan0@alexa.com', 'Costanza Riordan', 'nulla suspendisse', 'Pellentesque ultrices mattis odio.', '2022-06-04 16:29:33', 10);
insert into message (id, email, name, subject, text, time, user_id) values (2, 'cpinnegar1@marriott.com', 'Coop Pinnegar', 'aliquam lacus', 'Fusce congue, diam id ornare imperdiet, sapien urna pretium nisl, ut volutpat sapien arcu sed augue. Aliquam erat volutpat.', '2022-06-26 13:33:23', 10);
insert into message (id, email, name, subject, text, time, user_id) values (3, 'rhilland2@nymag.com', 'Ramonda Hilland', 'id nulla', 'Proin at turpis a pede posuere nonummy. Integer non velit.', '2022-09-23 10:47:56', 3);
insert into message (id, email, name, subject, text, time, user_id) values (4, 'tbrowse3@goo.ne.jp', 'Tarrah Browse', 'enim lorem', 'Morbi sem mauris, laoreet ut, rhoncus aliquet, pulvinar sed, nisl. Nunc rhoncus dui vel sem.', '2022-07-20 05:06:18', 4);
insert into message (id, email, name, subject, text, time, user_id) values (5, 'rkillingsworth4@dedecms.com', 'Rance Killingsworth', 'condimentum', 'Integer ac leo. Pellentesque ultrices mattis odio.', '2022-03-05 13:08:10', 9);
insert into message (id, email, name, subject, text, time, user_id) values (6, 'krecord5@livejournal.com', 'Kathryn Record', 'augue luctus', 'In est risus, auctor sed, tristique in, tempus sit amet, sem.', '2022-02-25 11:51:23', 5);
insert into message (id, email, name, subject, text, time, user_id) values (7, 'mfearns6@harvard.edu', 'Mycah Fearns', 'vel', 'Integer pede justo, lacinia eget, tincidunt eget, tempus vel, pede. Morbi porttitor lorem id ligula.', '2022-07-07 21:01:39', 9);
insert into message (id, email, name, subject, text, time, user_id) values (8, 'hphizakarley7@stanford.edu', 'Hope Phizakarley', 'suspendisse accumsan', 'Mauris enim leo, rhoncus sed, vestibulum sit amet, cursus id, turpis. Integer aliquet, massa id lobortis convallis, tortor risus dapibus augue, vel accumsan tellus nisi eu orci.', '2022-02-05 03:33:47', 4);
insert into message (id, email, name, subject, text, time, user_id) values (9, 'kglendenning8@cnbc.com', 'Keefe Glendenning', 'vel ipsum', 'Nulla suscipit ligula in lacus.', '2022-08-11 09:32:51', 20);
insert into message (id, email, name, subject, text, time, user_id) values (10, 'phanna9@slate.com', 'Paolo Hanna', 'semper interdum', 'Ut tellus. Nulla ut erat id mauris vulputate elementum.', '2022-01-20 02:25:38', 21);
insert into message (id, email, name, subject, text, time, user_id) values (11, 'dtintoa@telegraph.co.uk', 'Donielle Tinto', 'id pretium', 'Suspendisse accumsan tortor quis turpis. Sed ante.', '2022-04-10 14:27:32', 13);
insert into message (id, email, name, subject, text, time, user_id) values (12, 'rpuesb@telegraph.co.uk', 'Ray Pues', 'egestas', 'Nullam porttitor lacus at turpis.', '2022-04-19 13:30:00', 6);
insert into message (id, email, name, subject, text, time, user_id) values (13, 'swennamc@bandcamp.com', 'Shena Wennam', 'morbi sem', 'Morbi ut odio. Cras mi pede, malesuada in, imperdiet et, commodo vulputate, justo.', '2022-08-15 01:05:59', 10);
insert into message (id, email, name, subject, text, time, user_id) values (14, 'joddd@ehow.com', 'Jamal Odd', 'eleifend quam', 'Duis mattis egestas metus. Aenean fermentum.', '2022-03-07 21:49:49', 8);
insert into message (id, email, name, subject, text, time, user_id) values (15, 'cabrahamse@pbs.org', 'Cherilynn Abrahams', 'justo morbi', 'Integer aliquet, massa id lobortis convallis, tortor risus dapibus augue, vel accumsan tellus nisi eu orci. Mauris lacinia sapien quis libero.', '2022-06-28 12:13:47', 10);
insert into message (id, email, name, subject, text, time, user_id) values (16, 'mblumsomf@ed.gov', 'Maighdiln Blumsom', 'in tempor', 'Etiam faucibus cursus urna.', '2022-08-11 15:48:47', 19);
insert into message (id, email, name, subject, text, time, user_id) values (17, 'akalfg@oaic.gov.au', 'Albrecht Kalf', 'facilisi cras', 'Morbi non lectus. Aliquam sit amet diam in magna bibendum imperdiet.', '2022-02-19 10:47:34', 6);
insert into message (id, email, name, subject, text, time, user_id) values (18, 'azorzinh@histats.com', 'Allayne Zorzin', 'nisi volutpat', 'Quisque ut erat.', '2022-04-09 12:11:11', 10);
insert into message (id, email, name, subject, text, time, user_id) values (19, 'ttextoni@europa.eu', 'Towney Texton', 'vitae', 'Sed ante. Vivamus tortor.', '2022-10-01 03:55:25', 14);
insert into message (id, email, name, subject, text, time, user_id) values (20, 'ploynesj@smh.com.au', 'Perice Loynes', 'ut', 'Duis consequat dui nec nisi volutpat eleifend.', '2022-11-16 05:48:35', 1);
insert into message (id, email, name, subject, text, time, user_id) values (21, 'scolegatek@princeton.edu', 'Susanetta Colegate', 'dis parturient', 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit.', '2022-10-21 02:48:26', 4);


insert into user_contact (id, approved, code, code_time, code_trails, contact, type, user_id) values (1, 4, '51146a164219833b64619a981c06bd5a8dc8583b', '2022-02-12 10:52:57', 9, '805-413-3781', 16, 3);
insert into user_contact (id, approved, code, code_time, code_trails, contact, type, user_id) values (2, 13, 'b2f7516a89f4e070ce140f55186bb5371540f79b', '2022-10-13 08:57:19', 18, '632-399-6240', 1, 18);
insert into user_contact (id, approved, code, code_time, code_trails, contact, type, user_id) values (3, 19, '376a29edb374d89843858ce34b2c9dea7017be82', '2022-09-08 14:23:32', 1, '863-335-8311', 10, 7);
insert into user_contact (id, approved, code, code_time, code_trails, contact, type, user_id) values (4, 15, '32e255e99b8cd1cb5e4da42a568b55af7b878ec0', '2022-01-04 07:00:41', 19, '921-231-9729', 19, 6);
insert into user_contact (id, approved, code, code_time, code_trails, contact, type, user_id) values (5, 18, 'f193762ce15c71ec8bbd7ca169d3e11c23263db2', '2022-08-24 15:30:19', 18, '739-702-5824', 8, 6);
insert into user_contact (id, approved, code, code_time, code_trails, contact, type, user_id) values (6, 6, '8c8477612cfbb1e14d62642128e90ed9e6d884ff', '2022-09-30 09:39:17', 16, '660-425-1390', 10, 9);
insert into user_contact (id, approved, code, code_time, code_trails, contact, type, user_id) values (7, 14, '77e82ad7a5922baadb167d69384976670bc853ba', '2022-12-06 15:30:24', 15, '937-900-0925', 14, 10);
insert into user_contact (id, approved, code, code_time, code_trails, contact, type, user_id) values (8, 13, '3e9b6aeab31fde003670f1a897b7a3dccab0984b', '2022-04-27 19:06:05', 12, '487-740-3516', 13, 17);
insert into user_contact (id, approved, code, code_time, code_trails, contact, type, user_id) values (9, 21, '0c96ab7363e100c89fa7e237f714edf35faa73db', '2021-12-25 00:09:13', 5, '221-928-3031', 4, 16);
insert into user_contact (id, approved, code, code_time, code_trails, contact, type, user_id) values (10, 13, 'fe3e703dbc276e8fbf1e608039dfe63734b976a9', '2022-07-15 10:18:52', 6, '504-770-4637', 8, 7);
insert into user_contact (id, approved, code, code_time, code_trails, contact, type, user_id) values (11, 15, 'ccedd151400ac1b7236b708dd3bbaeedac804bea', '2022-07-07 05:02:17', 15, '229-935-1625', 21, 6);
insert into user_contact (id, approved, code, code_time, code_trails, contact, type, user_id) values (12, 6, 'f14bd12795ceb0e0bc3cfafc56d0cd9b4d552687', '2022-07-21 21:30:25', 5, '359-915-7781', 2, 12);
insert into user_contact (id, approved, code, code_time, code_trails, contact, type, user_id) values (13, 18, '0dd4abb40cb4e367f800a21b4ab3b0287b8ac77f', '2022-06-23 18:55:17', 10, '500-830-1184', 3, 7);
insert into user_contact (id, approved, code, code_time, code_trails, contact, type, user_id) values (14, 18, 'fdf89258dffd056648d9deb625475d5d94bfa7c6', '2022-12-20 16:12:55', 17, '919-209-6826', 12, 9);
insert into user_contact (id, approved, code, code_time, code_trails, contact, type, user_id) values (15, 17, 'eb66004fd8b3842689cbe7acd8f524a5efb97262', '2022-06-23 22:27:04', 21, '961-587-9919', 2, 2);
insert into user_contact (id, approved, code, code_time, code_trails, contact, type, user_id) values (16, 16, 'b59551160e8b250f1e077427cf62740ab19e52ca', '2022-01-05 05:51:58', 1, '811-789-4724', 13, 12);
insert into user_contact (id, approved, code, code_time, code_trails, contact, type, user_id) values (17, 19, '1fed7316d90e12d9c6c21d31dad4c7086d3eb0ac', '2022-05-17 00:48:43', 15, '171-716-4316', 12, 5);
insert into user_contact (id, approved, code, code_time, code_trails, contact, type, user_id) values (18, 2, '308c5638113d3571fbfdaafa7dc30da91a507867', '2022-07-21 18:51:42', 7, '218-989-4880', 9, 14);
insert into user_contact (id, approved, code, code_time, code_trails, contact, type, user_id) values (19, 16, '80110a62a49c904a639452fcd8d1bd638ea17565', '2022-10-27 10:41:41', 4, '324-505-7686', 15, 8);
insert into user_contact (id, approved, code, code_time, code_trails, contact, type, user_id) values (20, 10, '04a5032deaf1137157f959e96dfffbc8ce13e8d1', '2022-03-15 14:45:08', 2, '799-966-9464', 18, 9);
insert into user_contact (id, approved, code, code_time, code_trails, contact, type, user_id) values (21, 8, '15be81c3ffa08a4d8e590b72d62b19121f1172b3', '2022-06-02 04:36:31', 19, '533-700-3524', 17, 2);


-- insert into users_balance_transaction (user_entity_id, balance_transaction_id) values (1, 1);
-- insert into users_balance_transaction (user_entity_id, balance_transaction_id) values (2, 2);
-- insert into users_balance_transaction (user_entity_id, balance_transaction_id) values (3, 3);
-- insert into users_balance_transaction (user_entity_id, balance_transaction_id) values (4, 4);
-- insert into users_balance_transaction (user_entity_id, balance_transaction_id) values (5, 5);
-- insert into users_balance_transaction (user_entity_id, balance_transaction_id) values (6, 6);
-- insert into users_balance_transaction (user_entity_id, balance_transaction_id) values (7, 7);
-- insert into users_balance_transaction (user_entity_id, balance_transaction_id) values (8, 8);
-- insert into users_balance_transaction (user_entity_id, balance_transaction_id) values (9, 9);
-- insert into users_balance_transaction (user_entity_id, balance_transaction_id) values (10, 10);
-- insert into users_balance_transaction (user_entity_id, balance_transaction_id) values (11, 11);
-- insert into users_balance_transaction (user_entity_id, balance_transaction_id) values (12, 12);
-- insert into users_balance_transaction (user_entity_id, balance_transaction_id) values (13, 13);
-- insert into users_balance_transaction (user_entity_id, balance_transaction_id) values (14, 14);
-- insert into users_balance_transaction (user_entity_id, balance_transaction_id) values (15, 15);
-- insert into users_balance_transaction (user_entity_id, balance_transaction_id) values (16, 16);
-- insert into users_balance_transaction (user_entity_id, balance_transaction_id) values (17, 17);
-- insert into users_balance_transaction (user_entity_id, balance_transaction_id) values (18, 18);
-- insert into users_balance_transaction (user_entity_id, balance_transaction_id) values (19, 19);
-- insert into users_balance_transaction (user_entity_id, balance_transaction_id) values (20, 20);
-- insert into users_balance_transaction (user_entity_id, balance_transaction_id) values (21, 21);


-- рейтинги книги
insert into book_rating (id, value, book_id) values (1, 1, 21);
insert into book_rating (id, value, book_id) values (2, 1, 21);
insert into book_rating (id, value, book_id) values (3, 1, 21);
insert into book_rating (id, value, book_id) values (4, 1, 21);
insert into book_rating (id, value, book_id) values (5, 1, 21);
insert into book_rating (id, value, book_id) values (6, 1, 21);
insert into book_rating (id, value, book_id) values (7, 1, 21);
insert into book_rating (id, value, book_id) values (8, 1, 21);
insert into book_rating (id, value, book_id) values (9, 1, 21);
insert into book_rating (id, value, book_id) values (10, 1, 21);
insert into book_rating (id, value, book_id) values (11, 5, 21);
insert into book_rating (id, value, book_id) values (12, 5, 21);
insert into book_rating (id, value, book_id) values (13, 5, 21);
insert into book_rating (id, value, book_id) values (14, 5, 21);
insert into book_rating (id, value, book_id) values (15, 5, 21);
insert into book_rating (id, value, book_id) values (16, 5, 21);
insert into book_rating (id, value, book_id) values (17, 5, 21);
insert into book_rating (id, value, book_id) values (18, 5, 21);
insert into book_rating (id, value, book_id) values (19, 5, 21);
insert into book_rating (id, value, book_id) values (20, 5, 21);


-- отзыв на кингу с сылкой на рейтинг
insert into book_review (id, book_id, text, time, user_id, book_rating_id) values (1, 21, 'In blandit ultrices enim.', '2022-09-06 17:58:42', 15, 1);
insert into book_review (id, book_id, text, time, user_id, book_rating_id) values (2, 21, 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit.', '2022-11-16 17:11:47', 8, 2);
insert into book_review (id, book_id, text, time, user_id, book_rating_id) values (3, 21, 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit.', '2021-12-28 12:41:48', 4, 3);
insert into book_review (id, book_id, text, time, user_id, book_rating_id) values (4, 21, 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit.', '2022-05-16 06:57:05', 4, 4);
insert into book_review (id, book_id, text, time, user_id, book_rating_id) values (5, 22, 'Morbi vel lectus in quam fringilla rhoncus.', '2022-05-12 11:56:50', 16, 5);
insert into book_review (id, book_id, text, time, user_id, book_rating_id) values (6, 22, 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit.', '2022-11-14 21:18:44', 14, 6);
insert into book_review (id, book_id, text, time, user_id, book_rating_id) values (7, 22, 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit.', '2022-02-11 06:38:28', 5, 7);
insert into book_review (id, book_id, text, time, user_id, book_rating_id) values (8, 22, 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit.', '2022-02-11 00:13:04', 18, 8);
insert into book_review (id, book_id, text, time, user_id, book_rating_id) values (9, 22, 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit.', '2022-10-10 20:56:45', 14, 9);
insert into book_review (id, book_id, text,  time, user_id, book_rating_id) values (10, 22, 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit.', '2022-07-17 20:11:53', 8, 10);


-- лайк на отзыв, Value должен быть в пределах 1 до 5
insert into book_review_like (id, review_id, time, user_id, value) values (1, 1, '2022-11-18 03:42:54', 23, 9);
insert into book_review_like (id, review_id, time, user_id, value) values (2, 1, '2022-08-13 07:34:15', 90, 10);
insert into book_review_like (id, review_id, time, user_id, value) values (3, 1, '2022-05-06 19:29:25', 39, 12);
insert into book_review_like (id, review_id, time, user_id, value) values (4, 1, '2022-11-23 06:41:39', 54, 5);
insert into book_review_like (id, review_id, time, user_id, value) values (5, 10, '2022-06-27 20:59:47', 59, 2);
insert into book_review_like (id, review_id, time, user_id, value) values (6, 10, '2022-02-20 07:13:30', 45, 17);
insert into book_review_like (id, review_id, time, user_id, value) values (7, 10, '2022-08-29 15:31:08', 77, 1);
insert into book_review_like (id, review_id, time, user_id, value) values (8, 10, '2022-04-30 15:51:10', 40, 6);
insert into book_review_like (id, review_id, time, user_id, value) values (9, 10, '2022-03-09 13:26:43', 19, 1);
insert into book_review_like (id, review_id, time, user_id, value) values (10, 10, '2022-01-29 15:02:13', 57, 20);




-- сделать таблицу с bookRating, и book2Rating, там должно быть поле book_review которое может быть null

-- надо отправляьб с кнопки и рейтинга разные статусы
-- хранить таблицу с рейтингом без текста для плольззователя или в кукм, ограничить value 1-5
-- таблицу с отзывами
-- таблицу с лайками на отзывы - ограничить валуe 0 1 или сделать enum


-- рейтинга книги нет, отправляется по changeBookStatus
-- рейтинг по книге вычисляется по отзывам с тексттом и может быть без отзыва
-- BookReviewLike значение ограничить 0 1 like dislike
-- но зачем хранить строку на лайк когда достаточно в записи отзыва хранить кол-во лайков и дазлайков



-- insert into genre (id, name, parent_id, slug) values (1, 'Fantasy', 0, 'This is slug');
--
-- insert into genre (id, name, parent_id, slug) values (1, 'Fantasy', 0, 'This is slug');
-- insert into book2genre (id, book_id, genre_id) values (1, 1, 1);
-- insert into book2genre (id, book_id, genre_id) values (1, 2, 1);
--
-- insert into users (id, balance, hash, name, reg_time) values (1, 1000, '0hash', 'Joshe', '2022-11-11 15:00:00');
-- insert into users (id, balance, hash, name, reg_time) values (2, 10000, '1hash', 'Timmy', '2022-11-11 15:00:00');
-- insert into book2user (id, book_id, time, type_id, user_id) values (1, 1, '2022-11-11 15:00:00', 1, 1);
-- insert into book2user (id, book_id, time, type_id, user_id) values (2, 2, '2022-11-11 15:00:00', 1, 1);
-- insert into book2user (id, book_id, time, type_id, user_id) values (3, 1, '2022-11-11 15:00:00', 1, 2);
--
-- insert into file_download (id, user_id, book_id, count) values (1, 1, 1, 100);
-- insert into file_download (id, user_id, book_id, count) values (2, 1, 2, 200);
-- insert into file_download (id, user_id, book_id, count) values (3, 2, 2, 300);
--
--
--
--
--
-- insert into book_review (id, book_id, user_id, time, text) values (1, 1, 1, '2022-11-11 15:00:00', 'text');
-- insert into book_review (id, book_id, user_id, time, text) values (2, 1, 1, '2022-11-11 15:00:00', 'text2');
-- insert into book_review (id, book_id, user_id, time, text) values (3, 1, 2, '2022-11-11 15:00:00', 'text3');
--
-- insert into user_contact (id, approved, code, code_time, code_trails, contact, type, user_id) values (1, 0, 'code', '2022-11-11 15:00:00', 0, 'contact', 1, 1);
-- insert into user_contact (id, approved, code, code_time, code_trails, contact, type, user_id) values (2, 0, 'code2', '2022-11-11 15:00:00', 0, 'contact2', 0, 2);
--
-- insert into message (id, email, name, subject, text, time, user_id) values (1, 'email', 'name', 'subject', 'text', '2022-11-11 15:00:00', 1);
-- insert into message (id, email, name, subject, text, time, user_id) values (2, 'email2', 'name2', 'subject2', 'text2', '2022-11-11 15:00:00', 2);
--
-- insert into book_review_like (id, review_id, time, user_id, value) values (1, 1, '2022-11-11 15:00:00', 1, 100);
-- insert into book_review_like (id, review_id, time, user_id, value) values (2, 1, '2022-11-11 15:00:00', 2, 100);
-- insert into book_review_like (id, review_id, time, user_id, value) values (3, 2, '2022-11-11 15:00:00', 2, 100);
