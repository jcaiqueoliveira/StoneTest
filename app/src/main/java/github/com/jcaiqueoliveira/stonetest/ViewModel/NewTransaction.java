package github.com.jcaiqueoliveira.stonetest.ViewModel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.ObservableField;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import github.com.jcaiqueoliveira.stonetest.model.Card;
import github.com.jcaiqueoliveira.stonetest.model.TransactionsDb;
import github.com.jcaiqueoliveira.stonetest.service.StoneService;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import util.TextWatcherAdapter;


/**
 * Created by Kanda on 11/08/2016.
 */
public class NewTransaction implements ViewModel {
    public ObservableField<String> errorCardNumber, errorCardHolder, errorCardMonth, errorCardYear, errorCvv, errorValue, brand, month, year;
    private Context mContext;
    private StoneService mStoneService;
    private Card mCard;
    private Realm mRealmObject;
    private RealmConfiguration mRealmConfiguration;
    private static final int MONTH = 0;
    private static final int YEAR = 1;
    private static final int ACTUAL_YEAR = 16;
    private String[] mMonths = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    private String[] mYear = {"16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27"};
    private ProgressDialog mProgress;

    public NewTransaction(Context context) {
        this.mContext = context;
        mRealmConfiguration = new RealmConfiguration.Builder(mContext)/*.deleteRealmIfMigrationNeeded()*/.build();
        mRealmObject = Realm.getInstance(mRealmConfiguration);
        errorCardNumber = new ObservableField<>();
        errorCardHolder = new ObservableField<>();
        errorCardMonth = new ObservableField<>();
        errorCardYear = new ObservableField<>();
        errorCvv = new ObservableField<>();
        errorValue = new ObservableField<>();
        brand = new ObservableField<>();
        month = new ObservableField<>();
        year = new ObservableField<>();
        mCard = new Card();
        initializeStoneService();
    }

