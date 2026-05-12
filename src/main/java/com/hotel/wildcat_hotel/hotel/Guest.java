package com.hotel.wildcat_hotel.hotel;

import jakarta.persistence.*;

@Entity
@Table(name = "guest")
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guestID")
    private int guestID;

    // FK → room(roomID)
    @Column(name = "roomID", nullable = false)
    private int roomID;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_no", nullable = false)
    private String phoneNo;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "nationality", nullable = false)
    private String nationality;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Guest() {}

    public Guest(int roomID, String firstName, String lastName,
                 String email, String phoneNo, String city, String nationality) {
        this.roomID      = roomID;
        this.firstName   = firstName;
        this.lastName    = lastName;
        this.email       = email;
        this.phoneNo     = phoneNo;
        this.city        = city;
        this.nationality = nationality;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public int    getGuestID()     { return guestID; }
    public int    getRoomID()      { return roomID; }
    public String getFirstName()   { return firstName; }
    public String getLastName()    { return lastName; }
    public String getEmail()       { return email; }
    public String getPhoneNo()     { return phoneNo; }
    public String getCity()        { return city; }
    public String getNationality() { return nationality; }

    // ── Setters ───────────────────────────────────────────────────────────────

    public void setGuestID(int guestID)           { this.guestID     = guestID; }
    public void setRoomID(int roomID)             { this.roomID      = roomID; }
    public void setFirstName(String firstName)    { this.firstName   = firstName; }
    public void setLastName(String lastName)      { this.lastName    = lastName; }
    public void setEmail(String email)            { this.email       = email; }
    public void setPhoneNo(String phoneNo)        { this.phoneNo     = phoneNo; }
    public void setCity(String city)              { this.city        = city; }
    public void setNationality(String nationality){ this.nationality = nationality; }

    // ── toString ──────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return "Guest{id=" + guestID
                + ", name=" + firstName + " " + lastName
                + ", room=" + roomID + "}";
    }
}