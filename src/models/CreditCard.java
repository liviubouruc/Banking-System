package models;

public class CreditCard extends Card {
    double debt;

    public CreditCard(int cardId, String number, int CVV) {
        super(cardId, number, CVV);
        debt = 0;
    }

    @Override
    public String toString() {
        return "Credit Card; Account: " + cardId + " Number: " + number + " CVV: " + CVV + " Debt: " + debt;
    }
}
