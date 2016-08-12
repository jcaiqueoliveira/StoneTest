package github.com.jcaiqueoliveira.stonetest.View;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import github.com.jcaiqueoliveira.stonetest.R;
import github.com.jcaiqueoliveira.stonetest.ViewModel.MyTransactions;
import github.com.jcaiqueoliveira.stonetest.databinding.FragmentNewTransactionBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTransactionsFragment extends Fragment {

    private FragmentNewTransactionBinding mBinding;
    private MyTransactions mMyTransactions;

    public MyTransactionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_transactions, container, false);
        mMyTransactions = new MyTransactions(getActivity());
        View v = mBinding.getRoot();
        return v;
    }
}
