package vttp2023.batch3.assessment.paf.bookings.models;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Booking 
{
    @NotBlank(message = "Name cannot be blank.")
    private String name;
    
    @Email(message = "Please enter a valid email.")
    @NotBlank(message = "Email cannot be blank.")
    private String email;

    @NotNull(message = "date cannot be null")
    @FutureOrPresent(message = "date cannot be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date arrivalDate;

    @NotNull(message = "duration cannot be blank")
    private Integer duration;

    public Booking() {
    }

    public Booking(String name, String email, Date arrivalDate, Integer duration) {
        this.name = name;
        this.email = email;
        this.arrivalDate = arrivalDate;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    
    
}
