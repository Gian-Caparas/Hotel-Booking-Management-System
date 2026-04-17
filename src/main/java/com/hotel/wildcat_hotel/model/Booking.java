package com.hotel.wildcat_hotel.model;

public class Booking {
    private int id;
    private String name; 
    private String roomNumber; 
    private String roomType; 
    private int persons; 
    private String checkIn; // YYYY-MM-DD 
    private String checkOut; // YYYY-MM-DD 

    // Default constructor
    public Booking() {}

    public Booking(String name, String roomNumber, String roomType, int persons, String checkIn, String checkOut) {
        this.name = name;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.persons = persons;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    // Getters & Setters [cite: 125, 126]
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String r) { this.roomNumber = r; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String t) { this.roomType = t; }
    public int getPersons() { return persons; }
    public void setPersons(int p) { this.persons = p; }
    public String getCheckIn() { return checkIn; }
    public void setCheckIn(String d) { this.checkIn = d; }
    public String getCheckOut() { return checkOut; }
    public void setCheckOut(String d) { this.checkOut = d; }
}