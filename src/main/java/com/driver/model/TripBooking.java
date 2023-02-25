package com.driver.model;

import javax.persistence.*;

@Entity
@Table(name = "tripBooking")
public class TripBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tripBookingId;
    private String fromLocation;
    private String toLocation;

    private int distanceInKm;

    @Enumerated(value = EnumType.STRING)
    private TripStatus Status;

    private int bill;

    //it is an child for Driver and Customer it is visible for both

    //driver can have many tripbookings

    @ManyToOne
    @JoinColumn
    private Driver driver;

    // customer have also many tripbookings

    @ManyToOne
    @JoinColumn
    private Customer customer;

    public TripBooking() {
    }

    public TripBooking(String fromLocation, String toLocation, int distanceInKm, TripStatus status, int bill, Driver driver, Customer customer) {
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.distanceInKm = distanceInKm;
        Status = status;
        this.bill = bill;
        this.driver = driver;
        this.customer = customer;
    }

    public TripBooking(int tripBookingId, String fromLocation, String toLocation, int distanceInKm, TripStatus Status, int bill, Driver driver, Customer customer) {
        this.tripBookingId = tripBookingId;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.distanceInKm = distanceInKm;
        this.Status = Status;
        this.bill = bill;
        this.driver = driver;
        this.customer = customer;
    }

    public int getTripBookingId() {
        return tripBookingId;
    }

    public void setTripBookingId(int tripBookingId) {
        this.tripBookingId = tripBookingId;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public int getDistanceInKm() {
        return distanceInKm;
    }

    public void setDistanceInKm(int distanceInKm) {
        this.distanceInKm = distanceInKm;
    }

    public TripStatus getStatus() {
        return Status;
    }

    public void setStatus(TripStatus status) {
        this.Status = status;
    }

    public int getBill() {
        return bill;
    }

    public void setBill(int bill) {
        this.bill = bill;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}