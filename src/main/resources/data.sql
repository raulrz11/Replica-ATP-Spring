INSERT INTO tenista (nombre, puntos, pais, fecha_nacimiento, edad, altura, peso, inicio_profesional, mano_buena, reves, entrenador, imagen, price_money, victorias, derrotas)
VALUES ('Raul',1,'Espana', '2002-04-15', 22, 1.80, 60, '2017-01-01', 1, 0, 'Alejandro', 'https://via.placeholder.com/150', 1800.0, 6, 2);
INSERT INTO tenista (nombre, puntos, pais, fecha_nacimiento, edad, altura, peso, inicio_profesional, mano_buena, reves, entrenador, imagen, price_money, victorias, derrotas)
VALUES ('Eva', 2, 'Albania', '2005-02-01', 19, 1.60, 45, '2020-01-18', 0, 1, 'Javi', 'https://via.placeholder.com/150', 1000.0, 10, 8);
INSERT INTO tenista (nombre, puntos, pais, fecha_nacimiento, edad, altura, peso, inicio_profesional, mano_buena, reves, entrenador, imagen, price_money, victorias, derrotas)
VALUES ('Mangue', 3, 'Croacia', '2002-08-13', 23, 1.85, 70, '2015-03-05', 0, 1, 'Anny', 'https://via.placeholder.com/150', 1300.0, 4, 9);
INSERT INTO tenista (nombre, puntos, pais, fecha_nacimiento, edad, altura, peso, inicio_profesional, mano_buena, reves, entrenador, imagen, price_money, victorias, derrotas)
VALUES ('Miguel', 4, 'Espana', '2003-11-22', 22, 1.90, 100, '2024-12-20', 0, 0, 'Pepelu', 'https://via.placeholder.com/150', 1500.0, 6, 12);


INSERT INTO torneo (nombre, ubicacion, modalidad, categoria, superficie, entradas, premio, fecha_inicio, fecha_finalizacion, imagen)
VALUES ('Open de Australia','Madrid, Espana', 0, 0, 2, 2, 500000, '2024-07-05', '2024-07-10', 'https://via.placeholder.com/150');
INSERT INTO torneo (nombre, ubicacion, modalidad, categoria, superficie, entradas, premio, fecha_inicio, fecha_finalizacion, imagen)
VALUES ('Rolanga Ross','Bogota, Colombia', 1, 1, 1, 16, 250000, '2024-05-12', '2024-05-15', 'https://via.placeholder.com/150');


INSERT INTO users(nombre, username,email, password) VALUES ('Admin', 'admin', 'admin@gmail.com', '$2a$10$fJFyQsbXNA8QU4bfx0k0uujv/xWB42jikIfGDbXBkpTmPpGT3baUq');
insert into user_rols (user_id, rols)
values (1, 'USER');
insert into user_rols (user_id, rols)
values (1, 'ADMIN');
/*
admin
admin1
*/
INSERT INTO users(nombre, username,email, password) VALUES ('User', 'user', 'user@gmail.com', '$2a$12$FAietDaEvqwXy6wfzqJymupVLPb8m1BDRT8dxqVN1roB0aFchvey6');
insert into user_rols (user_id, rols)
values (2, 'USER');
/*
user
user1
*/