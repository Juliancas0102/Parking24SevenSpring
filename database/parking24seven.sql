CREATE DATABASE IF NOT EXISTS parking24seven
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

USE parking24seven;

CREATE TABLE IF NOT EXISTS usuarios (
    id INT NOT NULL AUTO_INCREMENT,
    nomusu VARCHAR(50) NOT NULL,
    passusu VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_usuarios_nomusu (nomusu)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
