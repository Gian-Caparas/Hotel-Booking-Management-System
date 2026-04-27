package com.hotel.wildcat_hotel.hotel;

import jakarta.persistence.*;

@Entity
@Table(name = "guest")
public class Guest implements RoomFees {

    @Id
    @Column(name = "passport_Number")
    private String passportNumber;

    @Column(name = "room_ID")
    private int roomID;

    @Column(name = "Name")
    private String name;

    @Column(name = "Email")
    private String email;

    @Column(name = "Address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "Nationality")
    private String nationality;

    @Column(name = "phoneNo")
    private String phoneNo;

    @Column(name = "Card_Number")
    private String cardNumber;

    @Column(name = "card_Pass")
    private String cardPass;

    @Column(name = "number_Of_Days")
    private int numberOfDays;

    @Column(name = "fees")
    private double fees;

    public Guest() {}

    public Guest(int roomID, int numberOfDays, String name, String email, String address,
                 String city, String nationality, String passportNumber,
                 String phoneNo, String cardNumber, String cardPass, double fees) {
        this.roomID = roomID;
        this.numberOfDays = numberOfDays;
        this.name = name;
        this.email = email;
        this.address = address;
        this.city = city;
        this.nationality = nationality;
        this.passportNumber = passportNumber;
        this.phoneNo = phoneNo;
        this.cardNumber = cardNumber;
        this.cardPass = cardPass;
        this.fees = fees;
    }

    // Getters & Setters
    public String getPassportNumber()                       { return passportNumber; }
    public void setPassportNumber(String passportNumber)    { this.passportNumber = passportNumber; }
    public int getRoomID()                                  { return roomID; }
    public void setRoomID(int roomID)                       { this.roomID = roomID; }
    public String getName()                                 { return name; }
    public void setName(String name)                        { this.name = name; }
    public String getEmail()                                { return email; }
    public void setEmail(String email)                      { this.email = email; }
    public String getAddress()                              { return address; }
    public void setAddress(String address)                  { this.address = address; }
    public String getCity()                                 { return city; }
    public void setCity(String city)                        { this.city = city; }
    public String getNationality()                          { return nationality; }
    public void setNationality(String nationality)          { this.nationality = nationality; }
    public String getPhoneNo()                              { return phoneNo; }
    public void setPhoneNo(String phoneNo)                  { this.phoneNo = phoneNo; }
    public String getCardNumber()                           { return cardNumber; }
    public void setCardNumber(String cardNumber)            { this.cardNumber = cardNumber; }
    public String getCardPass()                             { return cardPass; }
    public void setCardPass(String cardPass)                { this.cardPass = cardPass; }
    public int getNumberOfDays()                            { return numberOfDays; }
    public void setNumberOfDays(int numberOfDays)           { this.numberOfDays = numberOfDays; }
    public double getFees()                                 { return fees; }
    public void setFees(double fees)                        { this.fees = fees; }

    @Override
    public double CustomerRoomFees(Room room) {
        int days = (numberOfDays == 0) ? 1 : numberOfDays;
        return days * room.nightCost();
    }

    @Override
    public String toString() {
        return "Guest{name=" + name + ", room=" + roomID + ", days=" + numberOfDays + "}";
    }
}
