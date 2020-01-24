package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Query;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.mezyapps.new_reportanalyst.utils.SelectProductDataInterface;
import com.mezyapps.new_reportanalyst.utils.SharedLoginUtils;
import com.mezyapps.new_reportanalyst.utils.ShowProgressDialog;
import com.mezyapps.new_reportanalyst.view.adapter.OrderEntryProductAdapter;
import com.mezyapps.new_reportanalyst.view.adapter.ProductAutoCompleteAdapter;
import com.mezyapps.new_reportanalyst.view.adapter.PurchaseDTAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

public class AddProductActivity extends AppCompatActivity implements  SelectProductDataInterface {
    private ImageView iv_back;
    private Button btn_save,btn_delete,btn_update;
    private String databaseName;
    private AutoCompleteTextView autoCompleteTVProduct;
    private ConnectionCommon connectionCommon;
    private ShowProgressDialog showProgressDialog;
    private ArrayList<UserProfileModel> userProfileModelArrayList = new ArrayList<>();
    private ArrayList<ProductTableModel> productTableModelArrayList = new ArrayList<>();
    private ArrayList<OrderEntryProduct> orderEntryProductArrayList = new ArrayList<>();
    private ProductAutoCompleteAdapter productAutoCompleteAdapter;
    private EditText edtQty, edtBoxPacking, edtPacking, edtRate, editDist, edtDistAmt, edtGstAmt;
    private String dicountedAmt = "", prod_id;
    private Spinner spinnerGST;
    private TextView textSubTotal, textFinalTotal, textProdCnt;
    private String prod_name, box, pkg, qty, rate, sub_total, dist_per, dist_amt, gst_per, gst_amt, final_total;
    private AppDatabase appDatabase;
    private RelativeLayout rr_product_list;
    private Dialog dialog_product;
    private ArrayList<String> spinnerSelect=new ArrayList<>();
    private ArrayAdapter<String> spinnerArrayAdapter;
    private ScrollView scrollView_add_product;
    /*Validation*/
    boolean dist_amtv = false;
    private LinearLayout ll_update_delete;
    private Long prod_long_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        find_View_IdS();
        events();
    }

    private void find_View_IdS() {
        appDatabase = Room.databaseBuilder(AddProductActivity.this, AppDatabase.class, "ReportAnalyst")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
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
        rr_product_list = findViewById(R.id.rr_product_list);
        textProdCnt = findViewById(R.id.textProdCnt);
        ll_update_delete = findViewById(R.id.ll_update_delete);
        btn_delete = findViewById(R.id.btn_delete);
        btn_update = findViewById(R.id.btn_update);
        scrollView_add_product = findViewById(R.id.scrollView_add_product);



        userProfileModelArrayList = SharedLoginUtils.getUserProfile(AddProductActivity.this);
        databaseName = userProfileModelArrayList.get(0).getDb_name();


        autoCompleteTVProduct.setThreshold(0);

        spinnerSelect.add("Select GST");
        spinnerSelect.add("3");
        spinnerSelect.add("5");
        spinnerSelect.add("12");
        spinnerSelect.add("18");
        spinnerSelect.add("28");

        spinnerArrayAdapter= new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, spinnerSelect);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerGST.setAdapter(spinnerArrayAdapter);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void events() {
        productList();
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
                        spinnerGST.setSelection(0,true);
                        productList();
                        rr_product_list.setVisibility(View.VISIBLE);
                        scrollView_add_product.pageScroll(View.FOCUS_UP);
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
                if (!prod_rate.equalsIgnoreCase("0.00")) {
                    edtRate.setText(prod_rate);
                }
                String prod_pkg = productTableModel.getPACKING();
                if (!prod_pkg.equalsIgnoreCase("")) {
                    edtPacking.setText(prod_pkg);
                }
                else
                {
                    edtPacking.setText("1");
                }
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
                if (!strQty.equalsIgnoreCase("") && !strRate.equalsIgnoreCase("")) {
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
                    editDist.setText("");
                    spinnerGST.setSelection(0, true);
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

        rr_product_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogShowProductList();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogAlert = new Dialog(AddProductActivity.this);
                dialogAlert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogAlert.setContentView(R.layout.dialog_alert_message);

                dialogAlert.setCancelable(false);
                dialogAlert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                TextView textMsg = dialogAlert.findViewById(R.id.textMsg);
                Button btnYes = dialogAlert.findViewById(R.id.btnYes);
                Button btnNo = dialogAlert.findViewById(R.id.btnNo);
                textMsg.setText(getResources().getString(R.string.delete_msg));

                dialogAlert.show();
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogAlert.dismiss();
                        appDatabase.getProductDAO().deleteSingleProduct(prod_long_id);
                        Toast.makeText(AddProductActivity.this," Delete Product Successfully", Toast.LENGTH_SHORT).show();
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
                        spinnerGST.setSelection(0,true);
                        productList();
                        btn_save.setVisibility(View.VISIBLE);
                        ll_update_delete.setVisibility(View.GONE);
                        scrollView_add_product.pageScroll(View.FOCUS_UP);
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogAlert.dismiss();
                    }
                });

            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {

                    if (gst_per.equalsIgnoreCase("Select GST")) {
                        gst_per="";
                    }

                    long idVal = appDatabase.getProductDAO().getProductDataUpdate(prod_long_id,prod_name,box,pkg,qty,rate,sub_total,dist_per,gst_per,dist_amt,gst_amt,final_total);
                    if (idVal != 0) {
                        Toast.makeText(AddProductActivity.this, prod_name + " Update Product Successfully", Toast.LENGTH_SHORT).show();
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
                        spinnerGST.setSelection(0,true);
                        productList();
                        btn_save.setVisibility(View.VISIBLE);
                        ll_update_delete.setVisibility(View.GONE);
                        scrollView_add_product.pageScroll(View.FOCUS_UP);
                    } else {
                        Toast.makeText(AddProductActivity.this, prod_name + " Not Added", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    private void productList() {
        orderEntryProductArrayList.clear();
        orderEntryProductArrayList.addAll(appDatabase.getProductDAO().getAppProduct());
        int size = orderEntryProductArrayList.size();
        if (size > 0) {
            textProdCnt.setText(String.valueOf(size));
        } else {
            rr_product_list.setVisibility(View.GONE);
        }
    }

    private void dialogShowProductList() {
        dialog_product = new Dialog(AddProductActivity.this);
        dialog_product.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_product.setContentView(R.layout.dialog_product_list);

        RecyclerView recycler_view_product_list = dialog_product.findViewById(R.id.recycler_view_product_list);
        ImageView iv_close_dialog=dialog_product.findViewById(R.id.iv_close_dialog);

        dialog_product.setCancelable(false);
        dialog_product.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog_product.show();

        Window window = dialog_product.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddProductActivity.this);
        recycler_view_product_list.setLayoutManager(linearLayoutManager);

        Collections.reverse(orderEntryProductArrayList);
        OrderEntryProductAdapter orderEntryProductAdapter = new OrderEntryProductAdapter(AddProductActivity.this, orderEntryProductArrayList,true,this);
        recycler_view_product_list.setAdapter(orderEntryProductAdapter);
        orderEntryProductAdapter.notifyDataSetChanged();

        iv_close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_product.dismiss();
            }
        });

    }

    private boolean validation() {
        prod_name = autoCompleteTVProduct.getText().toString().trim();
        box = edtBoxPacking.getText().toString().trim();
        pkg = edtPacking.getText().toString().trim();
        qty = edtQty.getText().toString().trim();
        rate = edtRate.getText().toString().trim();
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
        }else if (qty.equalsIgnoreCase("")) {
            edtQty.setError("Enter qty");
            edtQty.requestFocus();
            return false;
        }else if(sub_total.equalsIgnoreCase("0"))
        {
            Toast.makeText(this, "Please Check Subtotal", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void selectProductData(OrderEntryProduct orderEntryProduct) {
        dialog_product.dismiss();
        autoCompleteTVProduct.setText(orderEntryProduct.getProduct_name());
        prod_id=String.valueOf(orderEntryProduct.getProduct_id());
        prod_long_id=orderEntryProduct.getId();
        edtBoxPacking.setText(orderEntryProduct.getBox_pkg());
        edtPacking.setText(orderEntryProduct.getPkg());
        edtQty.setText(orderEntryProduct.getQty());
        edtRate.setText(orderEntryProduct.getRate());
        textSubTotal.setText(orderEntryProduct.getSub_total());
        editDist.setText(orderEntryProduct.getDist_per());
        edtDistAmt.setText(orderEntryProduct.getDist_amt());
        String spinnerVal=orderEntryProduct.getGst_per();
        if(spinnerVal.equalsIgnoreCase(""))
        {
            spinnerGST.setSelection(0, true);
        }
        else {
            spinnerGST.setSelection(spinnerSelect.indexOf(spinnerVal), true);
        }
        edtGstAmt.setText(orderEntryProduct.getGst_amt());
        btn_save.setVisibility(View.GONE);
        ll_update_delete.setVisibility(View.VISIBLE);

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
                    productTableModelArrayList.clear();
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
