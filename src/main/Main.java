package main;

import services.TestService;

public class Main {
    public static void main(String[] args) {
        TestService service = new TestService();

        service.addClient();
        service.addClient();
        service.createAccount();
        service.addCard();
        service.addMoney();
        service.retrieveMoney();
        service.exchange();
        service.showAllExchanges();
        service.showAllTransactions();
        service.showClients();
    }
}
