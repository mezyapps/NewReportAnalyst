package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.db.AppDatabase;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProduct;
import com.mezyapps.new_reportanalyst.view.adapter.OrderEntryProductAdapter;
import com.mezyapps.new_reportanalyst.view.fragment.HomeFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class OrderEnteryActivity extends AppCompatActivity {

    private ImageView iv_back;
    private TextView textDate, textTotalQty, textTotalAmt;
    private String currentDate;
    private FloatingActionButton fab_add_product;
    private ArrayList<OrderEntryProduct> orderEntryProductArrayList = new ArrayList<>();
    private AppDatabase appDatabase;
    private OrderEntryProductAdapter orderEntryProductAdapter;
    private RecyclerView recycler_view_product;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_entery);

        find_View_IdS();
        events();
    }

    private void find_View_IdS() {
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "ReportAnalyst").allowMainThreadQueries().build();
        iv_back = findViewById(R.id.iv_back);
        textDate = findViewById(R.id.textDate);
        fab_add_product = findViewById(R.id.fab_add_product);
        textTotalQty = findViewById(R.id.textTotalQty);
        textTotalAmt = findViewById(R.id.textTotalAmt);
        recycler_view_product = findViewById(R.id.recycler_view_product);
        currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        textDate.setText(currentDate);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderEnteryActivity.this);
        recycler_view_product.setLayoutManager(linearLayoutManager);

    }

    private void events() {
        setAdapterData();
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
                startActivity(new Intent(OrderEnteryActivity.this, AddProductActivity.class));
            }
        });
    }

    private void setAdapterData() {
        orderEntryProductArrayList.clear();
        orderEntryProductArrayList.addAll(appDatabase.getProductDAO().getAppProduct());
        orderEntryProductAdapter = new OrderEntryProductAdapter(OrderEnteryActivity.this, orderEntryProductArrayList);
        Collections.reverse(orderEntryProductArrayList);
        recycler_view_product.setAdapter(orderEntryProductAdapter);
        orderEntryProductAdapter.notifyDataSetChanged();

        if (orderEntryProductArrayList.size() > 0) {
            double total_qty = 0.0, total_amt = 0.0;
            for (int i = 0; i < orderEntryProductArrayList.size(); i++) {
                total_amt = total_amt + Double.parseDouble(orderEntryProductArrayList.get(i).getFinal_total());
                total_qty = total_qty + Double.parseDouble(orderEntryProductArrayList.get(i).getQty());
            }
            textTotalQty.setText(String.valueOf(total_qty));
            textTotalAmt.setText(String.valueOf(total_amt));
        }
    }


    @Override
    public void onBackPressed() {
        if(orderEntryProductArrayList.size()>0) {
            doubleBackPressLogic();
        }
        else {
            startActivity(new Intent(OrderEnteryActivity.this,MainActivity.class));
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
            startActivity(new Intent(OrderEnteryActivity.this,MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setAdapterData();
    }
}

