package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.connection.ConnectionCommon;
import com.mezyapps.new_reportanalyst.model.SalesDetailsModel;
import com.mezyapps.new_reportanalyst.model.SalesReportModel;
import com.mezyapps.new_reportanalyst.model.UserProfileModel;
import com.mezyapps.new_reportanalyst.utils.SharedLoginUtils;
import com.mezyapps.new_reportanalyst.utils.ShowProgressDialog;
import com.mezyapps.new_reportanalyst.view.adapter.SalesDetailsAdapter;
import com.mezyapps.new_reportanalyst.view.adapter.SalesReportAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SaleDetailsActivity extends AppCompatActivity {

    private ImageView iv_back;
    private SalesReportModel salesReportModel;
    private TextView textBillNO, textDate, textPartyName, text_total_qty, textBillAMT;
    private String entry_id, databaseName;
    private RecyclerView recyclerView_product_list;
    private ArrayList<SalesDetailsModel> salesDetailsModelArrayList = new ArrayList<>();
    private SalesDetailsAdapter salesDetailsAdapter;
    private ConnectionCommon connectionCommon;
    private ShowProgressDialog showProgressDialog;
    private ArrayList<UserProfileModel> userProfileModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_deatils);

        find_View_IDs();
        events();
    }

    private void find_View_IDs() {
        connectionCommon = new ConnectionCommon();
        showProgressDialog = new ShowProgressDialog(SaleDetailsActivity.this);
        iv_back = findViewById(R.id.iv_back);
        textBillNO = findViewById(R.id.textBillNO);
        textDate = findViewById(R.id.textDate);
        textPartyName = findViewById(R.id.textPartyName);
        text_total_qty = findViewById(R.id.text_total_qty);
        textBillAMT = findViewById(R.id.textBillAMT);
        recyclerView_product_list = findViewById(R.id.recyclerView_product_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SaleDetailsActivity.this);
        recyclerView_product_list.setLayoutManager(linearLayoutManager);

        Bundle bundle = getIntent().getExtras();
        salesReportModel = bundle.getParcelable("SALES_REPORT");

        entry_id = salesReportModel.getEntryid();
        String bill_no = salesReportModel.getVchno();
        String date = salesReportModel.getVchdt();
        String party_name = salesReportModel.getGroupname();
        text_total_qty.setText("Bill Qty :"+"00");
        textBillAMT.setText("Bill Amt :"+"00");

        userProfileModelArrayList = SharedLoginUtils.getUserProfile(SaleDetailsActivity.this);
        databaseName = userProfileModelArrayList.get(0).getDb_name();

        textBillNO.setText(bill_no);
        textDate.setText(date);
        textPartyName.setText(party_name);

    }

    private void events() {
        SalesDetails salesDetails = new SalesDetails();
        salesDetails.execute("");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class SalesDetails extends AsyncTask<String, String, String> {

        String msg = "";
        boolean isSuccess = false;
        double TOTAL_AMT, TOTAL_QTY;

        @Override
        protected void onPreExecute() {
            showProgressDialog.showDialog();
        }

        @Override
        protected void onPostExecute(String message) {
            showProgressDialog.dismissDialog();
            if (message.equalsIgnoreCase("success")) {
                salesDetailsAdapter = new SalesDetailsAdapter(SaleDetailsActivity.this, salesDetailsModelArrayList);
                recyclerView_product_list.setAdapter(salesDetailsAdapter);
                salesDetailsAdapter.notifyDataSetChanged();
                text_total_qty.setText("Bill Qty : "+TOTAL_QTY);
                textBillAMT.setText("Bill Amt : "+TOTAL_AMT);
            } else {

            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection connection = connectionCommon.checkUserConnection(databaseName);
                if (connection == null) {
                    msg = "Check Your Internet Access!";
                } else {
                    String query =
                            "SELECT  * FROM TBL_SALE_DT WHERE ENTRYID=" + entry_id;

                    Statement stmt = connection.createStatement();
                    ResultSet resultSet = stmt.executeQuery(query);
                    salesDetailsModelArrayList.clear();
                    while (resultSet.next()) {
                        String prod_name = resultSet.getString("PMSTNAME");
                        String prod_qty = resultSet.getString("PRODQTY");
                        String cost_rate = resultSet.getString("COSTRATE");
                        String prod_gross_amt = resultSet.getString("PRODGROSSAMT");
                        String dist_per1 = resultSet.getString("TD_PER");
                        String dist_per2 = resultSet.getString("SP_PER");
                        String dist1 = resultSet.getString("TD_AMT");
                        String dist2 = resultSet.getString("SP_AMT");
                        String cgst_per = resultSet.getString("CGST_PER");
                        String sgst_per = resultSet.getString("SGST_PER");
                        String igst_per = resultSet.getString("IGST_AMT");
                        String cgst = resultSet.getString("CGST_AMT");
                        String sgst = resultSet.getString("SGST_AMT");
                        String igst = resultSet.getString("IGST_AMT");
                        String final_Amt = resultSet.getString("FINAL_AMT");


                        TOTAL_AMT = TOTAL_AMT + Double.parseDouble(final_Amt);
                        TOTAL_QTY = TOTAL_QTY + Double.parseDouble(prod_qty);

                        SalesDetailsModel salesDetailsModel = new SalesDetailsModel();
                        salesDetailsModel.setProd_name(prod_name);
                        salesDetailsModel.setProd_qty(prod_qty);
                        salesDetailsModel.setCost_rate(cost_rate);
                        salesDetailsModel.setProd_gross_amt(prod_gross_amt);
                        salesDetailsModel.setDist_per1(dist_per1);
                        salesDetailsModel.setDist_per2(dist_per2);
                        salesDetailsModel.setDist(dist1);
                        salesDetailsModel.setDist1(dist2);
                        salesDetailsModel.setCgst_per(cgst_per);
                        salesDetailsModel.setSgst_per(sgst_per);
                        salesDetailsModel.setIgst_per(igst_per);
                        salesDetailsModel.setCgst(cgst);
                        salesDetailsModel.setSgst(sgst);
                        salesDetailsModel.setIgst(igst);
                        salesDetailsModel.setFinal_Amt(final_Amt);

                        salesDetailsModelArrayList.add(salesDetailsModel);

                    }
                    if (salesDetailsModelArrayList.size() != 0) {
                        msg = "success";
                        isSuccess = true;
                    } else {
                        msg = "fail";
                        isSuccess = false;
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
}
