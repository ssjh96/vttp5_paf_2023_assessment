package vttp2023.batch3.assessment.paf.bookings.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vttp2023.batch3.assessment.paf.bookings.models.Booking;
import vttp2023.batch3.assessment.paf.bookings.repositories.ListingsRepository;

@Service
public class ListingsService 
{
	@Autowired
	private ListingsRepository listingsRepo;
		
	//TODO: Task 2
	public List<String> getAllCountries()
	{
		return listingsRepo.findDistinctCountries();
	}
	
	//TODO: Task 3
	public List<Document> getFilteredResults(String country, Integer noOfPeople, Double priceMin, Double priceMax)
	{
		Optional<Document> optDoc = listingsRepo.findListingsByCriteria(country, noOfPeople, priceMin, priceMax);

		if(optDoc.isEmpty())
		{
			return List.of(); // return empty list
		}

		Document filteredListingsDoc = optDoc.get();
		List<Document> filteredListings = filteredListingsDoc.getList("filtered", Document.class);

		return filteredListings;
	}

	//TODO: Task 4

	public Optional<Document> getListingDetailsById (String listingId)
	{
		Optional<Document> result = listingsRepo.findDetailsByListingId(listingId);

		return result;
	}
	

	//TODO: Task 5
	public Boolean checkVacancy (String listingId, Integer duration)
	{
		Integer vacancy = listingsRepo.checkVacancy(listingId);

		if ((vacancy - duration) < 0)
		{
			return false;
		}

		return true;
	}

	public Integer getVacancy (String listingId)
	{
		Integer vacancy = listingsRepo.checkVacancy(listingId);
		return vacancy;
	}

	@Transactional
	public String createReservation(String listingId, Booking booking)
	{
		String resvId = UUID.randomUUID().toString().substring(0, 8);

		listingsRepo.insertReservation(resvId, booking.getName(), booking.getEmail(), listingId, booking.getArrivalDate(), booking.getDuration());

		Integer newVacancy = getVacancy(listingId) - booking.getDuration();
		listingsRepo.updateResertvation(newVacancy, listingId);

		return resvId; // for displaying in task 5
	}
}
