-- ============================================================
--  hotelDB  |  Normalized Schema
--  Tables: user, room, guest, reservation
-- ============================================================

CREATE DATABASE IF NOT EXISTS `hotelDB`;
USE `hotelDB`;

-- ============================================================
--  TABLE: user
--  Stores system users (Admin, Staff, Customer)
-- ============================================================
CREATE TABLE `user` (
  `userID`     INT(3)       NOT NULL AUTO_INCREMENT,
  `user_name`  VARCHAR(50)  NOT NULL UNIQUE,
  `user_pass`  VARCHAR(255) NOT NULL,
  `role`       ENUM('Admin','Staff','Customer') NOT NULL DEFAULT 'Staff',
  `email`      VARCHAR(100) NOT NULL UNIQUE,
  `phone_no`   VARCHAR(11)  NOT NULL,
  PRIMARY KEY (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `user` (`user_name`, `user_pass`, `role`, `email`, `phone_no`) VALUES
('admin',    'admin123',   'Admin',    'admin@hotel.com',    '09000000001'),
('manager',  'manager1',   'Admin',    'manager@hotel.com',  '09000000002'),
('staff1',   'staff123',   'Staff',    'staff1@hotel.com',   '09000000003'),
('staff2',   'staff456',   'Staff',    'staff2@hotel.com',   '09000000004');


-- ============================================================
--  TABLE: room
--  Stores room information only
--  Pricing:
--    Economy  Single=100  Double=200  Triple=300
--    Normal   Single=50   Double=100  Triple=150
--    Vip      Single=200  Double=400  Triple=600
--  Status: AVAILABLE or OCCUPIED only
-- ============================================================
CREATE TABLE `room` (
  `roomID`          INT(3)       NOT NULL AUTO_INCREMENT,
  `room_type`       VARCHAR(20)  NOT NULL,
  `room_capacity`   VARCHAR(20)  NOT NULL,
  `room_rate`       DOUBLE       NOT NULL DEFAULT 0,
  `status`          ENUM('AVAILABLE','OCCUPIED') NOT NULL DEFAULT 'AVAILABLE',
  PRIMARY KEY (`roomID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=1;

-- Economy Single  rate=100  (rooms 1-10)
INSERT INTO `room` (`room_type`, `room_capacity`, `room_rate`, `status`) VALUES
('Economy','Single',100,'AVAILABLE'),('Economy','Single',100,'AVAILABLE'),
('Economy','Single',100,'AVAILABLE'),('Economy','Single',100,'AVAILABLE'),
('Economy','Single',100,'AVAILABLE'),('Economy','Single',100,'AVAILABLE'),
('Economy','Single',100,'AVAILABLE'),('Economy','Single',100,'AVAILABLE'),
('Economy','Single',100,'AVAILABLE'),('Economy','Single',100,'AVAILABLE');

-- Economy Double  rate=200  (rooms 11-20)
INSERT INTO `room` (`room_type`, `room_capacity`, `room_rate`, `status`) VALUES
('Economy','Double',200,'AVAILABLE'),('Economy','Double',200,'AVAILABLE'),
('Economy','Double',200,'AVAILABLE'),('Economy','Double',200,'AVAILABLE'),
('Economy','Double',200,'AVAILABLE'),('Economy','Double',200,'AVAILABLE'),
('Economy','Double',200,'AVAILABLE'),('Economy','Double',200,'AVAILABLE'),
('Economy','Double',200,'AVAILABLE'),('Economy','Double',200,'AVAILABLE');

-- Economy Triple  rate=300  (rooms 21-30)
INSERT INTO `room` (`room_type`, `room_capacity`, `room_rate`, `status`) VALUES
('Economy','Triple',300,'AVAILABLE'),('Economy','Triple',300,'AVAILABLE'),
('Economy','Triple',300,'AVAILABLE'),('Economy','Triple',300,'AVAILABLE'),
('Economy','Triple',300,'AVAILABLE'),('Economy','Triple',300,'AVAILABLE'),
('Economy','Triple',300,'AVAILABLE'),('Economy','Triple',300,'AVAILABLE'),
('Economy','Triple',300,'AVAILABLE'),('Economy','Triple',300,'AVAILABLE');

-- Normal Single  rate=50  (rooms 31-40)
INSERT INTO `room` (`room_type`, `room_capacity`, `room_rate`, `status`) VALUES
('Normal','Single',50,'AVAILABLE'),('Normal','Single',50,'AVAILABLE'),
('Normal','Single',50,'AVAILABLE'),('Normal','Single',50,'AVAILABLE'),
('Normal','Single',50,'AVAILABLE'),('Normal','Single',50,'AVAILABLE'),
('Normal','Single',50,'AVAILABLE'),('Normal','Single',50,'AVAILABLE'),
('Normal','Single',50,'AVAILABLE'),('Normal','Single',50,'AVAILABLE');

-- Normal Double  rate=100  (rooms 41-50)
INSERT INTO `room` (`room_type`, `room_capacity`, `room_rate`, `status`) VALUES
('Normal','Double',100,'AVAILABLE'),('Normal','Double',100,'AVAILABLE'),
('Normal','Double',100,'AVAILABLE'),('Normal','Double',100,'AVAILABLE'),
('Normal','Double',100,'AVAILABLE'),('Normal','Double',100,'AVAILABLE'),
('Normal','Double',100,'AVAILABLE'),('Normal','Double',100,'AVAILABLE'),
('Normal','Double',100,'AVAILABLE'),('Normal','Double',100,'AVAILABLE');

-- Normal Triple  rate=150  (rooms 51-60)
INSERT INTO `room` (`room_type`, `room_capacity`, `room_rate`, `status`) VALUES
('Normal','Triple',150,'AVAILABLE'),('Normal','Triple',150,'AVAILABLE'),
('Normal','Triple',150,'AVAILABLE'),('Normal','Triple',150,'AVAILABLE'),
('Normal','Triple',150,'AVAILABLE'),('Normal','Triple',150,'AVAILABLE'),
('Normal','Triple',150,'AVAILABLE'),('Normal','Triple',150,'AVAILABLE'),
('Normal','Triple',150,'AVAILABLE'),('Normal','Triple',150,'AVAILABLE');

-- Vip Single  rate=200  (rooms 61-70)
INSERT INTO `room` (`room_type`, `room_capacity`, `room_rate`, `status`) VALUES
('Vip','Single',200,'AVAILABLE'),('Vip','Single',200,'AVAILABLE'),
('Vip','Single',200,'AVAILABLE'),('Vip','Single',200,'AVAILABLE'),
('Vip','Single',200,'AVAILABLE'),('Vip','Single',200,'AVAILABLE'),
('Vip','Single',200,'AVAILABLE'),('Vip','Single',200,'AVAILABLE'),
('Vip','Single',200,'AVAILABLE'),('Vip','Single',200,'AVAILABLE');

-- Vip Double  rate=400  (rooms 71-80)
INSERT INTO `room` (`room_type`, `room_capacity`, `room_rate`, `status`) VALUES
('Vip','Double',400,'AVAILABLE'),('Vip','Double',400,'AVAILABLE'),
('Vip','Double',400,'AVAILABLE'),('Vip','Double',400,'AVAILABLE'),
('Vip','Double',400,'AVAILABLE'),('Vip','Double',400,'AVAILABLE'),
('Vip','Double',400,'AVAILABLE'),('Vip','Double',400,'AVAILABLE'),
('Vip','Double',400,'AVAILABLE'),('Vip','Double',400,'AVAILABLE');

-- Vip Triple  rate=600  (rooms 81-90)
INSERT INTO `room` (`room_type`, `room_capacity`, `room_rate`, `status`) VALUES
('Vip','Triple',600,'AVAILABLE'),('Vip','Triple',600,'AVAILABLE'),
('Vip','Triple',600,'AVAILABLE'),('Vip','Triple',600,'AVAILABLE'),
('Vip','Triple',600,'AVAILABLE'),('Vip','Triple',600,'AVAILABLE'),
('Vip','Triple',600,'AVAILABLE'),('Vip','Triple',600,'AVAILABLE'),
('Vip','Triple',600,'AVAILABLE'),('Vip','Triple',600,'AVAILABLE');


-- ============================================================
--  TABLE: guest
--  Stores guest personal information only
--  roomID → FK to room (which room is assigned to this guest)
-- ============================================================
CREATE TABLE `guest` (
  `guestID`      INT(3)       NOT NULL AUTO_INCREMENT,
  `roomID`       INT(3)       NOT NULL,
  `first_name`   VARCHAR(50)  NOT NULL,
  `last_name`    VARCHAR(50)  NOT NULL,
  `email`        VARCHAR(100) NOT NULL,
  `phone_no`     VARCHAR(11)  NOT NULL,
  `city`         VARCHAR(50)  NOT NULL,
  `nationality`  VARCHAR(50)  NOT NULL,
  PRIMARY KEY (`guestID`),
  CONSTRAINT `fk_guest_room`
    FOREIGN KEY (`roomID`) REFERENCES `room`(`roomID`)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ============================================================
--  TABLE: reservation
--  Stores booking/reservation info only
--  guestID → FK to guest
--  roomID  → FK to room
--  total_cost and number_of_days are derived at booking time
--    (total_cost = room.room_rate * number_of_days)
-- ============================================================
CREATE TABLE `reservation` (
  `reservationID`   INT(3)       NOT NULL AUTO_INCREMENT,
  `userID`          INT(3)       NOT NULL,
  `guestID`         INT(3)       NOT NULL,
  `roomID`          INT(3)       NOT NULL,
  `check_in_date`   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `check_out_date`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `number_of_days`  INT          NOT NULL DEFAULT 1,
  `total_cost`      DOUBLE       NOT NULL DEFAULT 0,
  `status`          ENUM('Active','Cancelled') NOT NULL DEFAULT 'Active',
  PRIMARY KEY (`reservationID`),
  CONSTRAINT `fk_reservation_user`
    FOREIGN KEY (`userID`) REFERENCES `user`(`userID`)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  CONSTRAINT `fk_reservation_guest`
    FOREIGN KEY (`guestID`) REFERENCES `guest`(`guestID`)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  CONSTRAINT `fk_reservation_room`
    FOREIGN KEY (`roomID`) REFERENCES `room`(`roomID`)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;