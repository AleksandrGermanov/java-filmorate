# java-filmorate

Filmorate by Aleksandr Germanov

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
и состояния этих запросов (0 - "неопределено", 1 - "принято",
2 - "отклонено"). Первичным ключом является пара пользовательских id. 
Если статус запроса - "принято", двое 
пользователей являются друзьями, а значит эта таблица также
позволяет получить список друзей конкретного пользователя.\
Таблица Genres содержит жанры фильмов под соответствующим id. 
Поскольку несколько жанров могут соответствовать одному фильму, и несколько
фильмов могут соответствовать одному жанру необходима таблица
Film_genres, первичным ключом в которой является пара film_id - genre_id.\
Таблицы Friendship_statuses, Ratings нужны для уменьшения
количества ошибок ввода и являются вспомогательными.

### Примеры запросов
_Получение списка id всех друзей пользователя_
```
SELECT request_exporter_id AS friend_id
FROM friendship_and_requests
WHERE request_importer_id = {target_user_id}
      AND
      friendship_status_id = 1(accepted)
UNION
SELECT request_importer_id AS friend_id
FROM friendship_and_requests
WHERE request_exporter_id = {target_user_id}
      AND friendship_status_id = 1(accepted)
```

_Получение списка id общих друзей 2-х пользователей_
```
SELECT friend_id
FROM (
    SELECT request_exporter_id AS friend_id,
           request_importer_id AS target_id
    FROM friendship_and_requests
    WHERE (
        request_importer_id = {target_user_id}
       OR request_importer_id = {target_user2_id}
       )
       AND friendship_status_id = 1(accepted)
    UNION
    SELECT request_importer_id AS friend_id,
       request_exporter_id AS target_id
    FROM friendship_and_requests
    WHERE (
        request_exporter_id = {target_user_id}
         OR request_exporter_id = {target_user2_id}
        )
         AND
         friendship_status_id = 1(accepted)
)
GROUP BY friend_id
HAVING COUNT(target_id) = 2   
```

_Получение списка id 10ти самых популярных фильмов_
```
SELECT DISTINCT film_id
FROM likes
ORDER BY COUNT(user_id) DESC
LIMIT 10
```

 
