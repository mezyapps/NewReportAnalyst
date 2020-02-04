package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.connection.ConnectionCommon;
import com.mezyapps.new_reportanalyst.db.AppDatabase;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProduct;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProductDT;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProductHD;
import com.mezyapps.new_reportanalyst.model.UserProfileModel;
import com.mezyapps.new_reportanalyst.utils.SharedLoginUtils;
import com.mezyapps.new_reportanalyst.utils.ShowProgressDialog;
import com.mezyapps.new_reportanalyst.view.adapter.OrderEntryProductAdapter;
import com.mezyapps.new_reportanalyst.view.adapter.OrderRegisterDTAdapter;
import com.mezyapps.new_reportanalyst.view.adapter.OrderRegisterHDAdapter;
import com.mezyapps.new_reportanalyst.view.fragment.ChancePasswordFragment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

public class OrderRegisterDTActivity extends AppCompatActivity {
    private ImageView iv_back, iv_delete_order, iv_upload_db;
    private AppDatabase appDatabase;
    private ArrayList<OrderEntryProductHD> orderEntryProductHDArrayList = new ArrayList<>();
    private ArrayList<OrderEntryProductDT> orderEntryProductDTArrayList = new ArrayList<>();
    private long orderno;
    private TextView textParty_name, text_total_qty, textBillAmt, textBillNo, textBillDate;
    private RecyclerView recycler_view_order_registerDT;
    private ShowProgressDialog showProgressDialog;
    private ConnectionCommon connectionCommon;
    private String databaseName,saleman_id, saleman_name,inclu_exclu;
    private ArrayList<UserProfileModel> userProfileModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_register_dt);


        find_view_IDs();
        events();

    }

    private void find_view_IDs() {
        showProgressDialog = new ShowProgressDialog(OrderRegisterDTActivity.this);
        connectionCommon = new ConnectionCommon(OrderRegisterDTActivity.this);
        iv_back = findViewById(R.id.iv_back);
        textParty_name = findViewById(R.id.textParty_name);
        text_total_qty = findViewById(R.id.text_total_qty);
        textBillAmt = findViewById(R.id.textBillAmt);
        textBillNo = findViewById(R.id.textBillNo);
        textBillDate = findViewById(R.id.textBillDate);
        recycler_view_order_registerDT = findViewById(R.id.recycler_view_order_registerDT);
        iv_delete_order = findViewById(R.id.iv_delete_order);
        iv_upload_db = findViewById(R.id.iv_upload_db);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderRegisterDTActivity.this);
        recycler_view_order_registerDT.setLayoutManager(linearLayoutManager);


        userProfileModelArrayList = SharedLoginUtils.getUserProfile(OrderRegisterDTActivity.this);
        databaseName = userProfileModelArrayList.get(0).getDb_name();
        saleman_id = userProfileModelArrayList.get(0).getSALESMAN_ID().trim();
        saleman_name = userProfileModelArrayList.get(0).getSALESMAN_NAME().trim();
        inclu_exclu = userProfileModelArrayList.get(0).getINCLU_EXCLU().trim();

        appDatabase=AppDatabase.getInStatce(OrderRegisterDTActivity.this);

        Bundle bundle = getIntent().getExtras();
        orderno = bundle.getLong("ORDER_NO");
        orderEntryProductHDArrayList.addAll(appDatabase.getProductHDDAO().getOnlyIDValue(orderno));

        textParty_name.setText(orderEntryProductHDArrayList.get(0).getParty_name());
        String total_qty = "Total Qty " + orderEntryProductHDArrayList.get(0).getTotal_qty();
        String bill_amt = "Total Amt " + orderEntryProductHDArrayList.get(0).getTotal_amt();
        String bill_no = "Bill No " + String.valueOf(orderEntryProductHDArrayList.get(0).getMaxID());


        text_total_qty.setText(total_qty);
        textBillAmt.setText(bill_amt);
        textBillNo.setText(bill_no);
        textBillDate.setText(orderEntryProductHDArrayList.get(0).getDate());

        orderEntryProductDTArrayList.addAll(appDatabase.getProductDTDAO().getOnlyIDValue(orderno));
        Collections.reverse(orderEntryProductHDArrayList);
        OrderRegisterDTAdapter orderRegisterDTAdapter = new OrderRegisterDTAdapter(OrderRegisterDTActivity.this, orderEntryProductDTArrayList);
        recycler_view_order_registerDT.setAdapter(orderRegisterDTAdapter);
        orderRegisterDTAdapter.notifyDataSetChanged();
    }

    private void events() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        iv_delete_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogAlert = new Dialog(OrderRegisterDTActivity.this);
                dialogAlert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogAlert.setContentView(R.layout.dialog_alert_message);

                dialogAlert.setCancelable(false);
                dialogAlert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                TextView textMsg = dialogAlert.findViewById(R.id.textMsg);
                Button btnYes = dialogAlert.findViewById(R.id.btnYes);
                Button btnNo = dialogAlert.findViewById(R.id.btnNo);
                textMsg.setText(getResources().getString(R.string.delete_msg));

                dialogAlert.show();
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogAlert.dismiss();
                        appDatabase.getProductHDDAO().deleteSingleProduct(orderno);
                        appDatabase.getProductDTDAO().deleteSingleProduct(orderno);
                        Toast.makeText(OrderRegisterDTActivity.this, "Delete Order Successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogAlert.dismiss();
                    }
                });

            }
        });
        iv_upload_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderEntryProductHDArrayList.size() > 0 && orderEntryProductHDArrayList.size() > 0) {
                    UploadDB uploadDB = new UploadDB();
                    uploadDB.execute("");
                }
            }
        });
    }

    public class UploadDB extends AsyncTask<String, String, String> {
        String msg = "";
        Boolean isSuccess = false;


        @Override
        protected void onPreExecute() {
            showProgressDialog.showDialog();
        }

        @Override
        protected void onPostExecute(String msg) {
            showProgressDialog.dismissDialog();
            if (isSuccess) {
                Toast.makeText(OrderRegisterDTActivity.this, msg, Toast.LENGTH_SHORT).show();
                appDatabase.getProductHDDAO().deleteSingleProduct(orderno);
                appDatabase.getProductDTDAO().deleteSingleProduct(orderno);
                orderEntryProductDTArrayList.clear();
                orderEntryProductHDArrayList.clear();
                onBackPressed();
            }
            Toast.makeText(OrderRegisterDTActivity.this, msg, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionCommon.checkUserConnection(databaseName);
                if (con == null) {
                    msg = "Error in connection with SQL server";
                } else {
                    int maxID = findMaxID(con);
                    boolean insertHD = callInsertHD(con,maxID);
                    if (insertHD) {
                        boolean insertDT = callInsertDT(con,maxID);
                        if (insertDT) {
                            msg = "Order Place Sucessfully";
                            isSuccess = true;
                        } else {
                            msg = "Order Not Place";
                            isSuccess = false;
                        }
                    } else {
                        msg = "Order Not Place";
                        isSuccess = false;
                    }

                }
            } catch (Exception ex) {
                msg = "Order Not Place";
                isSuccess = false;
            }

            return msg;
        }

    }

    private boolean callInsertDT(Connection con, int maxID) throws SQLException {
        boolean isInsert = false, insertDT = false;
        if (orderEntryProductHDArrayList.size() > 0) {
            for (int i = 0; i < orderEntryProductDTArrayList.size(); i++) {
                String entryid = String.valueOf(orderEntryProductDTArrayList.get(i).getMaxId());
                String prod_id = String.valueOf(orderEntryProductDTArrayList.get(i).getProduct_id());
                String prod_name = orderEntryProductDTArrayList.get(i).getProduct_name().trim();
                String box = orderEntryProductDTArrayList.get(i).getBox_pkg().trim();
                String pkg = orderEntryProductDTArrayList.get(i).getPkg().trim();
                String qty = orderEntryProductDTArrayList.get(i).getQty().trim();
                String rate = orderEntryProductDTArrayList.get(i).getRate().trim();
                String gross = orderEntryProductDTArrayList.get(i).getSub_total().trim();
                String dis_per = orderEntryProductDTArrayList.get(i).getDist_per1().trim();
                String dis_amt = orderEntryProductDTArrayList.get(i).getDist_amt1().trim();
                String dis_per2 = orderEntryProductDTArrayList.get(i).getDist_per1().trim();
                String dis_amt2 = orderEntryProductDTArrayList.get(i).getDist_amt1().trim();
                String gst_per = orderEntryProductDTArrayList.get(i).getGst_per().trim();
                String gst_amt = orderEntryProductDTArrayList.get(i).getGst_amt().trim();
                String final_amt = orderEntryProductDTArrayList.get(i).getFinal_total().trim();
                String HSN_CODE = orderEntryProductDTArrayList.get(i).getHsn_code().trim();
                String inclu_exlu = orderEntryProductDTArrayList.get(i).getInclu_exclu().trim();
                String net_total = orderEntryProductDTArrayList.get(i).getNet_total().trim();

                if (box.equalsIgnoreCase("")) {
                    box = "0";
                }
                if (pkg.equalsIgnoreCase("")) {
                    pkg = "0";
                }
                if (dis_per.equalsIgnoreCase("")) {
                    dis_amt = "0";
                    dis_per = "0";
                }
                if ((gst_per.equalsIgnoreCase(""))) {
                    gst_per = "0";
                    gst_amt = "0";
                }

                String query = "INSERT INTO MOB_ORD_DET (ENTRYID,PROD_ID,PROD_NAME,BOX,PKG,QTY,RATE,GROSS,DIS_PER,DIS_AMT,DIS_PER2,DIS_AMT2,GST_PER,GST_AMT,FINAL_AMT,HSN_CODE,INCLU_EXCLU) " +
                        "values(" + maxID + "," + prod_id + ",'" + prod_name + "'," + box + "," + pkg + "," + qty + "," + rate + "," + gross + "," + dis_per + "," +
                        dis_amt + "," + dis_per2 + "," + dis_amt2 + "," + gst_per + "," + gst_amt + "," + final_amt + ",'" + HSN_CODE + "','" + inclu_exlu + "')";
                Statement stmt = con.createStatement();
                int rs = stmt.executeUpdate(query);
                if (rs == 1) {
                    insertDT = true;
                } else {
                    insertDT = false;
                }
            }
            if (insertDT) {
                return isInsert = true;
            } else {
                return isInsert = false;
            }
        } else {
            return isInsert;
        }
    }

    private boolean callInsertHD(Connection con, int maxID) throws SQLException {
        boolean isInsert = false;
        if (orderEntryProductHDArrayList.size() > 0) {
            String group_name = orderEntryProductHDArrayList.get(0).getParty_name().trim();
            String total_qty = orderEntryProductHDArrayList.get(0).getTotal_qty().trim();
            String date = orderEntryProductHDArrayList.get(0).getDate().trim();
            String date_y_m_d = orderEntryProductHDArrayList.get(0).getDate_y_m_d().trim();
            String total_amt = orderEntryProductHDArrayList.get(0).getTotal_amt().trim();
            String entryid = String.valueOf(orderEntryProductHDArrayList.get(0).getMaxID());
            String balance = orderEntryProductHDArrayList.get(0).getBalance().trim();
            if (balance.equalsIgnoreCase("")) {
                balance = "0";
            }
            String group_id = orderEntryProductHDArrayList.get(0).getParty_id();
            String order_no = orderEntryProductHDArrayList.get(0).getOrder_no();

            String query = "INSERT INTO MOB_ORD_HEAD (ENTRYID,DATE,DATE_Y_M_D,ORDER_NO,GROUPID,GROUPNAME,BALANCE,TOTAL_QTY,TOTAL_AMT,SALESMAN_ID,SALESMAN_NAME,INCLU_EXCLU) " +
                    "values(" + maxID + ",'" + date + "','" + date_y_m_d + "'," + order_no + "," + group_id + ",'" + group_name + "'," + balance + "," + total_qty + "," + total_amt + "," + saleman_id + ",'" + saleman_name + "','"+inclu_exclu+"')";
            Statement stmt = con.createStatement();
            int rs = stmt.executeUpdate(query);
            if (rs == 1) {
                return isInsert = true;
            } else {
                return isInsert = false;
            }
        } else {
            return isInsert;
        }
    }

    private int findMaxID(Connection connection) throws SQLException {

        int maxID = 0;
        String query = "SELECT  MAX(ENTRYID) AS MAXID from MOB_ORD_HEAD";

        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);
        while (resultSet.next()) {
            maxID = resultSet.getInt("MAXID");
        }
        if(maxID==0)
        {
            maxID=1;
        }
        else
        {
            maxID=maxID+1;
        }
        return maxID;
    }
}
