package com.hotel.wildcat_hotel.hotel;

import jakarta.persistence.*;

@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roomID")
    private int roomID;

    @Column(name = "room_type", nullable = false)
    private String roomType;

    @Column(name = "room_capacity", nullable = false)
    private String roomCapacity;

    @Column(name = "room_rate", nullable = false)
    private double roomRate;

    // ENUM in DB: 'AVAILABLE' | 'OCCUPIED'
    @Column(name = "status", nullable = false)
    private String status;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Room() {}

    public Room(int roomID, String roomType, String roomCapacity,
                double roomRate, String status) {
        this.roomID       = roomID;
        this.roomType     = roomType;
        this.roomCapacity = roomCapacity;
        this.roomRate     = roomRate;
        this.status       = status;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public int    getRoomID()       { return roomID; }
    public String getRoomType()     { return roomType; }
    public String getRoomCapacity() { return roomCapacity; }
    public double getRoomRate()     { return roomRate; }
    public String getStatus()       { return status; }

    /** Convenience: true when room is available for booking */
    public boolean isAvailable()    { return "AVAILABLE".equalsIgnoreCase(status); }

    // ── Setters ───────────────────────────────────────────────────────────────

    public void setRoomID(int roomID)               { this.roomID       = roomID; }
    public void setRoomType(String roomType)         { this.roomType     = roomType; }
    public void setRoomCapacity(String roomCapacity) { this.roomCapacity = roomCapacity; }
    public void setRoomRate(double roomRate)         { this.roomRate     = roomRate; }

    /**
     * Only 'AVAILABLE' or 'OCCUPIED' are valid (mirrors the DB ENUM).
     */
    public void setStatus(String status) {
        if (!"AVAILABLE".equalsIgnoreCase(status) && !"OCCUPIED".equalsIgnoreCase(status)) {
            throw new IllegalArgumentException(
                    "Invalid room status: '" + status + "'. Use 'AVAILABLE' or 'OCCUPIED'.");
        }
        this.status = status.toUpperCase();
    }

    // ── toString ──────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return "Room{id=" + roomID
                + ", type=" + roomType
                + ", capacity=" + roomCapacity
                + ", rate=" + roomRate
                + ", status=" + status + "}";
    }
}