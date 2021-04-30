package models;

import java.util.Date;

public class Transaction {
    protected int accountId;
    protected double amount;
    private Date date;

    public Transaction(int accountId, double amount) {
        this.accountId = accountId;
        this.amount = amount;
        this.date = new Date();
    }

    public int getAccountId() {
        return accountId;
    }
    public double getAmount() {
        return amount;
    }
    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Transaction : Account ID: " + accountId + "; Amount: " + amount;
    }
}
