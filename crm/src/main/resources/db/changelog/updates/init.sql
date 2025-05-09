-- Создание таблицы role
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name varchar(20) NOT NULL
);

-- Создание таблицы subscriber
CREATE TABLE subscriber (
    id BIGSERIAL PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    login VARCHAR(11) NOT NULL UNIQUE,
    passport_data VARCHAR(10) UNIQUE
);

-- Создание таблицы связи subscriber_role
CREATE TABLE subscriber_roles (
    roles_id INT NOT NULL,
    subscriber_id INT NOT NULL,
    FOREIGN KEY (roles_id) REFERENCES roles(id),
    FOREIGN KEY (subscriber_id) REFERENCES subscriber(id),
    PRIMARY KEY (roles_id, subscriber_id)
);