package services;

import models.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.sql.*;

public class TestService {
    HashMap<Integer, Client> clients;
    ArrayList<ExchangeTransaction> exTransactions;
    ReaderService csvReader;
    WriterService auditWriter;
    DBConnection dbConnection;

    public TestService(DBConnection connection) {
        clients = new HashMap<>();
        exTransactions = new ArrayList<>();
        csvReader = ReaderService.getInstance();
        auditWriter = WriterService.getInstance();
        dbConnection = connection;
    }

    public void loadDataFromDatabase() {
        try (Statement statement = dbConnection.getConnection().createStatement()) {
            String query = "select * from clients";
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()) {
                Client client = new Client(rs.getInt("clientid"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("phonenumber"), rs.getString("birthdate"));
                clients.put(client.getClientId(), client);
            }

            query = "select * from accounts";
            rs = statement.executeQuery(query);
            while(rs.next()) {
                Account account = new Account(rs.getInt("accountid"), rs.getDouble("balance"), rs.getString("currencytype"), rs.getString("creationdate"));
                Client client = clients.get(rs.getInt("clientid"));
                client.addAccount(account);
            }

            query = "select * from cards";
            rs = statement.executeQuery(query);
            while(rs.next()) {
                if(rs.getDouble("debt") < 0) {
                    Card card = new DebitCard(rs.getInt("cardid"), rs.getString("number"), rs.getInt("CVV"));
                    Client client = clients.get(rs.getInt("clientid"));
                    for(Account account : client.getAccounts()) {
                        if (account.getId() == rs.getInt("accountid")) {
                            account.addCard(card);
                        }
                    }
                }
                else {
                    Card card = new CreditCard(rs.getInt("cardid"), rs.getString("number"), rs.getInt("CVV"));
                    Client client = clients.get(rs.getInt("clientid"));
                    for(Account account : client.getAccounts()) {
                        if (account.getId() == rs.getInt("accountid")) {
                            account.addCard(card);
                        }
                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addClient() {
//        String inputFile = "csvclients.csv";
//        ArrayList<String> lines = csvReader.readFromCSV(inputFile);
//        for(String line : lines) {
//            String []fields = line.replaceAll(" ", "").split(",");
//            Client client = new Client(fields[0], fields[1], fields[2], fields[3]);
//            int clientId = client.getClientId();
//            clients.put(clientId, client);
//        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter client details:");
        System.out.println("First name: ");
        String firstName = scanner.nextLine();
        System.out.println("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.println("Phone Number: ");
        String phoneNumber = scanner.nextLine();
        System.out.println("Birth date (DD-MM-YYYY):");
        String date = scanner.nextLine();

        Client client = new Client(Client.getMaxId()+1, firstName, lastName, phoneNumber, date);
        int clientId = client.getClientId();
        clients.put(clientId, client);

        try {
            String query = "insert into clients values(?,?,?,?,?)";
            PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, clientId);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setString(5, date);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        auditWriter.audit("addClient");
    }

    public void showClients() {
        System.out.println("Clients:");

        for(int id : clients.keySet())
            System.out.println(clients.get(id));
        auditWriter.audit("showClients");
    }

    public void addAccount() {
//        String inputFile = "csvaccounts.csv";
//        ArrayList<String> lines = csvReader.readFromCSV(inputFile);
//        for(String line : lines) {
//            String []fields = line.replaceAll(" ", "").split(",");
//            Client client = clients.get(Integer.parseInt(fields[0]));
//            Account account = new Account(Double.parseDouble(fields[1]), fields[2]);
//            client.addAccount(account);
//        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("For whom? (Enter client's id): ");
        int clientId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter currency type: ");
        String currencyType = scanner.nextLine();
        System.out.println("Enter the balance: ");
        double balance = scanner.nextDouble();

        Client client = clients.get(clientId);
        Account account = new Account(Account.getMaxId()+1, balance, currencyType);
        client.addAccount(account);

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String query = "insert into accounts values(?,?,?,?,?)";
            PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, Account.getMaxId()+1);
            preparedStatement.setDouble(2, balance);
            preparedStatement.setString(3, currencyType);
            preparedStatement.setString(4, formatter.format(account.getCreationDate()));
            preparedStatement.setInt(5, clientId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        auditWriter.audit("createAccount");
    }

    public void deleteAccount() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter client's id: ");
        int clientId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter account id: ");
        int accountId = scanner.nextInt();
        Client client = clients.get(clientId);
        client.deleteAccount(accountId);

        try(Statement statement = dbConnection.getConnection().createStatement()) {
            String query = "delete from accounts where accountid = " + accountId;
            statement.executeUpdate(query);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCard() {
//        String inputFile = "csvcards.csv";
//        ArrayList<String> lines = csvReader.readFromCSV(inputFile);
//        for(String line : lines) {
//            String []fields = line.replaceAll(" ", "").split(",");
//
//            Client client = clients.get(Integer.parseInt(fields[0]));
//            for(Account account : client.getAccounts()) {
//                if(account.getId() == Integer.parseInt(fields[1])) {
//                    if(fields[4].equals("credit")) {
//                        CreditCard card = new CreditCard(Integer.parseInt(fields[1]), fields[2], Integer.parseInt(fields[3]));
//                        account.addCard(card);
//                    } else {
//                        DebitCard card = new DebitCard(Integer.parseInt(fields[1]), fields[2], Integer.parseInt(fields[3]));
//                        account.addCard(card);
//                    }
//                }
//            }
//        }
        Scanner scanner = new Scanner(System.in);

        System.out.println("For whom? (Enter client's id): ");
        int clientId = scanner.nextInt();
        System.out.println("Enter account id: ");
        int accountId = scanner.nextInt(); scanner.nextLine();
        System.out.println("Enter card number: ");
        String number = scanner.nextLine();
        System.out.println("Enter CVV: ");
        int CVV = scanner.nextInt(); scanner.nextLine();
        System.out.println("Enter card type: (credit/debit) ");
        String cardType = scanner.nextLine();

        Client client = clients.get(clientId);
        if(client == null) {
            System.out.println("Invalid client!");
        }

        Card card;
        boolean found = false;
        for(Account account : client.getAccounts()) {
            if(account.getId() == accountId) {
                found = true;

                if(cardType.equals("debit")) {
                    card = new DebitCard(Card.getMaxId()+1, number, CVV);
                    account.addCard(card);

                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        String query = "insert into cards values(?,?,?,?,?,?,?)";
                        PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(query);
                        preparedStatement.setInt(1, Card.getMaxId()+1);
                        preparedStatement.setString(2, number);
                        preparedStatement.setInt(3, CVV);
                        preparedStatement.setString(4, formatter.format(card.getExpirationDate()));
                        preparedStatement.setDouble(5, 0);
                        preparedStatement.setInt(6, accountId);
                        preparedStatement.setInt(7, clientId);
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    card = new CreditCard(Card.getMaxId()+1, number, CVV);
                    account.addCard(card);

                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        String query = "insert into cards values(?,?,?,?,?,?,?)";
                        PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(query);
                        preparedStatement.setInt(1, Card.getMaxId()+1);
                        preparedStatement.setString(2, number);
                        preparedStatement.setInt(3, CVV);
                        preparedStatement.setString(4, formatter.format(card.getExpirationDate()));
                        preparedStatement.setInt(5, -1);
                        preparedStatement.setInt(6, accountId);
                        preparedStatement.setInt(7, clientId);
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if(!found) {
            System.out.println("Invalid account!");
        }

        auditWriter.audit("addCard");
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

//    public void addTransactions() {
//        String inputFile = "csvtransactions.csv";
//        ArrayList<String> lines = csvReader.readFromCSV(inputFile);
//        for(String line : lines) {
//            String []fields = line.replaceAll(" ", "").split(",");
//
//            if(fields[0].equals("0")) {
//                addMoney(Integer.parseInt(fields[1]),Integer.parseInt(fields[2]), Double.parseDouble(fields[3]));
//            } else if(fields[0].equals("1")) {
//                retrieveMoney(Integer.parseInt(fields[1]),Integer.parseInt(fields[2]), Double.parseDouble(fields[3]));
//            } else {
//                exchange(Integer.parseInt(fields[1]),Integer.parseInt(fields[2]), Integer.parseInt(fields[3]),Integer.parseInt(fields[4]), Double.parseDouble(fields[3]));
//            }
//        }
//
//        auditWriter.audit("addTransactions");
//    }

    public void addMoney() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("For whom? (Enter client's id): ");
        int clientId = scanner.nextInt();
        System.out.println("Enter account id: ");
        int accountId = scanner.nextInt();
        System.out.println("Enter amount: ");
        double amount = scanner.nextDouble();

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

                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    String query = "insert into transactions values(null,?,?,null,?)";
                    PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(query);
                    preparedStatement.setInt(1, accountId);
                    preparedStatement.setDouble(2, amount);
                    preparedStatement.setString(3, formatter.format(transaction.getDate()));
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }

                account.addMoney(transaction);
                try(Statement statement = dbConnection.getConnection().createStatement()) {
                    String query = "update accounts set balance = " + account.getBalance() + " where (accountid = " + accountId + ")";
                    statement.executeUpdate(query);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if(!found) {
            System.out.println("Invalid account!");
        }
    }

    public void retrieveMoney() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("For whom? (Enter client's id): ");
        int clientId = scanner.nextInt();
        System.out.println("Enter account id: ");
        int accountId = scanner.nextInt();
        System.out.println("Enter amount: ");
        double amount = scanner.nextDouble();

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

                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    String query = "insert into transactions values(null,?,?,null,?)";
                    PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(query);
                    preparedStatement.setInt(1, accountId);
                    preparedStatement.setDouble(2, -amount);
                    preparedStatement.setString(3, formatter.format(transaction.getDate()));
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }

                account.retrieveMoney(transaction);
                try(Statement statement = dbConnection.getConnection().createStatement()) {
                    String query = "update accounts set balance = " + account.getBalance() + " where (accountid = " + accountId + ")";
                    statement.executeUpdate(query);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if(!found) {
            System.out.println("Invalid account!");
        }
    }

    public void exchange() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("From whom? (Enter client's id): ");
        int clientId1 = scanner.nextInt();
        System.out.println("Enter account id: ");
        int account1 = scanner.nextInt();
        System.out.println("To whom? (Enter client's id): ");
        int clientId2 = scanner.nextInt();
        System.out.println("Enter account id: ");
        int account2 = scanner.nextInt();
        System.out.println("Enter amount: ");
        double amount = scanner.nextDouble();

        Client client1 = clients.get(clientId1);
        Client client2 = clients.get(clientId2);

        boolean found = false;
        for(Account account : client1.getAccounts()) {
            if(account.getId() == account1) {
                found = true;

                Transaction transaction = new Transaction(account1, amount);
                account.retrieveMoney(transaction);

                try(Statement statement = dbConnection.getConnection().createStatement()) {
                    String query = "update accounts set balance = " + account.getBalance() + " where (accountid = " + account1 + ")";
                    statement.executeUpdate(query);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
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

                try(Statement statement = dbConnection.getConnection().createStatement()) {
                    String query = "update accounts set balance = " + account.getBalance() + " where (accountid = " + account2 + ")";
                    statement.executeUpdate(query);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if(!found) {
            System.out.println("Invalid account!");
            return;
        }

        ExchangeTransaction ext = new ExchangeTransaction(account1, account2, amount);

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String query = "insert into transactions values(null,?,?,?,?)";
            PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, account1);
            preparedStatement.setDouble(2, amount);
            preparedStatement.setInt(3, account2);
            preparedStatement.setString(4, formatter.format(ext.getDate()));
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        exTransactions.add(ext);

        auditWriter.audit("exchange");
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
