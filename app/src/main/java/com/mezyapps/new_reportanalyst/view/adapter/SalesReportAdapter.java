package com.mezyapps.new_reportanalyst.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.database.DatabaseConstant;
import com.mezyapps.new_reportanalyst.model.SalesReportModel;
import com.mezyapps.new_reportanalyst.view.activity.SaleDetailsActivity;

import java.util.ArrayList;

public class SalesReportAdapter extends RecyclerView.Adapter<SalesReportAdapter.MyViewHolder> implements Filterable {
    private Context mContext;
    private ArrayList<SalesReportModel> salesReportModelArrayList;
    private  ArrayList<SalesReportModel> arrayListFiltered;

    public SalesReportAdapter(Context mContext, ArrayList<SalesReportModel> salesReportModelArrayList) {
        this.mContext = mContext;
        this.salesReportModelArrayList = salesReportModelArrayList;
        this.arrayListFiltered=salesReportModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_report_item_adpter, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final  SalesReportModel salesReportModel=salesReportModelArrayList.get(position);

        holder.textParty_name.setText(salesReportModel.getGroupname());
        String total_qty=salesReportModel.getTotalqty();
        String bill_amt=salesReportModel.getTotalbillamt();
        String bill_no=salesReportModel.getVchno();

        holder.text_total_qty.setText(total_qty);
        holder.textBillAmt.setText(bill_amt);
        holder.textBillNo.setText(bill_no);

        holder.textBillDate.setText(salesReportModel.getVchdt());
        String narration=salesReportModel.getNarration();

        if(narration.equalsIgnoreCase("")) {
            holder.textNarration.setVisibility(View.GONE);
        }
        else
        {
            holder.textNarration.setText(narration);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, SaleDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("SALES_REPORT", (Parcelable) salesReportModelArrayList.get(position));
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return salesReportModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textParty_name, text_total_qty, textBillAmt, textBillNo, textBillDate,textNarration;
        private CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textParty_name = itemView.findViewById(R.id.textParty_name);
            text_total_qty = itemView.findViewById(R.id.text_total_qty);
            textBillAmt = itemView.findViewById(R.id.textBillAmt);
            textBillNo = itemView.findViewById(R.id.textBillNo);
            textBillDate = itemView.findViewById(R.id.textBillDate);
            textNarration = itemView.findViewById(R.id.textNarration);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().replaceAll("\\s","").toLowerCase().trim();
                if (charString.isEmpty() || charSequence.equals("")) {
                    salesReportModelArrayList = arrayListFiltered;
                } else {
                    ArrayList<SalesReportModel> filteredList = new ArrayList<>();
                    for (int i = 0; i < salesReportModelArrayList.size(); i++) {
                        String bill_no=salesReportModelArrayList.get(i).getVchno().replaceAll("\\s","").toLowerCase().trim();
                        String  party_name=salesReportModelArrayList.get(i).getGroupname().toLowerCase().replaceAll("\\s","").toLowerCase().trim();
                        String  date=salesReportModelArrayList.get(i).getVchdt().toLowerCase().replaceAll("\\s","").toLowerCase().trim();
                        if ((bill_no.contains(charString))||(party_name.contains(charString))||(date.contains(charString))) {
                            filteredList.add(salesReportModelArrayList.get(i));
                        }
                    }
                    if (filteredList.size() > 0) {
                        salesReportModelArrayList = filteredList;
                    } else {
                        salesReportModelArrayList = arrayListFiltered;
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = salesReportModelArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                salesReportModelArrayList = (ArrayList<SalesReportModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
