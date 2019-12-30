package com.mezyapps.new_reportanalyst.database;

public class DatabaseConstant {

    public static final String DATABASE_NAME = "REPORTANALYST";
    public static final int DATABASE_VERSION = 1;

    public class ProductMaster {
        public static final String TABLE_NAME = "PMST";
        public static final String PRODID = "PRODID";
        public static final String PMSTCODE = "PMSTCODE";
        public static final String PMSTNAME = "PMSTNAME";
        public static final String CATEGORYNAME = "CATEGORYNAME";
        public static final String PRODPKGINCASE = "PRODPKGINCASE";
        public static final String PACKING = "PACKING";
        public static final String UNITNAME = "UNITNAME";
        public static final String COSTPRICE = "COSTPRICE";
        public static final String SALERATE = "SALERATE";
        public static final String MRP = "MRP";
        public static final String HSNCODE = "HSNCODE";
        public static final String GST = "GST";

        public static final String CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + PRODID + " INTEGER,"
                + PMSTCODE + " TEXT," + PMSTNAME + " TEXT," + CATEGORYNAME + " TEXT," + PRODPKGINCASE + " INTEGER,"
                + PACKING + " TEXT," + UNITNAME + " TEXT," + COSTPRICE + " REAL," + SALERATE + " REAL," + MRP + " REAL,"
                + HSNCODE + " TEXT," + GST + " REAL" + ")";

    }

    public class GroupPer {

            public static final String GROUP_TABLE="GROUPS_PER";
            public static final String GROUPID="GROUPID";
            public static final String GROUPCODE="GROUPCODE";
            public static final String GROUPNAME="GROUPNAME";
            public static final String ADD1="ADD1";
            public static final String ADD2="ADD2";
            public static final String ADD3="ADD3";
            public static final String ADD4="ADD4";
            public static final String GRP_INFO="GRP_INFO";
            public static final String AREANAME="AREANAME";
            public static final String STATE_NAME="STATE_NAME";
            public static final String GSTNO="GSTNO";
            public static final String MOBILENO="MOBILENO";
            public static final String E_MAIL="E_MAIL";
            public static final String SALESMAN="SALESMAN";

            public static final String CREATE_GROUPS_PER_TABLE = "CREATE TABLE " + GROUP_TABLE + "(" + GROUPID + " INTEGER,"
                + GROUPCODE + " TEXT," + GROUPNAME + " TEXT," + ADD1 + " TEXT," + ADD2 + " TEXT,"
                + ADD3 + " TEXT," + ADD4 + " TEXT," + GRP_INFO + " TEXT," + AREANAME + " TEXT," + STATE_NAME + " TEXT,"
                + GSTNO + " TEXT," + MOBILENO + " TEXT," + E_MAIL + " TEXT," + SALESMAN + " TEXT" + ")";

    }
    public class SalesTable
    {
        public static final String SALES_TABLE="TBL_SALE_HD";
        public static final String ENTRYID="ENTRYID";
        public static final String TRANSTYP_ID="TRANSTYP_ID";
        public static final String GROUPNAME="GROUPNAME";
        public static final String PREFIXID="PREFIXID";
        public static final String PREFIXNO="PREFIXNO";
        public static final String VCHNO="VCHNO";
        public static final String VCHDT="VCHDT";
        public static final String VCHDT_Y_M_D="VCHDT_Y_M_D";
        public static final String GROUPID="GROUPID";
        public static final String TOTALCASE="TOTALCASE";
        public static final String TOTALQTY="TOTALQTY";
        public static final String TOTALGROSSAMT="TOTALGROSSAMT";
        public static final String TOTAL_TD_AMT="TOTAL_TD_AMT";
        public static final String TOTAL_SP_AMT="TOTAL_SP_AMT";
        public static final String TOTALNETAMT="TOTALNETAMT";
        public static final String TOTALCGST_AMT="TOTALCGST_AMT";
        public static final String TOTALSGST_AMT="TOTALSGST_AMT";
        public static final String TOTALIGST_AMT="TOTALIGST_AMT";
        public static final String TOTALFINALAMT="TOTALFINALAMT";
        public static final String TOTALBILLAMT="TOTALBILLAMT";
        public static final String NARRATION="NARRATION";

