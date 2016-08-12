package github.com.jcaiqueoliveira.stonetest.model;

import io.realm.Realm;
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
}
