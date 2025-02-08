package vttp2023.batch3.assessment.paf.bookings.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ListingSearch {
   
    @NotBlank(message = "Country musst not be null, empty, or white spaces")
    private String country;
    
    @Min(value = 1, message = "Number of person cannot be less than 1")
    @Max(value = 10, message = "Number of person cannot be more than 10")
    @NotNull(message = "Input must not be null")
    private Integer noOfPeople;

    @Min(value = 1, message = "Price cannot be less than 1")
    @Max(value = 10000, message = "Price cannot be more than 10000")
    @NotNull(message = "Input must not be null")
    private Double priceMin;

    @Min(value = 1, message = "Price cannot be less than 1")
    @Max(value = 10000, message = "Price cannot be more than 10000")
    @NotNull(message = "Input must not be null")
    private Double priceMax;

    public ListingSearch() {
    }

    public ListingSearch(String country, int noOfPeople, double priceMin, double priceMax) {
        this.country = country;
        this.noOfPeople = noOfPeople;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getNoOfPeople() {
        return noOfPeople;
    }

    public void setNoOfPeople(Integer noOfPeople) {
        this.noOfPeople = noOfPeople;
    }

    public Double getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(Double priceMin) {
        this.priceMin = priceMin;
    }

    public Double getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(Double priceMax) {
        this.priceMax = priceMax;
    }

    
    
    
}
