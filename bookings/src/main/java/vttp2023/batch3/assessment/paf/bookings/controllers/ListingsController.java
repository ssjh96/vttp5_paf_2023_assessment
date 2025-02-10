package vttp2023.batch3.assessment.paf.bookings.controllers;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;

import vttp2023.batch3.assessment.paf.bookings.models.Booking;
import vttp2023.batch3.assessment.paf.bookings.models.ListingSearch;
import vttp2023.batch3.assessment.paf.bookings.services.ListingsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



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



	//TODO: Task 3
	// Process the search
	@GetMapping(path = "/search")
	public String searchListings(@Valid @ModelAttribute("listingSearch") ListingSearch listingSearch,
								BindingResult bindingResult, Model model, HttpSession httpSession) 
	{
		// Handle validation errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("countries", listingsService.getAllCountries());
            return "view1"; // Back to View 1 with error messages
        }

		// Save search criterias in session for backtracking
		httpSession.setAttribute("listingSearch", listingSearch);

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

	//TODO: Task 4
	@GetMapping(path = "/details")
	public String getListingDetails(@RequestParam ("listingId") String listingId, HttpSession httpSession, Model model) 
	{
		// Retrieve the criterias 
		// For repopulating if detailOptDoc is empty 
		// & for backtracking from view 3 to view 2
		ListingSearch listingSearch = (ListingSearch) httpSession.getAttribute("listingSearch");
		model.addAttribute("listingSearch", listingSearch); // added for back button to backtrack

		// retrieve the listing details
		Optional<Document> detailsOptDoc = listingsService.getListingDetailsById(listingId);

		if(detailsOptDoc.isEmpty())
		{
			// repopulate view2
			String country = listingSearch.getCountry();
			List<Document> filteredListingsDoc = listingsService.getFilteredResults(country, 
															listingSearch.getNoOfPeople(), 
															listingSearch.getPriceMin(), 
															listingSearch.getPriceMax());
			
			model.addAttribute("country", country);
			model.addAttribute("listings", filteredListingsDoc);
			
			//add error message for empty details
			model.addAttribute("errorMsg", "listing details not found for given listing, id: %s".formatted(listingId));

			return "view2";
		}

		// Populate view 3 with the details
		Document detailsDoc = detailsOptDoc.get();
		model.addAttribute("details", detailsDoc);
		httpSession.setAttribute("details", detailsDoc); // store in session

		// Add empty booking for posting in task 5
		model.addAttribute("booking", new Booking());

		return "view3";
	}
	
	
	//TODO: Task 5
	@PostMapping(path = "/book/{id}")
	public String postBooking(@PathVariable ("id") String listingId, 
								@Valid @ModelAttribute("booking") Booking booking, 
								BindingResult bindingResult, Model model,
								HttpSession httpSession) 
	{
		// If posting form has errors
		if(bindingResult.hasErrors())
		{
			// For view 3 back to view 2 (back button)
			ListingSearch listingSearch = (ListingSearch) httpSession.getAttribute("listingSearch");
			model.addAttribute("listingSearch", listingSearch); // added for back button to backtrack

			// repopulate with details doc
			Document detailsDoc = (Document) httpSession.getAttribute("details");
			model.addAttribute("details", detailsDoc);

			return "view3"; // return view 3 with errors
		}

		// If no vacancy
		if (!listingsService.checkVacancy(listingId, booking.getDuration()))
		{
			// For view 3 back to view 2 (back button)
			ListingSearch listingSearch = (ListingSearch) httpSession.getAttribute("listingSearch");
			model.addAttribute("listingSearch", listingSearch); // added for back button to backtrack

			// repopulate with details doc
			Document detailsDoc = (Document) httpSession.getAttribute("details");
			model.addAttribute("details", detailsDoc);

			// Add ErrorMsg
			model.addAttribute("errorMsg", "No Vancancy...");

			return "view3"; // return view 3 with errors
		}

		String resvId = listingsService.createReservation(listingId, booking);

		model.addAttribute("resvId", resvId);

		return "view4";
	}
	

}
