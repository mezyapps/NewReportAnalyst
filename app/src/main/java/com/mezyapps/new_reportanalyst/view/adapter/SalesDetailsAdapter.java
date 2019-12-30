package com.mezyapps.new_reportanalyst.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.model.SalesDetailsModel;


import java.util.ArrayList;

public class SalesDetailsAdapter  extends RecyclerView.Adapter<SalesDetailsAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<SalesDetailsModel> salesDetailsModelArrayList;

    public SalesDetailsAdapter(Context mContext, ArrayList<SalesDetailsModel> salesDetailsModelArrayList) {
        this.mContext = mContext;
        this.salesDetailsModelArrayList = salesDetailsModelArrayList;
    }

    @NonNull
    @Override
    public SalesDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_sale_dt_item_adpter, parent, false);
        return new SalesDetailsAdapter.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SalesDetailsAdapter.MyViewHolder holder, final int position) {
        final  SalesDetailsModel salesDetailsModel=salesDetailsModelArrayList.get(position);

        holder.textProd_Name.setText(salesDetailsModel.getProd_name());
        String item_total="Qty : "+salesDetailsModel.getProd_qty()+" X  Rs : "+salesDetailsModel.getCost_rate();
        holder.textQty_rate.setText(item_total);
        String qty_rate=salesDetailsModel.getProd_gross_amt();
        holder.textGross_amt.setText(qty_rate);


        String  dist_total=salesDetailsModel.getDist_per1()+"+"+salesDetailsModel.getDist_per2();
        String dist_per="Discount (%) = "+dist_total;

        double dist_amt1=Double.parseDouble(salesDetailsModel.getDist());
        double dist_amt2=Double.parseDouble(salesDetailsModel.getDist1());
        double dist_t_amt=dist_amt1+dist_amt2;

        holder.textDist_Per.setText(dist_per);
        holder.textTotalDist.setText(String.valueOf(dist_t_amt));


        double cgest_per=Double.parseDouble(salesDetailsModel.getCgst_per());
        double sgst_per=Double.parseDouble(salesDetailsModel.getSgst_per());
        double igst_per=Double.parseDouble(salesDetailsModel.getIgst_per());
        if(igst_per==0.00)
        {
            holder.ll_igest.setVisibility(View.GONE);
        }
        else
        {
            holder.ll_sgst.setVisibility(View.GONE);
            holder.ll_cgst.setVisibility(View.GONE);
        }
        double total_dist_per=cgest_per+sgst_per+igst_per;

        String cgst_per1="CGST (%) = "+cgest_per;
        String sgst_per1="SGST (%) = "+sgst_per;
        String igst_per1="IGST (%) = "+igst_per;

        holder.textCGstPer.setText(cgst_per1);
        holder.textSGstPer.setText(sgst_per1);
        holder.textIGstPer.setText(igst_per1);


        double cgest_amt=Double.parseDouble(salesDetailsModel.getCgst());
        double sgst_amt=Double.parseDouble(salesDetailsModel.getSgst());
        double igst_amt=Double.parseDouble(salesDetailsModel.getIgst());
        double total_dist_amt=cgest_amt+sgst_amt+igst_amt;

        holder.textTotalCGstAmt.setText(String.valueOf(cgest_amt));
        holder.textTotalGstPerAmt.setText(String.valueOf(sgst_amt));
        holder.textTotalIGstPerAmt.setText(String.valueOf(igst_amt));

        String total_final_amt=salesDetailsModel.getFinal_Amt();
        holder.textFinalTotal.setText(total_final_amt);
    }

    @Override
    public int getItemCount() {
        return salesDetailsModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textProd_Name,textQty_rate,textGross_amt,textDist_Per,textTotalDist
                ,textCGstPer,textTotalCGstAmt,textSGstPer,textTotalGstPerAmt,textIGstPer,textTotalIGstPerAmt,textFinalTotal;
        private LinearLayout ll_igest,ll_sgst,ll_cgst;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textProd_Name = itemView.findViewById(R.id.textProd_Name);
            textQty_rate = itemView.findViewById(R.id.textQty_rate);
            textGross_amt = itemView.findViewById(R.id.textGross_amt);
            textDist_Per = itemView.findViewById(R.id.textDist_Per);
            textTotalDist = itemView.findViewById(R.id.textTotalDist);
            textCGstPer = itemView.findViewById(R.id.textCGstPer);
            textTotalCGstAmt = itemView.findViewById(R.id.textTotalCGstAmt);
            ll_igest = itemView.findViewById(R.id.ll_igest);

            textSGstPer = itemView.findViewById(R.id.textSGstPer);
            textTotalGstPerAmt = itemView.findViewById(R.id.textTotalGstPerAmt);
            ll_sgst = itemView.findViewById(R.id.ll_sgst);
            ll_cgst = itemView.findViewById(R.id.ll_cgst);

            textIGstPer = itemView.findViewById(R.id.textIGstPer);
            textTotalIGstPerAmt = itemView.findViewById(R.id.textTotalIGstPerAmt);

            textFinalTotal = itemView.findViewById(R.id.textFinalTotal);
        }
    }
}
