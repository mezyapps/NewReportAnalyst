package com.mezyapps.new_reportanalyst.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName ="ProductTable")
public class ProductTableModel {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "PRODID")
    private  String PRODID;

    @ColumnInfo(name = "PMSTCODE")
    private  String PMSTCODE;

    @ColumnInfo(name = "PMSTNAME")
    private  String PMSTNAME;

    @ColumnInfo(name = "CATEGORYID")
    private  String CATEGORYID;

    @ColumnInfo(name = "CATEGORYNAME")
    private  String CATEGORYNAME;

    @ColumnInfo(name = "PGMSTID")
    private  String PGMSTID;

    @ColumnInfo(name = "PGMSTNAME")
    private  String PGMSTNAME;

    @ColumnInfo(name = "UNITID")
    private  String UNITID;

    @ColumnInfo(name = "UNITNAME")
    private  String UNITNAME;

    @ColumnInfo(name = "PRODPKGINCASE")
    private  String PRODPKGINCASE;

    @ColumnInfo(name = "SALERATE1")
    private  String SALERATE1;

    @ColumnInfo(name = "SALERATE2")
    private  String SALERATE2;

    @ColumnInfo(name = "SALERATE3")
    private  String SALERATE3;

    @ColumnInfo(name = "SALERATE4")
    private  String SALERATE4;

    @ColumnInfo(name = "MRP")
    private  String MRP;

    @ColumnInfo(name = "PACKING")
    private  String PACKING;

    @ColumnInfo(name = "HSNCODEID")
    private  String HSNCODEID;

    @ColumnInfo(name = "HSNCD")
    private  String HSNCD;

    @ColumnInfo(name = "IGST_PER")
    private  String IGST_PER;

    @ColumnInfo(name = "CGST_PER")
    private  String CGST_PER;

    @ColumnInfo(name = "SGST_PER")
    private  String SGST_PER;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPRODID() {
        return PRODID;
    }

    public void setPRODID(String PRODID) {
        this.PRODID = PRODID;
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

    public String getCATEGORYID() {
        return CATEGORYID;
    }

    public void setCATEGORYID(String CATEGORYID) {
        this.CATEGORYID = CATEGORYID;
    }

    public String getCATEGORYNAME() {
        return CATEGORYNAME;
    }

    public void setCATEGORYNAME(String CATEGORYNAME) {
        this.CATEGORYNAME = CATEGORYNAME;
    }

    public String getPGMSTID() {
        return PGMSTID;
    }

    public void setPGMSTID(String PGMSTID) {
        this.PGMSTID = PGMSTID;
    }

    public String getPGMSTNAME() {
        return PGMSTNAME;
    }

    public void setPGMSTNAME(String PGMSTNAME) {
        this.PGMSTNAME = PGMSTNAME;
    }

    public String getUNITID() {
        return UNITID;
    }

    public void setUNITID(String UNITID) {
        this.UNITID = UNITID;
    }

    public String getUNITNAME() {
        return UNITNAME;
    }

    public void setUNITNAME(String UNITNAME) {
        this.UNITNAME = UNITNAME;
    }

    public String getPRODPKGINCASE() {
        return PRODPKGINCASE;
    }

    public void setPRODPKGINCASE(String PRODPKGINCASE) {
        this.PRODPKGINCASE = PRODPKGINCASE;
    }

    public String getSALERATE1() {
        return SALERATE1;
    }

    public void setSALERATE1(String SALERATE1) {
        this.SALERATE1 = SALERATE1;
    }

    public String getSALERATE2() {
        return SALERATE2;
    }

    public void setSALERATE2(String SALERATE2) {
        this.SALERATE2 = SALERATE2;
    }

    public String getSALERATE3() {
        return SALERATE3;
    }

    public void setSALERATE3(String SALERATE3) {
        this.SALERATE3 = SALERATE3;
    }

    public String getSALERATE4() {
        return SALERATE4;
    }

    public void setSALERATE4(String SALERATE4) {
        this.SALERATE4 = SALERATE4;
    }

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public String getPACKING() {
        return PACKING;
    }

    public void setPACKING(String PACKING) {
        this.PACKING = PACKING;
    }

    public String getHSNCODEID() {
        return HSNCODEID;
    }

    public void setHSNCODEID(String HSNCODEID) {
        this.HSNCODEID = HSNCODEID;
    }

    public String getHSNCD() {
        return HSNCD;
    }

    public void setHSNCD(String HSNCD) {
        this.HSNCD = HSNCD;
    }

    public String getIGST_PER() {
        return IGST_PER;
    }

    public void setIGST_PER(String IGST_PER) {
        this.IGST_PER = IGST_PER;
    }

    public String getCGST_PER() {
        return CGST_PER;
    }

    public void setCGST_PER(String CGST_PER) {
        this.CGST_PER = CGST_PER;
    }

    public String getSGST_PER() {
        return SGST_PER;
    }

    public void setSGST_PER(String SGST_PER) {
        this.SGST_PER = SGST_PER;
    }
}
