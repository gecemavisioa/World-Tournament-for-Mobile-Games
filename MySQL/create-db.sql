DROP SCHEMA IF EXISTS `case-study`;

CREATE SCHEMA `case-study`;

use `case-study`;

CREATE TABLE IF NOT EXISTS `user_table` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(128) NOT NULL,
  `level` int DEFAULT 1,
  `coins` int DEFAULT 5000,
  `country` varchar(128) NOT NULL,
  `version` BIGINT DEFAULT 0,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `tournament` (	
	`id` int NOT NULL AUTO_INCREMENT,
    `date` TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS `reward` (
	`id` int NOT NULL AUTO_INCREMENT,
	`user_id` int  NOT NULL,
    `amount` int,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS `user_rank` (
	`id` int NOT NULL AUTO_INCREMENT,
	`user_id` int  NOT NULL,
    `tournament_date` TIMESTAMP,
    `user_rank` int,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES `user_table`(id)
);

CREATE TABLE IF NOT EXISTS `tournament_group` (
	`id` int NOT NULL AUTO_INCREMENT,
    `tournament_id` int  NOT NULL, 
    PRIMARY KEY (id),
    FOREIGN KEY (tournament_id) REFERENCES tournament(id)
);

CREATE TABLE IF NOT EXISTS `group_user` (
	`id` int NOT NULL AUTO_INCREMENT,
    `group_id` int,
    `user_id` int,
    `score` int DEFAULT 0,
    `is_processed` boolean DEFAULT false,
	PRIMARY KEY (id),
    FOREIGN KEY (group_id) REFERENCES tournament_group(id),
    FOREIGN KEY (user_id) REFERENCES `user_table`(id)
);