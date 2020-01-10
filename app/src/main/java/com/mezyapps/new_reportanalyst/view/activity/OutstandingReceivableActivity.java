package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.model.OutstandingPayableHDModel;
import com.mezyapps.new_reportanalyst.model.OutstandingReceivableModel;
import com.mezyapps.new_reportanalyst.view.adapter.OutstandingHDPayableAdapter;
import com.mezyapps.new_reportanalyst.view.adapter.OutstandingHDReceivableAdapter;

import java.util.ArrayList;

public class OutstandingReceivableActivity extends AppCompatActivity {

    private ImageView iv_back;
    private RecyclerView recyclerView_os_receivable;
    private ArrayList<OutstandingReceivableModel> outstandingReceivableModelArrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstanding_receivable);

        find_View_IdS();
        events();
    }

    private void find_View_IdS() {
        iv_back=findViewById(R.id.iv_back);
        recyclerView_os_receivable=findViewById(R.id.recyclerView_os_receivable);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(OutstandingReceivableActivity.this);
        recyclerView_os_receivable.setLayoutManager(linearLayoutManager);

        for(int i=0;i<20;i++)
        {
            OutstandingReceivableModel outstandingReceivableModel=new OutstandingReceivableModel();
            outstandingReceivableModel.setAmt("Amt : 100");
            outstandingReceivableModel.setParty_name("Party Name : XYZ");
            outstandingReceivableModelArrayList.add(outstandingReceivableModel);
        }

        OutstandingHDReceivableAdapter outstandingHDReceivableAdapter=new OutstandingHDReceivableAdapter(outstandingReceivableModelArrayList,OutstandingReceivableActivity.this);
        recyclerView_os_receivable.setAdapter(outstandingHDReceivableAdapter);
        outstandingHDReceivableAdapter.notifyDataSetChanged();

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
