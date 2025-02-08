package vttp2023.batch3.assessment.paf.bookings.services;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	

	//TODO: Task 5


}
