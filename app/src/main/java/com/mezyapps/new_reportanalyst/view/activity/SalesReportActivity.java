package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mezyapps.new_reportanalyst.R;

public class SalesReportActivity extends AppCompatActivity {

    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);

        find_View_IdS();
        events();
    }

    private void find_View_IdS() {
        iv_back=findViewById(R.id.iv_back);
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
