CREATE TABLE IF NOT EXISTS mpa_rating(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(40) UNIQUE
);

INSERT INTO mpa_rating (name)
	SELECT t.name
	FROM (SELECT 'G' AS name UNION ALL
	      SELECT 'PG' AS name UNION ALL
	      SELECT 'PG-13' AS name UNION ALL
	      SELECT 'R' AS name UNION ALL
	      SELECT 'NC-17' AS name) t
	WHERE NOT EXISTS (SELECT * FROM MPA_RATING);

CREATE TABLE IF NOT EXISTS genres(
id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name varchar(40) NOT NULL UNIQUE
);

INSERT INTO genres (name)
    	SELECT t.name
    	FROM (SELECT 'Комедия' AS name UNION ALL
    	      SELECT 'Драма' AS name UNION ALL
    	      SELECT 'Мультфильм' AS name UNION ALL
    	      SELECT 'Триллер' AS name UNION ALL
    	      SELECT 'Документальный' AS name UNION ALL
    	      SELECT 'Боевик' AS name) t
    	WHERE NOT EXISTS (SELECT * FROM GENRES);

CREATE TABLE IF NOT EXISTS films (
id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name varchar(40) NOT NULL,
date_release date,
description varchar(500),
duration integer,
mpa_id INTEGER REFERENCES mpa_rating (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS film_genres(
    primary key (film_id, genre_id),
    film_id INTEGER REFERENCES films (id) ON DELETE CASCADE,
    genre_id INTEGER REFERENCES genres (id) ON DELETE CASCADE
);

-- USERS --
CREATE TABLE IF NOT EXISTS statuses(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    status varchar(40) UNIQUE
);

INSERT INTO statuses (status)
	SELECT t.status
	FROM (SELECT 'requested' AS status UNION ALL
	      SELECT 'accepted' AS status) t
	WHERE NOT EXISTS (SELECT * FROM statuses);


CREATE TABLE IF NOT EXISTS users (
id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
login varchar(40) NOT NULL,
name varchar(40),
email varchar(40) NOT NULL,
birthday date NOT NULL
);


CREATE TABLE IF NOT EXISTS friends(
    user_id INTEGER NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    friend_id INTEGER NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    primary key (user_id, friend_id),
    status_id INTEGER REFERENCES statuses (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likes(
    user_id INTEGER NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    film_id INTEGER NOT NULL REFERENCES films (id) ON DELETE CASCADE,
    primary key (user_id, film_id)
);






