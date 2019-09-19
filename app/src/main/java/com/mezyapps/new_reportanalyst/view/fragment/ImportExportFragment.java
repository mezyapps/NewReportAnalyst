package com.mezyapps.new_reportanalyst.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.connection.ConnectionCommon;
import com.mezyapps.new_reportanalyst.database.DatabaseHandler;
import com.mezyapps.new_reportanalyst.utils.SharedLoginUtils;
import com.mezyapps.new_reportanalyst.utils.ShowProgressDialog;
import com.mezyapps.new_reportanalyst.view.activity.LoginActivity;
import com.mezyapps.new_reportanalyst.view.activity.MainActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class ImportExportFragment extends Fragment {

    private Context mContext;
    private TextView text_import_database;
    private ShowProgressDialog showProgressDialog;
    private ConnectionCommon connectionCommon;
    private DatabaseHandler databaseHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_import_export, container, false);

        mContext=getActivity();
        find_View_IdS(view);
        events();
        return view;
    }

    private void find_View_IdS(View view) {
        text_import_database=view.findViewById(R.id.text_import_database);
        connectionCommon = new ConnectionCommon(mContext);
        showProgressDialog = new ShowProgressDialog(mContext);
        databaseHandler=new DatabaseHandler(mContext);
    }

    private void events() {
        text_import_database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImportDatabase  importDatabase=new ImportDatabase();
                importDatabase.execute("");

            }
        });
    }
    public class ImportDatabase extends AsyncTask<String, String, String> {

        String msg = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            showProgressDialog.showDialog();
        }

        @Override
        protected void onPostExecute(String message) {
            showProgressDialog.dismissDialog();
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

                try {
                    Connection connection = connectionCommon.connectionDatabase();
                    if (connection == null) {
                        msg = "Check Your Internet Access!";
                    } else {
                        String query =
                                " SELECT PM.PRODID, PM.PMSTCODE, PM.PMSTNAME, CT.CATEGORYNAME, PM.PRODPKGINCASE, "+
                                " PM.PACKING, UN.UNITNAME, PM.COSTPRICE, PM.SALERATE1 AS SALERATE, PM.MRP, HSN.HSNCODE, HSN.IGST_PER AS GST "+
                                " FROM PMST AS PM INNER JOIN CATEGORYMASTER AS CT ON PM.CATEGORYID=CT.CATEGORYID " +
                                " INNER JOIN UNITMASTER AS UN ON PM.PURCPACKING=UN.UNITID " +
                                " INNER JOIN HSNCODE_MASTER AS HSN ON PM.HSNCODEID=HSN.HSNCODEID ";

                        Statement stmt = connection.createStatement();
                        ResultSet resultSet = stmt.executeQuery(query);
                        boolean result = false;
                        while (resultSet.next()) {
                            int product_id = resultSet.getInt("PRODID");
                            String product_code=resultSet.getString("PMSTCODE");
                            String product_name=resultSet.getString("PMSTNAME");
                            String category_name=resultSet.getString("CATEGORYNAME");
                            String product_pkg=resultSet.getString("PRODPKGINCASE");
                            String packing=resultSet.getString("PACKING");
                            String unit_name=resultSet.getString("UNITNAME");
                            String cost_price=resultSet.getString("COSTPRICE");
                            String sale_rate=resultSet.getString("SALERATE");
                            String mrp=resultSet.getString("MRP");
                            String hsn=resultSet.getString("HSNCODE");
                            String gst=resultSet.getString("GST");
                            result=databaseHandler.insertProductTable(product_id,product_code,product_name,category_name,product_pkg,packing,unit_name,cost_price,sale_rate,mrp,hsn,gst);
                        }
                        if(result)
                        {
                            isSuccess=true;
                            msg="Import Data Successfully";
                        }
                        else
                        {
                            isSuccess=true;
                            msg="Cannot Insert Data";
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
