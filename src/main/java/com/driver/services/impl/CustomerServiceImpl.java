package com.driver.services.impl;

import com.driver.model.*;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;

import java.util.List;

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
		Customer customer = customerRepository2.findById(customerId).get();
		List<TripBooking> tripBookingList = customer.getTripBookingList();

		for (TripBooking tripBooking:tripBookingList)
		{
			if (tripBooking.getStatus() == TripStatus.CONFIRMED)
				tripBooking.setStatus(TripStatus.CANCELED);
		}
		customerRepository2.delete(customer);
	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		TripBooking tripBooking;
		Customer customer = customerRepository2.findById(customerId).get();
		//to set the trip we find the driver as per our requirement

		Driver driver= null;
		List<Driver>driverList = driverRepository2.findAll();

		for (Driver driver1:driverList)
		{
			if(driver1.getCab().getAvailable())
			{
				if(driver==null || driver1.getDriverId() < driver.getDriverId())
					driver=driver1;
			}
		}

		if (driver ==null)
			throw new Exception("No cab available!");

		driver.getCab().setAvailable(false);

		//calculate the bill
		int rate= driver.getCab().getPerKmRate();
		int bill= rate*distanceInKm;
		tripBooking = new TripBooking(fromLocation,toLocation,distanceInKm,TripStatus.CONFIRMED,bill,driver,customer);

		//add tripbooking in customers list and update the customer in customerrepository
		customer.getTripBookingList().add(tripBooking);
		customerRepository2.save(customer);

		//add tripbooking in drivers list and update the driverrepository
		driver.getTripBookingList().add(tripBooking);
		driverRepository2.save(driver);

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
		int distance = tripBooking.getDistanceInKm();


		int driverId= tripBooking.getDriver().getDriverId();
		Cab cab= driverRepository2.findById(driverId).get().getCab();
		int rate=cab.getPerKmRate();

		tripBooking.setBill(distance*rate);

		cab.setAvailable(true);
		tripBookingRepository2.save(tripBooking);

	}
}
