package com.mezyapps.new_reportanalyst.view.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.connection.ConnectionCommon;
import com.mezyapps.new_reportanalyst.model.SalesDetailsModel;
import com.mezyapps.new_reportanalyst.model.UserProfileModel;
import com.mezyapps.new_reportanalyst.utils.SharedLoginUtils;
import com.mezyapps.new_reportanalyst.view.activity.LoginActivity;
import com.mezyapps.new_reportanalyst.view.activity.MainActivity;
import com.mezyapps.new_reportanalyst.view.activity.OrderEnteryActivity;
import com.mezyapps.new_reportanalyst.view.activity.OutstandingPayableActivity;
import com.mezyapps.new_reportanalyst.view.activity.OutstandingReceivableActivity;
import com.mezyapps.new_reportanalyst.view.activity.PurchaseReportActivity;
import com.mezyapps.new_reportanalyst.view.activity.SaleDetailsActivity;
import com.mezyapps.new_reportanalyst.view.activity.SalesReportActivity;
import com.mezyapps.new_reportanalyst.view.activity.SplashActivity;
import com.mezyapps.new_reportanalyst.view.activity.StockReportActivity;
import com.mezyapps.new_reportanalyst.view.adapter.SalesDetailsAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private Context mContext;
    private CardView cardView_Sales_Report, cardViewPurchase_Report, cardView_outstanding_receivable, cardView_outstanding_payable, cardView_stock_report, cardView_order_entry;
    private TextView textSalesManName;
    private ArrayList<UserProfileModel> userProfileModelArrayList = new ArrayList<>();
    private String username;
    private ConnectionCommon connectionCommon;

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
        connectionCommon = new ConnectionCommon();
        cardView_Sales_Report = view.findViewById(R.id.cardView_Sales_Report);
        cardViewPurchase_Report = view.findViewById(R.id.cardViewPurchase_Report);
        cardView_outstanding_receivable = view.findViewById(R.id.cardView_outstanding_receivable);
        cardView_outstanding_payable = view.findViewById(R.id.cardView_outstanding_payable);
        cardView_stock_report = view.findViewById(R.id.cardView_stock_report);
        cardView_order_entry = view.findViewById(R.id.cardView_order_entry);
        textSalesManName = view.findViewById(R.id.textSalesManName);

        userProfileModelArrayList = SharedLoginUtils.getUserProfile(mContext);
        String salesman_name = userProfileModelArrayList.get(0).getSALESMAN_NAME();
        username = userProfileModelArrayList.get(0).getUser_name();
        if (salesman_name.equalsIgnoreCase("")) {
            textSalesManName.setText(getResources().getString(R.string.app_name));
        } else {
            textSalesManName.setText("Hi " + salesman_name);
        }

    }

    private void events() {
        CheckUserSession userSession = new CheckUserSession();
        userSession.execute("");

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

    @SuppressLint("StaticFieldLeak")
    public class CheckUserSession extends AsyncTask<String, String, String> {

        String msg = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String message) {
            if (message.equalsIgnoreCase("unsuccess")) {
                dialog_session_expire();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection connection = connectionCommon.connectionDatabase();
                if (connection == null) {
                    msg = "Check Your Internet Access!";
                } else {
                    String query = "select * from UACCESS where USER_NAME='" + username + "'";

                    Statement stmt = connection.createStatement();
                    ResultSet resultSet = stmt.executeQuery(query);
                    if (resultSet.next()) {
                        msg = "success";
                        isSuccess = true;
                        connection.close();
                    } else {
                        msg = "unsuccess";
                        isSuccess = false;
                        connection.close();
                    }
                    connection.close();
                }
            } catch (Exception ex) {
                isSuccess = false;
                msg = ex.getMessage();
            }
            return msg;
        }
    }

    private void dialog_session_expire() {
        final Dialog dialog_session_expire = new Dialog(mContext);
        dialog_session_expire.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_session_expire.setContentView(R.layout.dialog_session_expire);

        dialog_session_expire.setCancelable(false);
        dialog_session_expire.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog_session_expire.show();

        Window window = dialog_session_expire.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedLoginUtils.removeLoginSharedUtils(mContext);
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        }, 2000);


    }

}
