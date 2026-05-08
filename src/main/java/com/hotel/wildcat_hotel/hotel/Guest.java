package com.hotel.wildcat_hotel.hotel;

import jakarta.persistence.*;

@Entity
@Table(name = "guest")
public class Guest implements RoomFees {

    @Id
    @Column(name = "guestID")
    private int guestID;

    @Column(name = "room_ID")
    private int roomID;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "city")
    private String city;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "check_in_date")
    private String checkInDate;

    @Column(name = "check_out_date")
    private String checkOutDate;

    @Column(name = "number_of_days")
    private int numberOfDays;

    @Column(name = "rate_per_night")
    private double ratePerNight;

    @Column(name = "total_fees")
    private double totalFees;

    public Guest(){}//Default constructor
    public Guest(int guestID, int roomID, String firstName, String lastName, String email, String phoneNo,
                 String city, String nationality, String checkInDate, String checkOutDate, int numberOfDays, double ratePerNight, double totalFees) {
        this.guestID = guestID;
        this.roomID = roomID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.city = city;
        this.nationality = nationality;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfDays = numberOfDays;
        this.ratePerNight = ratePerNight;
        this.totalFees = totalFees;
    }

    //Getters Methods
    public int getGuestID(){ return guestID;}
    public int getRoomID() { return roomID; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhoneNo() { return phoneNo; }
    public String getCity() { return city; }
    public String getNationality() { return nationality; }
    public String getCheckInDate() { return checkInDate; }
    public String getCheckOutDate() { return checkOutDate; }
    public int getNumberOfDays() { return numberOfDays; }
    public double getRatePerNight() { return ratePerNight; }
    public double getTotalFees() { return totalFees; }

    //Setters Methods
    public void setGuestID(int guestID) { this.guestID = guestID; }
    public void setRoomID(int roomID) { this.roomID = roomID; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo; }
    public void setCity(String city) { this.city = city; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public void setCheckInDate(String checkInDate) { this.checkInDate = checkInDate; }
    public void setCheckOutDate(String checkOutDate) { this.checkOutDate = checkOutDate; }
    public void setNumberOfDays(int numberOfDays) { this.numberOfDays = numberOfDays; }
    public void setRatePerNight(double ratePerNight) { this.ratePerNight = ratePerNight; }
    public void setTotalFees(double totalFees) { this.totalFees = totalFees; }  
    
    @Override
    public double CustomerRoomFees(Room room) {
        int days = (numberOfDays == 0) ? 1 : numberOfDays;
        return days * room.nightCost();
    }

    @Override
    public String toString() {
        return "Guest{name=" + firstName + " " + lastName + ", room=" + roomID + ", days=" + numberOfDays + "}";
    }
}
