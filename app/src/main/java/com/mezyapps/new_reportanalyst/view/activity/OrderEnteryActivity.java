package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.db.AppDatabase;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProduct;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OrderEnteryActivity extends AppCompatActivity {

    private ImageView iv_back;
    private TextView textDate,textTotalQty,textTotalAmt;
    private String currentDate;
    private FloatingActionButton fab_add_product;
    private ArrayList<OrderEntryProduct> orderEntryProductArrayList=new ArrayList<>();
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_entery);

        find_View_IdS();
        events();
    }

    private void find_View_IdS() {
        appDatabase= Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"ReportAnalyst").allowMainThreadQueries().build();
        iv_back = findViewById(R.id.iv_back);
        textDate = findViewById(R.id.textDate);
        fab_add_product = findViewById(R.id.fab_add_product);
        textTotalQty = findViewById(R.id.textTotalQty);
        textTotalAmt = findViewById(R.id.textTotalAmt);
        currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        textDate.setText(currentDate);

        orderEntryProductArrayList.addAll(appDatabase.getProductDAO().getAppProduct());
        if (orderEntryProductArrayList.size()>0) {
            double total_qty = 0.0, total_amt = 0.0;
            for (int i = 0; i < orderEntryProductArrayList.size(); i++) {
                total_amt = total_amt + Double.parseDouble(orderEntryProductArrayList.get(i).getFinal_total());
                total_qty = total_qty + Double.parseDouble(orderEntryProductArrayList.get(i).getQty());
            }
            textTotalQty.setText(String.valueOf(total_qty));
            textTotalAmt.setText(String.valueOf(total_amt));
        }
    }

    private void events() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
                startActivity(new Intent(OrderEnteryActivity.this,AddProductActivity.class));
            }
        });
    }
}

