package com.iconicsbooking.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iconicsbooking.exception.CompanyNotFoundException;
import com.iconicsbooking.exception.EventsNotFoundException;
import com.iconicsbooking.exception.IdNotFoundException;
import com.iconicsbooking.model.Availability;
import com.iconicsbooking.model.Events;
import com.iconicsbooking.model.IconicBooking;
import com.iconicsbooking.model.Status;
import com.iconicsbooking.repository.IconicBookingRepository;
@Service
public class IconicBookingServiceImpl implements IconicBookingService {
@Autowired
IconicBookingRepository iconicBookingRepository;

@Autowired
RestTemplate restTemplate;

private final String  BASEURL= "http://localhost:8082/events-api";
	
@Override
	public IconicBooking addComapny(IconicBooking company) {
		return iconicBookingRepository.save(company);
	}

	@Override
	public IconicBooking updateCompany(IconicBooking company) {
		return iconicBookingRepository.save(company);		
	}

	@Override
	public void deleteCompany(int companyId) {
		iconicBookingRepository.deleteById(companyId);
	}

	@Override
	public List<IconicBooking> getAll() {
		return iconicBookingRepository.findAll();
	}
	
	@Override
	public IconicBooking getById(int companyId) throws IdNotFoundException{
		IconicBooking bookingById= iconicBookingRepository.findById(companyId).get();
		if(bookingById== null)
			throw new IdNotFoundException("no company id found");
		return bookingById;
	}

	@Override
	public List<IconicBooking> getByCompanyName(String companyName) throws CompanyNotFoundException {
		List<IconicBooking> companyListByName = iconicBookingRepository.findByCompanyName(companyName);
		if(companyListByName.isEmpty())
			throw new CompanyNotFoundException("no company name found");
		return companyListByName;
	
	}

	@Override
	public List<IconicBooking> getByOwnerName(String ownerName) throws CompanyNotFoundException {
		List<IconicBooking> companyOwnerName= iconicBookingRepository.findByOwnerName(ownerName);
		if(companyOwnerName.isEmpty())
			throw new CompanyNotFoundException("No owner name found");
		return companyOwnerName;
	}

	@Override
	public List<Events> getByCompanyId(int companyId) throws CompanyNotFoundException {
		String url= BASEURL+"/events/companyId/"+companyId;
		ResponseEntity<List> eventByProvider= restTemplate.getForEntity(url, List.class);
		return eventByProvider.getBody();
	}
	@Override
	public List<IconicBooking> getByRating(double rating) throws CompanyNotFoundException{
		List<IconicBooking> companyByRating= iconicBookingRepository.findByRatingLessThan(rating);
		if(companyByRating.isEmpty())
			throw new CompanyNotFoundException("rating no found");
		return companyByRating;
	}

	@Override
	@JsonProperty
	public Events addEvent(Events events, int companyId) {
		System.out.println(events);
		String url=BASEURL+"/events/companyId/"+companyId;
		
		
		 HttpHeaders requestHeaders = new HttpHeaders();
	        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
	        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	        HttpEntity<Events> requestEntity = new HttpEntity<>(events, requestHeaders);
	        ResponseEntity<Events> responseEntity = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                requestEntity,
	                Events.class
	        );
	        
	        return responseEntity.getBody();
	}

	@Override
	public List<Events> getAllEvents() {
		String url=BASEURL+"/events";
	//List<Events> allEvent= restTemplate.getForObject(url,List.class);
		ResponseEntity<List> allEvent= restTemplate.getForEntity(url, List.class);
		return allEvent.getBody();
	}
	//http://localhost:8082/events-api
	@Override
	public Events getByEventId(int eventId) throws EventsNotFoundException {
		String url= BASEURL+"/events/eventId/"+eventId;
		Events eventById= restTemplate.getForObject(url, Events.class);
		return eventById;
	}
	//http://localhost:8082/events-api
	@Override
	public List<Events> getByEventProvider(String eventProvider) {
		String url= BASEURL+"/events/eventProvider/"+eventProvider;
		ResponseEntity<List> eventByProvider= restTemplate.getForEntity(url, List.class);
		return eventByProvider.getBody();
	}

	@Override
	public List<Events> getByEventName(String eventName) throws EventsNotFoundException{
		String url= BASEURL+"/events/eventName/"+eventName;
		ResponseEntity<List> eventByName= restTemplate.getForEntity(url, List.class);
		return eventByName.getBody();
	}

	@Override
	public List<Events> getByStartDate(String startDate) {
		String url= BASEURL+"/events/startDate/"+startDate;
		ResponseEntity<List> eventByStartDate= restTemplate.getForEntity(url, List.class);
		return eventByStartDate.getBody();
	}

	@Override
	public List<Events> getByEndDate(String endDate) {
		String url= BASEURL+"/events/endDate/"+endDate;
		ResponseEntity<List> eventByEndDate= restTemplate.getForEntity(url, List.class);
		return eventByEndDate.getBody();
	}

	@Override
	public List<Events> getByStatus(String status) {
		String url= BASEURL+"/events/status/"+status;
		ResponseEntity<List> eventByStatus= restTemplate.getForEntity(url, List.class);
		return eventByStatus.getBody();
	}

	@Override
	public List<Events> getByPrice(double price) {
		String url= BASEURL+"/events/price/"+price;
		ResponseEntity<List> eventByPrice= restTemplate.getForEntity(url, List.class);
		return eventByPrice.getBody();
	}

	@Override
	public List<Events> getByPriority(String priority) {
		String url= BASEURL+"/events/priority/"+priority;
		ResponseEntity<List> eventByPriority= restTemplate.getForEntity(url, List.class);
		return eventByPriority.getBody();
	}

	@Override
	public String assignEvent(int eventId) {
		
		String urlForEventById= BASEURL+"/events/eventId/"+eventId;
		Events eventById= restTemplate.getForEntity(urlForEventById, Events.class).getBody();
		
		eventById.setStatus(Status.IN_PROGRESS);
		eventById.setAvailability(Availability.NOT_AVAILABLE);
		//System.out.println(eventById.getIconicBooking().getCompanyId());
		String url= BASEURL+"/events";
		
		
		HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Events> requestEntity = new HttpEntity<>(eventById, requestHeaders);
        ResponseEntity<Events> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                Events.class
        );
		
		
		System.out.println(responseEntity.getStatusCodeValue());
		System.out.println(responseEntity.getBody());
		
		
		 //restTemplate.put(url, eventById);
		 return "event assigned SuccessFully";

	}

	@Override
	public String freeEvent(int eventId) throws EventsNotFoundException {
		String urlForEventById= BASEURL+"/events/eventId/"+eventId;
		Events eventById= restTemplate.getForEntity(urlForEventById, Events.class).getBody();
		System.out.println(eventById);
		eventById.setStatus(Status.NOT_STARTED);
		eventById.setAvailability(Availability.AVAILABLE);
//		eventById.setIconicBooking(eventById.getIconicBooking().getCompanyId());
	
		//System.out.println(eventById.getIconicBooking().getCompanyId());
		String url= BASEURL+"/events";
		
		
		HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Events> requestEntity = new HttpEntity<>(eventById, requestHeaders);
        ResponseEntity<Events> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                Events.class
        );
		
		
		System.out.println(responseEntity.getStatusCodeValue());
		System.out.println(responseEntity.getBody());
		
		
		 //restTemplate.put(url, eventById);
		 return "event is set to be Free";
	}
	
	

}
