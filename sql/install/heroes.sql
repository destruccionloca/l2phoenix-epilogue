CREATE TABLE IF NOT EXISTS `heroes` (
  `char_id` int NOT NULL default 0,
  `count` tinyint unsigned NOT NULL default 0,
  `played` tinyint NOT NULL default 0,
  `active` tinyint NOT NULL default 0,
  PRIMARY KEY  (`char_id`)
) TYPE=MyISAM;