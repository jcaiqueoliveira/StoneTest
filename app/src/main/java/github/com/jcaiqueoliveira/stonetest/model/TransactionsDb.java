package github.com.jcaiqueoliveira.stonetest.model;

import io.realm.RealmObject;

/**
 * Created by Kanda on 12/08/2016.
 */
public class TransactionsDb extends RealmObject {
    private String cardHolder;
    private String cardNumber;
    private String cardYear;
    private String cardMonth;
    private String cardBrand;
    private String cvv;
    private String value;
    private String stoneTransactionId;
    private String statusTransaction;



    public String getStoneTransactionId() {
        return stoneTransactionId;
    }

    public void setStoneTransactionId(String stoneTransactionId) {
        this.stoneTransactionId = stoneTransactionId;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardYear() {
        return cardYear;
    }

    public void setCardYear(String cardYear) {
        this.cardYear = cardYear;
    }

    public String getCardMonth() {
        return cardMonth;
    }

    public void setCardMonth(String cardMonth) {
        this.cardMonth = cardMonth;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatusTransaction() {
        return statusTransaction;
    }

    public void setStatusTransaction(String statusTransaction) {
        this.statusTransaction = statusTransaction;
    }
}
