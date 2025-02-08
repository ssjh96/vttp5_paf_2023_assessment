package vttp2023.batch3.assessment.paf.bookings.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp2023.batch3.assessment.paf.bookings.Utils.MongoParams;


@Repository
public class ListingsRepository 
{

	@Autowired
	private MongoTemplate template;

	//TODO: Task 2
	// db.listings.distinct('address.country')
	public List<String> findDistinctCountries()
	{
		// return template.findDistinct(new Query(), MongoParams.F_ADDRESS_COUNTRIES, MongoParams.C_LISTINGS, String.class);
		List<String> countriesList = template.findDistinct(new Query(), MongoParams.F_COUNTRY, MongoParams.C_LISTINGS, String.class);

		System.out.println(">>> Countries List: " + countriesList);

		return countriesList;
	}
	
	//TODO: Task 3


	//TODO: Task 4
	

	//TODO: Task 5


}
