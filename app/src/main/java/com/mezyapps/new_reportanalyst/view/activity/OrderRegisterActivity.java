package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.connection.ConnectionCommon;
import com.mezyapps.new_reportanalyst.db.AppDatabase;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProductDT;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProductHD;
import com.mezyapps.new_reportanalyst.model.SalesReportModel;
import com.mezyapps.new_reportanalyst.model.UserProfileModel;
import com.mezyapps.new_reportanalyst.utils.SharedLoginUtils;
import com.mezyapps.new_reportanalyst.utils.ShowProgressDialog;
import com.mezyapps.new_reportanalyst.view.adapter.OrderEntryProductAdapter;
import com.mezyapps.new_reportanalyst.view.adapter.OrderRegisterHDAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

public class OrderRegisterActivity extends AppCompatActivity {
    private ImageView iv_back, iv_no_data_found, iv_upload_db;
    private RecyclerView recycler_view_order_register;
    private AppDatabase appDatabase;
    private ArrayList<OrderEntryProductHD> orderEntryProductHDArrayList = new ArrayList<>();
    private OrderRegisterHDAdapter orderRegisterHDAdapter;
    private ShowProgressDialog showProgressDialog;
    private ConnectionCommon connectionCommon;
    private String databaseName, saleman_id, saleman_name, inclu_exclu;
    private ArrayList<UserProfileModel> userProfileModelArrayList = new ArrayList<>();
    private ArrayList<OrderEntryProductDT> orderEntryProductDTArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_register);

        find_view_IDs();
        events();
    }

    private void find_view_IDs() {
        showProgressDialog = new ShowProgressDialog(OrderRegisterActivity.this);
        connectionCommon = new ConnectionCommon(OrderRegisterActivity.this);
        iv_back = findViewById(R.id.iv_back);
        iv_no_data_found = findViewById(R.id.iv_no_data_found);
        iv_upload_db = findViewById(R.id.iv_upload_db);
        recycler_view_order_register = findViewById(R.id.recycler_view_order_register);
        appDatabase = AppDatabase.getInStatce(OrderRegisterActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderRegisterActivity.this);
        recycler_view_order_register.setLayoutManager(linearLayoutManager);

        userProfileModelArrayList = SharedLoginUtils.getUserProfile(OrderRegisterActivity.this);
        databaseName = userProfileModelArrayList.get(0).getDb_name();
        saleman_id = userProfileModelArrayList.get(0).getSALESMAN_ID();
        saleman_name = userProfileModelArrayList.get(0).getSALESMAN_NAME();
        inclu_exclu = userProfileModelArrayList.get(0).getINCLU_EXCLU();
    }

    private void events() {
        callOrderEntry();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        iv_upload_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderEntryProductHDArrayList.size() > 0) {
                    UploadDB uploadDB = new UploadDB();
                    uploadDB.execute("");
                } else {
                    Toast.makeText(OrderRegisterActivity.this, "No Order Added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void callOrderEntry() {
        orderEntryProductHDArrayList.clear();
        orderEntryProductHDArrayList.addAll(appDatabase.getProductHDDAO().getAllValue());
        if (orderEntryProductHDArrayList.size() > 0) {
            recycler_view_order_register.setVisibility(View.VISIBLE);
            orderRegisterHDAdapter = new OrderRegisterHDAdapter(OrderRegisterActivity.this, orderEntryProductHDArrayList);
            recycler_view_order_register.setAdapter(orderRegisterHDAdapter);
            orderRegisterHDAdapter.notifyDataSetChanged();
        } else {
            iv_no_data_found.setVisibility(View.VISIBLE);
            recycler_view_order_register.setVisibility(View.GONE);
        }
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
                Toast.makeText(OrderRegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                appDatabase.getProductHDDAO().deleteAllProduct();
                appDatabase.getProductDTDAO().deleteAllProduct();
                orderEntryProductDTArrayList.clear();
                orderEntryProductHDArrayList.clear();
                orderRegisterHDAdapter.notifyDataSetChanged();
                callOrderEntry();
            } else {
                Toast.makeText(OrderRegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionCommon.checkUserConnection(databaseName);
                if (con == null) {
                    msg = "Error in connection with SQL server";
                } else {
                    int maxID = findMaxID(con);
                    boolean insertHD = callInsertHD(con, maxID);
                    if (insertHD) {
                        msg = "Order Place Successfully";
                        isSuccess = true;
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

    private int findMaxID(Connection connection) throws SQLException {

        int maxID = 0;
        String query = "SELECT  MAX(ENTRYID) AS MAXID from MOB_ORD_HEAD";

        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);
        while (resultSet.next()) {
            maxID = resultSet.getInt("MAXID");
        }
        return maxID;
    }

    private boolean callInsertDT(Connection con, String orderno, int maxID) throws SQLException {
        boolean isInsert = false, insertDT = false;
        orderEntryProductDTArrayList.clear();
        orderEntryProductDTArrayList.addAll(appDatabase.getProductDTDAO().getOnlyIDValue(Long.parseLong(orderno)));
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
                String dis_per2 = orderEntryProductDTArrayList.get(i).getDist_per2().trim();
                String dis_amt2 = orderEntryProductDTArrayList.get(i).getDist_amt2().trim();
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

                String query = "INSERT INTO MOB_ORD_DET (ENTRYID,PROD_ID,PROD_NAME,BOX,PKG,QTY,RATE,GROSS,DIS_PER,DIS_AMT,DIS_PER2,DIS_AMT2,GST_PER,GST_AMT,FINAL_AMT,HSN_CODE,INCLU_EXCLU,NET_AMT) " +
                        "values(" + maxID + "," + prod_id + ",'" + prod_name + "'," + box + "," + pkg + "," + qty + "," + rate + "," + gross + "," + dis_per + "," +
                        dis_amt + "," + dis_per2 + "," + dis_amt2 + "," + gst_per + "," + gst_amt + "," + final_amt + ",'" + HSN_CODE + "','" + inclu_exlu + "'," + net_total + ")";
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
            for (int i = 0; i < orderEntryProductHDArrayList.size(); i++) {
                maxID = maxID + 1;
                String group_name = orderEntryProductHDArrayList.get(i).getParty_name().trim();
                String total_qty = orderEntryProductHDArrayList.get(i).getTotal_qty().trim();
                String date = orderEntryProductHDArrayList.get(i).getDate().trim();
                String date_y_m_d = orderEntryProductHDArrayList.get(i).getDate_y_m_d().trim();
                String total_amt = orderEntryProductHDArrayList.get(i).getTotal_amt().trim();
                String entryid = String.valueOf(orderEntryProductHDArrayList.get(i).getMaxID());
                String balance = orderEntryProductHDArrayList.get(i).getBalance().trim();
                if (balance.equalsIgnoreCase("")) {
                    balance = "0";
                }
                String group_id = orderEntryProductHDArrayList.get(i).getParty_id();
                String order_no = orderEntryProductHDArrayList.get(i).getOrder_no();

                String query = "INSERT INTO MOB_ORD_HEAD (ENTRYID,DATE,DATE_Y_M_D,ORDER_NO,GROUPID,GROUPNAME,BALANCE,TOTAL_QTY,TOTAL_AMT,SALESMAN_ID,SALESMAN_NAME,INCLU_EXCLU) " +
                        "values(" + maxID + ",'" + date + "','" + date_y_m_d + "'," + order_no + "," + group_id + ",'" + group_name + "'," + balance + "," + total_qty + "," + total_amt + "," + saleman_id + ",'" + saleman_name + "','" + inclu_exclu + "')";
                Statement stmt = con.createStatement();
                int rs = stmt.executeUpdate(query);
                if (rs == 1) {
                    boolean insertDT = callInsertDT(con, entryid, maxID);
                    if (insertDT) {
                        isInsert = true;
                    } else {
                        isInsert = false;
                    }
                } else {
                    isInsert = false;
                }
            }
        } else {
            return isInsert;
        }
        return isInsert;
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        callOrderEntry();
    }
}
