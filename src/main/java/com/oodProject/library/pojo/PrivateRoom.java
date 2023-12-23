package com.oodProject.library.pojo;

import java.time.LocalDateTime;

public class PrivateRoom {
	private int roomId;
	private Member rentedBy;
	private LocalDateTime fromDateTime;
	private LocalDateTime toDateTime;
	
    private int roomNumber;
    private boolean isOccupied;

    public PrivateRoom( int roomId, int roomNumber) {
        this.roomId=roomId;
        this.roomNumber = roomNumber;
        this.isOccupied = false;
        this.rentedBy=null;
        this.fromDateTime=null;
        this.toDateTime=null;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
    
    public Member getRentedBy()
    {
    	return this.rentedBy;
    }
    
    public void setRentedBy(Member rentedBy)
    {
    	this.rentedBy = rentedBy;
    }
    
    public int getRoomId()
    {
    	return this.roomId;
    }
    
    public void setRoomId(int roomId)
    {
    	this.roomId = roomId;
    }
    
    public LocalDateTime getFromDateTime()
    {
    	return this.fromDateTime;
    }
    
    public void setFromDateTime(LocalDateTime fromDateTime)
    {
    	this.fromDateTime=fromDateTime;
    }
    
    public LocalDateTime getToDateTime()
    {
    	return this.toDateTime;
    }
    
    public void setToDateTime(LocalDateTime toDateTime)
    {
    	this.toDateTime=toDateTime;
    }

    @Override
    public String toString() {
        return "Room Number: " + roomNumber + ", Occupied: " + isOccupied + "By: " + rentedBy;
    }
}
