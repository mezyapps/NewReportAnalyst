package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.database.DatabaseConstant;
import com.mezyapps.new_reportanalyst.database.DatabaseHandler;
import com.mezyapps.new_reportanalyst.model.SalesDetailsModel;
import com.mezyapps.new_reportanalyst.model.SalesReportModel;
import com.mezyapps.new_reportanalyst.view.adapter.SalesDetailsAdapter;
import com.mezyapps.new_reportanalyst.view.adapter.SalesReportAdapter;

import java.util.ArrayList;

public class SaleDetailsActivity extends AppCompatActivity {

    private ImageView iv_back;
    private SalesReportModel salesReportModel;
    private TextView textBillNO,textDate,textPartyName,text_total_qty,textBillAMT;
    private String entry_id;
    private RecyclerView recyclerView_product_list;
    private DatabaseHandler databaseHandler;
    private ArrayList<SalesDetailsModel> salesDetailsModelArrayList=new ArrayList<>();
    private SalesDetailsAdapter salesDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_deatils);

        find_View_IDs();
        events();
    }

    private void find_View_IDs() {
        databaseHandler = new DatabaseHandler(SaleDetailsActivity.this);
        iv_back=findViewById(R.id.iv_back);
        textBillNO=findViewById(R.id.textBillNO);
        textDate=findViewById(R.id.textDate);
        textPartyName=findViewById(R.id.textPartyName);
        text_total_qty=findViewById(R.id.text_total_qty);
        textBillAMT=findViewById(R.id.textBillAMT);
        recyclerView_product_list=findViewById(R.id.recyclerView_product_list);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(SaleDetailsActivity.this);
        recyclerView_product_list.setLayoutManager(linearLayoutManager);

        Bundle bundle = getIntent().getExtras();
        salesReportModel = bundle.getParcelable("SALES_REPORT");

        entry_id=salesReportModel.getEntryid();
        String bill_no=salesReportModel.getVchno();
        String date=salesReportModel.getVchdt();
        String party_name=salesReportModel.getGroupname();
        String total_qty="Bill Qty :"+salesReportModel.getTotal_qty();
        String total_amt="Bill Amt :"+salesReportModel.getTotal_amt();

        textBillNO.setText(bill_no);
        textDate.setText(date);
        textPartyName.setText(party_name);
        text_total_qty.setText(total_qty);
        textBillAMT.setText(total_amt);
    }

    private void events() {
        callGetProduct();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void callGetProduct() {
        String selectQuery =
                "SELECT  * FROM "+DatabaseConstant.SalesDetails.SALES_DETAILS_TABLE+" WHERE "+
                DatabaseConstant.SalesDetails.ENTRYID+"="+entry_id;


        SQLiteDatabase db = databaseHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            String prod_name = cursor.getString(cursor.getColumnIndex(DatabaseConstant.SalesDetails.PMSTNAME));
            String prod_qty = cursor.getString(cursor.getColumnIndex(DatabaseConstant.SalesDetails.PRODQTY));
            String cost_rate = cursor.getString(cursor.getColumnIndex(DatabaseConstant.SalesDetails.COSTRATE));
            String prod_gross_amt = cursor.getString(cursor.getColumnIndex(DatabaseConstant.SalesDetails.PRODGROSSAMT));
            String dist_per1 = cursor.getString(cursor.getColumnIndex(DatabaseConstant.SalesDetails.TD_PER));
            String dist_per2= cursor.getString(cursor.getColumnIndex(DatabaseConstant.SalesDetails.SP_PER));
            String dist1 = cursor.getString(cursor.getColumnIndex(DatabaseConstant.SalesDetails.TD_AMT));
            String dist2= cursor.getString(cursor.getColumnIndex(DatabaseConstant.SalesDetails.SP_AMT));
            String cgst_per = cursor.getString(cursor.getColumnIndex(DatabaseConstant.SalesDetails.CGST_PER));
            String sgst_per= cursor.getString(cursor.getColumnIndex(DatabaseConstant.SalesDetails.SGST_PER));
            String igst_per= cursor.getString(cursor.getColumnIndex(DatabaseConstant.SalesDetails.IGST_PER));
            String cgst = cursor.getString(cursor.getColumnIndex(DatabaseConstant.SalesDetails.CGST_AMT));
            String sgst= cursor.getString(cursor.getColumnIndex(DatabaseConstant.SalesDetails.SGST_AMT));
            String igst= cursor.getString(cursor.getColumnIndex(DatabaseConstant.SalesDetails.IGST_AMT));
            String final_Amt = cursor.getString(cursor.getColumnIndex(DatabaseConstant.SalesDetails.FINAL_AMT));


            SalesDetailsModel salesDetailsModel = new SalesDetailsModel();
            salesDetailsModel.setProd_name(prod_name);
            salesDetailsModel.setProd_qty(prod_qty);
            salesDetailsModel.setCost_rate(cost_rate);
            salesDetailsModel.setProd_gross_amt(prod_gross_amt);
            salesDetailsModel.setDist_per1(dist_per1);
            salesDetailsModel.setDist_per2(dist_per2);
            salesDetailsModel.setDist(dist1);
            salesDetailsModel.setDist1(dist2);
            salesDetailsModel.setCgst_per(cgst_per);
            salesDetailsModel.setSgst_per(sgst_per);
            salesDetailsModel.setIgst_per(igst_per);
            salesDetailsModel.setCgst(cgst);
            salesDetailsModel.setSgst(sgst);
            salesDetailsModel.setIgst(igst);
            salesDetailsModel.setFinal_Amt(final_Amt);

            salesDetailsModelArrayList.add(salesDetailsModel);

        }

        salesDetailsAdapter = new SalesDetailsAdapter(SaleDetailsActivity.this, salesDetailsModelArrayList);
        recyclerView_product_list.setAdapter(salesDetailsAdapter);
        salesDetailsAdapter.notifyDataSetChanged();
    }
}
