package com.mezyapps.new_reportanalyst.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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
import com.mezyapps.new_reportanalyst.utils.ShowProgressDialog;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ImportExportFragment extends Fragment {

    private Context mContext;
    private TextView text_import_database;
    private ShowProgressDialog showProgressDialog;
    private ConnectionCommon connectionCommon;
    private DatabaseHandler databaseHandler;
    private TextView text_product_table,text_groups_per,text_sales_table,text_sales_table_DT;
    private TextView text_count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_import_export, container, false);

        mContext = getActivity();
        find_View_IdS(view);
        events();
        return view;
    }

    private void find_View_IdS(View view) {
        text_import_database = view.findViewById(R.id.text_import_database);
        text_groups_per = view.findViewById(R.id.text_groups_per);
        text_count = view.findViewById(R.id.text_count);
        text_product_table = view.findViewById(R.id.text_product_table);
        text_sales_table = view.findViewById(R.id.text_sales_table);
        text_sales_table_DT = view.findViewById(R.id.text_sales_table_DT);
        connectionCommon = new ConnectionCommon(mContext);
        showProgressDialog = new ShowProgressDialog(mContext);
        databaseHandler = new DatabaseHandler(mContext);
    }

    private void events() {
        text_import_database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHandler.deleteAllTable();
                ImportDatabase importDatabase = new ImportDatabase();
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

        @SuppressLint("SetTextI18n")
        @Override
        protected String doInBackground(String... strings) {

            try {
                Connection connection = connectionCommon.connectionDatabase();
                if (connection == null) {
                    msg = "Check Your Internet Access!";
                } else {

                    //Import Product Table
                    text_product_table.setText("Product Importing In Progress...");
                    boolean proudctTableResult = importProdcutTable(connection);
                    if (proudctTableResult) {
                        text_product_table.setText("Import Product Table Successfully");
                    } else {
                        text_product_table.setText("Import Product Failed");
                        text_product_table.setTextColor(getResources().getColor(R.color.red));
                    }
                    //End Import Product Table

                    //Import Group Per Table
                    text_groups_per.setText("Groups Per Table Import In Progress");
                    boolean groupsper=importGroupsPer(connection);
                    if (groupsper) {
                        text_groups_per.setText("Import Group Per Table Successfully");
                    } else {
                        text_groups_per.setText("Import Group Per Failed");
                        text_groups_per.setTextColor(getResources().getColor(R.color.red));
                    }
                    //End Import Group Per Table

                    text_sales_table.setText("Sales Table HD Import In progress");
                    boolean salesTableHD=importSalesTable(connection);
                    if (salesTableHD) {
                        text_sales_table.setText("Import Sales HD Table Successfully");
                    } else {
                        text_sales_table.setText("Import Sales HD  Failed");
                        text_sales_table.setTextColor(getResources().getColor(R.color.red));
                    }

                    text_sales_table_DT.setText("Sales Table HD Import In progress");
                    boolean salesTableDT=importSalesTableDT(connection);
                    if (salesTableDT) {
                        isSuccess = true;
                        text_sales_table_DT.setText("Import Sales DT Table Successfully");
                        msg = "Import Table Successfully";
                    } else {
                        isSuccess = true;
                        text_sales_table_DT.setText("Import Sales DT  Failed");
                        text_sales_table_DT.setTextColor(getResources().getColor(R.color.red));
                        msg = "Cannot Insert Data";
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

    //Import All Table Methods
    private boolean importProdcutTable(Connection connection) {
        boolean proudctTableResult = false;
        int count=1;
        try {
            String query =
                    " SELECT PM.PRODID, PM.PMSTCODE, PM.PMSTNAME, CT.CATEGORYNAME, PM.PRODPKGINCASE, " +
                            " PM.PACKING, UN.UNITNAME, PM.COSTPRICE, PM.SALERATE1 AS SALERATE, PM.MRP, HSN.HSNCODE, HSN.IGST_PER AS GST " +
                            " FROM PMST AS PM INNER JOIN CATEGORYMASTER AS CT ON PM.CATEGORYID=CT.CATEGORYID " +
                            " INNER JOIN UNITMASTER AS UN ON PM.PURCPACKING=UN.UNITID " +
                            " INNER JOIN HSNCODE_MASTER AS HSN ON PM.HSNCODEID=HSN.HSNCODEID ";

            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            proudctTableResult=databaseHandler.insertTable(resultSet,"PMST");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (proudctTableResult) {
            return true;
        } else {
            return false;
        }
    }

    private boolean importGroupsPer(Connection connection) {
        boolean groupPerTable = false;
        try {
            String query = " SELECT GP.GROUPID, GP.GROUPCODE, GP.GROUPNAME, GP.ADD1, GP.ADD2, GP.ADD3, GP.ADD4, GP.GRP_INFO, " +
                            " AR.AREANAME, ST.STATE_NAME, GP.LBTNO AS GSTNO, GP.MOBILENO, GP.E_MAIL, SMN.GROUPCODE AS SALESMAN " +
                            " FROM GROUPS_PER AS GP INNER JOIN AREAMASTER AS AR ON GP.AREAID=AR.AREAID " +
                            " INNER JOIN STATE_MASTER AS ST ON GP.STATEID=ST.STATEID " +
                            " INNER JOIN GROUPS_PER AS SMN ON GP.SALESMAN_ID=SMN.GROUPID " +
                            " WHERE (GP.GRP_INFO ='SUND_DR' OR GP.GRP_INFO ='SUND_CR' OR GP.GRP_INFO ='CASH_AC') ";

            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            groupPerTable=databaseHandler.insertTable(resultSet,"GROUPS_PER");

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (groupPerTable) {
            return true;
        } else {
            return false;
        }
    }

    private boolean importSalesTable(Connection connection) {
        boolean salesTable = false;
        try {
            String query = "SELECT ENTRYID,TRANSTYP_ID,PREFIXID,PREFIXNO,VCHNO,VCHDT,VCHDT_Y_M_D,GROUPID,GROUPNAME,TOTALCASE,TOTALQTY,TOTALGROSSAMT," +
                    "TOTAL_TD_AMT,TOTAL_SP_AMT,TOTALNETAMT,TOTALCGST_AMT,TOTALSGST_AMT,TOTALIGST_AMT,TOTALFINALAMT,TOTALBILLAMT,NARRATION FROM TBL_SALE_HD";

            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            salesTable = databaseHandler.insertTable(resultSet,"TBL_SALE_HD");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (salesTable) {
            return true;
        } else {
            return false;
        }
    }
    private boolean importSalesTableDT(Connection connection) {
        boolean salesTableDT = false;
        try {
            String query = "SELECT ENTRYID,TRANSTYP_ID,PRODID,PMSTCODE,PMSTNAME,PRODCASE,PRODPKGINCASE,PRODQTY,COSTRATE,UNITID,UNITNAME,PRODGROSSAMT,TD_PER," +
                    "TD_AMT,SP_PER,SP_AMT,PRODNETAMT,CGST_PER,CGST_AMT,SGST_PER,SGST_AMT,IGST_PER,IGST_AMT,FINAL_AMT FROM TBL_SALE_DT";

            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            salesTableDT = databaseHandler.insertTable(resultSet,"TBL_SALE_DT");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (salesTableDT) {
            return true;
        } else {
            return false;
        }
    }



}
