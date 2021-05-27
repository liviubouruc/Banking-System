package main;

import services.TestService;
import services.DBConnection;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DBConnection dbConnection = DBConnection.getInstance();

        TestService service = new TestService(dbConnection);

        service.loadDataFromDatabase();

        boolean exit = false;
        Scanner scanner = new Scanner(System.in);
        while(!exit) {
            System.out.println("1.Add client\n2.Show Clients\n3.Add Account\n4.Add card\n5.Show Cards\n6.Add money\n7.Retrieve money\n8.Exchange\n9.Show all transactions\n10.Close account\n11.Exit");
            int var = scanner.nextInt();
            if(var == 1) service.addClient();
            else if(var == 2) service.showClients();
            else if(var == 3) service.addAccount();
            else if(var == 4) service.addCard();
            else if(var == 5) service.showCards();
            else if(var == 6) service.addMoney();
            else if(var == 7) service.retrieveMoney();
            else if(var == 8) service.exchange();
            else if(var == 9) service.showAllTransactions();
            else if(var == 10) service.deleteAccount();
            else if(var == 11) exit = true;
            else System.out.println("Invalid command");
        }
    }
}
