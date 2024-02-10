# java-filmorate

### Сервис оценки и обсуждения фильмов.
Разработка web-приложения с использованием SpringBoot, SpringData (JDBC), H2, SLF4J (Logback).

---


> [!NOTE]<br>
> приложение на Java SE 11<br>
> для запуска приложения необходимо:<br>
>  - склонировать репозиторий
>  - проверить и при необходимости освободить 8080 порт
>  - в терминале с поддержкой Maven и JDK 11 или выше выполнить
     (из папки с клонированным репозиторием)<br>
     `mvn package -Dproject.build.sourceEncoding=UTF-8 -Dproject.reporting.outputEncoding=UTF-8`<br>
     `java -jar ./target/filmorate-0.0.1-SNAPSHOT.jar`
>
> После запуска приложения можно воспользоваться [postman-коллекцией](https://github.com/yandex-praktikum/java-filmorate/blob/add-database/postman/sprint.json) 
    для проверки функциональности приложения
>
> Для пользователей Windows можно запустить файл `deploy.cmd` - скрипт
создаст и запустит jar c проектом (при условии установленного JDK, и если Maven добавлен в PATH).

### Функциональность

- Приложение умеет создавать, изменять, возвращать сущности пользователей, фильмов
- Возвращает список жанров по запросу.
- Умеет принимать Http-запросы (Tomcat).
- Умеет сохранять данные в БД (JDBC-H2).
- Реализована валидация данных (Hibernate).
- Реализовано логирование (Logback).
- Реализована возможность добавления в друзья пользователями друг друга.
- Реализован рейтинг фильмов по лайкам/дизлайкам.


### DB diagram
![DB diagram](/db_diagram.jpg)

Диаграмма базы данных для приложения Filmorate.

В диаграмме присутствуют 2 базовые сущности, описанные 
в таблицах Films и Users. Первичными ключами для этих сущностей
являются их id.\
Таблица Likes отражает отношение пользователей и фильмов.
Первичный ключ здесь составной и представлен в виде пары значений
film_id - user_id. Поскольку данное отношение представляется в 
виде 2-х состояний (Лайк есть(1) или его нет(2)), дополнительные колонки
в таблице не нужны, т.к. наличие или отсутусвие записи отражает
эти отношения.\
Таблица Friendship_and_requests является хранилищем списка запросов и
состояния этих запросов (1 - "неопределено", 2 - "принято",
3 - "отклонено"). Первичным ключом является пара пользовательских id. 
Если статус запроса - "принято", двое 
пользователей являются друзьями, а значит эта таблица также
позволяет получить список друзей конкретного пользователя.\
Таблица Genres содержит жанры фильмов под соответствующим id. 
Поскольку несколько жанров могут соответствовать одному фильму, и несколько
фильмов могут соответствовать одному жанру необходима таблица
Films_genres, первичным ключом в которой является пара film_id - genre_id.\
Таблицы Friendship_statuses, Ratings нужны для уменьшения
количества ошибок ввода и являются вспомогательными.

### Примеры запросов
_Получение пользователя_
```
SELECT u.*, 
req.request_exporter_id AS request_from, 
req.request_importer_id AS request_to, 
req.friendship_status_id AS status_id, 
s.name AS status_name 
FROM users AS u 
LEFT OUTER JOIN friendship_and_requests AS req ON (u.id = req.request_exporter_id 
OR u.id = req.request_importer_id) 
LEFT OUTER JOIN friendship_statuses AS s ON req.friendship_status_id = s.id 
WHERE u.id = ?
```

_Получение списка всех фильмов_
```
SELECT f.*, fg.genre_id, l.user_id,
r.name AS rating_name,
g.name AS genre_name
FROM films AS f
LEFT OUTER JOIN films_genres AS fg ON f.id = fg.film_id
LEFT OUTER JOIN likes AS l ON f.id = l.film_id
LEFT OUTER JOIN ratings AS r ON f.rating_id = r.id
LEFT OUTER JOIN genres AS g ON fg.genre_id = g.id
ORDER BY f.name, f.id  
```

_Получение списка id 10ти самых популярных фильмов_
```
SELECT DISTINCT film_id
FROM likes
ORDER BY COUNT(user_id) DESC
LIMIT 10
```

 
