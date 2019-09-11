package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.connection.ConnectionCommon;
import com.mezyapps.new_reportanalyst.model.DatabaseConfigModel;
import com.mezyapps.new_reportanalyst.utils.ConstantFields;
import com.mezyapps.new_reportanalyst.utils.DatabaseConfiguration;
import com.mezyapps.new_reportanalyst.utils.SharedLoginUtils;
import com.mezyapps.new_reportanalyst.utils.ShowProgressDialog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseConfigActivity extends AppCompatActivity {

    private TextInputEditText edit_ip_address, edit_database_name, edit_username, edit_password;
    private Button btn_save;
    private String ip_address,username,database,password;
    private Connection connection;
    private ConnectionCommon connectionCommon;
    private DatabaseConfigModel databaseConfigModel;
    private ArrayList<DatabaseConfigModel> databaseConfigModelArrayList=new ArrayList<>();
    private ShowProgressDialog showProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_config);

        find_View_IdS();
        events();
    }

    private void find_View_IdS() {
        edit_ip_address = findViewById(R.id.edit_ip_address);
        edit_database_name = findViewById(R.id.edit_database_name);
        edit_username = findViewById(R.id.edit_username);
        edit_password = findViewById(R.id.edit_password);
        btn_save = findViewById(R.id.btn_save);
        connectionCommon=new ConnectionCommon();
        showProgressDialog=new ShowProgressDialog(DatabaseConfigActivity.this);
    }
    private void events() {
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation())
                {
                    showProgressDialog.showDialog();
                    connection=connectionCommon.checkConnection(ip_address,database,username,password);
                    if(connection==null)
                    {
                        showProgressDialog.dismissDialog();
                        Toast.makeText(DatabaseConfigActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        showProgressDialog.dismissDialog();
                        databaseConfigModel=new DatabaseConfigModel(ip_address,username,database,password);
                        databaseConfigModelArrayList.add(databaseConfigModel);
                        Toast.makeText(DatabaseConfigActivity.this, "Connect Successfully", Toast.LENGTH_SHORT).show();
                        DatabaseConfiguration.addDatabaseConfig(DatabaseConfigActivity.this,databaseConfigModelArrayList);
                        DatabaseConfiguration.putDatabaseConfiguration(DatabaseConfigActivity.this);
                        startActivity(new Intent(DatabaseConfigActivity.this,LoginActivity.class));
                        finish();
                    }

                }
            }
        });
    }

    private boolean validation() {
        ip_address=edit_ip_address.getText().toString().trim();
        database=edit_database_name.getText().toString().trim();
        username=edit_username.getText().toString().trim();
        password=edit_password.getText().toString().trim();
        if(ip_address.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Enter IP Address", Toast.LENGTH_SHORT).show();
            return false;
        }else if (database.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Enter Database Name", Toast.LENGTH_SHORT).show();
            return false;
        }else if (username.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Enter Username Name", Toast.LENGTH_SHORT).show();
            return false;
        }else if (password.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
