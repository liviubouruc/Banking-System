package models;

import java.util.ArrayList;
import java.util.Date;

public class Account implements Comparable<Account> {
    private static int noOfAccounts;
    private int accountId;
    private Double balance;
    private Date creationDate;
    private ArrayList<Transaction> transactions;
    private ArrayList<Card> cards;

    static {
        noOfAccounts = 0;
    }
    {
        noOfAccounts++;
        this.accountId = noOfAccounts;
    }
    public Account() {
        this.balance = 0.0;
        this.creationDate = new Date();
        this.transactions = new ArrayList<>();
        this.cards = new ArrayList<>();
    }

    public int getId() {
        return accountId;
    }

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
}
