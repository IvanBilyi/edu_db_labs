CREATE DATABASE IF NOT EXISTS group_db;
USE group_db;

CREATE TABLE study_group (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    year INT NOT NULL
);