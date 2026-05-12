package com.hotel.wildcat_hotel.hotel;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservationID")
    private int reservationID;

    // FK → guest(guestID)
    @Column(name = "guestID", nullable = false)
    private int guestID;

    // FK → room(roomID)
    @Column(name = "roomID", nullable = false)
    private int roomID;

    @Column(name = "check_in_date", nullable = false)
    private Timestamp checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private Timestamp checkOutDate;

    @Column(name = "number_of_days", nullable = false)
    private int numberOfDays;

    @Column(name = "total_cost", nullable = false)
    private double totalCost;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Reservation() {}

    public Reservation(int guestID, int roomID,
                       Timestamp checkInDate, Timestamp checkOutDate,
                       int numberOfDays, double totalCost) {
        this.guestID      = guestID;
        this.roomID       = roomID;
        this.checkInDate  = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfDays = numberOfDays;
        this.totalCost    = totalCost;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public int       getReservationID() { return reservationID; }
    public int       getGuestID()       { return guestID; }
    public int       getRoomID()        { return roomID; }
    public Timestamp getCheckInDate()   { return checkInDate; }
    public Timestamp getCheckOutDate()  { return checkOutDate; }
    public int       getNumberOfDays()  { return numberOfDays; }
    public double    getTotalCost()     { return totalCost; }

    // ── Setters ───────────────────────────────────────────────────────────────

    public void setReservationID(int reservationID)   { this.reservationID = reservationID; }
    public void setGuestID(int guestID)               { this.guestID       = guestID; }
    public void setRoomID(int roomID)                 { this.roomID        = roomID; }
    public void setCheckInDate(Timestamp checkInDate) { this.checkInDate   = checkInDate; }
    public void setCheckOutDate(Timestamp checkOutDate){ this.checkOutDate  = checkOutDate; }
    public void setNumberOfDays(int numberOfDays)     { this.numberOfDays  = numberOfDays; }
    public void setTotalCost(double totalCost)        { this.totalCost     = totalCost; }

    // ── toString ──────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return "Reservation{id=" + reservationID
                + ", guestID=" + guestID
                + ", roomID=" + roomID
                + ", checkIn=" + checkInDate
                + ", checkOut=" + checkOutDate
                + ", days=" + numberOfDays
                + ", total=₱" + totalCost + "}";
    }
}