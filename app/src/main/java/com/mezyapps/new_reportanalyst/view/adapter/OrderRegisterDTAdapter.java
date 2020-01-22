package com.mezyapps.new_reportanalyst.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProductDT;

import java.util.ArrayList;

public class OrderRegisterDTAdapter extends RecyclerView.Adapter<OrderRegisterDTAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<OrderEntryProductDT> orderEntryProductDTArrayList;
    int cnt=1;

    public OrderRegisterDTAdapter(Context mContext, ArrayList<OrderEntryProductDT> orderEntryProductDTArrayList) {
        this.mContext = mContext;
        this.orderEntryProductDTArrayList = orderEntryProductDTArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_order_register_dt_adapter,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final OrderEntryProductDT orderEntryProduct=orderEntryProductDTArrayList.get(position);

        holder.textProductName.setText("("+cnt+") "+orderEntryProduct.getProduct_name());
        holder.textDist.setText(orderEntryProduct.getDist_amt());
        holder.textGst.setText(orderEntryProduct.getGst_amt());
        holder.textSubTotal.setText(orderEntryProduct.getFinal_total());
        cnt++;
    }

    @Override
    public int getItemCount() {
        return orderEntryProductDTArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textProductName,textDist,textGst,textSubTotal;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textProductName=itemView.findViewById(R.id.textProductName);
            textDist=itemView.findViewById(R.id.textDist);
            textGst=itemView.findViewById(R.id.textGst);
            textSubTotal=itemView.findViewById(R.id.textSubTotal);
        }
    }
}

