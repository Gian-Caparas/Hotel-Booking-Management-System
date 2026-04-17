package com.hotel.wildcat_hotel.model;

public class Room {
    private String roomNumber;
    private String roomType; 
    private int capacity;

    public Room(String roomNumber, String roomType, int capacity) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.capacity = capacity;
    }

    // Getters and Setters
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String r) { this.roomNumber = r; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String t) { this.roomType = t; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int c) { this.capacity = c; }
}