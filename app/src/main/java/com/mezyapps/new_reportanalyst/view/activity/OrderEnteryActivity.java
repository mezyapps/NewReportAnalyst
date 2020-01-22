package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.migration.Migration;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
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
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class OrderEnteryActivity extends AppCompatActivity  implements SelectProductDataInterface {

    private ImageView iv_back;
    private TextView textDate, textTotalQty, textTotalAmt, textBalance;
    private String currentDate, group_id, group_name, balance, total_qty, total_amt, date, order_no;
    private FloatingActionButton fab_add_product;
    private ArrayList<OrderEntryProduct> orderEntryProductArrayList = new ArrayList<>();
    private AppDatabase appDatabase;
    private OrderEntryProductAdapter orderEntryProductAdapter;
    private RecyclerView recycler_view_product;
    private boolean doubleBackToExitPressedOnce = false;
    private ShowProgressDialog showProgressDialog;
    private ConnectionCommon connectionCommon;
    private String databaseName;
    private ArrayList<UserProfileModel> userProfileModelArrayList = new ArrayList<>();
    private AutoCompleteTextView actv_customer_name;
    private ArrayList<GroupPerModel> groupPerModelArrayList = new ArrayList<>();
    private GroupPerAdapter groupPerAdapter;
    private Button btn_place_order;
    private EditText edtOrderNo;
    private long maxval,max;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_entery);

        find_View_IdS();
        events();
    }

    @SuppressLint("RestrictedApi")
    private void find_View_IdS() {
        showProgressDialog = new ShowProgressDialog(OrderEnteryActivity.this);
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "ReportAnalyst")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        connectionCommon = new ConnectionCommon();
        iv_back = findViewById(R.id.iv_back);
        textDate = findViewById(R.id.textDate);
        fab_add_product = findViewById(R.id.fab_add_product);
        textTotalQty = findViewById(R.id.textTotalQty);
        textTotalAmt = findViewById(R.id.textTotalAmt);
        recycler_view_product = findViewById(R.id.recycler_view_product);
        actv_customer_name = findViewById(R.id.actv_customer_name);
        btn_place_order = findViewById(R.id.btn_place_order);
        textBalance = findViewById(R.id.textBalance);
        edtOrderNo = findViewById(R.id.edtOrderNo);

        currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        textDate.setText(currentDate);

        userProfileModelArrayList = SharedLoginUtils.getUserProfile(OrderEnteryActivity.this);
        databaseName = userProfileModelArrayList.get(0).getDb_name();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderEnteryActivity.this);
        recycler_view_product.setLayoutManager(linearLayoutManager);

        actv_customer_name.setThreshold(0);
        fab_add_product.setVisibility(View.GONE);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void events() {
        findMax();
        setAdapterData();
        GroupPer groupPer = new GroupPer();
        groupPer.execute("");
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
                String groupname = groupPerModel.getGROUPNAME();
                group_id = groupPerModel.getGROUPID();
                fab_add_product.setVisibility(View.VISIBLE);
            }
        });

        actv_customer_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                actv_customer_name.showDropDown();
                return false;
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
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        fab_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderEnteryActivity.this, AddProductActivity.class));
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


                    if (orderEntryProductArrayList.size()>0) {
                        long idVal = appDatabase.getProductHDDAO().insertProductHD(orderEntryProductHD);
                        boolean insert=false;
                        if (idVal != 0) {
                            for (int i=0;i<orderEntryProductArrayList.size();i++)
                            {
                                OrderEntryProductDT orderEntryProductDT=new OrderEntryProductDT();
                                orderEntryProductDT.setProduct_id(orderEntryProductArrayList.get(i).getProduct_id());
                                orderEntryProductDT.setProduct_name(orderEntryProductArrayList.get(i).getProduct_name());
                                orderEntryProductDT.setBox_pkg(orderEntryProductArrayList.get(i).getBox_pkg());
                                orderEntryProductDT.setPkg(orderEntryProductArrayList.get(i).getPkg());
                                orderEntryProductDT.setQty(orderEntryProductArrayList.get(i).getQty());
                                orderEntryProductDT.setRate(orderEntryProductArrayList.get(i).getRate());
                                orderEntryProductDT.setSub_total(orderEntryProductArrayList.get(i).getSub_total());
                                orderEntryProductDT.setDist_per(orderEntryProductArrayList.get(i).getDist_per());
                                orderEntryProductDT.setGst_per(orderEntryProductArrayList.get(i).getGst_per());
                                orderEntryProductDT.setDist_amt(orderEntryProductArrayList.get(i).getDist_amt());
                                orderEntryProductDT.setGst_amt(orderEntryProductArrayList.get(i).getGst_amt());
                                orderEntryProductDT.setFinal_total(orderEntryProductArrayList.get(i).getFinal_total());
                                orderEntryProductDT.setMaxId(maxval);

                                 long inval=appDatabase.getProductDTDAO().insertProductDT(orderEntryProductDT);
                                if (idVal != 0) {
                                    insert=true;
                                }
                            }
                        }
                        if (insert)
                        {
                            textBalance.setText("");
                            actv_customer_name.setText("");
                            textTotalAmt.setText("");
                            textTotalQty.setText("");
                            orderEntryProductArrayList.clear();
                            appDatabase.getProductDAO().deleteAllProduct();
                            orderEntryProductAdapter.notifyDataSetChanged();
                            findMax();
                            Toast.makeText(OrderEnteryActivity.this, "Order Place Sucessfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(OrderEnteryActivity.this, "Order Not Place", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(OrderEnteryActivity.this, "Please Add product", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void findMax() {

        max=appDatabase.getProductHDDAO().getMaxValue();
        maxval=max+1;

        edtOrderNo.setText(String.valueOf(maxval));
    }

    private boolean validation() {
        group_name = actv_customer_name.getText().toString().trim();
        balance = textBalance.getText().toString().trim();
        total_qty=textTotalQty.getText().toString().trim();
        total_amt=textTotalAmt.getText().toString().trim();
        date=textDate.getText().toString().trim();
        order_no=edtOrderNo.getText().toString().trim();


        if(group_name.equalsIgnoreCase(""))
        {
            actv_customer_name.setError("Please Enter Customer Name");
            actv_customer_name.requestFocus();
            return false;
        } else if(total_amt.equalsIgnoreCase(""))
        {
            Toast.makeText(OrderEnteryActivity.this, "Please Add Product", Toast.LENGTH_SHORT).show();
            return false;
        }else if(total_qty.equalsIgnoreCase(""))
        {
            Toast.makeText(OrderEnteryActivity.this, "Please Add Product", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    private void setAdapterData() {
        orderEntryProductArrayList.clear();
        orderEntryProductArrayList.addAll(appDatabase.getProductDAO().getAppProduct());
        Collections.reverse(orderEntryProductArrayList);
        orderEntryProductAdapter = new OrderEntryProductAdapter(OrderEnteryActivity.this, orderEntryProductArrayList,false,this);
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

    // ============ End Double tab back press logic =================
    private void doubleBackPressLogic() {
        if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Your Order Product List Will Delete", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 1000);
        } else {
            appDatabase.getProductDAO().deleteAllProduct();
            startActivity(new Intent(OrderEnteryActivity.this, MainActivity.class));
            finish();
        }
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
            if (message.equalsIgnoreCase("success")) {
                groupPerAdapter = new GroupPerAdapter(OrderEnteryActivity.this, groupPerModelArrayList);
                actv_customer_name.setAdapter(groupPerAdapter);
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
                    String query = " SELECT GP.GROUPID, GP.GROUPCODE, GP.GROUPNAME, GP.ADD1, GP.ADD2, GP.ADD3, GP.ADD4, GP.GRP_INFO, " +
                            " AR.AREANAME, ST.STATE_NAME, GP.LBTNO AS GSTNO, GP.MOBILENO, GP.E_MAIL, SMN.GROUPCODE AS SALESMAN " +
                            " FROM GROUPS_PER AS GP INNER JOIN AREAMASTER AS AR ON GP.AREAID=AR.AREAID " +
                            " INNER JOIN STATE_MASTER AS ST ON GP.STATEID=ST.STATEID " +
                            " INNER JOIN GROUPS_PER AS SMN ON GP.SALESMAN_ID=SMN.GROUPID " +
                            " WHERE (GP.GRP_INFO ='SUND_DR' OR GP.GRP_INFO ='SUND_CR' OR GP.GRP_INFO ='CASH_AC') ";

                    Statement stmt = connection.createStatement();
                    ResultSet resultSet = stmt.executeQuery(query);
                    groupPerModelArrayList.clear();
                    while (resultSet.next()) {
                        String group_id = resultSet.getString("GROUPID");
                        String group_code = resultSet.getString("GROUPCODE");
                        String group_name = resultSet.getString("GROUPNAME");
                        String add1 = resultSet.getString("ADD1");
                        String add2 = resultSet.getString("ADD2");
                        String add3 = resultSet.getString("ADD3");
                        String add4 = resultSet.getString("ADD4");
                        String grp_info = resultSet.getString("GRP_INFO");
                        String areaname = resultSet.getString("AREANAME");
                        String gstno = resultSet.getString("GSTNO");
                        String mobileno = resultSet.getString("MOBILENO");
                        String e_mail = resultSet.getString("E_MAIL");
                        String salesman = resultSet.getString("SALESMAN");


                        GroupPerModel groupPerModel = new GroupPerModel();
                        groupPerModel.setGROUPID(group_id);
                        groupPerModel.setGROUPCODE(group_code);
                        groupPerModel.setGROUPNAME(group_name);
                        groupPerModel.setADD1(add1);
                        groupPerModel.setADD2(add2);
                        groupPerModel.setADD3(add3);
                        groupPerModel.setADD4(add4);
                        groupPerModel.setGRP_INFO(grp_info);
                        groupPerModel.setAREANAME(areaname);
                        groupPerModel.setGSTNO(gstno);
                        groupPerModel.setMOBILENO(mobileno);
                        groupPerModel.setE_MAIL(e_mail);
                        groupPerModel.setSALESMAN(salesman);

                        groupPerModelArrayList.add(groupPerModel);
                    }
                    if (groupPerModelArrayList.size() != 0) {
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

