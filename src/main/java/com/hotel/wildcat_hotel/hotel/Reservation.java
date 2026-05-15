package com.hotel.wildcat_hotel.hotel;

import com.hotel.wildcat_hotel.core.BaseEntity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "reservation")
@AttributeOverride(name = "entityId", column = @Column(name = "reservationID"))
public class Reservation extends BaseEntity {

    public static final String STATUS_ACTIVE = "Active";
    public static final String STATUS_CANCELLED = "Cancelled";

    // FK → guest(guestID)
    @Column(name = "guestID", nullable = false)
    private int guestID;

    // FK → room(roomID)
    @Column(name = "roomID", nullable = false)
    private int roomID;

    @Column(name = "userID", nullable = false)
    private int userID;

    @Column(name = "check_in_date", nullable = false)
    private Timestamp checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private Timestamp checkOutDate;

    @Column(name = "number_of_days", nullable = false)
    private int numberOfDays;

    @Column(name = "total_cost", nullable = false)
    private double totalCost;

    @Column(name = "status", nullable = false)
    private String status = STATUS_ACTIVE;

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
        this.userID       = 0; // default (should be set by callers when a user is available)
        this.status       = STATUS_ACTIVE;
    }

    public Reservation(int guestID, int roomID, int userID,
                       Timestamp checkInDate, Timestamp checkOutDate,
                       int numberOfDays, double totalCost) {
        this.guestID      = guestID;
        this.roomID       = roomID;
        this.userID       = userID;
        this.checkInDate  = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfDays = numberOfDays;
        this.totalCost    = totalCost;
        this.status       = STATUS_ACTIVE;
    }

    public Reservation(int guestID, int roomID, int userID,
                       Timestamp checkInDate, Timestamp checkOutDate,
                       int numberOfDays, double totalCost, String status) {
        this.guestID      = guestID;
        this.roomID       = roomID;
        this.userID       = userID;
        this.checkInDate  = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfDays = numberOfDays;
        this.totalCost    = totalCost;
        setStatus(status);
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public int       getReservationID() { return getEntityId(); }
    public int       getGuestID()       { return guestID; }
    public int       getRoomID()        { return roomID; }
    public int       getUserID()        { return userID; }
    public Timestamp getCheckInDate()   { return checkInDate; }
    public Timestamp getCheckOutDate()  { return checkOutDate; }
    public int       getNumberOfDays()  { return numberOfDays; }
    public double    getTotalCost()     { return totalCost; }
    public String    getStatus()        { return status; }

    // ── Setters ───────────────────────────────────────────────────────────────

    public void setReservationID(int reservationID)   { setEntityId(reservationID); }
    public void setGuestID(int guestID)               { this.guestID       = guestID; }
    public void setRoomID(int roomID)                 { this.roomID        = roomID; }
    public void setUserID(int userID)                 { this.userID        = userID; }
    public void setCheckInDate(Timestamp checkInDate) { this.checkInDate   = checkInDate; }
    public void setCheckOutDate(Timestamp checkOutDate){ this.checkOutDate  = checkOutDate; }
    public void setNumberOfDays(int numberOfDays)     { this.numberOfDays  = numberOfDays; }
    public void setTotalCost(double totalCost)        { this.totalCost     = totalCost; }
    public void setStatus(String status) {
        if (status == null) {
            throw new IllegalArgumentException("Reservation status must not be null.");
        }
        if (STATUS_ACTIVE.equalsIgnoreCase(status)) {
            this.status = STATUS_ACTIVE;
            return;
        }
        if (STATUS_CANCELLED.equalsIgnoreCase(status)) {
            this.status = STATUS_CANCELLED;
            return;
        }
        throw new IllegalArgumentException("Reservation status must be Active or Cancelled.");
    }

    // ── toString ──────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return "Reservation{id=" + getReservationID()
                + ", guestID=" + guestID
                + ", roomID=" + roomID
                + ", userID=" + userID
                + ", checkIn=" + checkInDate
                + ", checkOut=" + checkOutDate
                + ", days=" + numberOfDays
                + ", status=" + status
                + ", total=₱" + totalCost + "}";
    }
}