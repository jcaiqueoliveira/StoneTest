package github.com.jcaiqueoliveira.stonetest.ViewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;

import retrofit2.Retrofit;


/**
 * Created by Kanda on 11/08/2016.
 */
public class NewTransaction extends BaseObservable {
    public String errorCardHolder, errorCardNumber, errorCardMonth, errorCardYear, errorCvv, errorValue;
    private Context mContext;
    private Retrofit mRetrofit;

    public NewTransaction(Context context) {
        this.mContext = context;
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://private-eafd6f-stone8.apiary-mock.com/")
                .build();
    }


    public void newTransaction(View v) {
        //Toast.makeText(context, "It's Done!", Toast.LENGTH_SHORT).show();
    }
}
