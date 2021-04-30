package models;

public class ExchangeTransaction extends Transaction {
    private int receiverAccountId;

    public ExchangeTransaction(int accountId, int receiverAccountId, double amount) {
        super(accountId, amount);
        this.receiverAccountId = receiverAccountId;
    }

    public int getReceiverAccountId() {
        return receiverAccountId;
    }

    @Override
    public String toString() {
        return "Transaction between:  (from)" + accountId + " (to)" + receiverAccountId + "\nAmount: " + amount;
    }
}