        public static final String CREATE_SALES_TABLE = "CREATE TABLE " +SALES_TABLE + "(" + ENTRYID + " INTEGER,"
                + TRANSTYP_ID + " TEXT," + PREFIXID + " TEXT," + PREFIXNO + " TEXT," + VCHNO + " TEXT,"
                + VCHDT + " TEXT," + VCHDT_Y_M_D + " TEXT," + GROUPID + " TEXT," + GROUPNAME + " TEXT," + TOTALCASE + " TEXT,"
                + TOTALQTY + " TEXT," + TOTALGROSSAMT + " TEXT," + TOTAL_TD_AMT + " TEXT," + TOTAL_SP_AMT + " TEXT,"
                +TOTALNETAMT + " TEXT,"+TOTALCGST_AMT + " TEXT,"+TOTALSGST_AMT + " TEXT,"+TOTALIGST_AMT + " TEXT,"
                +TOTALFINALAMT + " TEXT,"+TOTALBILLAMT + " TEXT,"+NARRATION + " TEXT"+")";
    }
    public class SalesDetails
    {
        public static final String SALES_DETAILS_TABLE="TBL_SALE_DT";
        public static final String ENTRYID="ENTRYID";
        public static final String TRANSTYP_ID="TRANSTYP_ID";
        public static final String PRODID="PRODID";
        public static final String PMSTCODE="PMSTCODE";
        public static final String PMSTNAME="PMSTNAME";
        public static final String PRODCASE="PRODCASE";
        public static final String PRODPKGINCASE="PRODPKGINCASE";
        public static final String PRODQTY="PRODQTY";
        public static final String COSTRATE="COSTRATE";
        public static final String UNITID="UNITID";
        public static final String UNITNAME="UNITNAME";
        public static final String PRODGROSSAMT="PRODGROSSAMT";
        public static final String TD_PER="TD_PER";
        public static final String TD_AMT="TD_AMT";
        public static final String SP_PER="SP_PER";
        public static final String SP_AMT="SP_AMT";
        public static final String PRODNETAMT="PRODNETAMT";
        public static final String CGST_PER="CGST_PER";
        public static final String CGST_AMT="CGST_AMT";
        public static final String SGST_PER="SGST_PER";
        public static final String SGST_AMT="SGST_AMT";
        public static final String IGST_PER="IGST_PER";
        public static final String IGST_AMT="IGST_AMT";
        public static final String FINAL_AMT="FINAL_AMT";


        public static final String CREATE_SALES_DETAILS = "CREATE TABLE " +SALES_DETAILS_TABLE + "(" + ENTRYID + " INTEGER,"
                + TRANSTYP_ID + " TEXT," + PRODID + " TEXT," + PMSTCODE + " TEXT," + PMSTNAME + " TEXT,"
                + PRODCASE + " TEXT," + PRODPKGINCASE + " TEXT," + PRODQTY + " TEXT," + COSTRATE + " TEXT," + UNITID + " TEXT,"
                + UNITNAME + " TEXT," + PRODGROSSAMT + " TEXT," + TD_PER + " TEXT," + TD_AMT + " TEXT,"
                + SP_PER + " TEXT,"+SP_AMT + " TEXT,"+PRODNETAMT + " TEXT,"+CGST_PER + " TEXT,"
                + CGST_AMT + " TEXT,"+SGST_PER + " TEXT,"+SGST_AMT + " TEXT,"+IGST_PER + " TEXT,"+IGST_AMT + " TEXT,"+FINAL_AMT + " TEXT"+")";
    }

    public class PurchaseTableHD
    {
        public static final String PURCHASE_TABLE="TBL_PURCH_HD";
        public static final String ENTRYID="ENTRYID";
        public static final String TRANSTYP_ID="TRANSTYP_ID";
        public static final String GROUPNAME="GROUPNAME";
        public static final String PREFIXID="PREFIXID";
        public static final String PREFIXNO="PREFIXNO";
        public static final String VCHNO="VCHNO";
        public static final String VCHDT="VCHDT";
        public static final String VCHDT_Y_M_D="VCHDT_Y_M_D";
        public static final String GROUPID="GROUPID";
        public static final String TOTALCASE="TOTALCASE";
        public static final String TOTALQTY="TOTALQTY";
        public static final String TOTALGROSSAMT="TOTALGROSSAMT";
        public static final String TOTAL_TD_AMT="TOTAL_TD_AMT";
        public static final String TOTAL_SP_AMT="TOTAL_SP_AMT";
        public static final String TOTALNETAMT="TOTALNETAMT";
        public static final String TOTALCGST_AMT="TOTALCGST_AMT";
        public static final String TOTALSGST_AMT="TOTALSGST_AMT";
        public static final String TOTALIGST_AMT="TOTALIGST_AMT";
        public static final String TOTALFINALAMT="TOTALFINALAMT";
        public static final String TOTALBILLAMT="TOTALBILLAMT";
        public static final String NARRATION="NARRATION";

