package com.munchmash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.munchmash.R;
import com.munchmash.model.TransactionModel;
import com.munchmash.util.Utils;

import java.util.ArrayList;

/**
 * Created by Sumit on 06/08/17.
 */

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.TransactionViewHolder> {

    private Context context;
    private ArrayList<TransactionModel> transactionModelArrayList;

    public TransactionHistoryAdapter(Context context, ArrayList<TransactionModel> transactionModelArrayList) {
        this.context = context;
        this.transactionModelArrayList = transactionModelArrayList;
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_transaction_history, parent, false));
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        TransactionModel transactionModel = transactionModelArrayList.get(position);
        holder.tvTransactionId.setText(transactionModel.getTransactionId());
        if (transactionModel.getTransactionMode().trim().equalsIgnoreCase("credit"))
            holder.tvTransactionMode.setTextColor(context.getResources().getColor(R.color.green));
        else
            holder.tvTransactionMode.setTextColor(context.getResources().getColor(R.color.colorPrimary));

        holder.tvTransactionMode.setText(transactionModel.getTransactionMode());

        holder.tvTime.setText(Utils.formatDateTwoLines(transactionModel.getDateTime()));
        holder.tvTransactionAmount.setText("Transaction Amount :-" + " Rs" + transactionModel.getTransactionAmount());
        holder.tvDescription.setText(transactionModel.getDescription());
    }

    @Override
    public int getItemCount() {
        return transactionModelArrayList.size();
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTransactionId;
        private TextView tvTransactionMode;
        private TextView tvTime;
        private TextView tvTransactionAmount;
        private TextView tvDescription;


        TransactionViewHolder(View itemView) {
            super(itemView);
            tvTransactionId = itemView.findViewById(R.id.tv_transaction_id);
            tvTransactionMode = itemView.findViewById(R.id.tv_transaction_mode);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvTransactionAmount = itemView.findViewById(R.id.tv_transaction_amount);
            tvDescription = itemView.findViewById(R.id.tv_description);
        }
    }
}
