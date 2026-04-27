package com.hotel.wildcat_hotel.hotel;

import jakarta.persistence.*;
import com.hotel.wildcat_hotel.project.DataBase;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roomID")
    private int roomID;

    @Column(name = "room_Type")
    private String roomType;

    @Column(name = "room_capacity")
    private String roomCapacity;

    @Column(name = "Check_In_Date")
    @Temporal(TemporalType.DATE)
    private Date checkInDate;

    @Column(name = "Check_Out_Date")
    @Temporal(TemporalType.DATE)
    private Date checkOutDate;

    @Column(name = "isEmpty")
    private boolean isEmpty;

    public Room() {}

    public Room(int roomID, String roomType, String roomCapacity, Date checkInDate, Date checkOutDate, boolean isEmpty) {
        this.roomID = roomID;
        this.roomType = roomType;
        this.roomCapacity = roomCapacity;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.isEmpty = isEmpty;
    }

    // Getters
    public int getRoomID()           { return roomID; }
    public String getRoomType()      { return roomType; }
    public String getRoomCapacity()  { return roomCapacity; }
    public Date getCheckInDate()     { return checkInDate; }
    public Date getCheckOutDate()    { return checkOutDate; }
    public boolean isEmpty()         { return isEmpty; }

    // Setters
    public void setRoomID(int roomID)               { this.roomID = roomID; }
    public void setRoomType(String roomType)         { this.roomType = roomType; }
    public void setRoomCapacity(String roomCapacity) { this.roomCapacity = roomCapacity; }
    public void setCheckInDate(Date checkInDate)     { this.checkInDate = checkInDate; }
    public void setCheckOutDate(Date checkOutDate)   { this.checkOutDate = checkOutDate; }
    public void setEmpty(boolean isEmpty)            { this.isEmpty = isEmpty; }

    // Pricing logic
    public double nightCost() {
        if ("Economy".equals(roomType)  && "Single".equals(roomCapacity)) return 50;
        if ("Economy".equals(roomType)  && "Double".equals(roomCapacity)) return 80;
        if ("Economy".equals(roomType)  && "Family".equals(roomCapacity)) return 120;
        if ("Standard".equals(roomType) && "Single".equals(roomCapacity)) return 70;
        if ("Standard".equals(roomType) && "Double".equals(roomCapacity)) return 110;
        if ("Standard".equals(roomType) && "Family".equals(roomCapacity)) return 160;
        if ("Deluxe".equals(roomType)   && "Single".equals(roomCapacity)) return 100;
        if ("Deluxe".equals(roomType)   && "Double".equals(roomCapacity)) return 150;
        if ("Deluxe".equals(roomType)   && "Family".equals(roomCapacity)) return 220;
        return 0;
    }

    // Find first vacant room matching type and capacity
    public static Room findAvailableRoom(String roomType, String roomCapacity) {
        List<Room> rooms = DataBase.getAvailableRooms();
        for (Room room : rooms) {
            if (room.isEmpty()
                    && room.getRoomType().equals(roomType)
                    && room.getRoomCapacity().equals(roomCapacity)) {
                return room;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Room{id=" + roomID + ", type=" + roomType + ", capacity=" + roomCapacity + ", empty=" + isEmpty + "}";
    }
}
