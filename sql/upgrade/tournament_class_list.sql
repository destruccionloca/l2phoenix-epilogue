DROP TABLE IF EXISTS `tournament_class_list`;
CREATE TABLE `tournament_class_list` (
  `class_name` varchar(19) NOT NULL default '',
  `class_id` int(10) unsigned NOT NULL default '0',
  `type` int(11) NOT NULL default '0'
) ENGINE=MyISAM;

INSERT INTO `tournament_class_list` VALUES ('Fighter',0,1);
INSERT INTO `tournament_class_list` VALUES ('Warrior',1,1);
INSERT INTO `tournament_class_list` VALUES ('Gladiator',2,1);
INSERT INTO `tournament_class_list` VALUES ('Duelist',88,1);
INSERT INTO `tournament_class_list` VALUES ('Warlord',3,1);
INSERT INTO `tournament_class_list` VALUES ('Dreadnought',89,1);
INSERT INTO `tournament_class_list` VALUES ('Knight',4,1);
INSERT INTO `tournament_class_list` VALUES ('Paladin',5,1);
INSERT INTO `tournament_class_list` VALUES ('PhoenixKnight',90,1);
INSERT INTO `tournament_class_list` VALUES ('DarkAvenger',6,1);
INSERT INTO `tournament_class_list` VALUES ('HellKnight',91,1);
INSERT INTO `tournament_class_list` VALUES ('Rogue',7,1);
INSERT INTO `tournament_class_list` VALUES ('TreasureHunter',8,1);
INSERT INTO `tournament_class_list` VALUES ('Adventurer',93,1);
INSERT INTO `tournament_class_list` VALUES ('Hawkeye',9,1);
INSERT INTO `tournament_class_list` VALUES ('Sagittarius',92,1);
INSERT INTO `tournament_class_list` VALUES ('Mage',10,2);
INSERT INTO `tournament_class_list` VALUES ('Wizard',11,2);
INSERT INTO `tournament_class_list` VALUES ('Sorceror',12,2);
INSERT INTO `tournament_class_list` VALUES ('Archmage',94,2);
INSERT INTO `tournament_class_list` VALUES ('Necromancer',13,2);
INSERT INTO `tournament_class_list` VALUES ('Soultaker',95,2);
INSERT INTO `tournament_class_list` VALUES ('Warlock',14,2);
INSERT INTO `tournament_class_list` VALUES ('ArcanaLord',96,2);
INSERT INTO `tournament_class_list` VALUES ('Cleric',15,3);
INSERT INTO `tournament_class_list` VALUES ('Bishop',16,3);
INSERT INTO `tournament_class_list` VALUES ('Cardinal',97,3);
INSERT INTO `tournament_class_list` VALUES ('Prophet',17,3);
INSERT INTO `tournament_class_list` VALUES ('Hierophant',98,3);
INSERT INTO `tournament_class_list` VALUES ('Fighter',18,1);
INSERT INTO `tournament_class_list` VALUES ('Knight',19,1);
INSERT INTO `tournament_class_list` VALUES ('TempleKnight',20,1);
INSERT INTO `tournament_class_list` VALUES ('EvaTemplar',99,1);
INSERT INTO `tournament_class_list` VALUES ('SwordSinger',21,3);
INSERT INTO `tournament_class_list` VALUES ('SwordMuse',100,3);
INSERT INTO `tournament_class_list` VALUES ('Scout',22,1);
INSERT INTO `tournament_class_list` VALUES ('PlainsWalker',23,1);
INSERT INTO `tournament_class_list` VALUES ('WindRider',101,1);
INSERT INTO `tournament_class_list` VALUES ('SilverRanger',24,1);
INSERT INTO `tournament_class_list` VALUES ('MoonlightSentinel',102,1);
INSERT INTO `tournament_class_list` VALUES ('Mage',25,2);
INSERT INTO `tournament_class_list` VALUES ('Wizard',26,2);
INSERT INTO `tournament_class_list` VALUES ('SpellSinger',27,2);
INSERT INTO `tournament_class_list` VALUES ('MysticMuse',103,2);
INSERT INTO `tournament_class_list` VALUES ('ElementalSummoner',28,2);
INSERT INTO `tournament_class_list` VALUES ('ElementalMaster',104,2);
INSERT INTO `tournament_class_list` VALUES ('Oracle',29,3);
INSERT INTO `tournament_class_list` VALUES ('Elder',30,3);
INSERT INTO `tournament_class_list` VALUES ('EvaSaint',105,3);
INSERT INTO `tournament_class_list` VALUES ('Fighter',31,1);
INSERT INTO `tournament_class_list` VALUES ('PaulusKnight',32,1);
INSERT INTO `tournament_class_list` VALUES ('ShillienKnight',33,1);
INSERT INTO `tournament_class_list` VALUES ('ShillienTemplar',106,1);
INSERT INTO `tournament_class_list` VALUES ('BladeDancer',34,3);
INSERT INTO `tournament_class_list` VALUES ('SpectralDancer',107,3);
INSERT INTO `tournament_class_list` VALUES ('Assassin',35,1);
INSERT INTO `tournament_class_list` VALUES ('AbyssWalker',36,1);
INSERT INTO `tournament_class_list` VALUES ('GhostHunter',108,1);
INSERT INTO `tournament_class_list` VALUES ('PhantomRanger',37,1);
INSERT INTO `tournament_class_list` VALUES ('GhostSentinel',109,1);
INSERT INTO `tournament_class_list` VALUES ('Mage',38,2);
INSERT INTO `tournament_class_list` VALUES ('DarkWizard',39,2);
INSERT INTO `tournament_class_list` VALUES ('Spellhowler',40,2);
INSERT INTO `tournament_class_list` VALUES ('StormScreamer',110,2);
INSERT INTO `tournament_class_list` VALUES ('PhantomSummoner',41,2);
INSERT INTO `tournament_class_list` VALUES ('SpectralMaster',111,2);
INSERT INTO `tournament_class_list` VALUES ('ShillienOracle',42,3);
INSERT INTO `tournament_class_list` VALUES ('ShillienElder',43,3);
INSERT INTO `tournament_class_list` VALUES ('ShillienSaint',112,3);
INSERT INTO `tournament_class_list` VALUES ('Fighter',44,1);
INSERT INTO `tournament_class_list` VALUES ('Raider',45,1);
INSERT INTO `tournament_class_list` VALUES ('Destroyer',46,1);
INSERT INTO `tournament_class_list` VALUES ('Titan',113,1);
INSERT INTO `tournament_class_list` VALUES ('Monk',47,1);
INSERT INTO `tournament_class_list` VALUES ('Tyrant',48,1);
INSERT INTO `tournament_class_list` VALUES ('GrandKhauatari',114,1);
INSERT INTO `tournament_class_list` VALUES ('Mage',49,2);
INSERT INTO `tournament_class_list` VALUES ('Shaman',50,3);
INSERT INTO `tournament_class_list` VALUES ('Overlord',51,3);
INSERT INTO `tournament_class_list` VALUES ('Dominator',115,3);
INSERT INTO `tournament_class_list` VALUES ('Warcryer',52,3);
INSERT INTO `tournament_class_list` VALUES ('Doomcryer',116,3);
INSERT INTO `tournament_class_list` VALUES ('Fighter',53,1);
INSERT INTO `tournament_class_list` VALUES ('Scavenger',54,1);
INSERT INTO `tournament_class_list` VALUES ('BountyHunter',55,1);
INSERT INTO `tournament_class_list` VALUES ('FortuneSeeker',117,1);
INSERT INTO `tournament_class_list` VALUES ('Artisan',56,1);
INSERT INTO `tournament_class_list` VALUES ('Warsmith',57,1);
INSERT INTO `tournament_class_list` VALUES ('Maestro',118,1);
INSERT INTO `tournament_class_list` VALUES ('SoldierM',123,3);
INSERT INTO `tournament_class_list` VALUES ('SoldierF',124,3);
INSERT INTO `tournament_class_list` VALUES ('Trooper',125,3);
INSERT INTO `tournament_class_list` VALUES ('Warder',126,3);
INSERT INTO `tournament_class_list` VALUES ('Berserker',127,3);
INSERT INTO `tournament_class_list` VALUES ('SoulbreakerM',128,3);
INSERT INTO `tournament_class_list` VALUES ('SoulbreakerF',129,3);
INSERT INTO `tournament_class_list` VALUES ('Arbalester',130,3);
INSERT INTO `tournament_class_list` VALUES ('Doombringer',131,3);
INSERT INTO `tournament_class_list` VALUES ('Soulhound',132,3);
INSERT INTO `tournament_class_list` VALUES ('Trickster',134,3);
INSERT INTO `tournament_class_list` VALUES ('Inspector',135,3);
INSERT INTO `tournament_class_list` VALUES ('Judicator',136,3);