package com.mezyapps.new_reportanalyst.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.model.SalesReportModel;

import java.util.ArrayList;

public class SalesReportAdapter extends RecyclerView.Adapter<SalesReportAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<SalesReportModel> salesReportModelArrayList;

    public SalesReportAdapter(Context mContext, ArrayList<SalesReportModel> salesReportModelArrayList) {
        this.mContext = mContext;
        this.salesReportModelArrayList = salesReportModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_report_item_adpter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final  SalesReportModel salesReportModel=salesReportModelArrayList.get(position);

        holder.textParty_name.setText(salesReportModel.getGroupname());
        holder.text_total_qty.setText(salesReportModel.getTotalqty());
        holder.textBillAmt.setText(salesReportModel.getTotalfinalamt());

        holder.textBillNo.setText(salesReportModel.getVchno());
        holder.textBillDate.setText(salesReportModel.getVchdt());
        holder.textNarration.setText(salesReportModel.getNarration());

    }

    @Override
    public int getItemCount() {
        return salesReportModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textParty_name, text_total_qty, textBillAmt, textBillNo, textBillDate,textNarration;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textParty_name = itemView.findViewById(R.id.textParty_name);
            text_total_qty = itemView.findViewById(R.id.text_total_qty);
            textBillAmt = itemView.findViewById(R.id.textBillAmt);
            textBillNo = itemView.findViewById(R.id.textBillNo);
            textBillDate = itemView.findViewById(R.id.textBillDate);
            textNarration = itemView.findViewById(R.id.textNarration);
        }
    }
}
