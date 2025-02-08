package vttp2023.batch3.assessment.paf.bookings.bootstrap;

import java.util.List;

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
        
        System.out.println(">>> Testing distinct countries..");
        List<String> countriesList = listingsRepository.findDistinctCountries();

        for (String c : countriesList)
        {
            System.out.println(">>>" + c);
        }
    }
    
}
