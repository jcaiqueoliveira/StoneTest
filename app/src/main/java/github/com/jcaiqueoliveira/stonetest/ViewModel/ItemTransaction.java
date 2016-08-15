package github.com.jcaiqueoliveira.stonetest.ViewModel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import github.com.jcaiqueoliveira.stonetest.model.TransactionsDb;
import github.com.jcaiqueoliveira.stonetest.service.StoneService;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kanda on 14/08/2016.
 */
public class ItemTransaction extends BaseObservable {
    private Context mContext;
    private TransactionsDb mTransactionsDb;
    private StoneService mStoneService;
    private ProgressDialog mProgress;
    private static final int CANCEL_TRANSACTION = 0;
    private DataListener listener;

    public ItemTransaction(Context context, TransactionsDb transactionsDb, DataListener listener) {
        this.mContext = context;
        this.mTransactionsDb = transactionsDb;
        this.listener = listener;
        initializeStoneService();
    }

    public String getCardNumber() {
        return "Número cartão: " + mTransactionsDb.getCardNumber();
    }

    public String getValueTransaction() {
        return "Valor R$: " + mTransactionsDb.getValue();
    }

    public String getStatusTransaction() {
        return "Status transação: " + mTransactionsDb.getStatusTransaction();
    }

    public void onClick(View v) {
        showDialog();
    }

    public void setTransaction(TransactionsDb transaction) {
        mTransactionsDb = transaction;
    }

    public void showDialog() {
        String[] items = {"Cancelar transação", "Enviar comprovante"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Stone Test");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int position) {
                if (position == CANCEL_TRANSACTION) {
                    cancelTransaction();
                } else {
                    sendReceipt();
                }
            }
        });
        builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private void initializeStoneService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://private-eafd6f-stone8.apiary-mock.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mStoneService = retrofit.create(StoneService.class);
    }

    private void cancelTransaction() {
        mProgress = ProgressDialog.show(mContext, "Stone Test", "Aguarde", true, false);
        mProgress.show();
        Call<Void> cancelTransaction = mStoneService.deleteTransaction(mTransactionsDb.getStoneTransactionId());
        cancelTransaction.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mProgress.dismiss();
                Toast.makeText(mContext, "Cancelado com sucesso", Toast.LENGTH_SHORT).show();
                deleteTransaction();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
                mProgress.dismiss();
                Toast.makeText(mContext, "Não foi possível cancelar a transação. Tente novamente", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteTransaction() {
        Realm realm;
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(mContext)/*.deleteRealmIfMigrationNeeded()*/.build();
        realm = Realm.getInstance(realmConfiguration);
        realm.beginTransaction();
        mTransactionsDb.deleteFromRealm();
        realm.commitTransaction();
        listener.onDataChanged();

    }

    private void sendReceipt() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mTransactionsDb.toString());
        sendIntent.setType("text/plain");

// Verify that the intent will resolve to an activity
        if (sendIntent.resolveActivity(mContext.getPackageManager()) != null) {
            mContext.startActivity(sendIntent);
        }
    }
}
