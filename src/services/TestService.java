package services;

import models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TestService {
    HashMap<Integer, Client> clients;
    ArrayList<ExchangeTransaction> exTransactions;

    public TestService() {
        clients = new HashMap<>();
        exTransactions = new ArrayList<>();
    }

    public void addClient() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter client details:");
        System.out.println("First name: ");
        String firstName = scanner.nextLine();
        System.out.println("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.println("Phone Number: ");
        String phoneNumber = scanner.nextLine();
        System.out.println("Birth date:");
        System.out.println("Year: ");
        int year = scanner.nextInt();
        System.out.println("Month: ");
        int month = scanner.nextInt();
        System.out.println("Day: ");
        int day = scanner.nextInt();

        Client client = new Client(firstName, lastName, phoneNumber, year, month, day);
        int clientId = client.getClientId();
        clients.put(clientId, client);
    }

    public void showClients() {
        for(int id : clients.keySet())
            System.out.println(clients.get(id));
    }

    public void createAccount() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("For whom? (Enter client's id): ");
        int clientId = scanner.nextInt();
        Client client = clients.get(clientId);

        Account account = new Account();
        client.addAccount(account);
    }

    public void addCard() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("For whom? (Enter client's id): ");
        int clientId = scanner.nextInt();
        System.out.println("Enter account id: ");
        int accountId = scanner.nextInt();
        System.out.printf("Enter card number: ");
        String number = scanner.nextLine();
        System.out.println("Enter CVV: ");
        int CVV = scanner.nextInt();
        System.out.println("Enter card type: (credit/debit) ");
        String cardType = scanner.nextLine();

        Client client = clients.get(clientId);
        if(client == null) {
            System.out.println("Invalid client!");
        }

        boolean found = false;
        for(Account account : client.getAccounts()) {
            if(account.getId() == accountId) {
                found = true;

                if(cardType.equals("debit")) {
                    DebitCard card = new DebitCard(accountId, number, CVV);
                    account.addCard(card);
                } else {
                    DebitCard card = new DebitCard(accountId, number, CVV);
                    account.addCard(card);
                }
            }
        }
        if(!found) {
            System.out.println("Invalid account!");
        }
    }

    public void showCards() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Client id: ");
        int clientId = scanner.nextInt();
        System.out.println("Enter account id: ");
        int accountId = scanner.nextInt();

        Client client = clients.get(clientId);
        if(client == null) {
            System.out.println("Invalid client!");
        }
        boolean found = false;
        for(Account account : client.getAccounts()) {
            if(account.getId() == accountId) {
                for(Card card : account.getCards())
                    System.out.println(card);
            }
        }
        if(!found) {
            System.out.println("Invalid account!");
        }
    }


    public void addMoney() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("To whom? (Enter client's id): ");
        int clientId = scanner.nextInt();
        System.out.printf("Enter account id: ");
        int accountId = scanner.nextInt();
        System.out.println("How much? ");
        double amount = scanner.nextInt();

        Client client = clients.get(clientId);
        if(client == null) {
            System.out.println("Invalid client!");
        }
        boolean found = false;
        for(Account account : client.getAccounts()) {
            if(account.getId() == accountId) {
                found = true;

                Transaction transaction = new Transaction(accountId, amount);
                account.addMoney(transaction);
            }
        }
        if(!found) {
            System.out.println("Invalid account!");
        }
    }
    public void retrieveMoney() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("To whom? (Enter client's id): ");
        int clientId = scanner.nextInt();
        System.out.printf("Enter account id: ");
        int accountId = scanner.nextInt();
        System.out.println("How much? ");
        double amount = scanner.nextInt();

        Client client = clients.get(clientId);
        if(client == null) {
            System.out.println("Invalid client!");
        }
        boolean found = false;
        for(Account account : client.getAccounts()) {
            if(account.getId() == accountId) {
                found = true;

                Transaction transaction = new Transaction(accountId, amount);
                account.retrieveMoney(transaction);
            }
        }
        if(!found) {
            System.out.println("Invalid account!");
        }
    }

    public void exchange() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter client 1 (from): ");
        int clientId1 = scanner.nextInt();
        System.out.println("Enter client 2 (to): ");
        int clientId2 = scanner.nextInt();
        System.out.printf("Enter AccountId 1: ");
        int account1 = scanner.nextInt();
        System.out.printf("Enter AccountId 2: ");
        int account2 = scanner.nextInt();
        System.out.println("Enter amount: ");
        int amount = scanner.nextInt();

        Client client1 = clients.get(clientId1);
        if(client1 == null) {
            System.out.println("Invalid client!");
        }
        Client client2 = clients.get(clientId2);
        if(client2 == null) {
            System.out.println("Invalid client!");
        }

        boolean found = false;
        for(Account account : client1.getAccounts()) {
            if(account.getId() == account1) {
                found = true;

                Transaction transaction = new Transaction(account1, amount);
                account.retrieveMoney(transaction);
            }
        }
        if(!found) {
            System.out.println("Invalid account!");
        }

        found = false;
        for(Account account : client2.getAccounts()) {
            if(account.getId() == account2) {
                found = true;

                Transaction transaction = new Transaction(account1, amount);
                account.addMoney(transaction);
            }
        }
        if(!found) {
            System.out.println("Invalid account!");
        }

        ExchangeTransaction ext = new ExchangeTransaction(account1, account2, amount);
        exTransactions.add(ext);
    }

    public void showAllTransactions() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Client id: ");
        int clientId = scanner.nextInt();
        System.out.println("Enter account id: ");
        int accountId = scanner.nextInt();

        Client client = clients.get(clientId);
        if(client == null) {
            System.out.println("Invalid client!");
        }
        boolean found = false;
        for(Account account : client.getAccounts()) {
            if(account.getId() == accountId) {
                for(Transaction transaction : account.getTransactions())
                    System.out.println(transaction);
            }
        }
        if(!found) {
            System.out.println("Invalid account!");
        }
    }

    public void showAllExchanges() {
        for(ExchangeTransaction ext : exTransactions) {
            System.out.println(ext);
        }
    }
}
