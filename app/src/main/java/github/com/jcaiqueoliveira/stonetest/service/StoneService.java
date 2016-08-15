package github.com.jcaiqueoliveira.stonetest.service;

import github.com.jcaiqueoliveira.stonetest.model.Card;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Kanda on 11/08/2016.
 */
public interface StoneService {
    @POST("transaction")
    Call<String> newTransaction(@Body Card card);

    @DELETE("transaction/{idTransaction}")
    Call<Void> deleteTransaction(@Path("idTransaction") String path);
}
