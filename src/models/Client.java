package models;

import java.util.ArrayList;
import java.util.Collections;

public class Client extends Person {
    private static int noOfClients;
    private int clientId;
    private ArrayList<Account> accounts;

    static {
        noOfClients = 0;
    }

    {
        this.clientId = noOfClients;
        noOfClients++;
    }
    public Client(String firstName, String lastName, String phoneNumber, int year, int month, int day) {
        super(firstName, lastName, phoneNumber, year, month, day);
        this.accounts = new ArrayList<>();
    }

    public int getClientId() {
        return clientId;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void showAccounts() {
        Collections.sort(accounts);
        for(Account account : accounts)
            System.out.println(account);
    }

    @Override
    public String toString() {
        return "ID: " + clientId + " Client: " + firstName + " " + lastName;
    }
}
