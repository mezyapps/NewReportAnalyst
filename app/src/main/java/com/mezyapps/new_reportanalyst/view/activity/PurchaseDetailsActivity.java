package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.connection.ConnectionCommon;
import com.mezyapps.new_reportanalyst.model.PurchaseDTModel;
import com.mezyapps.new_reportanalyst.model.PurchaseReportModel;
import com.mezyapps.new_reportanalyst.model.UserProfileModel;
import com.mezyapps.new_reportanalyst.utils.SharedLoginUtils;
import com.mezyapps.new_reportanalyst.utils.ShowProgressDialog;
import com.mezyapps.new_reportanalyst.view.adapter.PurchaseDTAdapter;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class PurchaseDetailsActivity extends AppCompatActivity {

    private ImageView iv_back;
    private PurchaseReportModel PurchaseReportModel;
    private TextView textBillNO,textDate,textPartyName,text_total_qty,textBillAMT;
    private String entry_id,databaseName;
    private RecyclerView recyclerView_product_list;
    private ArrayList<PurchaseDTModel> purchaseDTModelArrayList=new ArrayList<>();
    private PurchaseDTAdapter purchaseDTAdapter;
    private ConnectionCommon connectionCommon;
    private ShowProgressDialog showProgressDialog;
    private ArrayList<UserProfileModel> userProfileModelArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_details);

        find_View_IDs();
        events();
    }

    private void find_View_IDs() {
        connectionCommon = new ConnectionCommon();
        showProgressDialog = new ShowProgressDialog(PurchaseDetailsActivity.this);
        iv_back=findViewById(R.id.iv_back);
        textBillNO=findViewById(R.id.textBillNO);
        textDate=findViewById(R.id.textDate);
        textPartyName=findViewById(R.id.textPartyName);
        text_total_qty=findViewById(R.id.text_total_qty);
        textBillAMT=findViewById(R.id.textBillAMT);
        recyclerView_product_list=findViewById(R.id.recyclerView_product_list);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(PurchaseDetailsActivity.this);
        recyclerView_product_list.setLayoutManager(linearLayoutManager);

        userProfileModelArrayList= SharedLoginUtils.getUserProfile(PurchaseDetailsActivity.this);
        databaseName = userProfileModelArrayList.get(0).getDb_name();

        Bundle bundle = getIntent().getExtras();
        PurchaseReportModel = bundle.getParcelable("PURCHASE_REPORT");

        entry_id=PurchaseReportModel.getEntryid();
        String bill_no=PurchaseReportModel.getVchno();
        String date=PurchaseReportModel.getVchdt();
        String party_name=PurchaseReportModel.getGroupname();
        String total_qty="Bill Qty : 00";
        String total_amt="Bill Amt : 00";

        textBillNO.setText(bill_no);
        textDate.setText(date);
        textPartyName.setText(party_name);
        text_total_qty.setText(total_qty);
        textBillAMT.setText(total_amt);
    }

    private void events() {
        PurchaseDetails purchaseDetails = new PurchaseDetails();
        purchaseDetails.execute("");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @SuppressLint("StaticFieldLeak")
    public class PurchaseDetails extends AsyncTask<String, String, String> {

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
                purchaseDTAdapter = new PurchaseDTAdapter(PurchaseDetailsActivity.this, purchaseDTModelArrayList);
                recyclerView_product_list.setAdapter(purchaseDTAdapter);
                purchaseDTAdapter.notifyDataSetChanged();
                text_total_qty.setText("Bill Qty : "+TOTAL_QTY);
                textBillAMT.setText("Bill Amt : "+TOTAL_AMT);
            }
            else
            {
                purchaseDTAdapter.notifyDataSetChanged();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection connection = connectionCommon.checkUserConnection(databaseName);
                if (connection == null) {
                    msg = "Check Your Internet Access!";
                } else {
                    String selectQuery =
                            "SELECT  * FROM TBL_PURCH_DT WHERE ENTRYID="+entry_id;


                    Statement stmt = connection.createStatement();
                    ResultSet resultSet = stmt.executeQuery(selectQuery);
                    purchaseDTModelArrayList.clear();
                    while (resultSet.next()) {
                        String prod_name = resultSet.getString("PMSTNAME");
                        String prod_qty = resultSet.getString("PRODQTY");
                        String cost_rate = resultSet.getString("COSTRATE");
                        String prod_gross_amt = resultSet.getString("PRODGROSSAMT");
                        String dist_per1 = resultSet.getString("TD_PER");
                        String dist_per2= resultSet.getString("SP_PER");
                        String dist1 = resultSet.getString("TD_AMT");
                        String dist2= resultSet.getString("SP_AMT");
                        String cgst_per = resultSet.getString("CGST_PER");
                        String sgst_per= resultSet.getString("SGST_PER");
                        String igst_per= resultSet.getString("IGST_AMT");
                        String cgst = resultSet.getString("CGST_AMT");
                        String sgst= resultSet.getString("SGST_AMT");
                        String igst= resultSet.getString("IGST_AMT");
                        String final_Amt = resultSet.getString("FINAL_AMT");

                        TOTAL_AMT = TOTAL_AMT + Double.parseDouble(final_Amt);
                        TOTAL_QTY = TOTAL_QTY + Double.parseDouble(prod_qty);

                        PurchaseDTModel purchaseDTModel = new PurchaseDTModel();
                        purchaseDTModel.setProd_name(prod_name);
                        purchaseDTModel.setProd_qty(prod_qty);
                        purchaseDTModel.setCost_rate(cost_rate);
                        purchaseDTModel.setProd_gross_amt(prod_gross_amt);
                        purchaseDTModel.setDist_per1(dist_per1);
                        purchaseDTModel.setDist_per2(dist_per2);
                        purchaseDTModel.setDist(dist1);
                        purchaseDTModel.setDist1(dist2);
                        purchaseDTModel.setCgst_per(cgst_per);
                        purchaseDTModel.setSgst_per(sgst_per);
                        purchaseDTModel.setIgst_per(igst_per);
                        purchaseDTModel.setCgst(cgst);
                        purchaseDTModel.setSgst(sgst);
                        purchaseDTModel.setIgst(igst);
                        purchaseDTModel.setFinal_Amt(final_Amt);
                        purchaseDTModelArrayList.add(purchaseDTModel);
                    }
                    if (purchaseDTModelArrayList.size() != 0) {
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
