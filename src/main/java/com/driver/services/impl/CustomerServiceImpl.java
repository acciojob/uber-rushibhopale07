package com.driver.services.impl;

import com.driver.model.*;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		customerRepository2.deleteById(customerId);
	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		TripBooking tripBooking=new TripBooking();
		tripBooking.setStatus(TripStatus.CONFIRMED);
		Driver driver= tripBooking.getDriver();
		int driverId= driver.getDriverId();
		Cab cab= driverRepository2.findById(driverId).get().getCab();
		cab.setAvailable(false);
		return tripBooking;
	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking= tripBookingRepository2.findById(tripId).get();
		tripBooking.setStatus(TripStatus.CANCELED);
		Driver driver= tripBooking.getDriver();
		int driverId= driver.getDriverId();
		Cab cab= driverRepository2.findById(driverId).get().getCab();
		cab.setAvailable(true);
		tripBookingRepository2.save(tripBooking);
		driverRepository2.save(driver);
	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking= tripBookingRepository2.findById(tripId).get();
		tripBooking.setStatus(TripStatus.COMPLETED);
		Driver driver= tripBooking.getDriver();
		int driverId= driver.getDriverId();
		Cab cab= driverRepository2.findById(driverId).get().getCab();
		cab.setAvailable(true);
		tripBookingRepository2.save(tripBooking);
		driverRepository2.save(driver);
	}
}
