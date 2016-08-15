package github.com.jcaiqueoliveira.stonetest;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import github.com.jcaiqueoliveira.stonetest.ViewModel.DataListener;
import github.com.jcaiqueoliveira.stonetest.ViewModel.ItemTransaction;
import github.com.jcaiqueoliveira.stonetest.databinding.ItemTransactionBinding;
import github.com.jcaiqueoliveira.stonetest.model.TransactionsDb;

/**
 * Created by Kanda on 14/08/2016.
 */

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder> {

    private List<TransactionsDb> transactionsDbs;
    public static DataListener listener;


    public TransactionsAdapter(List<TransactionsDb> transactions, DataListener listener) {
        this.transactionsDbs = transactions;
        this.listener = listener;

    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemTransactionBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_transaction,
                parent,
                false);
        return new TransactionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        holder.bindRepository(transactionsDbs.get(position));
    }

    @Override
    public int getItemCount() {
        return transactionsDbs.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder implements DataListener {
        final ItemTransactionBinding binding;

        public TransactionViewHolder(ItemTransactionBinding binding) {
            super(binding.cardView);
            this.binding = binding;
        }

        void bindRepository(TransactionsDb transactionsDb) {
            if (binding.getViewModel() == null) {
                binding.setViewModel(new ItemTransaction(itemView.getContext(), transactionsDb, this));
            } else {
                binding.getViewModel().setTransaction(transactionsDb);
            }
        }

        @Override
        public void onDataChanged() {
            listener.onDataChanged();
        }
    }
}