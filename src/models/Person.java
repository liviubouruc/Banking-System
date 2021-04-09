package models;

import java.util.Calendar;
import java.util.Date;

public class Person {
    protected String firstName;
    protected String lastName;
    protected String phoneNumber;
    protected Date dateOfBirth;

    public Person(String firstName, String lastName, String phoneNumber, int year, int month, int day) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        this.dateOfBirth = calendar.getTime();
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }
}
