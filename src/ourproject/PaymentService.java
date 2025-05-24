package ourproject;

public class PaymentService {
    public boolean processPayment(double amount, String paymentMethod) {

        return Math.random() > 0.2;
    }

    public String generateTransactionId() {
        return "TXN" + System.currentTimeMillis();
    }
}