-- INSERT INTO `lock` SELECT DISTINCT `Account`, 'HWID', `HWID` FROM `hwids_log`;
-- INSERT INTO `lock` SELECT DISTINCT `Account`, 'IP', `IP` FROM `hwids_log`;
-- INSERT INTO `lock` SELECT `login`, 'IP', `AllowIPs` FROM `accounts`;