package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mezyapps.new_reportanalyst.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SalesReportActivity extends AppCompatActivity {

    private ImageView iv_back,iv_custom_calender;
    private TextView textDateStart, textDateEnd,textDateStartCustom, textDateEndCustom,text_today_date;
    private String currentDate;
    private boolean isStartDate;
    private LinearLayout linear_layout_custom_day,linear_layout_today_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);

        find_View_IdS();
        events();
    }

    private void find_View_IdS() {
        iv_back = findViewById(R.id.iv_back);
        textDateStart = findViewById(R.id.textDateStart);
        textDateEnd = findViewById(R.id.textDateEnd);
        iv_custom_calender = findViewById(R.id.iv_custom_calender);
        linear_layout_today_date = findViewById(R.id.linear_layout_today_date);
        linear_layout_custom_day = findViewById(R.id.linear_layout_custom_day);
        text_today_date = findViewById(R.id.text_today_date);

        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        textDateEnd.setText(currentDate);
        textDateStart.setText(currentDate);

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
    }

    //Custom Date Dialog
    private void customDateDialog() {
        final Dialog customDateDialog= new Dialog(SalesReportActivity.this);
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
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );


        //Events Custom Date
        text_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_layout_today_date.setVisibility(View.VISIBLE);
                linear_layout_custom_day.setVisibility(View.GONE);
                customDateDialog.dismiss();
                currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                text_today_date.setText(currentDate);
                Toast.makeText(SalesReportActivity.this, currentDate, Toast.LENGTH_SHORT).show();
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
                String yesterday=dateFormat.format(cal.getTime());

                text_today_date.setText(yesterday);
                Toast.makeText(SalesReportActivity.this, yesterday, Toast.LENGTH_SHORT).show();
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
                String startDate=dateFormat.format(cal.getTime());
                cal.add(Calendar.DATE, 6);
                String endDate = dateFormat.format(cal.getTime());

                textDateStart.setText(startDate);
                textDateEnd.setText(endDate);

                Toast.makeText(SalesReportActivity.this, startDate+" "+endDate, Toast.LENGTH_SHORT).show();
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
                String startDate=dateFormat.format(cal.getTime());
                cal.add(Calendar.MONTH, 1);
                cal.add(Calendar.DATE, -1);
                String endDate = dateFormat.format(cal.getTime());

                textDateStart.setText(startDate);
                textDateEnd.setText(endDate);

                Toast.makeText(SalesReportActivity.this, startDate+" "+endDate, Toast.LENGTH_SHORT).show();
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
                String endDate=dateFormat.format(lastDateOfPreviousMonth);
                cal.set(Calendar.DATE, 1);
                Date firstDateOfPreviousMonth = cal.getTime();
                String startDate=dateFormat.format(firstDateOfPreviousMonth);

                textDateStart.setText(startDate);
                textDateEnd.setText(endDate);

                Toast.makeText(SalesReportActivity.this, startDate+" "+endDate, Toast.LENGTH_SHORT).show();
            }
        });
        text_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_layout_today_date.setVisibility(View.GONE);
                linear_layout_custom_day.setVisibility(View.VISIBLE);
                customDateDialog.dismiss();
                final Dialog customDateDialogDate= new Dialog(SalesReportActivity.this);
                customDateDialogDate.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customDateDialogDate.setContentView(R.layout.custom_date_calendar);

                textDateStartCustom = customDateDialogDate.findViewById(R.id.textDateStart);
                textDateEndCustom = customDateDialogDate.findViewById(R.id.textDateEnd);
                TextView textAll = customDateDialogDate.findViewById(R.id.textAll);

                currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                textDateStartCustom.setText(currentDate);
                textDateEndCustom.setText(currentDate);

                customDateDialogDate.setCancelable(false);
                customDateDialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                customDateDialogDate.show();
                Window window = customDateDialogDate.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );

                textDateStartCustom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isStartDate=true;
                        customDatePickerDialog();
                    }
                });
                textDateEndCustom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isStartDate=false;
                        customDatePickerDialog();
                    }
                });

                textAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDateDialogDate.dismiss();
                        String startDate=textDateStartCustom.getText().toString();
                        String endDate=textDateEndCustom.getText().toString();
                        textDateStart.setText(startDate);
                        textDateEnd.setText(endDate);
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

                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        String dateString = format.format(calendar.getTime());

                        if(isStartDate) {
                            textDateStartCustom.setText(dateString);
                        }
                        else {
                            textDateEndCustom.setText(dateString);
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }
}
