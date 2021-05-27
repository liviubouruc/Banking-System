package models;

import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Account implements Comparable<Account> {
    private static int noOfAccounts;
    private int accountId;
    private static int maxId = 0;
    private Double balance;
    private String currencyType;
    private Date creationDate;
    private ArrayList<Transaction> transactions;
    private ArrayList<Card> cards;

//    static {
//        noOfAccounts = 0;
//    }
//
//    {
//        this.accountId = noOfAccounts;
//        noOfAccounts++;
//
//    }
    public Account(int accountId, Double balance, String currencyType) {
        this.accountId = accountId;
        maxId = Math.max(maxId, accountId);
        this.balance = balance;
        this.currencyType = currencyType;
        this.creationDate = new Date();
        this.transactions = new ArrayList<>();
        this.cards = new ArrayList<>();
    }
    public Account(int accountId, Double balance, String currencyType, String creationDate) {
        this.accountId = accountId;
        maxId = Math.max(maxId, accountId);
        this.balance = balance;
        this.currencyType = currencyType;
        try {
            this.creationDate = new SimpleDateFormat("dd-MM-yyyy").parse(creationDate);
        }
        catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        this.transactions = new ArrayList<>();
        this.cards = new ArrayList<>();
    }

    public int getId() {
        return accountId;
    }
    public static int getMaxId() { return maxId; }

    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
    public ArrayList<Card> getCards() {
        return cards;
    }

    public void addMoney(Transaction transaction) {
        balance += transaction.getAmount();
        transactions.add(transaction);
    }
    public void retrieveMoney(Transaction transaction) {
        balance -= transaction.getAmount();
        transactions.add(transaction);
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    @Override
    public int compareTo(Account o) {
        return this.balance.compareTo(o.balance);
    }

    @Override
    public String toString() {
        return "Account ID: " + accountId + " Balance: " + balance + " Currecy type: " + currencyType + " Creation Date: " + creationDate;
    }
}
