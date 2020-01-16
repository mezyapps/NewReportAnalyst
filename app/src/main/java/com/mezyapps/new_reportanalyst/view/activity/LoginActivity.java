package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private ArrayList<UserProfileModel> userProfileModelArrayList=new ArrayList<>();

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

    private void events() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckLogin checkLogin = new CheckLogin();
                checkLogin.execute("");
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
                            UserProfileModel userProfileModel=new UserProfileModel(user_id,user_name,user_pass,db_name,db_user_name,db_user_pass,user_type,SALESMAN_ID);
                            userProfileModelArrayList.add(userProfileModel);
                            Connection connection1=connectionCommon.checkUserConnection(db_name);
                            if (connection1!=null) {
                                SharedLoginUtils.putLoginSharedUtils(LoginActivity.this);
                                SharedLoginUtils.addUserProfile(LoginActivity.this, userProfileModelArrayList);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                                msg = "Login successful";
                                isSuccess = true;
                            }
                            else
                            {
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

