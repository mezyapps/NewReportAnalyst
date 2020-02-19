package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.migration.Migration;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.connection.ConnectionCommon;
import com.mezyapps.new_reportanalyst.db.AppDatabase;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProduct;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProductDT;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProductHD;
import com.mezyapps.new_reportanalyst.model.GroupPerModel;
import com.mezyapps.new_reportanalyst.model.ProductTableModel;
import com.mezyapps.new_reportanalyst.model.UserProfileModel;
import com.mezyapps.new_reportanalyst.utils.SelectProductDataInterface;
import com.mezyapps.new_reportanalyst.utils.SharedLoginUtils;
import com.mezyapps.new_reportanalyst.utils.ShowProgressDialog;
import com.mezyapps.new_reportanalyst.view.adapter.GroupPerAdapter;
import com.mezyapps.new_reportanalyst.view.adapter.OrderEntryProductAdapter;
import com.mezyapps.new_reportanalyst.view.adapter.ProductAutoCompleteAdapter;
import com.mezyapps.new_reportanalyst.view.fragment.HomeFragment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class OrderEnteryActivity extends AppCompatActivity implements SelectProductDataInterface {

    private ImageView iv_back, iv_import_table;
    private TextView textDate, textTotalQty, textTotalAmt, textBalance,textArea;
    private String currentDate,currentDate_Y_M_D, group_id = "", group_name, balance, total_qty, total_amt, date, order_no;
    private FloatingActionButton fab_add_product,fab_add_customer;
    private ArrayList<OrderEntryProduct> orderEntryProductArrayList = new ArrayList<>();
    private OrderEntryProductAdapter orderEntryProductAdapter;
    private RecyclerView recycler_view_product;
    private ShowProgressDialog showProgressDialog;
    private ConnectionCommon connectionCommon;
    private String databaseName, select_group_name = "";
    private ArrayList<UserProfileModel> userProfileModelArrayList = new ArrayList<>();
    private AutoCompleteTextView actv_customer_name;
    private ArrayList<GroupPerModel> groupPerModelArrayList = new ArrayList<>();
    private GroupPerAdapter groupPerAdapter;
    private Button btn_place_order;
    private EditText edtOrderNo;
    private long maxval, max;
    private AppDatabase appDatabase;
    private String saleman_id,saleman_name,disc1,disc2,sales_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_entery);

        find_View_IdS();
        events();
    }

    @SuppressLint("RestrictedApi")
    private void find_View_IdS() {
        appDatabase = AppDatabase.getInStatce(OrderEnteryActivity.this);
        showProgressDialog = new ShowProgressDialog(OrderEnteryActivity.this);
        connectionCommon = new ConnectionCommon();
        iv_back = findViewById(R.id.iv_back);
        textDate = findViewById(R.id.textDate);
        fab_add_product = findViewById(R.id.fab_add_product);
        fab_add_customer = findViewById(R.id.fab_add_customer);
        textTotalQty = findViewById(R.id.textTotalQty);
        textTotalAmt = findViewById(R.id.textTotalAmt);
        recycler_view_product = findViewById(R.id.recycler_view_product);
        actv_customer_name = findViewById(R.id.actv_customer_name);
        btn_place_order = findViewById(R.id.btn_place_order);
        textBalance = findViewById(R.id.textBalance);
        textArea = findViewById(R.id.textArea);
        edtOrderNo = findViewById(R.id.edtOrderNo);
        iv_import_table = findViewById(R.id.iv_import_table);

        currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        currentDate_Y_M_D = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
        textDate.setText(currentDate);

        userProfileModelArrayList = SharedLoginUtils.getUserProfile(OrderEnteryActivity.this);
        databaseName = userProfileModelArrayList.get(0).getDb_name();
        saleman_id = userProfileModelArrayList.get(0).getSALESMAN_ID();
        saleman_name = userProfileModelArrayList.get(0).getSALESMAN_NAME();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderEnteryActivity.this);
        recycler_view_product.setLayoutManager(linearLayoutManager);

        actv_customer_name.setThreshold(0);
        fab_add_product.setVisibility(View.GONE);


    }

    @SuppressLint("ClickableViewAccessibility")
    private void events() {
        findMax();
        setAdapterData();
        callSetGroupPer();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        actv_customer_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                GroupPerModel groupPerModel = (GroupPerModel) adapterView.getItemAtPosition(position);
                select_group_name = groupPerModel.getGROUPNAME();
                group_id = groupPerModel.getGROUPID();
                textBalance.setText(groupPerModel.getBALANCE().trim());
                textArea.setText(groupPerModel.getAREANAME().trim());
                fab_add_product.setVisibility(View.VISIBLE);
                disc1=groupPerModel.getDISC1();
                disc2=groupPerModel.getDISC2();
                sales_name=groupPerModel.getSP_NAME();
            }
        });

        iv_import_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appDatabase.getGroupPerDAO().deleteAllGroupPer();
                appDatabase.getPMSTDAO().deleteAllProdcut();
                GroupPer groupPer = new GroupPer();
                groupPer.execute("");
            }
        });

        actv_customer_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                actv_customer_name.showDropDown();
                return false;
            }
        });
        actv_customer_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cust_name = actv_customer_name.getText().toString().trim();
                if (cust_name.equalsIgnoreCase("")) {
                    fab_add_product.setVisibility(View.GONE);
                } else {
                    fab_add_product.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(OrderEnteryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);

                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                String dateString = format.format(calendar.getTime());
                                textDate.setText(dateString);

                                SimpleDateFormat format_y_m_d = new SimpleDateFormat("yyyy/MM/dd");
                                currentDate_Y_M_D = format_y_m_d.format(calendar.getTime());

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        fab_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderEnteryActivity.this, AddProductActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra("DISC1",disc1)
                        .putExtra("DISC2",disc2)
                        .putExtra("SALES",sales_name));
            }
        });
        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    OrderEntryProductHD orderEntryProductHD = new OrderEntryProductHD();
                    orderEntryProductHD.setParty_id(group_id);
                    orderEntryProductHD.setMaxID(maxval);
                    orderEntryProductHD.setParty_name(group_name);
                    orderEntryProductHD.setOrder_no(order_no);
                    orderEntryProductHD.setDate(date);
                    orderEntryProductHD.setBalance(balance);
                    orderEntryProductHD.setTotal_amt(total_amt);
                    orderEntryProductHD.setTotal_qty(total_qty);
                    orderEntryProductHD.setSalesman_id(saleman_id);
                    orderEntryProductHD.setSalesman_name(saleman_name);
                    orderEntryProductHD.setDate_y_m_d(currentDate_Y_M_D);


                    if (orderEntryProductArrayList.size() > 0) {
                        long idVal = appDatabase.getProductHDDAO().insertProductHD(orderEntryProductHD);
                        boolean insert = false;
                        if (idVal != 0) {
                            for (int i = 0; i < orderEntryProductArrayList.size(); i++) {
                                OrderEntryProductDT orderEntryProductDT = new OrderEntryProductDT();
                                orderEntryProductDT.setProduct_id(orderEntryProductArrayList.get(i).getProduct_id());
                                orderEntryProductDT.setProduct_name(orderEntryProductArrayList.get(i).getProduct_name());
                                orderEntryProductDT.setBox_pkg(orderEntryProductArrayList.get(i).getBox_pkg());
                                orderEntryProductDT.setPkg(orderEntryProductArrayList.get(i).getPkg());
                                orderEntryProductDT.setQty(orderEntryProductArrayList.get(i).getQty());
                                orderEntryProductDT.setRate(orderEntryProductArrayList.get(i).getRate());
                                orderEntryProductDT.setSub_total(orderEntryProductArrayList.get(i).getSub_total());
                                orderEntryProductDT.setDist_per1(orderEntryProductArrayList.get(i).getDist_per1());
                                orderEntryProductDT.setGst_per(orderEntryProductArrayList.get(i).getGst_per());
                                orderEntryProductDT.setDist_amt1(orderEntryProductArrayList.get(i).getDist_amt1());
                                orderEntryProductDT.setGst_amt(orderEntryProductArrayList.get(i).getGst_amt());
                                orderEntryProductDT.setFinal_total(orderEntryProductArrayList.get(i).getFinal_total());
                                orderEntryProductDT.setDist_per2(orderEntryProductArrayList.get(i).getDist_per2());
                                orderEntryProductDT.setDist_amt2(orderEntryProductArrayList.get(i).getDist_amt2());
                                orderEntryProductDT.setHsn_code(orderEntryProductArrayList.get(i).getHsn_no());
                                orderEntryProductDT.setInclu_exclu(orderEntryProductArrayList.get(i).getInclu_exclu());
                                orderEntryProductDT.setNet_total(orderEntryProductArrayList.get(i).getNet_total());

                                orderEntryProductDT.setMaxId(maxval);

                                long inval = appDatabase.getProductDTDAO().insertProductDT(orderEntryProductDT);
                                if (idVal != 0) {
                                    insert = true;
                                }
                            }
                        }
                        if (insert) {
                            textBalance.setText("");
                            actv_customer_name.setText("");
                            textTotalAmt.setText("");
                            textTotalQty.setText("");
                            orderEntryProductArrayList.clear();
                            appDatabase.getProductDAO().deleteAllProduct();
                            orderEntryProductAdapter.notifyDataSetChanged();
                            findMax();
                            Toast.makeText(OrderEnteryActivity.this, "Order Place Sucessfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(OrderEnteryActivity.this, "Order Not Place", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(OrderEnteryActivity.this, "Please Add product", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        fab_add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderEnteryActivity.this,AddCustomerActivity.class));
            }
        });
    }

    private void callSetGroupPer() {
        groupPerModelArrayList.clear();
        groupPerModelArrayList.addAll(appDatabase.getGroupPerDAO().getAllGroupPerTable());
        groupPerAdapter = new GroupPerAdapter(OrderEnteryActivity.this, groupPerModelArrayList);
        actv_customer_name.setAdapter(groupPerAdapter);
    }

    private void findMax() {

        max = appDatabase.getProductHDDAO().getMaxValue();
        maxval = max + 1;

        edtOrderNo.setText(String.valueOf(maxval));
    }

    private boolean validation() {
        group_name = actv_customer_name.getText().toString().trim();
        balance = textBalance.getText().toString().trim();
        total_qty = textTotalQty.getText().toString().trim();
        total_amt = textTotalAmt.getText().toString().trim();
        date = textDate.getText().toString().trim();
        order_no = edtOrderNo.getText().toString().trim();


        if (group_id.equalsIgnoreCase("")) {
            actv_customer_name.setError("Please Select Customer Name");
            actv_customer_name.requestFocus();
            return false;
        } else if (!select_group_name.equals(group_name)) {
            actv_customer_name.setError("Please Select Valid Customer Name");
            actv_customer_name.requestFocus();
            return false;
        } else if (total_amt.equalsIgnoreCase("")) {
            Toast.makeText(OrderEnteryActivity.this, "Please Add Product", Toast.LENGTH_SHORT).show();
            return false;
        } else if (total_qty.equalsIgnoreCase("")) {
            Toast.makeText(OrderEnteryActivity.this, "Please Add Product", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    private void setAdapterData() {
        orderEntryProductArrayList.clear();
        orderEntryProductArrayList.addAll(appDatabase.getProductDAO().getAppProduct());
        Collections.reverse(orderEntryProductArrayList);
        orderEntryProductAdapter = new OrderEntryProductAdapter(OrderEnteryActivity.this, orderEntryProductArrayList, false, this);
        recycler_view_product.setAdapter(orderEntryProductAdapter);
        orderEntryProductAdapter.notifyDataSetChanged();

        if (orderEntryProductArrayList.size() > 0) {
            double total_qty = 0.0, total_amt = 0.0;
            for (int i = 0; i < orderEntryProductArrayList.size(); i++) {
                total_amt = total_amt + Double.parseDouble(orderEntryProductArrayList.get(i).getFinal_total());
                total_qty = total_qty + Double.parseDouble(orderEntryProductArrayList.get(i).getQty());
            }
            textTotalQty.setText(String.format("%.2f", total_qty));
            textTotalAmt.setText(String.format("%.2f", total_amt));
        } else {
            textTotalQty.setText("");
            textTotalAmt.setText("");
        }
    }

    @Override
    public void onBackPressed() {
        if (orderEntryProductArrayList.size() > 0) {
            doubleBackPressLogic();
        } else {
            startActivity(new Intent(OrderEnteryActivity.this, MainActivity.class));
            finish();
        }
    }

    private void doubleBackPressLogic() {
        final Dialog dialogAlert = new Dialog(OrderEnteryActivity.this);
        dialogAlert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAlert.setContentView(R.layout.dialog_alert_message);

        dialogAlert.setCancelable(false);
        dialogAlert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView textMsg = dialogAlert.findViewById(R.id.textMsg);
        Button btnYes = dialogAlert.findViewById(R.id.btnYes);
        Button btnNo = dialogAlert.findViewById(R.id.btnNo);
        textMsg.setText(getResources().getString(R.string.exit_msg));

        dialogAlert.show();
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAlert.dismiss();
                appDatabase.getProductDAO().deleteAllProduct();
                startActivity(new Intent(OrderEnteryActivity.this, MainActivity.class));
                finish();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAlert.dismiss();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setAdapterData();
    }

    @Override
    public void selectProductData(OrderEntryProduct orderEntryProduct) {

    }

    @SuppressLint("StaticFieldLeak")
    public class GroupPer extends AsyncTask<String, String, String> {

        String msg = "";
        boolean isSuccess = false;


        @Override
        protected void onPreExecute() {
            showProgressDialog.showDialog();
        }

        @Override
        protected void onPostExecute(String message) {
            showProgressDialog.dismissDialog();
            if (isSuccess) {
                Toast.makeText(OrderEnteryActivity.this, message, Toast.LENGTH_SHORT).show();
                callSetGroupPer();
            } else {
                Toast.makeText(OrderEnteryActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection connection = connectionCommon.checkUserConnection(databaseName);
                if (connection == null) {
                    msg = "Check Your Internet Access!";
                } else {
                    boolean insertGroupPer = callInsertGroupPerTable(connection);
                    if(insertGroupPer)
                    {
                        boolean insertProduct = callInsertProduct(connection);
                        if(insertGroupPer)
                        {
                            msg="Import Table Successfully";
                            isSuccess=true;
                        }
                        else
                        {
                            msg=" Not Import Table";
                            isSuccess=false;
                        }
                    }
                    else {
                        msg=" Not Import Table";
                        isSuccess=false;
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

    private boolean callInsertProduct(Connection connection) throws SQLException {
        long idVal = 0;

        String query =
                " SELECT * FROM MOB_PMST";

        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);
        while (resultSet.next()) {
            String prod_id = resultSet.getString("PRODID");
            String prod_code = resultSet.getString("PMSTCODE");
            String prod_name = resultSet.getString("PMSTNAME");
            String categoryid = resultSet.getString("CATEGORYID");
            String categoryname = resultSet.getString("CATEGORYNAME");
            String pgmstid = resultSet.getString("PGMSTID");
            String pgmstname = resultSet.getString("PGMSTNAME");
            String unitid = resultSet.getString("UNITID");
            String unitname = resultSet.getString("UNITNAME");
            String prodpkgincase = resultSet.getString("PRODPKGINCASE");
            String salerate1 = resultSet.getString("SALERATE1");
            String salerate2 = resultSet.getString("SALERATE2");
            String salerate3 = resultSet.getString("SALERATE3");
            String salerate4 = resultSet.getString("SALERATE4");
            String mrp = resultSet.getString("MRP");
            String packing = resultSet.getString("PACKING");
            String hsncodeid = resultSet.getString("HSNCODEID");
            String hsncd = resultSet.getString("HSNCD");
            String IGST_PER = resultSet.getString("IGST_PER");
            String cgst_per = resultSet.getString("CGST_PER");
            String sgst_per = resultSet.getString("SGST_PER");

            ProductTableModel productTableModel = new ProductTableModel();
            productTableModel.setPRODID(prod_id);
            productTableModel.setPMSTCODE(prod_code);
            productTableModel.setPMSTNAME(prod_name);
            productTableModel.setCATEGORYID(categoryid);
            productTableModel.setCATEGORYNAME(categoryname);
            productTableModel.setPGMSTID(pgmstid);
            productTableModel.setPGMSTNAME(pgmstname);
            productTableModel.setUNITID(unitid);
            productTableModel.setUNITNAME(unitname);
            productTableModel.setPRODPKGINCASE(prodpkgincase);
            productTableModel.setSALERATE1(salerate1);
            productTableModel.setSALERATE2(salerate2);
            productTableModel.setSALERATE3(salerate3);
            productTableModel.setSALERATE4(salerate4);
            productTableModel.setMRP(mrp);
            productTableModel.setPACKING(packing);
            productTableModel.setHSNCODEID(hsncodeid);
            productTableModel.setHSNCD(hsncd);
            productTableModel.setIGST_PER(IGST_PER);
            productTableModel.setCGST_PER(cgst_per);
            productTableModel.setSGST_PER(sgst_per);


            idVal = appDatabase.getPMSTDAO().addProduct(productTableModel);

        }

        if (idVal != 0) {
            return true;
        } else {
            return false;
        }

    }

    private boolean callInsertGroupPerTable(Connection connection) throws SQLException {
        long idVal = 0;

        String query = " SELECT * FROM MOB_GROUPS_PER";

        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);
        while (resultSet.next()) {
            String group_id = resultSet.getString("GROUPID");
            String balance = resultSet.getString("BALANCE");
            String group_code = resultSet.getString("GROUPCODE");
            String group_name = resultSet.getString("GROUPNAME");
            String add1 = resultSet.getString("ADD1");
            String add2 = resultSet.getString("ADD2");
            String add3 = resultSet.getString("ADD3");
            String add4 = resultSet.getString("ADD4");
            String areaname = resultSet.getString("AREANAME");
            String city_name = resultSet.getString("CITYNAME");
            String stateid = resultSet.getString("STATEID");
            String state_code = resultSet.getString("STATE_CODE");
            String state_name = resultSet.getString("STATE_NAME");
            String gstno = resultSet.getString("GSTNO");
            String telno_o = resultSet.getString("TELNO_O");
            String telno_r = resultSet.getString("TELNO_R");
            String mobileno = resultSet.getString("MOBILENO");
            String salesman_id = resultSet.getString("SALESMAN_ID");
            String salesman_name = resultSet.getString("SALESMAN_NAME");
            String gst_type_id = resultSet.getString("GST_TYPE_ID");
            String gst_type = resultSet.getString("GST_TYPE");
            String disc1 = resultSet.getString("DISC1");
            String disc2 = resultSet.getString("DISC2");
            String sp_name = resultSet.getString("SP_NAME");


            GroupPerModel groupPerModel = new GroupPerModel();
            groupPerModel.setGROUPID(group_id);
            groupPerModel.setBALANCE(balance);
            groupPerModel.setGROUPCODE(group_code);
            groupPerModel.setGROUPNAME(group_name);
            groupPerModel.setADD1(add1);
            groupPerModel.setADD2(add2);
            groupPerModel.setADD3(add3);
            groupPerModel.setADD4(add4);
            groupPerModel.setAREANAME(areaname);
            groupPerModel.setCITYNAME(city_name);
            groupPerModel.setSTATEID(stateid);
            groupPerModel.setSTATE_CODE(state_code);
            groupPerModel.setSTATE_NAME(state_name);
            groupPerModel.setGSTNO(gstno);
            groupPerModel.setTELNO_O(telno_o);
            groupPerModel.setTELNO_R(telno_r);
            groupPerModel.setMOBILENO(mobileno);
            groupPerModel.setSALESMAN_ID(salesman_id);
            groupPerModel.setSALESMAN_NAME(salesman_name);
            groupPerModel.setGST_TYPE_ID(gst_type_id);
            groupPerModel.setGST_TYPE(gst_type);
            groupPerModel.setDISC1(disc1);
            groupPerModel.setDISC2(disc2);
            groupPerModel.setSP_NAME(sp_name);

            idVal = appDatabase.getGroupPerDAO().addProduct(groupPerModel);
        }
        if (idVal != 0) {
            return true;
        } else {
            return false;
        }
    }
}

