package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.model.OutstandingPayableHDModel;
import com.mezyapps.new_reportanalyst.view.adapter.OutstandingHDPayableAdapter;

import java.util.ArrayList;

public class OutstandingPayableActivity extends AppCompatActivity {

    private ImageView iv_back;
    private RecyclerView recyclerView_os_payable;
    private ArrayList<OutstandingPayableHDModel> outstandingPayableHDModelArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstanding_payable);
        find_View_IdS();
        events();
    }

    private void find_View_IdS() {
        iv_back = findViewById(R.id.iv_back);
        recyclerView_os_payable = findViewById(R.id.recyclerView_os_payable);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(OutstandingPayableActivity.this);
        recyclerView_os_payable.setLayoutManager(linearLayoutManager);

        for(int i=0;i<20;i++)
        {
            OutstandingPayableHDModel outstandingPayableHDModel=new OutstandingPayableHDModel();
            outstandingPayableHDModel.setAmt("Amt : 100");
            outstandingPayableHDModel.setParty_name("Party Name : XYZ");
            outstandingPayableHDModelArrayList.add(outstandingPayableHDModel);
        }

        OutstandingHDPayableAdapter outstandingHDPayableAdapter=new OutstandingHDPayableAdapter(outstandingPayableHDModelArrayList,OutstandingPayableActivity.this);
        recyclerView_os_payable.setAdapter(outstandingHDPayableAdapter);
        outstandingHDPayableAdapter.notifyDataSetChanged();
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
