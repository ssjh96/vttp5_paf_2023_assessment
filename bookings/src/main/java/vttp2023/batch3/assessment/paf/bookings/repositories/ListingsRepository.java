package vttp2023.batch3.assessment.paf.bookings.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;
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
	// 	{ $sort: { price: -1 } }, // sort descending order
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

		SortOperation sortPriceDescending = Aggregation.sort(Sort.Direction.DESC, "price");

		GroupOperation groupRelevant = Aggregation.group("address.country")
													.push(new BasicDBObject()
														.append("id", "$_id")					// Include ID for search later
														.append("name", "$name")				// Include the name
														.append("price", "$price")				// Inclue the price
														.append("image", "$images.picture_url")	// Include the image Url
														).as("filtered");

		// Build aggregation pipeline
		Aggregation pipeline = Aggregation.newAggregation(matchCountry, sortPriceDescending, groupRelevant);

		// Execute aggregation and get the results
		AggregationResults<Document> aggResults = template.aggregate(pipeline, MongoParams.C_LISTINGS, Document.class);

		// return the document
		Document result = aggResults.getUniqueMappedResult();

		return Optional.ofNullable(result);
	}

	//TODO: Task 4
	// db.listings.findOne(
	// 	{ _id : "13530122"},
	// 	{_id:1, description:1, 'address.street':1, 'address.suburb':1, 'address.country':1, 'images.picture_url':1, price:1, amenities:1}
	// )	
	public Optional<Document> findDetailsByListingId(String listingId)
	{
		// ObjectId listingObjectId = new ObjectId(listingId); // cannot since _id is a string not an objectid 
		// System.out.println(">>> Test 4: listing objectId: " + listingObjectId.toHexString());
		// Document result = template.findById(listingObjectId, Document.class);

		Criteria criteria = Criteria.where(MongoParams.F_ID).is(listingId);
		Query query = Query.query(criteria);

		query.fields().include(MongoParams.F_ID, MongoParams.F_DESCRIPTION, MongoParams.F_STREET, MongoParams.F_SUBURB, MongoParams.F_COUNTRY, MongoParams.F_PIC_URL, MongoParams.F_PRICE, MongoParams.F_AMENITIES);

		// Perform the query
		Document results = template.findOne(query, Document.class, MongoParams.C_LISTINGS);
		return Optional.ofNullable(results);
	}
	

	//TODO: Task 5


}
