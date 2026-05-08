CREATE DATABASE `hotelDB`;
USE `hotelDB`;

--  TABLE: user
CREATE TABLE `user` (
  `user_name`  VARCHAR(50)  NOT NULL,
  `user_pass`  VARCHAR(50)  NOT NULL,
  `is_admin`   TINYINT(1)   NOT NULL DEFAULT 0,
  `role` VARCHAR(20)        NOT NULL DEFAULT 'Staff',
  PRIMARY KEY (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `user` VALUES
('admin',   'admin123', 1, 'Administrator'),
('manager', 'manager1', 1, 'Manager'),
('staff1',  'staff123', 0, 'Staff'),
('staff2',  'staff456', 0, 'Staff');

--  TABLE: room
--  Pricing:
--    Economy  Single=100  Double=200  Triple=300
--    Normal   Single=50   Double=100  Triple=150
--    Vip      Single=200  Double=400  Triple=600

CREATE TABLE `room` (
  `roomID`          INT          NOT NULL AUTO_INCREMENT,
  `room_Type`       VARCHAR(20)  NOT NULL,
  `room_capacity`   VARCHAR(20)  NOT NULL,
  `rate_per_night`  DOUBLE       NOT NULL DEFAULT 0,
  `status`          VARCHAR(20)  NOT NULL DEFAULT 'AVAILABLE',
  `Check_In_Date`   DATE         DEFAULT NULL,
  `Check_Out_Date`  DATE         DEFAULT NULL,
  PRIMARY KEY (`roomID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=1;

-- Economy Single  rate=100  (rooms 1-10)
INSERT INTO `room` (`room_Type`,`room_capacity`,`rate_per_night`,`status`) VALUES
('Economy','Single',100,'AVAILABLE'),('Economy','Single',100,'AVAILABLE'),
('Economy','Single',100,'AVAILABLE'),('Economy','Single',100,'AVAILABLE'),
('Economy','Single',100,'AVAILABLE'),('Economy','Single',100,'AVAILABLE'),
('Economy','Single',100,'AVAILABLE'),('Economy','Single',100,'AVAILABLE'),
('Economy','Single',100,'AVAILABLE'),('Economy','Single',100,'AVAILABLE');

-- Economy Double  rate=200  (rooms 11-20)
INSERT INTO `room` (`room_Type`,`room_capacity`,`rate_per_night`,`status`) VALUES
('Economy','Double',200,'AVAILABLE'),('Economy','Double',200,'AVAILABLE'),
('Economy','Double',200,'AVAILABLE'),('Economy','Double',200,'AVAILABLE'),
('Economy','Double',200,'AVAILABLE'),('Economy','Double',200,'AVAILABLE'),
('Economy','Double',200,'AVAILABLE'),('Economy','Double',200,'AVAILABLE'),
('Economy','Double',200,'AVAILABLE'),('Economy','Double',200,'AVAILABLE');

-- Economy Triple  rate=300  (rooms 21-30)
INSERT INTO `room` (`room_Type`,`room_capacity`,`rate_per_night`,`status`) VALUES
('Economy','Triple',300,'AVAILABLE'),('Economy','Triple',300,'AVAILABLE'),
('Economy','Triple',300,'AVAILABLE'),('Economy','Triple',300,'AVAILABLE'),
('Economy','Triple',300,'AVAILABLE'),('Economy','Triple',300,'AVAILABLE'),
('Economy','Triple',300,'AVAILABLE'),('Economy','Triple',300,'AVAILABLE'),
('Economy','Triple',300,'AVAILABLE'),('Economy','Triple',300,'AVAILABLE');

-- Normal Single  rate=50  (rooms 31-40)
INSERT INTO `room` (`room_Type`,`room_capacity`,`rate_per_night`,`status`) VALUES
('Normal','Single',50,'AVAILABLE'),('Normal','Single',50,'AVAILABLE'),
('Normal','Single',50,'AVAILABLE'),('Normal','Single',50,'AVAILABLE'),
('Normal','Single',50,'AVAILABLE'),('Normal','Single',50,'AVAILABLE'),
('Normal','Single',50,'AVAILABLE'),('Normal','Single',50,'AVAILABLE'),
('Normal','Single',50,'AVAILABLE'),('Normal','Single',50,'AVAILABLE');

-- Normal Double  rate=100  (rooms 41-50)
INSERT INTO `room` (`room_Type`,`room_capacity`,`rate_per_night`,`status`) VALUES
('Normal','Double',100,'AVAILABLE'),('Normal','Double',100,'AVAILABLE'),
('Normal','Double',100,'AVAILABLE'),('Normal','Double',100,'AVAILABLE'),
('Normal','Double',100,'AVAILABLE'),('Normal','Double',100,'AVAILABLE'),
('Normal','Double',100,'AVAILABLE'),('Normal','Double',100,'AVAILABLE'),
('Normal','Double',100,'AVAILABLE'),('Normal','Double',100,'AVAILABLE');

-- Normal Triple  rate=150  (rooms 51-60)
INSERT INTO `room` (`room_Type`,`room_capacity`,`rate_per_night`,`status`) VALUES
('Normal','Triple',150,'AVAILABLE'),('Normal','Triple',150,'AVAILABLE'),
('Normal','Triple',150,'AVAILABLE'),('Normal','Triple',150,'AVAILABLE'),
('Normal','Triple',150,'AVAILABLE'),('Normal','Triple',150,'AVAILABLE'),
('Normal','Triple',150,'AVAILABLE'),('Normal','Triple',150,'AVAILABLE'),
('Normal','Triple',150,'AVAILABLE'),('Normal','Triple',150,'AVAILABLE');

-- Vip Single  rate=200  (rooms 61-70)
INSERT INTO `room` (`room_Type`,`room_capacity`,`rate_per_night`,`status`) VALUES
('Vip','Single',200,'AVAILABLE'),('Vip','Single',200,'AVAILABLE'),
('Vip','Single',200,'AVAILABLE'),('Vip','Single',200,'AVAILABLE'),
('Vip','Single',200,'AVAILABLE'),('Vip','Single',200,'AVAILABLE'),
('Vip','Single',200,'AVAILABLE'),('Vip','Single',200,'AVAILABLE'),
('Vip','Single',200,'AVAILABLE'),('Vip','Single',200,'AVAILABLE');

-- Vip Double  rate=400  (rooms 71-80)
INSERT INTO `room` (`room_Type`,`room_capacity`,`rate_per_night`,`status`) VALUES
('Vip','Double',400,'AVAILABLE'),('Vip','Double',400,'AVAILABLE'),
('Vip','Double',400,'AVAILABLE'),('Vip','Double',400,'AVAILABLE'),
('Vip','Double',400,'AVAILABLE'),('Vip','Double',400,'AVAILABLE'),
('Vip','Double',400,'AVAILABLE'),('Vip','Double',400,'AVAILABLE'),
('Vip','Double',400,'AVAILABLE'),('Vip','Double',400,'AVAILABLE');

-- Vip Triple  rate=600  (rooms 81-90)
INSERT INTO `room` (`room_Type`,`room_capacity`,`rate_per_night`,`status`) VALUES
('Vip','Triple',600,'AVAILABLE'),('Vip','Triple',600,'AVAILABLE'),
('Vip','Triple',600,'AVAILABLE'),('Vip','Triple',600,'AVAILABLE'),
('Vip','Triple',600,'AVAILABLE'),('Vip','Triple',600,'AVAILABLE'),
('Vip','Triple',600,'AVAILABLE'),('Vip','Triple',600,'AVAILABLE'),
('Vip','Triple',600,'AVAILABLE'),('Vip','Triple',600,'AVAILABLE');


--  TABLE: guest  (created by Check-In — guest is physically here)
--  Columns match CheckIn.fxml fields exactly
CREATE TABLE `guest` (
  `guestID`         INT          NOT NULL AUTO_INCREMENT,
  `room_ID`         INT          NOT NULL,
  `first_name`      VARCHAR(50)  NOT NULL,
  `last_name`       VARCHAR(50)  NOT NULL,
  `email`           VARCHAR(100) NOT NULL,
  `phone_no`        VARCHAR(30)  NOT NULL,
  `city`            VARCHAR(50)  NOT NULL,
  `nationality`     VARCHAR(50)  NOT NULL,
  `check_in_date`   DATE         NOT NULL,
  `check_out_date`  DATE         NOT NULL,
  `number_of_days`  INT          NOT NULL DEFAULT 1,
  `rate_per_night`  DOUBLE       NOT NULL DEFAULT 0,
  `total_fees`      DOUBLE       NOT NULL DEFAULT 0,
  PRIMARY KEY (`guestID`),
  FOREIGN KEY (`room_ID`) REFERENCES `room`(`roomID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
--  TABLE: reservation  (created by Book a Room — guest not here yet)
--  Room status → RESERVED (not OCCUPIED)
-- ============================================================
CREATE TABLE `reservation` (
  `reservationID`   INT          NOT NULL AUTO_INCREMENT,
  `room_ID`         INT          NOT NULL,
  `first_name`      VARCHAR(50)  NOT NULL,
  `last_name`       VARCHAR(50)  NOT NULL,
  `email`           VARCHAR(100) NOT NULL,
  `phone_no`        VARCHAR(30)  NOT NULL,
  `room_type`       VARCHAR(20)  NOT NULL,
  `room_capacity`   VARCHAR(20)  NOT NULL,
  `check_in_date`   DATE         NOT NULL,
  `check_out_date`  DATE         NOT NULL,
  `number_of_days`  INT          NOT NULL DEFAULT 1,
  `rate_per_night`  DOUBLE       NOT NULL DEFAULT 0,
  `total_fees`      DOUBLE       NOT NULL DEFAULT 0,
  `status`          VARCHAR(20)  NOT NULL DEFAULT 'RESERVED',
  PRIMARY KEY (`reservationID`),
  FOREIGN KEY (`room_ID`) REFERENCES `room`(`roomID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;