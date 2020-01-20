package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.connection.ConnectionCommon;
import com.mezyapps.new_reportanalyst.db.AppDatabase;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProduct;
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
    private EditText edtQty, edtBoxPacking, edtPacking, edtRate, editDist, edtDistAmt, edtGstAmt;
    private String dicountedAmt = "", prod_id;
    private Spinner spinnerGST;
    private TextView textSubTotal, textFinalTotal;
    private String prod_name, box, pkg, qty, rate, sub_total, dist_per, dist_amt, gst_per, gst_amt, final_total;
    private AppDatabase appDatabase;

    /*Validation*/
    boolean dist_amtv = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        find_View_IdS();
        events();
    }

    private void find_View_IdS() {
        appDatabase = Room.databaseBuilder(AddProductActivity.this, AppDatabase.class, "ReportAnalyst").allowMainThreadQueries().build();
        showProgressDialog = new ShowProgressDialog(AddProductActivity.this);
        connectionCommon = new ConnectionCommon();
        iv_back = findViewById(R.id.iv_back);
        btn_save = findViewById(R.id.btn_save);
        autoCompleteTVProduct = findViewById(R.id.autoCompleteTVProduct);
        edtQty = findViewById(R.id.edtQty);
        edtBoxPacking = findViewById(R.id.edtBoxPacking);
        edtPacking = findViewById(R.id.edtPacking);
        edtRate = findViewById(R.id.edtRate);
        textSubTotal = findViewById(R.id.textSubTotal);
        editDist = findViewById(R.id.editDist);
        edtDistAmt = findViewById(R.id.edtDistAmt);
        spinnerGST = findViewById(R.id.spinnerGST);
        textFinalTotal = findViewById(R.id.textFinalTotal);
        edtGstAmt = findViewById(R.id.edtGstAmt);

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
                if (validation()) {
                    OrderEntryProduct orderEntryProduct = new OrderEntryProduct();
                    orderEntryProduct.setProduct_id(Long.parseLong(prod_id));
                    orderEntryProduct.setProduct_name(prod_name);
                    orderEntryProduct.setBox_pkg(box);
                    orderEntryProduct.setPkg(pkg);
                    orderEntryProduct.setQty(qty);
                    orderEntryProduct.setRate(rate);
                    orderEntryProduct.setSub_total(sub_total);
                    orderEntryProduct.setDist_per(dist_per);
                    orderEntryProduct.setDist_amt(dist_amt);
                    if (gst_per.equalsIgnoreCase("Select GST")) {
                        orderEntryProduct.setGst_per("");
                    } else {
                        orderEntryProduct.setGst_per(gst_per);
                    }
                    orderEntryProduct.setGst_amt(gst_amt);
                    orderEntryProduct.setFinal_total(final_total);

                    long idVal = appDatabase.getProductDAO().addProduct(orderEntryProduct);
                    if (idVal != 0) {
                        Toast.makeText(AddProductActivity.this, prod_name + " Add Product Successfully", Toast.LENGTH_SHORT).show();
                        autoCompleteTVProduct.setText("");
                        edtBoxPacking.setText("");
                        edtPacking.setText("");
                        edtQty.setText("");
                        edtRate.setText("");
                        textSubTotal.setText("0");
                        editDist.setText("");
                        edtDistAmt.setText("");
                        edtGstAmt.setText("");
                        textFinalTotal.setText("0");
                        autoCompleteTVProduct.requestFocus();

                    } else {
                        Toast.makeText(AddProductActivity.this, prod_name + " Not Added", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        autoCompleteTVProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ProductTableModel productTableModel = (ProductTableModel) adapterView.getItemAtPosition(position);
                String prod_name = productTableModel.getPMSTNAME();
                prod_id = productTableModel.getPRODID();
                String prod_rate = productTableModel.getSALERATE();
                if (!prod_rate.equalsIgnoreCase("0.00"))
                {
                    edtRate.setText(prod_rate);
                }
                String prod_pkg = productTableModel.getPACKING();
                edtPacking.setText(prod_pkg);
            }
        });

        autoCompleteTVProduct.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                autoCompleteTVProduct.showDropDown();
                return false;
            }
        });
        edtQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String strQty = edtQty.getText().toString().trim();
                String strRate = edtRate.getText().toString().trim();
                if (!strQty.equalsIgnoreCase("")&&!strRate.equalsIgnoreCase("")) {
                    double qty = Double.parseDouble(edtQty.getText().toString().trim());
                    double rate = Double.parseDouble(edtRate.getText().toString().trim());
                    double total = qty * rate;
                    String totalStr = String.format("%.2f", total);
                    textSubTotal.setText(totalStr);
                    textFinalTotal.setText(totalStr);
                } else {
                    textSubTotal.setText("0");
                    textFinalTotal.setText("0");
                    edtDistAmt.setText("");
                    edtGstAmt.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String strRate = edtRate.getText().toString().trim();
                String strQty = edtQty.getText().toString().trim();
                if ((!strQty.equalsIgnoreCase("")) && (!strRate.equalsIgnoreCase(""))) {
                    double qty = Double.parseDouble(edtQty.getText().toString().trim());
                    double rate = Double.parseDouble(edtRate.getText().toString().trim());
                    double total = qty * rate;
                    String totalStr = String.format("%.2f", total);
                    textSubTotal.setText(totalStr);
                    textFinalTotal.setText(totalStr);
                } else {
                    textSubTotal.setText("0");
                    textFinalTotal.setText("0");
                    edtDistAmt.setText("");
                    edtGstAmt.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtBoxPacking.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String strBox = edtBoxPacking.getText().toString().trim();
                String strPkg = edtPacking.getText().toString().trim();
                if ((!strBox.equalsIgnoreCase("")) && (!strPkg.equalsIgnoreCase(""))) {
                    double box = Double.parseDouble(edtBoxPacking.getText().toString().trim());
                    double pak = Double.parseDouble(edtPacking.getText().toString().trim());
                    double total = box * pak;
                    String totalStr = String.format("%.2f", total);
                    edtQty.setText(totalStr);
                } else {
                    textSubTotal.setText("0");
                    textFinalTotal.setText("0");
                    edtDistAmt.setText("");
                    edtGstAmt.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtPacking.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String strBox = edtBoxPacking.getText().toString().trim();
                String strPkg = edtPacking.getText().toString().trim();
                if ((!strBox.equalsIgnoreCase("")) && (!strPkg.equalsIgnoreCase(""))) {
                    double box = Double.parseDouble(edtBoxPacking.getText().toString().trim());
                    double pak = Double.parseDouble(edtPacking.getText().toString().trim());
                    double total = box * pak;
                    String totalStr = String.format("%.2f", total);
                    edtQty.setText(totalStr);
                } else {
                    textSubTotal.setText("0");
                    textFinalTotal.setText("0");
                    edtDistAmt.setText("");
                    edtGstAmt.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editDist.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String subtotal = textSubTotal.getText().toString().trim();
                String distStr = editDist.getText().toString().trim();
                if ((!subtotal.equalsIgnoreCase("0"))) {
                    if (!distStr.equalsIgnoreCase("")) {
                        double subtotalD = Double.parseDouble(textSubTotal.getText().toString().trim());
                        double dist = Double.parseDouble(distStr);
                        if (dist < 100) {
                            double total = subtotalD / 100;
                            double finaldist = total * dist;
                            dicountedAmt = String.valueOf(subtotalD - finaldist);
                            String totalStr = String.format("%.2f", finaldist);
                            edtDistAmt.setText(totalStr);
                            String finalTotal = String.format("%.2f", Double.parseDouble(dicountedAmt));
                            textFinalTotal.setText(finalTotal);
                        } else {
                            editDist.setError("Discount Not more than 100");
                            editDist.requestFocus();
                            dist_amtv = false;
                        }
                    } else {
                        edtDistAmt.setText("");
                        textFinalTotal.setText(subtotal);
                    }
                } else {
                    textSubTotal.setText("0");
                    textFinalTotal.setText("0");
                    edtDistAmt.setText("");
                    edtGstAmt.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        spinnerGST.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                String selectStr = spinnerGST.getSelectedItem().toString();
                if (!selectStr.equalsIgnoreCase("Select GST")) {
                    dicountedAmt = textFinalTotal.getText().toString();
                    if (!dicountedAmt.equalsIgnoreCase("")) {
                        double dictAmt = Double.parseDouble(dicountedAmt);
                        double gst = Double.parseDouble(selectStr);
                        double total = dictAmt / 100;
                        double finalgst = total * gst;
                        String finalTotal = String.format("%.2f", dictAmt + finalgst);
                        String totalStr = String.format("%.2f", finalgst);
                        textFinalTotal.setText(finalTotal);
                        edtGstAmt.setText(totalStr);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private boolean validation() {
        prod_name = autoCompleteTVProduct.getText().toString().trim();
        box = edtBoxPacking.getText().toString().trim();
        pkg = edtPacking.getText().toString().trim();
        qty = edtQty.getText().toString().trim();
        rate = edtQty.getText().toString().trim();
        sub_total = textSubTotal.getText().toString().trim();
        dist_per = editDist.getText().toString().trim();
        dist_amt = edtDistAmt.getText().toString().trim();
        gst_per = spinnerGST.getSelectedItem().toString();
        gst_amt = edtGstAmt.getText().toString().trim();
        final_total = textFinalTotal.getText().toString().trim();

        if (prod_name.equalsIgnoreCase("")) {
            autoCompleteTVProduct.setError("Select Product Name");
            autoCompleteTVProduct.requestFocus();
            return false;
        } else if (qty.equalsIgnoreCase("")) {
            edtQty.setError("Enter qty");
            edtQty.requestFocus();
        }

        return true;
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
