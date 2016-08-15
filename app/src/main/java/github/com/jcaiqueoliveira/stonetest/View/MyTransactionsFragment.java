package github.com.jcaiqueoliveira.stonetest.View;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import github.com.jcaiqueoliveira.stonetest.R;
import github.com.jcaiqueoliveira.stonetest.TransactionsAdapter;
import github.com.jcaiqueoliveira.stonetest.ViewModel.DataListener;
import github.com.jcaiqueoliveira.stonetest.databinding.FragmentMyTransactionsBinding;
import github.com.jcaiqueoliveira.stonetest.model.TransactionsDb;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTransactionsFragment extends Fragment implements DataListener {

    private FragmentMyTransactionsBinding mBinding;
    private RealmConfiguration mRealmConfiguration;

    public MyTransactionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_transactions, container, false);
        setupRecyclerView(mBinding.transactionRecyclerView);
        View v = mBinding.getRoot();
        return v;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        TransactionsAdapter adapter = new TransactionsAdapter(getMyTransactions(),this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.notifyDataSetChanged();
    }

    private RealmResults<TransactionsDb> getMyTransactions() {
        mRealmConfiguration = new RealmConfiguration.Builder(getActivity()).build();
        Realm.setDefaultConfiguration(mRealmConfiguration);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TransactionsDb> transactionsDbs = realm.where(TransactionsDb.class).findAll();
        return transactionsDbs;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDataChanged() {
        setupRecyclerView(mBinding.transactionRecyclerView);
    }
}
