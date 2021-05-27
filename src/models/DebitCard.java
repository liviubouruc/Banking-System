package models;

public class DebitCard extends Card {

    public DebitCard(int cardId, String number, int CVV) {
        super(cardId, number, CVV);
    }

    @Override
    public String toString() {
        return "Debit Card; Account: " + cardId + " Number: " + number + " CVV: " + CVV;
    }
}
