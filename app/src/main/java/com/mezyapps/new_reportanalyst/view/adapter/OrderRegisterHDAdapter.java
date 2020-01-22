package com.mezyapps.new_reportanalyst.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProductHD;
import com.mezyapps.new_reportanalyst.model.SalesReportModel;
import com.mezyapps.new_reportanalyst.view.activity.OrderRegisterDTActivity;
import com.mezyapps.new_reportanalyst.view.activity.SaleDetailsActivity;

import java.util.ArrayList;

public class OrderRegisterHDAdapter extends RecyclerView.Adapter<OrderRegisterHDAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<OrderEntryProductHD> orderEntryProductHDArrayList;

    public OrderRegisterHDAdapter(Context mContext, ArrayList<OrderEntryProductHD> orderEntryProductHDArrayList) {
        this.mContext = mContext;
        this.orderEntryProductHDArrayList = orderEntryProductHDArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_order_register,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final OrderEntryProductHD orderEntryProductHD=orderEntryProductHDArrayList.get(position);

        holder.textParty_name.setText(orderEntryProductHD.getParty_name());
        String total_qty="Total Qty "+orderEntryProductHD.getTotal_qty();
        String bill_amt="Total Amt "+orderEntryProductHD.getTotal_amt();
        String bill_no="Bill No "+String.valueOf(orderEntryProductHD.getMaxID());

        holder.text_total_qty.setText(total_qty);
        holder.textBillAmt.setText(bill_amt);
        holder.textBillNo.setText(bill_no);

        holder.textBillDate.setText(orderEntryProductHD.getDate());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, OrderRegisterDTActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("ORDER_NO",orderEntryProductHD.getMaxID());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderEntryProductHDArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textParty_name, text_total_qty, textBillAmt, textBillNo, textBillDate;
        private CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textParty_name = itemView.findViewById(R.id.textParty_name);
            text_total_qty = itemView.findViewById(R.id.text_total_qty);
            textBillAmt = itemView.findViewById(R.id.textBillAmt);
            textBillNo = itemView.findViewById(R.id.textBillNo);
            textBillDate = itemView.findViewById(R.id.textBillDate);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
