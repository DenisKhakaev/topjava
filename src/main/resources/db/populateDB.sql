DELETE
FROM user_roles;
DELETE
FROM users;
DELETE
FROM meals;
ALTER SEQUENCE global_seq
  RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('ROLE_USER', 100000),
       ('ROLE_ADMIN', 100001);

ALTER SEQUENCE global_seq
  RESTART WITH 1;
INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, '2020-02-24 19:30', 'Завтрак', 350),
       (100000, '2020-02-24 20:30', 'Обед', 500),
       (100000, '2020-02-24 20:35', 'Пельмени', 800),
       (100000, '2020-02-24 20:40', 'Суп', 330),
       (100000, '2020-02-24 22:30', 'Яблоко', 50),
       (100000, '2020-02-26 22:30', 'Груша', 50);
