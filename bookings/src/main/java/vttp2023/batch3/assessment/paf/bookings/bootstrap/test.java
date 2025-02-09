package vttp2023.batch3.assessment.paf.bookings.bootstrap;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import vttp2023.batch3.assessment.paf.bookings.repositories.ListingsRepository;

@Component
public class test implements CommandLineRunner
{
    @Autowired
    private ListingsRepository listingsRepository;

    @Override
    public void run(String... args) throws Exception {
        
        // Test Task 2
        System.out.println(">>> Testing distinct countries..");
        List<String> countriesList = listingsRepository.findDistinctCountries();

        for (String c : countriesList)
        {
            System.out.println(">>>" + c);
        }



        // Test Task 3
        System.out.println(">>> Testing Filter");

        Optional<Document> filteredResultOpt = listingsRepository.findListingsByCriteria("aUsTraLia", 2, 50, 100);

        if (filteredResultOpt.isEmpty())
        {
            System.out.println(">>> Filtered Result is Empty");
        }

        Document filteredResult = filteredResultOpt.get();

        System.out.println(">>> Filtered result: \n\n" + filteredResult.toJson());

        List<Document> filtered = filteredResult.getList("filtered", Document.class);
        for (Document listing : filtered)
        {
            System.out.println(">>> listing: \n\n" + listing.toJson());
        }

        System.out.println();



        // Test task 4
        Optional<Document> t4_optDoc = listingsRepository.findDetailsByListingId("13530122");
        System.out.println(">>> Test 4: Document gotten is: " + t4_optDoc.get());
        
    }
    
}
