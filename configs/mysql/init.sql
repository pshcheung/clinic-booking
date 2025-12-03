CREATE DATABASE event_tracker;
USE event_tracker;

-- Table event_tracker.event

CREATE TABLE `event` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `version` bigint(11) NOT NULL DEFAULT '0',
  `created_by` varchar(100) DEFAULT NULL,
  `created_date` datetime NOT NULL,
  `last_updated_by` varchar(100) DEFAULT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `code` varchar(50) NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `status` int(2) unsigned DEFAULT '0',
  `owner_id` bigint(11) UNSIGNED NOT NULL,
  `spot_available` int(7) UNSIGNED NOT NULL DEFAULT 0,
  `spot_left` int(7) UNSIGNED NOT NULL DEFAULT 0,
  `location` varchar(1000),
  `location_url` varchar(255),
  `duration_in_days` int(5) UNSIGNED NOT NULL DEFAULT 0,
  `duration_in_hours` int(5) UNSIGNED NOT NULL DEFAULT 0,
  `duration_in_minutes` int(5) UNSIGNED NOT NULL DEFAULT 0,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `rsvp_date` datetime NOT NULL,
  `open_to_member` Tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

CREATE UNIQUE INDEX `event_id` USING BTREE ON `event` (`id`);
CREATE UNIQUE INDEX `event_code` USING BTREE ON `event` (`code`);

-- Table event_tracker.event_group

CREATE TABLE `event_group`
(
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `version` bigint(11) NOT NULL DEFAULT '0',
  `created_by` varchar(100) DEFAULT NULL,
  `created_date` datetime NOT NULL,
  `last_updated_by` varchar(100) DEFAULT NULL,
  `last_updated_date` datetime DEFAULT NULL,
  `code` varchar(50) NOT NULL,
  `name` varchar(255),
  `description` Varchar(1000),
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
 DEFAULT CHARSET=utf8mb4
 AUTO_INCREMENT = 5
 ROW_FORMAT = DYNAMIC
;

CREATE UNIQUE INDEX `event_group_id` USING BTREE ON `event_group` (`id`);
CREATE UNIQUE INDEX `event_group_code` USING BTREE ON `event_group` (`code`);

-- Table event_tracker.event_registration

-- CREATE TABLE `event_registration` (
--  `event_id` bigint(11) unsigned NOT NULL,
--  `user_id` bigint(11) unsigned NOT NULL,
--  `status` int(2) unsigned DEFAULT '0',
--  PRIMARY KEY (`event_id`, `user_id`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- CREATE INDEX `event_registration_user_id` USING BTREE ON `event_registration` (`user_id`);

-- Create relationships section ------------------------------------------------- 

-- ALTER TABLE `event` ADD CONSTRAINT `event_ownership` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
-- ALTER TABLE `event_registration` ADD CONSTRAINT `event_registration_event` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
-- ALTER TABLE `event_registration` ADD CONSTRAINT `event_registration_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Populate data section ------------------------------------------------- 

INSERT INTO `event`(`id`,`version`,`created_by`,`created_date`,`last_updated_by`,`last_updated_date`,`code`,`name`,`description`,`status`,`owner_id`,`spot_available`,`spot_left`,`location`,`location_url`,`duration_in_days`,`duration_in_hours`,`duration_in_minutes`,`start_date`,`end_date`,`rsvp_date`,`open_to_member`) VALUES (1,0,'initial-load','2020-05-08 00:00:00',null,null,'event#1','event #1','Some sort of event 1',2,3,5,5,'Somewhere',null,0,0,120,'2022-06-08 00:00:00','2022-06-14 00:00:00','2022-06-06 00:00:00',1);
INSERT INTO `event`(`id`,`version`,`created_by`,`created_date`,`last_updated_by`,`last_updated_date`,`code`,`name`,`description`,`status`,`owner_id`,`spot_available`,`spot_left`,`location`,`location_url`,`duration_in_days`,`duration_in_hours`,`duration_in_minutes`,`start_date`,`end_date`,`rsvp_date`,`open_to_member`) VALUES (2,0,'initial-load','2020-05-08 00:00:00',null,null,'event#2','event #2','Some sort of event 2',2,3,5,5,'Somewhere',null,0,0,120,'2022-06-09 00:00:00','2022-06-15 00:00:00','2022-06-07 00:00:00',1);
