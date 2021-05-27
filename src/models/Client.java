package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Client extends Person {
    private static int noOfClients;
    private int clientId;
    private static int maxId = 0;
    private ArrayList<Account> accounts;

//    static {
//        noOfClients = 0;
//    }
//
//    {
//        this.clientId = noOfClients;
//        noOfClients++;
//    }
    public Client(int clientId, String firstName, String lastName, String phoneNumber, String date) {
        super(firstName, lastName, phoneNumber, date);
        this.clientId = clientId;
        maxId = Math.max(maxId, clientId);
        this.accounts = new ArrayList<>();
    }

    public int getClientId() {
        return clientId;
    }

    public static int getMaxId() {
        return maxId;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }
    public void deleteAccount(int accountId) {
        for(Account account : accounts) {
            if(account.getId() == accountId) {
                accounts.remove(account);
                break;
            }
        }
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
