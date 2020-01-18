package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.connection.ConnectionCommon;
import com.mezyapps.new_reportanalyst.model.ProductTableModel;
import com.mezyapps.new_reportanalyst.model.PurchaseDTModel;
import com.mezyapps.new_reportanalyst.model.UserProfileModel;
import com.mezyapps.new_reportanalyst.utils.SharedLoginUtils;
import com.mezyapps.new_reportanalyst.utils.ShowProgressDialog;
import com.mezyapps.new_reportanalyst.view.adapter.ProductAutoCompleteAdapter;
import com.mezyapps.new_reportanalyst.view.adapter.PurchaseDTAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class AddProductActivity extends AppCompatActivity {
    private ImageView iv_back;
    private Button btn_save;
    private String databaseName;
    private AutoCompleteTextView autoCompleteTVProduct;
    private ConnectionCommon connectionCommon;
    private ShowProgressDialog showProgressDialog;
    private ArrayList<UserProfileModel> userProfileModelArrayList = new ArrayList<>();
    private ArrayList<ProductTableModel> productTableModelArrayList = new ArrayList<>();
    private ProductAutoCompleteAdapter productAutoCompleteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        find_View_IdS();
        events();
    }

    private void find_View_IdS() {
        showProgressDialog = new ShowProgressDialog(AddProductActivity.this);
        connectionCommon = new ConnectionCommon();
        iv_back = findViewById(R.id.iv_back);
        btn_save = findViewById(R.id.btn_save);
        autoCompleteTVProduct = findViewById(R.id.autoCompleteTVProduct);

        userProfileModelArrayList = SharedLoginUtils.getUserProfile(AddProductActivity.this);
        databaseName = userProfileModelArrayList.get(0).getDb_name();

        autoCompleteTVProduct.setThreshold(0);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void events() {
        ProductDetails productDetails = new ProductDetails();
        productDetails.execute("");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddProductActivity.this, "hiii", Toast.LENGTH_SHORT).show();
            }
        });

        autoCompleteTVProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ProductTableModel productTableModel = (ProductTableModel) adapterView.getItemAtPosition(position);
                String prod_name = productTableModel.getPMSTNAME();
                Toast.makeText(AddProductActivity.this, prod_name, Toast.LENGTH_SHORT).show();
            }
        });

        autoCompleteTVProduct.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                autoCompleteTVProduct.showDropDown();
                return false;
            }
        });


    }

    @SuppressLint("StaticFieldLeak")
    public class ProductDetails extends AsyncTask<String, String, String> {

        String msg = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            showProgressDialog.showDialog();
        }

        @Override
        protected void onPostExecute(String message) {
            showProgressDialog.dismissDialog();
            if (message.equalsIgnoreCase("success")) {
                productAutoCompleteAdapter = new ProductAutoCompleteAdapter(AddProductActivity.this, productTableModelArrayList);
                autoCompleteTVProduct.setAdapter(productAutoCompleteAdapter);
            } else {
                Toast.makeText(AddProductActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection connection = connectionCommon.checkUserConnection(databaseName);
                if (connection == null) {
                    msg = "Check Your Internet Access!";
                } else {
                    String query =
                            " SELECT PM.PRODID, PM.PMSTCODE, PM.PMSTNAME, CT.CATEGORYNAME, PM.PRODPKGINCASE, " +
                                    " PM.PACKING, UN.UNITNAME, PM.COSTPRICE, PM.SALERATE1 AS SALERATE, PM.MRP, HSN.HSNCODE, HSN.IGST_PER AS GST " +
                                    " FROM PMST AS PM INNER JOIN CATEGORYMASTER AS CT ON PM.CATEGORYID=CT.CATEGORYID " +
                                    " INNER JOIN UNITMASTER AS UN ON PM.PURCPACKING=UN.UNITID " +
                                    " INNER JOIN HSNCODE_MASTER AS HSN ON PM.HSNCODEID=HSN.HSNCODEID ";

                    Statement stmt = connection.createStatement();
                    ResultSet resultSet = stmt.executeQuery(query);
                    while (resultSet.next()) {
                        String prod_id = resultSet.getString("PRODID");
                        String prod_code = resultSet.getString("PMSTCODE");
                        String prod_name = resultSet.getString("PMSTNAME");
                        String categoryname = resultSet.getString("CATEGORYNAME");
                        String prodpkgincase = resultSet.getString("PRODPKGINCASE");
                        String packing = resultSet.getString("PACKING");
                        String unitname = resultSet.getString("UNITNAME");
                        String costprice = resultSet.getString("COSTPRICE");
                        String salerate = resultSet.getString("SALERATE");
                        String mrp = resultSet.getString("MRP");
                        String hsncode = resultSet.getString("HSNCODE");
                        String gst = resultSet.getString("GST");


                        ProductTableModel productTableModel = new ProductTableModel();
                        productTableModel.setPRODID(prod_id);
                        productTableModel.setPMSTCODE(prod_code);
                        productTableModel.setPMSTNAME(prod_name);
                        productTableModel.setCATEGORYNAME(categoryname);
                        productTableModel.setPRODPKGINCASE(prodpkgincase);
                        productTableModel.setPACKING(packing);
                        productTableModel.setUNITNAME(unitname);
                        productTableModel.setCOSTPRICE(costprice);
                        productTableModel.setSALERATE(salerate);
                        productTableModel.setMRP(mrp);
                        productTableModel.setHSNCODE(hsncode);
                        productTableModel.setGST(gst);

                        productTableModelArrayList.add(productTableModel);
                    }
                    if (productTableModelArrayList.size() != 0) {
                        msg = "success";
                        isSuccess = true;
                    } else {
                        msg = "fail";
                        isSuccess = false;
                    }
                    connection.close();
                }
            } catch (Exception ex) {
                isSuccess = false;
                msg = ex.getMessage();
            }
            return msg;
        }
    }
}
