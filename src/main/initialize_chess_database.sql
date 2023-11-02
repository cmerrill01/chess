CREATE DATABASE chess;
USE chess;
CREATE TABLE users (
	username varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	password varchar(255) NOT NULL,
	PRIMARY KEY (username)
);
CREATE TABLE auth_tokens (
	auth_token char(36) NOT NULL,
	username varchar(255) NOT NULL,
	PRIMARY KEY (auth_token)
);
CREATE TABLE games (
	game_id int NOT NULL AUTO_INCREMENT,
	white_username varchar(255),
	black_username varchar(255),
	game_name varchar(255) NOT NULL,
	game text NOT NULL,
	PRIMARY KEY (game_id)
);