package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.connection.ConnectionCommon;
import com.mezyapps.new_reportanalyst.model.AreaMasterModel;
import com.mezyapps.new_reportanalyst.model.SalesReportModel;
import com.mezyapps.new_reportanalyst.model.UserProfileModel;
import com.mezyapps.new_reportanalyst.utils.NetworkUtils;
import com.mezyapps.new_reportanalyst.utils.SharedLoginUtils;
import com.mezyapps.new_reportanalyst.utils.ShowProgressDialog;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AddCustomerActivity extends AppCompatActivity {
    private ImageView iv_back;
    private TextInputEditText edtName, edtAddress1, edtAddress2, edtAddress3, edtAddress4,
            edtMobileNo, edtEmailId, edtGstNo;
    private Spinner spinnerArea;
    private Button btnSave;
    private String name, address1, address2, address3, address4, area_id, area, gst_no, mobile_no,
            salesman_name, salesman_id, email_id;
    private ShowProgressDialog showProgressDialog;
    private ArrayList<UserProfileModel> userProfileModelArrayList = new ArrayList<>();
    private ArrayList<AreaMasterModel> areaMasterModelArrayList = new ArrayList<>();
    private ArrayList<String> stringArrayList = new ArrayList<>();
    private String databaseName;
    private ConnectionCommon connectionCommon;
    private ArrayAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        find_View_Ids();
        events();
    }

    private void find_View_Ids() {
        showProgressDialog = new ShowProgressDialog(AddCustomerActivity.this);
        connectionCommon = new ConnectionCommon(AddCustomerActivity.this);
        iv_back = findViewById(R.id.iv_back);
        edtName = findViewById(R.id.edtName);
        edtAddress1 = findViewById(R.id.edtAddress1);
        edtAddress2 = findViewById(R.id.edtAddress2);
        edtAddress3 = findViewById(R.id.edtAddress3);
        edtAddress4 = findViewById(R.id.edtAddress4);
        spinnerArea = findViewById(R.id.spinnerArea);
        edtMobileNo = findViewById(R.id.edtMobileNo);
        edtEmailId = findViewById(R.id.edtEmailId);
        edtGstNo = findViewById(R.id.edtGstNo);
        btnSave = findViewById(R.id.btnSave);

        userProfileModelArrayList = SharedLoginUtils.getUserProfile(AddCustomerActivity.this);
        databaseName = userProfileModelArrayList.get(0).getDb_name();
        salesman_id = userProfileModelArrayList.get(0).getSALESMAN_ID();
        salesman_name = userProfileModelArrayList.get(0).getSALESMAN_NAME();
    }

    private void events() {
        AreaMaster areaMaster = new AreaMaster();
        areaMaster.execute("");

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    if (NetworkUtils.isNetworkAvailable(AddCustomerActivity.this)) {
                        AddCustomer addCustomer = new AddCustomer();
                        addCustomer.execute("");
                    } else {
                        NetworkUtils.isNetworkNotAvailable(AddCustomerActivity.this);
                    }
                }
            }
        });

        spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                try {
                    int area_id_int = Integer.parseInt(areaMasterModelArrayList.get(position).getArea_id());
                    area = areaMasterModelArrayList.get(position).getArea_name();
                    area_id = String.valueOf(area_id_int);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public class AreaMaster extends AsyncTask<String, String, String> {
        String msg = "";
        Boolean isSuccess = false;


        @Override
        protected void onPreExecute() {
            showProgressDialog.showDialog();
        }

        @Override
        protected void onPostExecute(String msg) {
            showProgressDialog.dismissDialog();
            if (isSuccess) {
                arrayAdapter = new ArrayAdapter(AddCustomerActivity.this, android.R.layout.simple_spinner_item, stringArrayList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerArea.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            } else {
                arrayAdapter = new ArrayAdapter(AddCustomerActivity.this, android.R.layout.simple_spinner_item, stringArrayList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerArea.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionCommon.checkUserConnection(databaseName);
                if (con == null) {
                    msg = "Error in connection with SQL server";
                } else {

                    String query = "SELECT * FROM MOB_AREA WHERE RESERVED='N'";

                    Statement stmt = con.createStatement();
                    ResultSet resultSet = stmt.executeQuery(query);
                    areaMasterModelArrayList.clear();
                    stringArrayList.clear();
                    while (resultSet.next()) {
                        String area_id = resultSet.getString("AREAID");
                        String area_name = resultSet.getString("AREANAME");
                        String city_name = resultSet.getString("CITYNAME");


                        AreaMasterModel areaMasterModel = new AreaMasterModel();
                        areaMasterModel.setArea_id(area_id);
                        areaMasterModel.setArea_name(area_name);
                        areaMasterModel.setCity_name(city_name);

                        areaMasterModelArrayList.add(areaMasterModel);
                        stringArrayList.add(area_name);

                    }
                    if (areaMasterModelArrayList.size() != 0) {
                        msg = "success";
                        isSuccess = true;
                    } else {
                        msg = "fail";
                        isSuccess = false;
                    }
                    con.close();
                }
            } catch (Exception ex) {
                msg = "New Customer  Not Added";
                isSuccess = false;
            }

            return msg;
        }

    }

    public class AddCustomer extends AsyncTask<String, String, String> {
        String msg = "";
        Boolean isSuccess = false;


        @Override
        protected void onPreExecute() {
            showProgressDialog.showDialog();
        }

        @Override
        protected void onPostExecute(String msg) {
            showProgressDialog.dismissDialog();
            if (isSuccess) {
                Toast.makeText(AddCustomerActivity.this, msg, Toast.LENGTH_SHORT).show();
                onBackPressed();
            } else {
                Toast.makeText(AddCustomerActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionCommon.checkUserConnection(databaseName);
                if (con == null) {
                    msg = "Error in connection with SQL server";
                } else {
                    int maxID = findMaxID(con);
                    boolean insertHD = callGroupPer(con, maxID);
                    if (insertHD) {
                        boolean insertGroupPerNew = callGroupPerNew(con, maxID);
                        if (insertGroupPerNew) {
                            msg = "New Customer Add Successfully";
                            isSuccess = true;
                        } else {
                            msg = "New Customer  Not Added";
                            isSuccess = false;
                        }
                    } else {
                        msg = "New Customer  Not Added";
                        isSuccess = false;
                    }

                }
            } catch (Exception ex) {
                msg = "New Customer  Not Added";
                isSuccess = false;
            }

            return msg;
        }

    }


    private int findMaxID(Connection con) throws SQLException {
        int maxID = 0;
        String query = "SELECT  MAX(GROUPID) AS MAXID from MOB_GROUPS_PER";

        Statement stmt = con.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);
        while (resultSet.next()) {
            maxID = resultSet.getInt("MAXID");
        }
        return maxID + 1;
    }

    private boolean callGroupPer(Connection con, int maxID) throws SQLException {
        boolean isInsert = false;

        String query = "INSERT INTO MOB_GROUPS_PER (GROUPID,GROUPCODE,GROUPNAME,ADD1,ADD2,ADD3,ADD4,AREANAME,GSTNO,MOBILENO,SALESMAN_ID,SALESMAN_NAME) " +
                "values(" + maxID + ",'" + name + "','" + name + "','" + address1 + "','" + address2 + "','" + address3 + "','" + address4 + "','" + area + "','" + gst_no + "','" + mobile_no + "'," + salesman_id + ",'" + salesman_name + "')";
        Statement stmt = con.createStatement();
        int rs = stmt.executeUpdate(query);
        if (rs == 1) {
            isInsert = true;

        } else {
            isInsert = false;
        }

        return isInsert;
    }

    private boolean callGroupPerNew(Connection con, int maxID) throws SQLException {
        boolean isInsert = false;

        String query = "INSERT INTO MOB_NEW_GROUPS_PER (GROUPID,GROUPCODE,GROUPNAME,ADD1,ADD2,ADD3,ADD4,AREAID,AREANAME,GSTNO,MOBILE_NO,EMAIL_ID,SALESMAN_ID,SALESMAN_NAME) " +
                "values(" + maxID + ",'" + name + "','" + name + "','" + address1 + "','" + address2 + "','" + address3 + "','" + address4 + "'," + area_id + ",'" + area +
                "','" + gst_no + "','" + mobile_no + "','" + email_id + "'," + salesman_id + ",'" + salesman_name + "')";
        Statement stmt = con.createStatement();
        int rs = stmt.executeUpdate(query);
        if (rs == 1) {
            isInsert = true;

        } else {
            isInsert = false;
        }

        return isInsert;

    }

    private boolean validation() {
        name = edtName.getText().toString().trim();
        address1 = edtAddress1.getText().toString().trim();
        address2 = edtAddress2.getText().toString().trim();
        address3 = edtAddress3.getText().toString().trim();
        address4 = edtAddress4.getText().toString().trim();
        gst_no = edtGstNo.getText().toString().trim();
        mobile_no = edtMobileNo.getText().toString().trim();
        email_id = edtEmailId.getText().toString().trim();


        if (name.equalsIgnoreCase("")) {
            edtName.setError("Enter Name");
            edtName.requestFocus();
            return false;
        } else if (mobile_no.equalsIgnoreCase("")) {
            edtMobileNo.setError("Enter Mobile No");
            edtMobileNo.requestFocus();
            return false;
        } else if (mobile_no.length() < 10) {
            edtMobileNo.setError("Enter valid Mobile No");
            edtMobileNo.requestFocus();
            return false;
        } else if (gst_no.equalsIgnoreCase("")) {
            edtGstNo.setError("Enter GST No");
            edtGstNo.requestFocus();
            return false;
        } else if (gst_no.length() < 15) {
            edtGstNo.setError("Enter valid Gst No");
            edtGstNo.requestFocus();
            return false;
        }
        return true;
    }
}