        public static final String CREATE_PURCHASE_TABLE = "CREATE TABLE " +PURCHASE_TABLE + "(" + ENTRYID + " INTEGER,"
                + TRANSTYP_ID + " TEXT," + PREFIXID + " TEXT," + PREFIXNO + " TEXT," + VCHNO + " TEXT,"
                + VCHDT + " TEXT," + VCHDT_Y_M_D + " TEXT," + GROUPID + " TEXT," + GROUPNAME + " TEXT," + TOTALCASE + " TEXT,"
                + TOTALQTY + " TEXT," + TOTALGROSSAMT + " TEXT," + TOTAL_TD_AMT + " TEXT," + TOTAL_SP_AMT + " TEXT,"
                +TOTALNETAMT + " TEXT,"+TOTALCGST_AMT + " TEXT,"+TOTALSGST_AMT + " TEXT,"+TOTALIGST_AMT + " TEXT,"
                +TOTALFINALAMT + " TEXT,"+TOTALBILLAMT + " TEXT,"+NARRATION + " TEXT"+")";
    }
    public class PurchaseDetails
    {
        public static final String PURCHASE_DETAILS_TABLE="TBL_PURCH_DT";
        public static final String ENTRYID="ENTRYID";
        public static final String TRANSTYP_ID="TRANSTYP_ID";
        public static final String PRODID="PRODID";
        public static final String PMSTCODE="PMSTCODE";
        public static final String PMSTNAME="PMSTNAME";
        public static final String PRODCASE="PRODCASE";
        public static final String PRODPKGINCASE="PRODPKGINCASE";
        public static final String PRODQTY="PRODQTY";
        public static final String COSTRATE="COSTRATE";
        public static final String UNITID="UNITID";
        public static final String UNITNAME="UNITNAME";
        public static final String PRODGROSSAMT="PRODGROSSAMT";
        public static final String TD_PER="TD_PER";
        public static final String TD_AMT="TD_AMT";
        public static final String SP_PER="SP_PER";
        public static final String SP_AMT="SP_AMT";
        public static final String PRODNETAMT="PRODNETAMT";
        public static final String CGST_PER="CGST_PER";
        public static final String CGST_AMT="CGST_AMT";
        public static final String SGST_PER="SGST_PER";
        public static final String SGST_AMT="SGST_AMT";
        public static final String IGST_PER="IGST_PER";
        public static final String IGST_AMT="IGST_AMT";
        public static final String FINAL_AMT="FINAL_AMT";


        public static final String CREATE_PURCHASE_DETAILS = "CREATE TABLE " +PURCHASE_DETAILS_TABLE + "(" + ENTRYID + " INTEGER,"
                + TRANSTYP_ID + " TEXT," + PRODID + " TEXT," + PMSTCODE + " TEXT," + PMSTNAME + " TEXT,"
                + PRODCASE + " TEXT," + PRODPKGINCASE + " TEXT," + PRODQTY + " TEXT," + COSTRATE + " TEXT," + UNITID + " TEXT,"
                + UNITNAME + " TEXT," + PRODGROSSAMT + " TEXT," + TD_PER + " TEXT," + TD_AMT + " TEXT,"
                + SP_PER + " TEXT,"+SP_AMT + " TEXT,"+PRODNETAMT + " TEXT,"+CGST_PER + " TEXT,"
                + CGST_AMT + " TEXT,"+SGST_PER + " TEXT,"+SGST_AMT + " TEXT,"+IGST_PER + " TEXT,"+IGST_AMT + " TEXT,"+FINAL_AMT + " TEXT"+")";
    }
}
