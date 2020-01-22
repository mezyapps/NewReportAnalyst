package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.db.AppDatabase;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProduct;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProductDT;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProductHD;
import com.mezyapps.new_reportanalyst.view.adapter.OrderEntryProductAdapter;
import com.mezyapps.new_reportanalyst.view.adapter.OrderRegisterDTAdapter;
import com.mezyapps.new_reportanalyst.view.adapter.OrderRegisterHDAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class OrderRegisterDTActivity extends AppCompatActivity {
    private ImageView iv_back;
    private AppDatabase appDatabase;
    private ArrayList<OrderEntryProductHD> orderEntryProductHDArrayList = new ArrayList<>();
    private ArrayList<OrderEntryProductDT> orderEntryProductDTArrayList = new ArrayList<>();
    private long orderno;
    private TextView textParty_name, text_total_qty, textBillAmt, textBillNo, textBillDate;
    private RecyclerView recycler_view_order_registerDT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_register_dt);


        find_view_IDs();
        events();

    }

    private void find_view_IDs() {
        iv_back = findViewById(R.id.iv_back);
        textParty_name = findViewById(R.id.textParty_name);
        text_total_qty = findViewById(R.id.text_total_qty);
        textBillAmt = findViewById(R.id.textBillAmt);
        textBillNo = findViewById(R.id.textBillNo);
        textBillDate = findViewById(R.id.textBillDate);
        recycler_view_order_registerDT = findViewById(R.id.recycler_view_order_registerDT);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(OrderRegisterDTActivity.this);
        recycler_view_order_registerDT.setLayoutManager(linearLayoutManager);


        appDatabase = Room.databaseBuilder(OrderRegisterDTActivity.this, AppDatabase.class, "ReportAnalyst")
                .allowMainThreadQueries()
                .build();

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
        OrderRegisterDTAdapter orderRegisterDTAdapter=new OrderRegisterDTAdapter(OrderRegisterDTActivity.this,orderEntryProductDTArrayList);
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
    }
}
