package com.mezyapps.new_reportanalyst.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.model.OutstandingPayableHDModel;
import com.mezyapps.new_reportanalyst.model.OutstandingReceivableModel;

import java.util.ArrayList;

public class OutstandingHDReceivableAdapter  extends RecyclerView.Adapter<OutstandingHDReceivableAdapter.MyViewHolder> {

    private ArrayList<OutstandingReceivableModel> outstandingReceivableModelArrayList;
    private Context mContext;

    public OutstandingHDReceivableAdapter(ArrayList<OutstandingReceivableModel> outstandingReceivableModelArrayList, Context mContext) {
        this.outstandingReceivableModelArrayList = outstandingReceivableModelArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public OutstandingHDReceivableAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_os_hd_payable,parent,false);
        return new OutstandingHDReceivableAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OutstandingHDReceivableAdapter.MyViewHolder holder, int position) {
        final  OutstandingReceivableModel outstandingReceivableModel=outstandingReceivableModelArrayList.get(position);

        holder.textPartyNamePayable.setText(outstandingReceivableModel.getParty_name());
        holder.textAmount.setText(outstandingReceivableModel.getAmt());
    }

    @Override
    public int getItemCount() {
        return outstandingReceivableModelArrayList.size();
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