    private void initializeStoneService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://private-eafd6f-stone8.apiary-mock.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mStoneService = retrofit.create(StoneService.class);
    }


    public TextWatcher cardHolder = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(s.toString())) {
                errorCardHolder.set("Campo Obrigatório");
            } else {
                mCard.setCardHolder(s.toString());
                errorCardHolder.set(null);
            }
        }
    };
    public TextWatcher cardNumber = new TextWatcherAdapter() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            errorCardNumber.set(null);
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() < 12) {
                errorCardNumber.set("Cartão inválido");
            } else {
                errorCardNumber.set(null);
                mCard.setCardNumber(s.toString());
                brand.set(getCardBrand(s.toString()));
            }
        }
    };

    public TextWatcher cvv = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() < 3) {
                errorCvv.set("Cvv inválido");
            } else {
                errorCvv.set(null);
                mCard.setCvv(s.toString());
            }
        }
    };

    public TextWatcher value = new TextWatcherAdapter() {
        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(s.toString())) {
                errorValue.set("Campo obrigatório");
            } else {
                errorValue = null;
                mCard.setValue(s.toString());
            }
        }
    };

    public void onClickMonth(View v) {
        showDialog("Mês Validade", MONTH, mMonths);
    }

    public void onClickYear(View v) {
        showDialog("Ano Validade", YEAR, mYear);
    }

    public void newTransaction(View v) {

        mCard.setCardMonth(month.get());
        mCard.setCardYear(year.get());

        if (checkFields()) {
            mProgress = ProgressDialog.show(mContext, "Stone Test", "Aguarde", true, false);
            Call<String> newTransaction = mStoneService.newTransaction(mCard);
            newTransaction.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == 200) {
                        saveTransactionInDb(response.body(), "SUCESSO");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    t.printStackTrace();
                    mProgress.dismiss();
                    Toast.makeText(mContext, "Ocorreu um erro. Tente novamente", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void saveTransactionInDb(final String stoneTransactionId, final String statusTransaction) {
        final RealmResults<TransactionsDb> transactionsDbs = mRealmObject.where(TransactionsDb.class).findAll();
        Log.e("SIZE", " size: " + transactionsDbs.size());
        mRealmObject.beginTransaction();
        TransactionsDb transactionsDb = mRealmObject.createObject(TransactionsDb.class);
        transactionsDb.setCardHolder(mCard.getCardHolder());
        transactionsDb.setCardYear(mCard.getCardYear());
        transactionsDb.setCardMonth(mCard.getCardMonth());
        transactionsDb.setCardNumber(mCard.getCardNumber());
        transactionsDb.setCardBrand(mCard.getCardBrand());
        transactionsDb.setValue(mCard.getValue());
        transactionsDb.setStatusTransaction(statusTransaction);
        transactionsDb.setStoneTransactionId(stoneTransactionId);
        mRealmObject.commitTransaction();
        transactionsDbs.addChangeListener(new RealmChangeListener<RealmResults<TransactionsDb>>() {
            @Override
            public void onChange(RealmResults<TransactionsDb> element) {
                mProgress.dismiss();
                Toast.makeText(mContext, "Sucesso", Toast.LENGTH_LONG).show();
            }
        });
    }

    public String getCardBrand(String pan) {
        String bin = pan.substring(0, 6);
        boolean isBanese = bin.matches("^636117|^637470|^637473");//ok
        boolean isVisa = pan.matches("^4[0-9]{12}(?:[0-9]{3})?$");//ok
        boolean isMaster = bin.matches("^5[1-5][0-9]{4}");//ok
        boolean isAmex = pan.matches("^3[47][0-9]{13}$"); //ok
        boolean isHiperCard = bin.matches("^637568|^637095|^606282");//ok
        boolean isElo = bin.matches("401178|^401179|^431274|^438935|^451416|^457393|^457631|^457632|^504175|^627780|^636297|^636368|636369|^(506699|5067[0-6]\\d|50677[0-8])|^(50900\\d|5090[1-9]\\d|509[1-9]\\d{2})|^65003[1-3]|^(65003[5-9]|65004\\d|65005[0-1])|^(65040[5-9]|6504[1-3]\\d)|^(65048[5-9]|65049\\d|6505[0-2]\\d|65053     [0-8])|^(65054[1-9]|6505[5-8]\\d|65059[0-8])|^(65070\\d|65071[0-8])|^65072[0-7]|^(65090[1-9]|65091\\d|650920)|^(65165[2-9]|6516[6-7]\\d)|^(65500\\d|65501\\d)|^(65502[1-9]|6550[3-4]\\d|65505[0-8])/"); // ok
        boolean isDiners = pan.matches("^3(?:0[0-5]|[68][0-9])[0-9]{11}$");// ok

        if (isBanese) return "BANESE";
        else if (isAmex) return "AMEX";
        else if (isDiners) return "DINERS";
        else if (isHiperCard) return "HIPERCARD";
        else if (isElo) return "ELO";
        else if (isVisa) return "VISA";
        else if (isMaster) return "MASTER";
        else return "DESCONHECIDO";
    }

    public void showDialog(String title, final int type, String[] items) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int position) {
                if (type == MONTH)
                    month.set(String.valueOf(position + 1));
                else
                    year.set(String.valueOf(ACTUAL_YEAR + position));
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

    @Override
    public void onDestroy() {
        mContext = null;
        mRealmObject.close();
    }

    private boolean checkFields() {
        boolean fieldsIsOk = true;
        if (TextUtils.isEmpty(mCard.getCardMonth())) {
            errorCardMonth.set("Campo obrigatório");
            fieldsIsOk = false;
        }
        if (TextUtils.isEmpty(mCard.getCardYear())) {
            errorCardYear.set("Campo obrigatório");
            fieldsIsOk = false;
        }
        if (TextUtils.isEmpty(mCard.getCardHolder())) {
            errorCardHolder.set("Campo obrigatório");
            fieldsIsOk = false;
        }
        if (TextUtils.isEmpty(mCard.getCardNumber())) {
            errorCardNumber.set("Campo obrigatório");
            fieldsIsOk = false;
        }
        if (TextUtils.isEmpty(mCard.getCvv())) {
            errorCvv.set("Campo obrigatório");
            fieldsIsOk = false;
        }
        if (TextUtils.isEmpty(mCard.getValue())) {
            errorValue.set("Campo obrigatório");
            fieldsIsOk = false;
        }
        return fieldsIsOk;
    }
}
