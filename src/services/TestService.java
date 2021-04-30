package services;

import models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TestService {
    HashMap<Integer, Client> clients;
    ArrayList<ExchangeTransaction> exTransactions;
    ReaderService csvReader;
    WriterService auditWriter;

    public TestService() {
        clients = new HashMap<>();
        exTransactions = new ArrayList<>();
        csvReader = ReaderService.getInstance();
        auditWriter = WriterService.getInstance();
    }

    public void addClients() {
        String inputFile = "csvclients.csv";
        ArrayList<String> lines = csvReader.readFromCSV(inputFile);
        for(String line : lines) {
            String []fields = line.replaceAll(" ", "").split(",");
            Client client = new Client(fields[0], fields[1], fields[2], Integer.parseInt(fields[3]), Integer.parseInt(fields[4]), Integer.parseInt(fields[5]));
            int clientId = client.getClientId();
            clients.put(clientId, client);
        }
        auditWriter.audit("addClients");
    }

    public void showClients() {
        System.out.println("Clients:");
        for(int id : clients.keySet())
            System.out.println(clients.get(id));
        auditWriter.audit("showClients");
    }

    public void addAccounts() {
        String inputFile = "csvaccounts.csv";
        ArrayList<String> lines = csvReader.readFromCSV(inputFile);
        for(String line : lines) {
            String []fields = line.replaceAll(" ", "").split(",");
            Client client = clients.get(Integer.parseInt(fields[0]));
            Account account = new Account(Double.parseDouble(fields[1]), fields[2]);
            client.addAccount(account);
        }
        auditWriter.audit("createAccount");
    }

    public void addCards() {
        String inputFile = "csvcards.csv";
        ArrayList<String> lines = csvReader.readFromCSV(inputFile);
        for(String line : lines) {
            String []fields = line.replaceAll(" ", "").split(",");

            Client client = clients.get(Integer.parseInt(fields[0]));
            for(Account account : client.getAccounts()) {
                if(account.getId() == Integer.parseInt(fields[1])) {
                    if(fields[4].equals("credit")) {
                        CreditCard card = new CreditCard(Integer.parseInt(fields[1]), fields[2], Integer.parseInt(fields[3]));
                        account.addCard(card);
                    } else {
                        DebitCard card = new DebitCard(Integer.parseInt(fields[1]), fields[2], Integer.parseInt(fields[3]));
                        account.addCard(card);
                    }
                }
            }
        }
        auditWriter.audit("addCards");
    }

    public void showCards() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Show cards");
        System.out.println("Enter Client id: ");
        int clientId = scanner.nextInt();
        System.out.println("Enter account id: ");
        int accountId = scanner.nextInt();

        Client client = clients.get(clientId);
        if(client == null) {
            System.out.println("Invalid client!");
            return;
        }
        boolean found = false;
        for(Account account : client.getAccounts()) {
            if(account.getId() == accountId) {
                found = true;
                for(Card card : account.getCards())
                    System.out.println(card);
            }
        }
        if(!found) {
            System.out.println("Invalid account!");
            return;
        }
        auditWriter.audit("showCards");
    }

    public void addTransactions() {
        String inputFile = "csvtransactions.csv";
        ArrayList<String> lines = csvReader.readFromCSV(inputFile);
        for(String line : lines) {
            String []fields = line.replaceAll(" ", "").split(",");

            if(fields[0].equals("0")) {
                addMoney(Integer.parseInt(fields[1]),Integer.parseInt(fields[2]), Double.parseDouble(fields[3]));
            } else if(fields[0].equals("1")) {
                retrieveMoney(Integer.parseInt(fields[1]),Integer.parseInt(fields[2]), Double.parseDouble(fields[3]));
            } else {
                exchange(Integer.parseInt(fields[1]),Integer.parseInt(fields[2]), Integer.parseInt(fields[3]),Integer.parseInt(fields[4]), Double.parseDouble(fields[3]));
            }
        }
        auditWriter.audit("addTransactions");
    }

    public void addMoney(int clientId, int accountId, double amount) {
        Client client = clients.get(clientId);
        if(client == null) {
            System.out.println("Invalid client!");
            return;
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

    public void retrieveMoney(int clientId, int accountId, double amount) {
        Client client = clients.get(clientId);
        if(client == null) {
            System.out.println("Invalid client!");
            return;
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

    public void exchange(int clientId1, int account1, int clientId2, int account2, double amount) {
        Client client1 = clients.get(clientId1);
        Client client2 = clients.get(clientId2);

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
            return;
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
            return;
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
            return;
        }
        boolean found = false;
        for(Account account : client.getAccounts()) {
            if(account.getId() == accountId) {
                found = true;
                for(Transaction transaction : account.getTransactions())
                    System.out.println(transaction);
            }
        }
        if(!found) {
            System.out.println("Invalid account!");
            return;
        }
        auditWriter.audit("shoeAllTransactions");
    }

    public void showAllExchanges() {
        for(ExchangeTransaction ext : exTransactions) {
            System.out.println(ext);
        }
        auditWriter.audit("showAllExchanges");
    }
}
