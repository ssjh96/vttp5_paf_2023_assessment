package vttp2023.batch3.assessment.paf.bookings.controllers;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import vttp2023.batch3.assessment.paf.bookings.models.ListingSearch;
import vttp2023.batch3.assessment.paf.bookings.services.ListingsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;


@Controller
public class ListingsController {

	@Autowired
	private ListingsService listingsService;

	//TODO: Task 2
	// Display the landing page with the form
	@GetMapping(path = {"/", "/landing"})
	public String showLandingPage (Model model) 
	{
		List<String> countries = listingsService.getAllCountries();
		
		model.addAttribute("listingSearch", new ListingSearch());
		model.addAttribute("countries", countries);

		return "view1";
	}

	// Process the search
	@GetMapping(path = "/search")
	public String searchListings(@Valid @ModelAttribute("listingSearch") ListingSearch listingSearch,
								BindingResult bindingResult, Model model) 
	{
		// Handle validation errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("countries", listingsService.getAllCountries());
            return "view1"; // Back to View 1 with error messages
        }

		String country = listingSearch.getCountry();
		model.addAttribute("country", country);

		// Get filtered results from the service
		List<Document> filteredListingsDoc = listingsService.getFilteredResults(country, 
															listingSearch.getNoOfPeople(), 
															listingSearch.getPriceMin(), 
															listingSearch.getPriceMax());
		
		// If no listings found return an error message
		if(filteredListingsDoc.isEmpty())
		{
			model.addAttribute("errorMsg", "No accomodations found for given criteria.");
		}

		// Pass filtered results to View 2
		model.addAttribute("listings", filteredListingsDoc);

		return "view2";
	}
	
	

	//TODO: Task 3


	//TODO: Task 4
	

	//TODO: Task 5


}
