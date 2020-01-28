package com.mezyapps.new_reportanalyst.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.connection.ConnectionCommon;
import com.mezyapps.new_reportanalyst.model.UserProfileModel;
import com.mezyapps.new_reportanalyst.utils.SharedLoginUtils;
import com.mezyapps.new_reportanalyst.utils.ShowProgressDialog;
import com.mezyapps.new_reportanalyst.view.activity.LoginActivity;
import com.mezyapps.new_reportanalyst.view.activity.OrderEnteryActivity;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;


public class ChancePasswordFragment extends Fragment {

    private Context mContext;
    private Button btn_update;
    private TextInputEditText  edit_password,edit_confirm_password;
    private ShowProgressDialog showProgressDialog;
    private ArrayList<UserProfileModel> userProfileModelArrayList = new ArrayList<>();
    private ConnectionCommon connectionCommon;
    private String user_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_chance_password, container, false);
        mContext=getActivity();
        find_View_IdS(view);
        events();

        return view;
    }

    private void find_View_IdS(View view) {
        edit_password =view.findViewById(R.id.edit_password);
        btn_update =view.findViewById(R.id.btn_update);
        edit_confirm_password =view.findViewById(R.id.edit_confirm_password);

        connectionCommon = new ConnectionCommon(mContext);
        showProgressDialog = new ShowProgressDialog(mContext);

        userProfileModelArrayList = SharedLoginUtils.getUserProfile(mContext);
        user_id = userProfileModelArrayList.get(0).getUser_id();
    }

    private void events() {
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoChange doChange = new DoChange();
                doChange.execute("");
            }
        });
    }
    public class DoChange extends AsyncTask<String,String,String>
    {
        String msg= "";
        Boolean isSuccess = false;
        String password=edit_confirm_password.getText().toString().trim();
        String confirm_password=edit_confirm_password.getText().toString().trim();


        @Override
        protected void onPreExecute() {
        showProgressDialog.showDialog();
        }

        @Override
        protected void onPostExecute(String msg) {
            showProgressDialog.dismissDialog();
            Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();

            if(isSuccess) {
                Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            if(password.trim().equals("") || confirm_password.trim().equals("")){
                msg = "Please enter Password";
            }else if(!password.equals(confirm_password))
            {
                msg = "Password Not Match";
            }else {
                if (password.equals(confirm_password)){
                    try {
                        Connection con = connectionCommon.connectionDatabase();
                        if (con == null) {
                            msg = "Error in connection with SQL server";
                        } else {
                            String query = "UPDATE UACCESS SET  USER_PASS='" + password + "' WHERE  USER_ID='" + user_id + "' ";
                            Statement stmt = con.createStatement();
                            int rs=stmt.executeUpdate(query);
                            if(rs == 1)
                            {
                                msg = "Successfully";
                                isSuccess=true;
                            }
                            else
                            {
                                msg = "Invalid Username";
                                isSuccess = false;
                            }

                        }
                    }
                    catch (Exception ex)
                    {
                        msg = "Invalid Username";
                        isSuccess = false;
                    }

                } else {
                    msg = "Password Not Match";
                }
            }
            return msg;
        }

    }
}
