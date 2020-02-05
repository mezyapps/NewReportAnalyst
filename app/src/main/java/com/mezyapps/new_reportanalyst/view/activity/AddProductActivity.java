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

public class AddProductActivity extends AppCompatActivity implements SelectProductDataInterface {
    private ImageView iv_back;
    private Button btn_save, btn_delete, btn_update;
    private String databaseName;
    private AutoCompleteTextView autoCompleteTVProduct;
    private ConnectionCommon connectionCommon;
    private ShowProgressDialog showProgressDialog;
    private ArrayList<UserProfileModel> userProfileModelArrayList = new ArrayList<>();
    private ArrayList<ProductTableModel> productTableModelArrayList = new ArrayList<>();
    private ArrayList<OrderEntryProduct> orderEntryProductArrayList = new ArrayList<>();
    private ProductAutoCompleteAdapter productAutoCompleteAdapter;
    private EditText edtQty, edtBoxPacking, edtPacking, edtRate, editDist, edtDistAmt, edtGstAmt, editGst, editDist2, edtDistAmt2;
    private String dicountedAmt = "", prod_id = "";
    private TextView textSubTotal, textFinalTotal, textProdCnt, textHsn_no, textInclu_Exclu;
    private String prod_name, select_prod, box, pkg, qty, rate, sub_total, dist_per, dist_amt, dist_per2, dist_amt2, gst_per,
            gst_amt, final_total, hsn_no;
    private AppDatabase appDatabase;
    private RelativeLayout rr_product_list;
    private Dialog dialog_product;
    private ScrollView scrollView_add_product;
    /*Validation*/
    boolean dist_amtv = false, disc_amtv2 = false;
    private LinearLayout ll_update_delete;
    private Long prod_long_id;
    private String disc1, disc2, sp_name, inclu_exclu, netTotal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        find_View_IdS();
        events();
    }

    private void find_View_IdS() {
        appDatabase = AppDatabase.getInStatce(AddProductActivity.this);
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
        editGst = findViewById(R.id.editGst);
        textFinalTotal = findViewById(R.id.textFinalTotal);
        edtGstAmt = findViewById(R.id.edtGstAmt);
        rr_product_list = findViewById(R.id.rr_product_list);
        textProdCnt = findViewById(R.id.textProdCnt);
        ll_update_delete = findViewById(R.id.ll_update_delete);
        btn_delete = findViewById(R.id.btn_delete);
        btn_update = findViewById(R.id.btn_update);
        textHsn_no = findViewById(R.id.textHsn_no);
        editDist2 = findViewById(R.id.editDist2);
        edtDistAmt2 = findViewById(R.id.edtDistAmt2);
        textInclu_Exclu = findViewById(R.id.textInclu_Exclu);
        scrollView_add_product = findViewById(R.id.scrollView_add_product);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            disc1 = extras.getString("DISC1");
            disc2 = extras.getString("DISC2");
            sp_name = extras.getString("SALES");
            if (disc1.equalsIgnoreCase(""))
                disc1 = "0.0";
            if (disc2.equalsIgnoreCase(""))
                disc2 = "0.0";
            editDist.setText(disc1);
            editDist2.setText(disc2);
        }

        userProfileModelArrayList = SharedLoginUtils.getUserProfile(AddProductActivity.this);
        databaseName = userProfileModelArrayList.get(0).getDb_name();
        inclu_exclu = userProfileModelArrayList.get(0).getINCLU_EXCLU();

        if (inclu_exclu.equalsIgnoreCase("I")) {
            textInclu_Exclu.setText("Inclusive");
        } else {
            textInclu_Exclu.setText("Exclusive");
        }


        autoCompleteTVProduct.setThreshold(0);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void events() {
        productList();

        productTableModelArrayList.clear();
        productTableModelArrayList.addAll(appDatabase.getPMSTDAO().getAllProduct());
        productAutoCompleteAdapter = new ProductAutoCompleteAdapter(AddProductActivity.this, productTableModelArrayList);
        autoCompleteTVProduct.setAdapter(productAutoCompleteAdapter);

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
                    orderEntryProduct.setDist_per1(dist_per);
                    orderEntryProduct.setDist_amt1(dist_amt);
                    orderEntryProduct.setDist_per2(dist_per2);
                    orderEntryProduct.setDist_amt2(dist_amt2);
                    orderEntryProduct.setGst_per(gst_per);
                    orderEntryProduct.setGst_amt(gst_amt);
                    orderEntryProduct.setFinal_total(final_total);
                    orderEntryProduct.setHsn_no(hsn_no);
                    orderEntryProduct.setInclu_exclu(inclu_exclu);
                    orderEntryProduct.setNet_total(netTotal);

                    long idVal = appDatabase.getProductDAO().addProduct(orderEntryProduct);
                    if (idVal != 0) {
                        Toast.makeText(AddProductActivity.this, prod_name + " Add Product Successfully", Toast.LENGTH_SHORT).show();
                        autoCompleteTVProduct.setText("");
                        edtBoxPacking.setText("");
                        edtPacking.setText("");
                        edtQty.setText("");
                        edtRate.setText("");
                        textSubTotal.setText("0");
                        edtDistAmt.setText("");
                        edtGstAmt.setText("");
                        edtDistAmt2.setText("");
                        textFinalTotal.setText("0");
                        textHsn_no.setText("");
                        autoCompleteTVProduct.requestFocus();
                        netTotal = "";
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
                select_prod = productTableModel.getPMSTNAME();
                prod_id = productTableModel.getPRODID();
                String prod_rate = "0";
                String sp_name1 = sp_name.toString().replaceAll("\\s", "").toLowerCase().trim();

                if (sp_name1.equalsIgnoreCase("SALERATE1")) {
                    prod_rate = productTableModel.getSALERATE1();
                } else if (sp_name1.equalsIgnoreCase("SALERATE2")) {
                    prod_rate = productTableModel.getSALERATE2();
                } else if (sp_name1.equalsIgnoreCase("SALERATE3")) {
                    prod_rate = productTableModel.getSALERATE3();
                } else if (sp_name1.equalsIgnoreCase("SALERATE4")) {
                    prod_rate = productTableModel.getSALERATE4();
                }

                if (!prod_rate.equalsIgnoreCase("0.00")) {
                    edtRate.setText(prod_rate);
                }


                String prod_pkg = productTableModel.getPACKING();
                if (!prod_pkg.equalsIgnoreCase("")) {
                    edtPacking.setText(prod_pkg);
                } else {
                    edtPacking.setText("1");
                }
                String gst_per = productTableModel.getIGST_PER();
                editGst.setText(gst_per);
                textHsn_no.setText(productTableModel.getHSNCD());
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
                    callCalucatePrice();
                } else {
                    textSubTotal.setText("0");
                    textFinalTotal.setText("0");
                    edtDistAmt.setText("");
                    edtDistAmt2.setText("");
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
                    callCalucatePrice();
                } else {
                    textSubTotal.setText("0");
                    textFinalTotal.setText("0");
                    edtDistAmt.setText("");
                    edtDistAmt2.setText("");
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
                    edtDistAmt2.setText("");
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
                    edtDistAmt2.setText("");
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
                            callCalucatePrice();
                        } else {
                            editDist.setError("Discount Not more than 100");
                            editDist.requestFocus();
                            dist_amtv = false;
                        }
                    } else {
                        edtDistAmt.setText("");
                    }
                } else {
                    textSubTotal.setText("0");
                    textFinalTotal.setText("0");
                    edtDistAmt2.setText("");
                    edtDistAmt.setText("");
                    edtGstAmt.setText("");

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editDist2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String subtotal = textSubTotal.getText().toString().trim();
                String distStr = editDist2.getText().toString().trim();
                if ((!subtotal.equalsIgnoreCase("0"))) {
                    if (!distStr.equalsIgnoreCase("")) {
                        double subtotalD = Double.parseDouble(textSubTotal.getText().toString().trim());
                        double dist = Double.parseDouble(distStr);
                        if (dist < 100) {
                            callCalucatePrice();
                        } else {
                            editDist2.setError("Discount Not more than 100");
                            editDist2.requestFocus();
                            disc_amtv2 = false;
                        }
                    } else {
                        edtDistAmt2.setText("");
                    }
                } else {
                    textSubTotal.setText("0");
                    textFinalTotal.setText("0");
                    edtDistAmt2.setText("");
                    edtDistAmt.setText("");
                    edtGstAmt.setText("");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                        Toast.makeText(AddProductActivity.this, " Delete Product Successfully", Toast.LENGTH_SHORT).show();
                        autoCompleteTVProduct.setText("");
                        edtBoxPacking.setText("");
                        edtPacking.setText("");
                        edtQty.setText("");
                        edtRate.setText("");
                        textSubTotal.setText("0");
                        edtDistAmt.setText("");
                        edtDistAmt2.setText("");
                        edtGstAmt.setText("");
                        textFinalTotal.setText("0");
                        textHsn_no.setText("");
                        autoCompleteTVProduct.requestFocus();
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
                    OrderEntryProduct orderEntryProduct = new OrderEntryProduct();
                    orderEntryProduct.setId(prod_long_id);
                    orderEntryProduct.setProduct_name(prod_name);
                    orderEntryProduct.setBox_pkg(box);
                    orderEntryProduct.setPkg(pkg);
                    orderEntryProduct.setQty(qty);
                    orderEntryProduct.setRate(rate);
                    orderEntryProduct.setSub_total(sub_total);
                    orderEntryProduct.setDist_per1(dist_per);
                    orderEntryProduct.setDist_amt1(dist_amt);
                    orderEntryProduct.setDist_per2(dist_per2);
                    orderEntryProduct.setDist_amt2(dist_amt2);
                    orderEntryProduct.setGst_per(gst_per);
                    orderEntryProduct.setGst_amt(gst_amt);
                    orderEntryProduct.setFinal_total(final_total);

                    long idVal = appDatabase.getProductDAO().getProductDataUpdate(orderEntryProduct);
                    if (idVal != 0) {
                        Toast.makeText(AddProductActivity.this, prod_name + " Update Product Successfully", Toast.LENGTH_SHORT).show();
                        autoCompleteTVProduct.setText("");
                        edtBoxPacking.setText("");
                        edtPacking.setText("");
                        edtQty.setText("");
                        edtRate.setText("");
                        textSubTotal.setText("0");
                        edtDistAmt.setText("");
                        edtDistAmt2.setText("");
                        edtGstAmt.setText("");
                        textFinalTotal.setText("0");
                        autoCompleteTVProduct.requestFocus();
                        textHsn_no.setText("");
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

    private void callCalucatePrice() {

        double qty = Double.parseDouble(edtQty.getText().toString().trim());
        double rate = Double.parseDouble(edtRate.getText().toString().trim());
        double total = qty * rate;

        /*Discount Calculation AND GST Calculation*/
        String discStr1 = editDist.getText().toString();
        String discStr2 = editDist2.getText().toString();

        if (discStr1.equalsIgnoreCase("")) {
            discStr1 = "0";
        }
        if (discStr2.equalsIgnoreCase("")) {
            discStr2 = "0";
        }

        double disc1 = Double.parseDouble(discStr1);
        double disc2 = Double.parseDouble(discStr2);


        /*GST Calculation*/
        String gstStr = editGst.getText().toString().trim();
        if (gstStr.equalsIgnoreCase("0")) {
            gstStr = "0";
        }

        /*GST INCLUSIVE AND EXCLUSIVE*/

        double gst = Double.parseDouble(gstStr);
        double finaltotal = 0, gstAMT = 0;
        double discAmt1 = 0, discAmt2 = 0, subTotal1 = 0, subTotal2=0;
        double subTotal = total;

        if (inclu_exclu.equalsIgnoreCase("I")) {
            double gper = 0;
            if (gstStr.equalsIgnoreCase("5.00")) {
                gper = 1.05;
            } else if (gstStr.equalsIgnoreCase("12.00")) {
                gper = 1.12;
            } else if (gstStr.equalsIgnoreCase("18.00")) {
                gper = 1.18;
            } else if (gstStr.equalsIgnoreCase("28.00")) {
                gper = 1.28;
            }
            double inclsive = subTotal / gper;
            subTotal=inclsive;
        }

        discAmt1 = (subTotal / 100) * disc1;
        subTotal -= discAmt1;

        discAmt2 = (subTotal / 100) * disc2;
        subTotal -= discAmt2;

        gstAMT = (subTotal / 100) * gst;
        subTotal += gstAMT;


        /*if (inclu_exclu.equalsIgnoreCase("E")) {

            double subTotal = total;

            discAmt1 = (subTotal / 100) * disc1;
            subTotal1 = subTotal - discAmt1;

            discAmt2 = (subTotal1 / 100) * disc2;
            subTotal2 = subTotal1 - discAmt2;

            gstAMT = (subTotal2 / 100) * gst;
            finaltotal = subTotal2 + gstAMT;

        } else {
            double gper = 0;
            if (gstStr.equalsIgnoreCase("5.00")) {
                gper = 1.05;
            } else if (gstStr.equalsIgnoreCase("12.00")) {
                gper = 1.12;
            } else if (gstStr.equalsIgnoreCase("18.00")) {
                gper = 1.18;
            } else if (gstStr.equalsIgnoreCase("28.00")) {
                gper = 1.28;
            }
            double subTotal = total;
            double inclsive = subTotal / gper;

            discAmt1 = (inclsive / 100) * disc1;
            subTotal1 = inclsive - discAmt1;

            discAmt2 = (subTotal1 / 100) * disc2;
            subTotal2 = subTotal1 - discAmt2;

            gstAMT = (subTotal2 / 100) * gst;
            finaltotal = subTotal2 + gstAMT;
        }*/


        netTotal = String.valueOf(subTotal1);

        String subTotalStr = String.format("%.2f", total);
        String finalTotalStr = String.format("%.2f", subTotal);

        String discAmtStr1 = String.format("%.2f", discAmt1);
        String discAmtStr2 = String.format("%.2f", discAmt2);
        String gstAmtStr = String.format("%.2f", gstAMT);

        edtDistAmt.setText(discAmtStr1);
        edtDistAmt2.setText(discAmtStr2);
        edtGstAmt.setText(gstAmtStr);

        textSubTotal.setText(subTotalStr);
        textFinalTotal.setText(finalTotalStr);
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
        ImageView iv_close_dialog = dialog_product.findViewById(R.id.iv_close_dialog);

        dialog_product.setCancelable(false);
        dialog_product.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog_product.show();

        Window window = dialog_product.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddProductActivity.this);
        recycler_view_product_list.setLayoutManager(linearLayoutManager);

        Collections.reverse(orderEntryProductArrayList);
        OrderEntryProductAdapter orderEntryProductAdapter = new OrderEntryProductAdapter(AddProductActivity.this, orderEntryProductArrayList, true, this);
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
        dist_per2 = editDist2.getText().toString().trim();
        dist_amt2 = edtDistAmt2.getText().toString().trim();
        gst_per = editGst.getText().toString();
        gst_amt = edtGstAmt.getText().toString().trim();
        final_total = textFinalTotal.getText().toString().trim();
        hsn_no = textHsn_no.getText().toString().trim();

        if (prod_id.equalsIgnoreCase("")) {
            autoCompleteTVProduct.setError("Select Product Name");
            autoCompleteTVProduct.requestFocus();
            return false;
        } else if (!prod_name.equals(select_prod)) {
            autoCompleteTVProduct.setError("Select Valid Product Name");
            autoCompleteTVProduct.requestFocus();
            return false;
        } else if (qty.equalsIgnoreCase("")) {
            edtQty.setError("Enter qty");
            edtQty.requestFocus();
            return false;
        } else if (sub_total.equalsIgnoreCase("0.0")) {
            Toast.makeText(this, "Please Check Subtotal", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void selectProductData(OrderEntryProduct orderEntryProduct) {
        dialog_product.dismiss();
        autoCompleteTVProduct.setText(orderEntryProduct.getProduct_name());
        prod_id = String.valueOf(orderEntryProduct.getProduct_id());
        prod_long_id = orderEntryProduct.getId();
        edtBoxPacking.setText(orderEntryProduct.getBox_pkg());
        edtPacking.setText(orderEntryProduct.getPkg());
        edtQty.setText(orderEntryProduct.getQty());
        edtRate.setText(orderEntryProduct.getRate());
        textSubTotal.setText(orderEntryProduct.getSub_total());
        editDist.setText(orderEntryProduct.getDist_per1());
        edtDistAmt.setText(orderEntryProduct.getDist_amt1());
        editDist2.setText(orderEntryProduct.getDist_per2());
        edtDistAmt2.setText(orderEntryProduct.getDist_amt2());
        textHsn_no.setText(orderEntryProduct.getHsn_no());
        String gst_per = orderEntryProduct.getGst_per();
        editGst.setText(gst_per);
        edtGstAmt.setText(orderEntryProduct.getGst_amt());
        btn_save.setVisibility(View.GONE);
        ll_update_delete.setVisibility(View.VISIBLE);

    }
}
