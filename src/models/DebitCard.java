package models;

public class DebitCard extends Card {

    public DebitCard(int ownerId, String number, int CVV) {
        super(ownerId, number, CVV);
    }

    @Override
    public String toString() {
        return "Debit Card; Account: " + ownerId + " Number: " + number + " CVV: " + CVV;
    }
}
