package github.com.jcaiqueoliveira.stonetest.ViewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;

import github.com.jcaiqueoliveira.stonetest.model.TransactionsDb;

/**
 * Created by Kanda on 14/08/2016.
 */
public class ItemTransaction extends BaseObservable {
    private Context mContext;
    private TransactionsDb mTransactionsDb;

    public ItemTransaction(Context context, TransactionsDb transactionsDb) {
        this.mContext = context;
        this.mTransactionsDb = transactionsDb;
    }

    public String getCardNumber() {
        return mTransactionsDb.getCardNumber();
    }

    public String getValueTransaction() {
        return mTransactionsDb.getValue();
    }

    public String getStatusTransaction() {
        return mTransactionsDb.getStatusTransaction();
    }

    public void onClick(View v) {

    }

    public void setTransaction(TransactionsDb transaction) {
        mTransactionsDb = transaction;
    }
}
