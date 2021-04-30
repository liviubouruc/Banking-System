package models;

public class CreditCard extends Card {
    double debt;

    public CreditCard(int ownerId, String number, int CVV) {
        super(ownerId, number, CVV);
        debt = 0;
    }

    @Override
    public String toString() {
        return "Credit Card; Account: " + ownerId + " Number: " + number + " CVV: " + CVV + " Debt: " + debt;
    }
}
