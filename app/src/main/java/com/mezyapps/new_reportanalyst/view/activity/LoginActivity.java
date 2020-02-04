package com.mezyapps.new_reportanalyst.view.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.connection.ConnectionCommon;
import com.mezyapps.new_reportanalyst.model.UserProfileModel;
import com.mezyapps.new_reportanalyst.utils.SharedLoginUtils;
import com.mezyapps.new_reportanalyst.utils.ShowProgressDialog;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private TextInputEditText edit_username, edit_password;
    private ShowProgressDialog showProgressDialog;
    private ConnectionCommon connectionCommon;
    private ArrayList<UserProfileModel> userProfileModelArrayList = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        find_view_IdS();
        events();
    }

    private void find_view_IdS() {
        edit_username = findViewById(R.id.edit_username);
        edit_password = findViewById(R.id.edit_password);
        btn_login = findViewById(R.id.btn_login);

        connectionCommon = new ConnectionCommon(LoginActivity.this);
        showProgressDialog = new ShowProgressDialog(LoginActivity.this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void events() {

        getMobileNumber();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckLogin checkLogin = new CheckLogin();
                checkLogin.execute("");
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getMobileNumber() {
        String mobile_no1, mobile_no2;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager sManager = (SubscriptionManager) getApplicationContext().getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            String infoSim1 = sManager.getActiveSubscriptionInfoForSimSlotIndex(0).getNumber();
            String infoSim2 = sManager.getActiveSubscriptionInfoForSimSlotIndex(1).getNumber();
            if (infoSim1 != null || infoSim2 != null) {
                mobile_no1 = String.valueOf(infoSim1);
                mobile_no2 = String.valueOf(infoSim2);
                callMobileNo(mobile_no1, mobile_no2);
            }
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager.getSimSerialNumber() != null) {
                mobile_no1 = telephonyManager.getLine1Number();
                callMobileNo(mobile_no1, "");
            }
        }
    }

    private void callMobileNo(final String mobile_no1, final String mobile_no2) {
        final Dialog dialog_contact = new Dialog(LoginActivity.this);
        dialog_contact.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_contact.setContentView(R.layout.dialog_contact);
        final TextView textView_mobileNo1 = dialog_contact.findViewById(R.id.textView_mobileNo1);
        final TextView textView_mobileNo2 = dialog_contact.findViewById(R.id.textView_mobileNo2);
        TextView textClose = dialog_contact.findViewById(R.id.textClose);

        dialog_contact.setCancelable(false);
        dialog_contact.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog_contact.show();

        Window window = dialog_contact.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        final String lastTenDigits1, lastTenDigits2;
        if (mobile_no1.length() > 10) {
            lastTenDigits1 = mobile_no1.substring(mobile_no1.length() - 10);
        } else {
            lastTenDigits1 = mobile_no1;
        }

        if (mobile_no2.length() > 10) {
            lastTenDigits2 = mobile_no2.substring(mobile_no2.length() - 10);
        } else {
            lastTenDigits2 = mobile_no2;
        }

        if (lastTenDigits2.equalsIgnoreCase("")) {
            textView_mobileNo2.setVisibility(View.GONE);
        }

        textView_mobileNo1.setText(lastTenDigits1);
        textView_mobileNo2.setText(lastTenDigits2);

        textView_mobileNo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_username.setText(lastTenDigits1);
                dialog_contact.dismiss();
                edit_password.requestFocus();

            }
        });
        textView_mobileNo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_username.setText(lastTenDigits2);
                dialog_contact.dismiss();
                edit_password.requestFocus();
            }
        });
        textClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_contact.dismiss();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class CheckLogin extends AsyncTask<String, String, String> {

        String msg = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            showProgressDialog.showDialog();
        }

        @Override
        protected void onPostExecute(String message) {
            showProgressDialog.dismissDialog();
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String username = edit_username.getText().toString();
            String password = edit_password.getText().toString();
            if (username.trim().equals("") || password.trim().equals(""))
                msg = "Please enter Username and Password";
            else {
                try {
                    Connection connection = connectionCommon.connectionDatabase();
                    if (connection == null) {
                        msg = "Check Your Internet Access!";
                    } else {
                        String query = "select * from UACCESS where USER_NAME='" + username + "' and USER_PASS='" + password + "'";
                        Statement stmt = connection.createStatement();
                        ResultSet resultSet = stmt.executeQuery(query);
                        if (resultSet.next()) {
                            String user_id = String.valueOf(resultSet.getInt("USER_ID"));
                            String user_name = String.valueOf(resultSet.getString("USER_NAME"));
                            String user_pass = String.valueOf(resultSet.getString("USER_PASS"));
                            String db_name = String.valueOf(resultSet.getString("DB_NAME"));
                            String db_user_name = String.valueOf(resultSet.getString("DB_USER_NAME"));
                            String db_user_pass = String.valueOf(resultSet.getString("DB_USER_PASS"));
                            String user_type = String.valueOf(resultSet.getString("USER_TYPE"));
                            String SALESMAN_ID = String.valueOf(resultSet.getString("SALESMAN_ID"));
                            String SALESMAN_NAME = String.valueOf(resultSet.getString("SALESMAN_NAME"));
                            String INCLU_EXCLU = String.valueOf(resultSet.getString("INCLU_EXCLU"));
                            UserProfileModel userProfileModel = new UserProfileModel(user_id, user_name, user_pass, db_name, db_user_name, db_user_pass, user_type, SALESMAN_ID, SALESMAN_NAME,INCLU_EXCLU);
                            userProfileModelArrayList.add(userProfileModel);
                            Connection connection1 = connectionCommon.checkUserConnection(db_name);
                            if (connection1 != null) {
                                SharedLoginUtils.putLoginSharedUtils(LoginActivity.this);
                                SharedLoginUtils.addUserProfile(LoginActivity.this, userProfileModelArrayList);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                                msg = "Login successful";
                                isSuccess = true;
                            } else {
                                msg = "Invalid Credentials!";
                                isSuccess = false;
                            }
                            connection.close();
                        } else {
                            msg = "Invalid Credentials!";
                            isSuccess = false;
                            connection.close();
                        }
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    msg = ex.getMessage();
                }
            }
            return msg;
        }
    }
}

