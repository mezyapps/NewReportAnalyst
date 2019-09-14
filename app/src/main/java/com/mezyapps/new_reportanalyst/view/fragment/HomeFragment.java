package com.mezyapps.new_reportanalyst.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.view.activity.OrderEnteryActivity;
import com.mezyapps.new_reportanalyst.view.activity.OutstandingPayableActivity;
import com.mezyapps.new_reportanalyst.view.activity.OutstandingReceivableActivity;
import com.mezyapps.new_reportanalyst.view.activity.PurchaseReportActivity;
import com.mezyapps.new_reportanalyst.view.activity.SalesReportActivity;
import com.mezyapps.new_reportanalyst.view.activity.StockReportActivity;

public class HomeFragment extends Fragment {

    private Context mContext;
    private CardView cardView_Sales_Report, cardViewPurchase_Report, cardView_outstanding_receivable, cardView_outstanding_payable, cardView_stock_report, cardView_order_entry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getActivity();

        find_View_IdS(view);
        events();

        return view;
    }

    private void find_View_IdS(View view) {
        cardView_Sales_Report = view.findViewById(R.id.cardView_Sales_Report);
        cardViewPurchase_Report = view.findViewById(R.id.cardViewPurchase_Report);
        cardView_outstanding_receivable = view.findViewById(R.id.cardView_outstanding_receivable);
        cardView_outstanding_payable = view.findViewById(R.id.cardView_outstanding_payable);
        cardView_stock_report = view.findViewById(R.id.cardView_stock_report);
        cardView_order_entry = view.findViewById(R.id.cardView_order_entry);
    }

    private void events() {
        cardView_Sales_Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SalesReportActivity.class));
            }
        });
        cardViewPurchase_Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PurchaseReportActivity.class));
            }
        });
        cardView_outstanding_receivable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, OutstandingReceivableActivity.class));
            }
        });
        cardView_outstanding_payable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, OutstandingPayableActivity.class));
            }
        });
        cardView_stock_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, StockReportActivity.class));
            }
        });
        cardView_order_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, OrderEnteryActivity.class));
            }
        });
    }


}
