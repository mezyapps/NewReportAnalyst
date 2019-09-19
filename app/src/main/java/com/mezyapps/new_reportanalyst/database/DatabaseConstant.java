package com.mezyapps.new_reportanalyst.database;

public class DatabaseConstant {

    public static final String DATABASE_NAME="REPORTANALYST";
    public static final int DATABASE_VERSION = 1;

    public class ProductMaster
    {
        public static final String TABLE_NAME="PMST";
        public static final String PRODID="PRODID";
        public static final String PMSTCODE="PMSTCODE";
        public static final String PMSTNAME="PMSTNAME";
        public static final String CATEGORYNAME="CATEGORYNAME";
        public static final String PRODPKGINCASE="PRODPKGINCASE";
        public static final String PACKING="PACKING";
        public static final String UNITNAME="UNITNAME";
        public static final String COSTPRICE="COSTPRICE";
        public static final String SALERATE="SALERATE";
        public static final String MRP="MRP";
        public static final String HSNCODE="HSNCODE";
        public static final String GST="GST";

        public static final String CREATE_PRODUCT_TABLE="CREATE TABLE " +TABLE_NAME + "(" +PRODID +" INTEGER,"
                + PMSTCODE + " TEXT," +PMSTNAME + " TEXT," +CATEGORYNAME +" TEXT," +PRODPKGINCASE +" TEXT,"
                +PACKING + " TEXT," +UNITNAME +" TEXT," +COSTPRICE +" TEXT," +SALERATE +" TEXT," +MRP +" TEXT,"
                +HSNCODE +" TEXT," +GST +" TEXT" + ")";

    }
}
