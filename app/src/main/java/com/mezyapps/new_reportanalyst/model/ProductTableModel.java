package com.mezyapps.new_reportanalyst.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName ="ProductTable")
public class ProductTableModel {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "prod_id")
    private String PRODID;

    @ColumnInfo(name = "pmst_code")
    private String PMSTCODE;

    @ColumnInfo(name = "pmst_name")
    private String PMSTNAME;

    @ColumnInfo(name = "categoryname")
    private String CATEGORYNAME;

    @ColumnInfo(name = "prodpkgincase")
    private String PRODPKGINCASE;

    @ColumnInfo(name = "packing")
    private String PACKING;

    @ColumnInfo(name = "unitname")
    private String UNITNAME;

    @ColumnInfo(name = "costprice")
    private String COSTPRICE;

    @ColumnInfo(name = "salerate")
    private String SALERATE;

    @ColumnInfo(name = "mrp")
    private String MRP;

    @ColumnInfo(name = "hsncode")
    private String HSNCODE;

    @ColumnInfo(name = "gst")
    private String GST;

    public String getPRODID() {
        return PRODID;
    }

    public void setPRODID(String PRODID) {
        this.PRODID = PRODID;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPMSTCODE() {
        return PMSTCODE;
    }

    public void setPMSTCODE(String PMSTCODE) {
        this.PMSTCODE = PMSTCODE;
    }

    public String getPMSTNAME() {
        return PMSTNAME;
    }

    public void setPMSTNAME(String PMSTNAME) {
        this.PMSTNAME = PMSTNAME;
    }

    public String getCATEGORYNAME() {
        return CATEGORYNAME;
    }

    public void setCATEGORYNAME(String CATEGORYNAME) {
        this.CATEGORYNAME = CATEGORYNAME;
    }

    public String getPRODPKGINCASE() {
        return PRODPKGINCASE;
    }

    public void setPRODPKGINCASE(String PRODPKGINCASE) {
        this.PRODPKGINCASE = PRODPKGINCASE;
    }

    public String getPACKING() {
        return PACKING;
    }

    public void setPACKING(String PACKING) {
        this.PACKING = PACKING;
    }

    public String getUNITNAME() {
        return UNITNAME;
    }

    public void setUNITNAME(String UNITNAME) {
        this.UNITNAME = UNITNAME;
    }

    public String getCOSTPRICE() {
        return COSTPRICE;
    }

    public void setCOSTPRICE(String COSTPRICE) {
        this.COSTPRICE = COSTPRICE;
    }

    public String getSALERATE() {
        return SALERATE;
    }

    public void setSALERATE(String SALERATE) {
        this.SALERATE = SALERATE;
    }

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public String getHSNCODE() {
        return HSNCODE;
    }

    public void setHSNCODE(String HSNCODE) {
        this.HSNCODE = HSNCODE;
    }

    public String getGST() {
        return GST;
    }

    public long getId() {
        return id;
    }

    public void setGST(String GST) {
        this.GST = GST;
    }
}
