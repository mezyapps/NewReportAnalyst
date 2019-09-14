package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.mezyapps.new_reportanalyst.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SalesReportActivity extends AppCompatActivity {

    private ImageView iv_back,iv_custom_calender;
    private TextView textDateStart, textDateEnd;
    private String currentDate;
    private boolean isStartDate;

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

        textDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDate=true;
                customDatePickerDialog();
            }
        });
        textDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDate=false;
                customDatePickerDialog();
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
                            textDateStart.setText(dateString);
                        }
                        else
                        {
                            textDateStart.setText(dateString);
                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }
}
