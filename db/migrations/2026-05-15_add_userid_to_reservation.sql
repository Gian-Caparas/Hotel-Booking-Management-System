-- Migration: Add user ownership to reservation records
-- Date: 2026-05-15
-- Purpose: Ensure each reservation is linked to the user who created it

USE `hoteldb`;

-- 1) Add userID column if missing
SET @col_exists := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'reservation'
      AND COLUMN_NAME = 'userID'
);

SET @add_col_sql := IF(
    @col_exists = 0,
    'ALTER TABLE `reservation` ADD COLUMN `userID` INT(3) NOT NULL',
    'SELECT "column userID already exists"'
);

PREPARE stmt FROM @add_col_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 2) Backfill userID for existing rows if needed
-- Uses the first admin user as fallback owner for old historical rows.
SET @fallback_user_id := (
    SELECT userID
    FROM `user`
    ORDER BY userID ASC
    LIMIT 1
);

UPDATE `reservation`
SET `userID` = @fallback_user_id
WHERE (`userID` IS NULL OR `userID` = 0)
  AND @fallback_user_id IS NOT NULL;

-- 3) Add FK if missing
SET @fk_exists := (
    SELECT COUNT(*)
    FROM information_schema.REFERENTIAL_CONSTRAINTS
    WHERE CONSTRAINT_SCHEMA = DATABASE()
      AND CONSTRAINT_NAME = 'fk_reservation_user'
      AND TABLE_NAME = 'reservation'
);

SET @add_fk_sql := IF(
    @fk_exists = 0,
    'ALTER TABLE `reservation` ADD CONSTRAINT `fk_reservation_user` FOREIGN KEY (`userID`) REFERENCES `user`(`userID`) ON UPDATE CASCADE ON DELETE CASCADE',
    'SELECT "constraint fk_reservation_user already exists"'
);

PREPARE stmt2 FROM @add_fk_sql;
EXECUTE stmt2;
DEALLOCATE PREPARE stmt2;
