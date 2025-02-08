package vttp2023.batch3.assessment.paf.bookings.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;

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
	// db.listings.aggregate([
	// 	{ $match: { 
	// 		'address.country': { $regex: 'australia', $options :'i' },
	// 		accommodates: {$gte: 1},
	// 		price: { $gte: 1, $lte: 500 } }  
	// 	},
	// 	{ $sort: { price: 1 } }, // sort ascending order
	// 	{ $group: { 
	// 		_id: '$address.country',
	// 		filtered: { 
	// 			$push : { 
	//				id: '$_id',
	// 				name: '$name', 
	// 				price: '$price', 
	// 				image: '$images.picture_url'}
	// 			}
	// 	}}
	// ])
	public Optional<Document> findListingsByCriteria(String country, int noOfPeople, double priceMin, double priceMax)
	{
		// Define match criteria
		Criteria criteria = Criteria.where(MongoParams.F_COUNTRY)
									.regex(country, "i") // Match country (case-insensitive)
									.and(MongoParams.F_ACCOMODATES).gte(noOfPeople) // Match accomodates >= noOfPeople
									.and(MongoParams.F_PRICE).gte(priceMin).lte(priceMax); // Match price within range

		MatchOperation matchCountry = Aggregation.match(criteria);

		SortOperation sortPriceAscending = Aggregation.sort(Sort.Direction.ASC, "price");

		GroupOperation groupRelevant = Aggregation.group("address.country")
													.push(new BasicDBObject()
														.append("id", "$_id")					// Include ID for search later
														.append("name", "$name")				// Include the name
														.append("price", "$price")				// Inclue the price
														.append("image", "$images.picture_url")	// Include the image Url
														).as("filtered");

		// Build aggregation pipeline
		Aggregation pipeline = Aggregation.newAggregation(matchCountry, sortPriceAscending, groupRelevant);

		// Execute aggregation and get the results
		AggregationResults<Document> aggResults = template.aggregate(pipeline, MongoParams.C_LISTINGS, Document.class);

		// return the document
		Document result = aggResults.getUniqueMappedResult();

		return Optional.ofNullable(result);
	}



	//TODO: Task 4
	

	//TODO: Task 5


}
