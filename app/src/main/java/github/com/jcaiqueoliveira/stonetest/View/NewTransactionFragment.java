package github.com.jcaiqueoliveira.stonetest.View;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import github.com.jcaiqueoliveira.stonetest.R;
import github.com.jcaiqueoliveira.stonetest.ViewModel.NewTransaction;
import github.com.jcaiqueoliveira.stonetest.databinding.FragmentNewTransactionBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewTransactionFragment extends Fragment {

    private FragmentNewTransactionBinding mBinding;
    private NewTransaction mNewTransaction;

    public NewTransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_transaction, container, false);
        View v = mBinding.getRoot();
        mNewTransaction = new NewTransaction(getActivity());
        mBinding.setNewTransaction(mNewTransaction);
        return v;
    }
}
