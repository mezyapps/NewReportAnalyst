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
import com.mezyapps.new_reportanalyst.utils.DatabaseConfiguration;
import com.mezyapps.new_reportanalyst.utils.SharedLoginUtils;
import com.mezyapps.new_reportanalyst.utils.ShowProgressDialog;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private TextInputEditText edit_username, edit_password;
    private ShowProgressDialog showProgressDialog;
    private ConnectionCommon connectionCommon;

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
                        String query = "select USERID from UACCESS where USERNAME='" + username + "' and USERPASSWORD='" + password + "'";
                        Statement stmt = connection.createStatement();
                        ResultSet resultSet = stmt.executeQuery(query);
                        if (resultSet.next()) {
                            String user_id = String.valueOf(resultSet.getInt("USERID"));
                            SharedLoginUtils.putLoginSharedUtils(LoginActivity.this);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                            msg = "Login successful";
                            isSuccess = true;
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

