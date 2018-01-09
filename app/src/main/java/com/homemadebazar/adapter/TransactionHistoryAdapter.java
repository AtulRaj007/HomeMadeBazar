package com.homemadebazar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homemadebazar.R;

/**
 * Created by Sumit on 06/08/17.
 */

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.TransactionViewHolder> {

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_transaction_history, parent, false));
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder {

        public TransactionViewHolder(View itemView) {
            super(itemView);
        }
    }
}
