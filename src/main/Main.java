package main;

import services.TestService;

public class Main {
    public static void main(String[] args) {
        TestService service = new TestService();

        service.addClients();
        service.showClients();
        service.addAccounts();
        service.addCards();
        service.showCards();
        service.addTransactions();
        service.showAllExchanges();
        service.showAllTransactions();
    }
}
