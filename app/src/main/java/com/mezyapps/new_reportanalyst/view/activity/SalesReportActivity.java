package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.connection.ConnectionCommon;
import com.mezyapps.new_reportanalyst.model.SalesReportModel;
import com.mezyapps.new_reportanalyst.model.UserProfileModel;
import com.mezyapps.new_reportanalyst.utils.SharedLoginUtils;
import com.mezyapps.new_reportanalyst.utils.ShowProgressDialog;
import com.mezyapps.new_reportanalyst.view.adapter.SalesReportAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SalesReportActivity extends AppCompatActivity {

    private ImageView iv_back, iv_custom_calender, iv_search, iv_back_search, iv_export_pdf;
    private TextView textDateStart, textDateEnd, textDateStartCustom, textDateEndCustom, text_today_date, textTotalAmt;
    private String currentDate, databaseName;
    private boolean isStartDate;
    private LinearLayout linear_layout_custom_day, linear_layout_today_date;
    private RecyclerView recyclerView_Sales;
    private SalesReportAdapter salesReportAdapter;
    private ArrayList<SalesReportModel> salesReportModelArrayList = new ArrayList<>();
    private ArrayList<UserProfileModel> userProfileModelArrayList = new ArrayList<>();
    private RelativeLayout rr_toolbar, rr_toolbar_search;
    private EditText edit_search;
    private ConnectionCommon connectionCommon;
    private ShowProgressDialog showProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);

        find_View_IdS();
        events();
    }

    private void find_View_IdS() {
        connectionCommon = new ConnectionCommon();
        showProgressDialog = new ShowProgressDialog(SalesReportActivity.this);
        iv_back = findViewById(R.id.iv_back);
        textDateStart = findViewById(R.id.textDateStart);
        textDateEnd = findViewById(R.id.textDateEnd);
        iv_search = findViewById(R.id.iv_search);
        iv_custom_calender = findViewById(R.id.iv_custom_calender);
        linear_layout_today_date = findViewById(R.id.linear_layout_today_date);
        linear_layout_custom_day = findViewById(R.id.linear_layout_custom_day);
        text_today_date = findViewById(R.id.text_today_date);
        recyclerView_Sales = findViewById(R.id.recyclerView_Sales);
        rr_toolbar = findViewById(R.id.rr_toolbar);
        rr_toolbar_search = findViewById(R.id.rr_toolbar_search);
        iv_back_search = findViewById(R.id.iv_back_search);
        iv_export_pdf = findViewById(R.id.iv_export_pdf);
        textTotalAmt = findViewById(R.id.textTotalAmt);
        edit_search = findViewById(R.id.edit_search);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SalesReportActivity.this);
        recyclerView_Sales.setLayoutManager(linearLayoutManager);

        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        textDateEnd.setText(currentDate);
        textDateStart.setText(currentDate);

        userProfileModelArrayList=SharedLoginUtils.getUserProfile(SalesReportActivity.this);
        databaseName = userProfileModelArrayList.get(0).getDb_name();

        SalesReportAll salesReportAll = new SalesReportAll();
        salesReportAll.execute("");

    }

    private void events() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        iv_custom_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDateDialog();
            }
        });

        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rr_toolbar.setVisibility(View.GONE);
                rr_toolbar_search.setVisibility(View.VISIBLE);
            }
        });

        iv_back_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rr_toolbar_search.setVisibility(View.GONE);
                rr_toolbar.setVisibility(View.VISIBLE);
            }
        });

        iv_export_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SalesReportActivity.this, "Working In Progress", Toast.LENGTH_SHORT).show();
            }
        });

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                salesReportAdapter.getFilter().filter(edit_search.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class SalesReportAll extends AsyncTask<String, String, String> {

        String msg = "";
        boolean isSuccess = false;
        String TOTAL_AMT = "00", TOTAL_QTY = "00";

        @Override
        protected void onPreExecute() {
            showProgressDialog.showDialog();
        }

        @Override
        protected void onPostExecute(String message) {
            showProgressDialog.dismissDialog();
            if (message.equalsIgnoreCase("success")) {
                textTotalAmt.setText(TOTAL_AMT);
                salesReportAdapter = new SalesReportAdapter(SalesReportActivity.this, salesReportModelArrayList);
                recyclerView_Sales.setAdapter(salesReportAdapter);
                salesReportAdapter.notifyDataSetChanged();
            }
            else
            {
                salesReportAdapter.notifyDataSetChanged();
                textTotalAmt.setText("00");
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
                            "SELECT  *," +
                                    "(select sum(TOTALBILLAMT) from TBL_SALE_HD) as[TOTAL_AMT]," +
                                    "(select sum(TOTALQTY) from TBL_SALE_HD) as[TOTAL_QTY]" +
                                    " FROM TBL_SALE_HD "+
                                    " ORDER BY VCHDT_Y_M_D DESC,PREFIXID DESC,PREFIXNO DESC";

                    Statement stmt = connection.createStatement();
                    ResultSet resultSet = stmt.executeQuery(query);
                    salesReportModelArrayList.clear();
                    while (resultSet.next()) {
                        String entryid = resultSet.getString("ENTRYID");
                        String group_name = resultSet.getString("GROUPNAME");
                        String group_id = resultSet.getString("VCHNO");
                        String qty = resultSet.getString("TOTALQTY");
                        String finalAmt = resultSet.getString("TOTALBILLAMT");
                        String narration = resultSet.getString("NARRATION");
                        String date = resultSet.getString("VCHDT");
                        TOTAL_AMT = resultSet.getString("TOTAL_AMT");
                        TOTAL_QTY = resultSet.getString("TOTAL_QTY");

                        SalesReportModel salesReportModel = new SalesReportModel();
                        salesReportModel.setGroupname(group_name);
                        salesReportModel.setEntryid(entryid);
                        salesReportModel.setTotalqty("Total qty : " + qty);
                        salesReportModel.setTotalbillamt("Bill Amt : " + finalAmt);
                        salesReportModel.setVchno("Bill No : " + group_id);
                        salesReportModel.setVchdt(date);
                        salesReportModel.setTotal_qty(TOTAL_QTY);
                        salesReportModel.setTotal_amt(TOTAL_AMT);
                        salesReportModel.setNarration(narration);
                        salesReportModelArrayList.add(salesReportModel);
                    }
                    if (salesReportModelArrayList.size() != 0) {
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

    @SuppressLint("StaticFieldLeak")
    public class SingleDateFilter extends AsyncTask<String, String, String> {

        String msg = "";
        boolean isSuccess = false;
        String TOTAL_AMT = "00", TOTAL_QTY = "00";
        String date;
        public SingleDateFilter(String date) {
            this.date=date;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog.showDialog();
        }

        @Override
        protected void onPostExecute(String message) {
            showProgressDialog.dismissDialog();
            if (message.equalsIgnoreCase("success")) {
                textTotalAmt.setText(TOTAL_AMT);
                salesReportAdapter = new SalesReportAdapter(SalesReportActivity.this, salesReportModelArrayList);
                recyclerView_Sales.setAdapter(salesReportAdapter);
                salesReportAdapter.notifyDataSetChanged();
            }
            else
            {
                salesReportAdapter.notifyDataSetChanged();
                textTotalAmt.setText("00");
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
                            "SELECT  *," +
                                    "(select sum(TOTALBILLAMT) from TBL_SALE_HD) as[TOTAL_AMT]," +
                                    "(select sum(TOTALQTY) from TBL_SALE_HD) as[TOTAL_QTY]" +
                                    " FROM TBL_SALE_HD "+
                                    " WHERE VCHDT_Y_M_D='" + date + "'" +
                                    " ORDER BY VCHDT_Y_M_D DESC,PREFIXID DESC,PREFIXNO DESC";


                    Statement stmt = connection.createStatement();
                    ResultSet resultSet = stmt.executeQuery(query);
                    salesReportModelArrayList.clear();
                    while (resultSet.next()) {
                        String entryid = resultSet.getString("ENTRYID");
                        String group_name = resultSet.getString("GROUPNAME");
                        String group_id = resultSet.getString("VCHNO");
                        String qty = resultSet.getString("TOTALQTY");
                        String finalAmt = resultSet.getString("TOTALBILLAMT");
                        String narration = resultSet.getString("NARRATION");
                        String date = resultSet.getString("VCHDT");
                        TOTAL_AMT = resultSet.getString("TOTAL_AMT");
                        TOTAL_QTY = resultSet.getString("TOTAL_QTY");

                        SalesReportModel salesReportModel = new SalesReportModel();
                        salesReportModel.setGroupname(group_name);
                        salesReportModel.setEntryid(entryid);
                        salesReportModel.setTotalqty("Total qty : " + qty);
                        salesReportModel.setTotalbillamt("Bill Amt : " + finalAmt);
                        salesReportModel.setVchno("Bill No : " + group_id);
                        salesReportModel.setVchdt(date);
                        salesReportModel.setTotal_qty(TOTAL_QTY);
                        salesReportModel.setTotal_amt(TOTAL_AMT);
                        salesReportModel.setNarration(narration);
                        salesReportModelArrayList.add(salesReportModel);
                    }
                    if (salesReportModelArrayList.size() != 0) {
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

    @SuppressLint("StaticFieldLeak")
    public class TwoDateFilter extends AsyncTask<String, String, String> {

        String msg = "";
        boolean isSuccess = false;
        String TOTAL_AMT = "00", TOTAL_QTY = "00";
        String date1,date2;

        public TwoDateFilter(String date1,String date2) {
            this.date1=date1;
            this.date2=date2;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog.showDialog();
        }

        @Override
        protected void onPostExecute(String message) {
            showProgressDialog.dismissDialog();
            if (message.equalsIgnoreCase("success")) {
                textTotalAmt.setText(TOTAL_AMT);
                salesReportAdapter = new SalesReportAdapter(SalesReportActivity.this, salesReportModelArrayList);
                recyclerView_Sales.setAdapter(salesReportAdapter);
                salesReportAdapter.notifyDataSetChanged();
            }
            else
            {
                salesReportAdapter.notifyDataSetChanged();
                textTotalAmt.setText("00");
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
                            "SELECT  *," +
                                    "(select sum(TOTALBILLAMT) from TBL_SALE_HD) as[TOTAL_AMT]," +
                                    "(select sum(TOTALQTY) from TBL_SALE_HD) as[TOTAL_QTY]" +
                                    " FROM TBL_SALE_HD "+
                                    " WHERE VCHDT_Y_M_D BETWEEN '" + date1 + "' AND '" + date2 + "'" +
                                    " ORDER BY VCHDT_Y_M_D DESC,PREFIXID DESC,PREFIXNO DESC";


                    Statement stmt = connection.createStatement();
                    ResultSet resultSet = stmt.executeQuery(query);
                    salesReportModelArrayList.clear();
                    while (resultSet.next()) {
                        String entryid = resultSet.getString("ENTRYID");
                        String group_name = resultSet.getString("GROUPNAME");
                        String group_id = resultSet.getString("VCHNO");
                        String qty = resultSet.getString("TOTALQTY");
                        String finalAmt = resultSet.getString("TOTALBILLAMT");
                        String narration = resultSet.getString("NARRATION");
                        String date = resultSet.getString("VCHDT");
                        TOTAL_AMT = resultSet.getString("TOTAL_AMT");
                        TOTAL_QTY = resultSet.getString("TOTAL_QTY");

                        SalesReportModel salesReportModel = new SalesReportModel();
                        salesReportModel.setGroupname(group_name);
                        salesReportModel.setEntryid(entryid);
                        salesReportModel.setTotalqty("Total qty : " + qty);
                        salesReportModel.setTotalbillamt("Bill Amt : " + finalAmt);
                        salesReportModel.setVchno("Bill No : " + group_id);
                        salesReportModel.setVchdt(date);
                        salesReportModel.setTotal_qty(TOTAL_QTY);
                        salesReportModel.setTotal_amt(TOTAL_AMT);
                        salesReportModel.setNarration(narration);
                        salesReportModelArrayList.add(salesReportModel);
                    }
                    if (salesReportModelArrayList.size() != 0) {
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

    //Custom Date Dialog
    private void customDateDialog() {
        final Dialog customDateDialog = new Dialog(SalesReportActivity.this);
        customDateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDateDialog.setContentView(R.layout.custom_date);

        TextView text_today = customDateDialog.findViewById(R.id.text_today);
        TextView text_yesterday = customDateDialog.findViewById(R.id.text_yesterday);
        TextView text_this_week = customDateDialog.findViewById(R.id.text_this_week);
        TextView text_this_month = customDateDialog.findViewById(R.id.text_this_month);
        TextView text_last_month = customDateDialog.findViewById(R.id.text_last_month);
        final TextView text_custom = customDateDialog.findViewById(R.id.text_custom);


        customDateDialog.setCancelable(true);
        customDateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        customDateDialog.show();

        Window window = customDateDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        //Events Custom Date
        text_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_layout_today_date.setVisibility(View.VISIBLE);
                linear_layout_custom_day.setVisibility(View.GONE);
                customDateDialog.dismiss();
                currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                String SendDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
                text_today_date.setText(currentDate);
                SingleDateFilter singleDateFilter = new SingleDateFilter(SendDate);
                singleDateFilter.execute("");
            }
        });
        text_yesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_layout_today_date.setVisibility(View.VISIBLE);
                linear_layout_custom_day.setVisibility(View.GONE);
                customDateDialog.dismiss();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);
                String yesterday = dateFormat.format(cal.getTime());
                text_today_date.setText(yesterday);


                DateFormat sendDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Calendar calSend = Calendar.getInstance();
                calSend.add(Calendar.DATE, -1);
                String yesterdaySend = sendDateFormat.format(calSend.getTime());
                text_today_date.setText(yesterday);


                SingleDateFilter singleDateFilter = new SingleDateFilter(yesterdaySend);
                singleDateFilter.execute("");
                // Toast.makeText(SalesReportActivity.this, yesterday, Toast.LENGTH_SHORT).show();
            }
        });
        text_this_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_layout_today_date.setVisibility(View.GONE);
                linear_layout_custom_day.setVisibility(View.VISIBLE);
                customDateDialog.dismiss();

                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                String startDate = dateFormat.format(cal.getTime());
                cal.add(Calendar.DATE, 6);
                String endDate = dateFormat.format(cal.getTime());

                DateFormat dateFormatSend = new SimpleDateFormat("yyyy/MM/dd");
                Calendar calSend = Calendar.getInstance();
                calSend.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                String startDateSend = dateFormatSend.format(calSend.getTime());
                calSend.add(Calendar.DATE, 6);
                String endDateSend = dateFormatSend.format(calSend.getTime());

                textDateStart.setText(startDate);
                textDateEnd.setText(endDate);
                TwoDateFilter twoDateFilter = new TwoDateFilter(startDateSend,endDateSend);
                twoDateFilter.execute("");

                // Toast.makeText(SalesReportActivity.this, startDate+" "+endDate, Toast.LENGTH_SHORT).show();
            }
        });
        text_this_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_layout_today_date.setVisibility(View.GONE);
                linear_layout_custom_day.setVisibility(View.VISIBLE);
                customDateDialog.dismiss();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, 1);
                String startDate = dateFormat.format(cal.getTime());
                cal.add(Calendar.MONTH, 1);
                cal.add(Calendar.DATE, -1);
                String endDate = dateFormat.format(cal.getTime());

                DateFormat dateFormatSend = new SimpleDateFormat("yyyy/MM/dd");
                Calendar calSend = Calendar.getInstance();
                calSend.set(Calendar.DAY_OF_MONTH, 1);
                String startDateSend = dateFormatSend.format(calSend.getTime());
                calSend.add(Calendar.MONTH, 1);
                calSend.add(Calendar.DATE, -1);
                String endDateSend = dateFormatSend.format(calSend.getTime());

                textDateStart.setText(startDate);
                textDateEnd.setText(endDate);
                TwoDateFilter twoDateFilter = new TwoDateFilter(startDateSend,endDateSend);
                twoDateFilter.execute("");
                //Toast.makeText(SalesReportActivity.this, startDate+" "+endDate, Toast.LENGTH_SHORT).show();
            }
        });
        text_last_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_layout_today_date.setVisibility(View.GONE);
                linear_layout_custom_day.setVisibility(View.VISIBLE);
                customDateDialog.dismiss();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DATE, 1);
                cal.add(Calendar.DAY_OF_MONTH, -1);
                Date lastDateOfPreviousMonth = cal.getTime();
                String endDate = dateFormat.format(lastDateOfPreviousMonth);
                cal.set(Calendar.DATE, 1);
                Date firstDateOfPreviousMonth = cal.getTime();
                String startDate = dateFormat.format(firstDateOfPreviousMonth);


                DateFormat dateFormatSend = new SimpleDateFormat("yyyy/MM/dd");
                Calendar calSend = Calendar.getInstance();
                calSend.set(Calendar.DATE, 1);
                calSend.add(Calendar.DAY_OF_MONTH, -1);
                Date lastDateOfPreviousMonthSend = calSend.getTime();
                String endDateSend = dateFormatSend.format(lastDateOfPreviousMonthSend);
                calSend.set(Calendar.DATE, 1);
                Date firstDateOfPreviousMonthSend = calSend.getTime();
                String startDateSend = dateFormatSend.format(firstDateOfPreviousMonthSend);


                textDateStart.setText(startDate);
                textDateEnd.setText(endDate);
                TwoDateFilter twoDateFilter = new TwoDateFilter(startDateSend,endDateSend);
                twoDateFilter.execute("");
                //Toast.makeText(SalesReportActivity.this, startDate+" "+endDate, Toast.LENGTH_SHORT).show();
            }
        });
        text_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_layout_today_date.setVisibility(View.GONE);
                linear_layout_custom_day.setVisibility(View.VISIBLE);
                customDateDialog.dismiss();
                final Dialog customDateDialogDate = new Dialog(SalesReportActivity.this);
                customDateDialogDate.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customDateDialogDate.setContentView(R.layout.custom_date_calendar);

                textDateStartCustom = customDateDialogDate.findViewById(R.id.textDateStart);
                textDateEndCustom = customDateDialogDate.findViewById(R.id.textDateEnd);
                TextView textAll = customDateDialogDate.findViewById(R.id.textAll);

                currentDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());

                textDateStartCustom.setText(currentDate);
                textDateEndCustom.setText(currentDate);

                customDateDialogDate.setCancelable(false);
                customDateDialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                customDateDialogDate.show();
                Window window = customDateDialogDate.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                textDateStartCustom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isStartDate = true;
                        customDatePickerDialog();
                    }
                });
                textDateEndCustom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isStartDate = false;
                        customDatePickerDialog();
                    }
                });

                textAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDateDialogDate.dismiss();
                        String startDate = textDateStartCustom.getText().toString();
                        String endDate = textDateEndCustom.getText().toString();
                        textDateStart.setText(startDate);
                        textDateEnd.setText(endDate);

                        TwoDateFilter twoDateFilter = new TwoDateFilter(startDate,endDate);
                        twoDateFilter.execute("");
                    }
                });
            }
        });
    }

    private void customDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(SalesReportActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);

                        SimpleDateFormat format = new SimpleDateFormat("yyy/MM/dd");
                        String dateString = format.format(calendar.getTime());

                        if (isStartDate) {
                            textDateStartCustom.setText(dateString);
                        } else {
                            textDateEndCustom.setText(dateString);
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }
}
