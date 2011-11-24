--DROP TABLE `cchess`.`move`;

CREATE TABLE  `cchess`.`move` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `game_id` INT(10) UNSIGNED NOT NULL,
  `number` INT(10) UNSIGNED NOT NULL,
  `move` VARCHAR(10) NOT NULL,
  `movedate` DATETIME NOT NULL,
  `fen` VARCHAR(70) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX (`id`),
  INDEX (`game_id`),
  FOREIGN KEY `move_game` (game_id) REFERENCES game (id)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8
