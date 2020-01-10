package com.mezyapps.new_reportanalyst.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.model.OutstandingPayableHDModel;

import java.util.ArrayList;

public class OutstandingHDPayableAdapter extends RecyclerView.Adapter<OutstandingHDPayableAdapter.MyViewHolder> {

    private ArrayList<OutstandingPayableHDModel> outstandingPayableHDModelArrayList;
    private Context mContext;

    public OutstandingHDPayableAdapter(ArrayList<OutstandingPayableHDModel> outstandingPayableHDModelArrayList, Context mContext) {
        this.outstandingPayableHDModelArrayList = outstandingPayableHDModelArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_os_hd_payable,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final  OutstandingPayableHDModel outstandingPayableHDModel=outstandingPayableHDModelArrayList.get(position);

        holder.textPartyNamePayable.setText(outstandingPayableHDModel.getParty_name());
        holder.textAmount.setText(outstandingPayableHDModel.getAmt());
    }

    @Override
    public int getItemCount() {
        return outstandingPayableHDModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textPartyNamePayable,textAmount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textPartyNamePayable=itemView.findViewById(R.id.textPartyNamePayable);
            textAmount=itemView.findViewById(R.id.textAmount);
        }
    }
}
