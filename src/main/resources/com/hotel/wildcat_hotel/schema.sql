-- 1. Create Users Table
CREATE TABLE users (
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY, 
    username VARCHAR(50) NOT NULL UNIQUE, 
    password VARCHAR(255) NOT NULL, 
    role VARCHAR(10) NOT NULL -- 'ADMIN' or 'CUSTOMER'
);

-- 2. Create Rooms Table
CREATE TABLE rooms (
    room_number VARCHAR(10) PRIMARY KEY, 
    room_type VARCHAR(10) NOT NULL, -- 'SINGLE', 'DOUBLE', 'DELUXE'
    capacity INTEGER NOT NULL 
);

-- 3. Create Bookings Table
CREATE TABLE bookings (
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY, 
    user_id INTEGER REFERENCES users(id), 
    name VARCHAR(100) NOT NULL, 
    room_number VARCHAR(10) REFERENCES rooms(room_number), 
    room_type VARCHAR(10) NOT NULL, 
    persons INTEGER NOT NULL, 
    check_in DATE NOT NULL, 
    check_out DATE NOT NULL, 
    status VARCHAR(20) DEFAULT 'CONFIRMED'
);

-- 4. Seed Data (Add the 35 rooms)
INSERT INTO rooms VALUES ('101', 'SINGLE', 1), ('102', 'SINGLE', 1), ('201', 'DOUBLE', 2), ('301', 'DELUXE', 4);
-- Note: Continue this for all 35 rooms as per the guide.