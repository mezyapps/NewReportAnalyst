package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.db.AppDatabase;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProductHD;
import com.mezyapps.new_reportanalyst.utils.SharedLoginUtils;
import com.mezyapps.new_reportanalyst.view.adapter.OrderEntryProductAdapter;
import com.mezyapps.new_reportanalyst.view.adapter.OrderRegisterHDAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class OrderRegisterActivity extends AppCompatActivity {
    private ImageView iv_back;
    private RecyclerView recycler_view_order_register;
    private AppDatabase appDatabase;
    private ArrayList<OrderEntryProductHD> orderEntryProductHDArrayList=new ArrayList<>();
    private OrderRegisterHDAdapter orderRegisterHDAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_register);

        find_view_IDs();
        events();
    }

    private void find_view_IDs() {
        iv_back=findViewById(R.id.iv_back);
        recycler_view_order_register=findViewById(R.id.recycler_view_order_register);
        appDatabase = Room.databaseBuilder(OrderRegisterActivity.this, AppDatabase.class, "ReportAnalyst")
                .allowMainThreadQueries()
                .build();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(OrderRegisterActivity.this);
        recycler_view_order_register.setLayoutManager(linearLayoutManager);


        orderEntryProductHDArrayList.clear();
        orderEntryProductHDArrayList.addAll(appDatabase.getProductHDDAO().getAllValue());
        orderRegisterHDAdapter=new OrderRegisterHDAdapter(OrderRegisterActivity.this,orderEntryProductHDArrayList);
        recycler_view_order_register.setAdapter(orderRegisterHDAdapter);
        orderRegisterHDAdapter.notifyDataSetChanged();
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
