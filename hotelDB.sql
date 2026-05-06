-- ============================================
-- HotelFX Database
-- Database: hotelDB
-- ============================================

CREATE DATABASE IF NOT EXISTS `hotelDB`;
USE `hotelDB`;

-- ============================================
-- Table: user
-- ============================================
CREATE TABLE IF NOT EXISTS `user` (
    `userID`    int          NOT NULL AUTO_INCREMENT,
    `user_name` varchar(50)  NOT NULL,
    `user_pass` varchar(50)  NOT NULL,
    `is_admin`  tinyint(1)   NOT NULL DEFAULT 0,
    PRIMARY KEY (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `user` (`user_name`, `user_pass`, `is_admin`) VALUES
('admin',   'admin123', 1),
('manager', 'manager1', 1),
('staff1',  'staff123', 0),
('staff2',  'staff456', 0);

-- ============================================
-- Table: room
-- ============================================
CREATE TABLE IF NOT EXISTS `room` (
    `roomID`         int         NOT NULL AUTO_INCREMENT,
    `room_Type`      varchar(15) NOT NULL,
    `room_capacity`  varchar(15) NOT NULL,
    `Check_In_Date`  date        NOT NULL,
    `Check_Out_Date` date        NOT NULL,
    `isEmpty`        tinyint(1)  NOT NULL DEFAULT 1,
    PRIMARY KEY (`roomID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=1;

INSERT INTO `room` (`room_Type`, `room_capacity`, `Check_In_Date`, `Check_Out_Date`, `isEmpty`) VALUES
('Economy', 'Single', '2024-01-01', '2024-01-01', 1),
('Economy', 'Single', '2024-01-01', '2024-01-01', 1),
('Economy', 'Single', '2024-01-01', '2024-01-01', 1),
('Economy', 'Single', '2024-01-01', '2024-01-01', 1),
('Economy', 'Single', '2024-01-01', '2024-01-01', 1),
('Economy', 'Single', '2024-01-01', '2024-01-01', 1),
('Economy', 'Single', '2024-01-01', '2024-01-01', 1),
('Economy', 'Single', '2024-01-01', '2024-01-01', 1),
('Economy', 'Single', '2024-01-01', '2024-01-01', 1),
('Economy', 'Single', '2024-01-01', '2024-01-01', 1),
('Economy', 'Double', '2024-01-01', '2024-01-01', 1),
('Economy', 'Double', '2024-01-01', '2024-01-01', 1),
('Economy', 'Double', '2024-01-01', '2024-01-01', 1),
('Economy', 'Double', '2024-01-01', '2024-01-01', 1),
('Economy', 'Double', '2024-01-01', '2024-01-01', 1),
('Economy', 'Double', '2024-01-01', '2024-01-01', 1),
('Economy', 'Double', '2024-01-01', '2024-01-01', 1),
('Economy', 'Double', '2024-01-01', '2024-01-01', 1),
('Economy', 'Double', '2024-01-01', '2024-01-01', 1),
('Economy', 'Double', '2024-01-01', '2024-01-01', 1),
('Economy', 'Triple', '2024-01-01', '2024-01-01', 1),
('Economy', 'Triple', '2024-01-01', '2024-01-01', 1),
('Economy', 'Triple', '2024-01-01', '2024-01-01', 1),
('Economy', 'Triple', '2024-01-01', '2024-01-01', 1),
('Economy', 'Triple', '2024-01-01', '2024-01-01', 1),
('Economy', 'Triple', '2024-01-01', '2024-01-01', 1),
('Economy', 'Triple', '2024-01-01', '2024-01-01', 1),
('Economy', 'Triple', '2024-01-01', '2024-01-01', 1),
('Economy', 'Triple', '2024-01-01', '2024-01-01', 1),
('Economy', 'Triple', '2024-01-01', '2024-01-01', 1),
('Normal', 'Single', '2024-01-01', '2024-01-01', 1),
('Normal', 'Single', '2024-01-01', '2024-01-01', 1),
('Normal', 'Single', '2024-01-01', '2024-01-01', 1),
('Normal', 'Single', '2024-01-01', '2024-01-01', 1),
('Normal', 'Single', '2024-01-01', '2024-01-01', 1),
('Normal', 'Single', '2024-01-01', '2024-01-01', 1),
('Normal', 'Single', '2024-01-01', '2024-01-01', 1),
('Normal', 'Single', '2024-01-01', '2024-01-01', 1),
('Normal', 'Single', '2024-01-01', '2024-01-01', 1),
('Normal', 'Single', '2024-01-01', '2024-01-01', 1),
('Normal', 'Double', '2024-01-01', '2024-01-01', 1),
('Normal', 'Double', '2024-01-01', '2024-01-01', 1),
('Normal', 'Double', '2024-01-01', '2024-01-01', 1),
('Normal', 'Double', '2024-01-01', '2024-01-01', 1),
('Normal', 'Double', '2024-01-01', '2024-01-01', 1),
('Normal', 'Double', '2024-01-01', '2024-01-01', 1),
('Normal', 'Double', '2024-01-01', '2024-01-01', 1),
('Normal', 'Double', '2024-01-01', '2024-01-01', 1),
('Normal', 'Double', '2024-01-01', '2024-01-01', 1),
('Normal', 'Double', '2024-01-01', '2024-01-01', 1),
('Normal', 'Triple', '2024-01-01', '2024-01-01', 1),
('Normal', 'Triple', '2024-01-01', '2024-01-01', 1),
('Normal', 'Triple', '2024-01-01', '2024-01-01', 1),
('Normal', 'Triple', '2024-01-01', '2024-01-01', 1),
('Normal', 'Triple', '2024-01-01', '2024-01-01', 1),
('Normal', 'Triple', '2024-01-01', '2024-01-01', 1),
('Normal', 'Triple', '2024-01-01', '2024-01-01', 1),
('Normal', 'Triple', '2024-01-01', '2024-01-01', 1),
('Normal', 'Triple', '2024-01-01', '2024-01-01', 1),
('Normal', 'Triple', '2024-01-01', '2024-01-01', 1),
('Vip', 'Single', '2024-01-01', '2024-01-01', 1),
('Vip', 'Single', '2024-01-01', '2024-01-01', 1),
('Vip', 'Single', '2024-01-01', '2024-01-01', 1),
('Vip', 'Single', '2024-01-01', '2024-01-01', 1),
('Vip', 'Single', '2024-01-01', '2024-01-01', 1),
('Vip', 'Single', '2024-01-01', '2024-01-01', 1),
('Vip', 'Single', '2024-01-01', '2024-01-01', 1),
('Vip', 'Single', '2024-01-01', '2024-01-01', 1),
('Vip', 'Single', '2024-01-01', '2024-01-01', 1),
('Vip', 'Single', '2024-01-01', '2024-01-01', 1),
('Vip', 'Double', '2024-01-01', '2024-01-01', 1),
('Vip', 'Double', '2024-01-01', '2024-01-01', 1),
('Vip', 'Double', '2024-01-01', '2024-01-01', 1),
('Vip', 'Double', '2024-01-01', '2024-01-01', 1),
('Vip', 'Double', '2024-01-01', '2024-01-01', 1),
('Vip', 'Double', '2024-01-01', '2024-01-01', 1),
('Vip', 'Double', '2024-01-01', '2024-01-01', 1),
('Vip', 'Double', '2024-01-01', '2024-01-01', 1),
('Vip', 'Double', '2024-01-01', '2024-01-01', 1),
('Vip', 'Double', '2024-01-01', '2024-01-01', 1),
('Vip', 'Triple', '2024-01-01', '2024-01-01', 1),
('Vip', 'Triple', '2024-01-01', '2024-01-01', 1),
('Vip', 'Triple', '2024-01-01', '2024-01-01', 1),
('Vip', 'Triple', '2024-01-01', '2024-01-01', 1),
('Vip', 'Triple', '2024-01-01', '2024-01-01', 1),
('Vip', 'Triple', '2024-01-01', '2024-01-01', 1),
('Vip', 'Triple', '2024-01-01', '2024-01-01', 1),
('Vip', 'Triple', '2024-01-01', '2024-01-01', 1),
('Vip', 'Triple', '2024-01-01', '2024-01-01', 1),
('Vip', 'Triple', '2024-01-01', '2024-01-01', 1);

-- ============================================
-- Table: guest
-- ============================================
CREATE TABLE IF NOT EXISTS `guest` (
    `guestID`         int          NOT NULL AUTO_INCREMENT,
    `firstName`       varchar(50)  NOT NULL,
    `lastName`        varchar(50)  NOT NULL,
    `EmailAddress`    varchar(50)  NOT NULL,
    `Address`         varchar(50)  NOT NULL,
    `Nationality`     varchar(50)  NOT NULL,
    `PhoneNumber`     varchar(50)  NOT NULL,
    `Number_Of_Days`  int          NOT NULL DEFAULT 1,
    `TotalFees`       double       NOT NULL DEFAULT 0,
    PRIMARY KEY (`guestID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;