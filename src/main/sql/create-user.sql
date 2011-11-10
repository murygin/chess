CREATE TABLE  `chess`.`user` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(100),
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(32),
  `salt` VARCHAR(32),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uni_login` (`login`),
  UNIQUE KEY `uni_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8
