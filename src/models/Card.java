package models;

import java.util.Calendar;
import java.util.Date;

public abstract class Card {
    protected int ownerId;
    protected String number;
    protected Date expirationDate;
    protected int CVV;

    public Card(int ownerId, String number, int CVV) {
        this.ownerId = ownerId;
        this.number = number;
        this.CVV = CVV;

        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        calendar.setTime(now);
        calendar.add(Calendar.YEAR, 3);
        this.expirationDate = calendar.getTime();
    }

    public String getNumber() {
        return number;
    }
    public Date getExpirationDate() {
        return expirationDate;
    }
    public int getCVV() {
        return CVV;
    }

    @Override
    abstract public String toString();
}
