package com.mezyapps.new_reportanalyst.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "GroupPer")
public class GroupPerModel {

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name="GROUPID")
    private String GROUPID;

    @ColumnInfo(name="BALANCE")
    private String BALANCE;

    @ColumnInfo(name="GROUPCODE")
    private String GROUPCODE;

    @ColumnInfo(name="GROUPNAME")
    private String GROUPNAME;

    @ColumnInfo(name="ADD1")
    private String ADD1;

    @ColumnInfo(name="ADD2")
    private String ADD2;

    @ColumnInfo(name="ADD3")
    private String ADD3;

    @ColumnInfo(name="ADD4")
    private String ADD4;

    @ColumnInfo(name="AREANAME")
    private String AREANAME;

    @ColumnInfo(name="CITYNAME")
    private String CITYNAME;

    @ColumnInfo(name="STATEID")
    private String STATEID;

    @ColumnInfo(name="STATE_CODE")
    private String STATE_CODE;

    @ColumnInfo(name="STATE_NAME")
    private String STATE_NAME;

    @ColumnInfo(name="GSTNO")
    private String GSTNO;

    @ColumnInfo(name="TELNO_O")
    private String TELNO_O;

    @ColumnInfo(name="TELNO_R")
    private String TELNO_R;

    @ColumnInfo(name="MOBILENO")
    private String MOBILENO;

    @ColumnInfo(name="SALESMAN_ID")
    private String SALESMAN_ID;

    @ColumnInfo(name="SALESMAN_NAME")
    private String SALESMAN_NAME;

    @ColumnInfo(name="GST_TYPE_ID")
    private String GST_TYPE_ID;

    @ColumnInfo(name="GST_TYPE")
    private String GST_TYPE;

    @ColumnInfo(name="DISC1")
    private String DISC1;

    @ColumnInfo(name="DISC2")
    private String DISC2;

    @ColumnInfo(name="SP_NAME")
    private String SP_NAME;


    public String getGROUPID() {
        return GROUPID;
    }

    public void setGROUPID(String GROUPID) {
        this.GROUPID = GROUPID;
    }

    public String getBALANCE() {
        return BALANCE;
    }

    public void setBALANCE(String BALANCE) {
        this.BALANCE = BALANCE;
    }

    public String getGROUPCODE() {
        return GROUPCODE;
    }

    public void setGROUPCODE(String GROUPCODE) {
        this.GROUPCODE = GROUPCODE;
    }

    public String getGROUPNAME() {
        return GROUPNAME;
    }

    public void setGROUPNAME(String GROUPNAME) {
        this.GROUPNAME = GROUPNAME;
    }

    public String getADD1() {
        return ADD1;
    }

    public void setADD1(String ADD1) {
        this.ADD1 = ADD1;
    }

    public String getADD2() {
        return ADD2;
    }

    public void setADD2(String ADD2) {
        this.ADD2 = ADD2;
    }

    public String getADD3() {
        return ADD3;
    }

    public void setADD3(String ADD3) {
        this.ADD3 = ADD3;
    }

    public String getADD4() {
        return ADD4;
    }

    public void setADD4(String ADD4) {
        this.ADD4 = ADD4;
    }

    public String getAREANAME() {
        return AREANAME;
    }

    public void setAREANAME(String AREANAME) {
        this.AREANAME = AREANAME;
    }

    public String getCITYNAME() {
        return CITYNAME;
    }

    public void setCITYNAME(String CITYNAME) {
        this.CITYNAME = CITYNAME;
    }

    public String getSTATEID() {
        return STATEID;
    }

    public void setSTATEID(String STATEID) {
        this.STATEID = STATEID;
    }

    public String getSTATE_CODE() {
        return STATE_CODE;
    }

    public void setSTATE_CODE(String STATE_CODE) {
        this.STATE_CODE = STATE_CODE;
    }

    public String getSTATE_NAME() {
        return STATE_NAME;
    }

    public void setSTATE_NAME(String STATE_NAME) {
        this.STATE_NAME = STATE_NAME;
    }

    public String getGSTNO() {
        return GSTNO;
    }

    public void setGSTNO(String GSTNO) {
        this.GSTNO = GSTNO;
    }

    public String getTELNO_O() {
        return TELNO_O;
    }

    public void setTELNO_O(String TELNO_O) {
        this.TELNO_O = TELNO_O;
    }

    public String getTELNO_R() {
        return TELNO_R;
    }

    public void setTELNO_R(String TELNO_R) {
        this.TELNO_R = TELNO_R;
    }

    public String getMOBILENO() {
        return MOBILENO;
    }

    public void setMOBILENO(String MOBILENO) {
        this.MOBILENO = MOBILENO;
    }

    public String getSALESMAN_ID() {
        return SALESMAN_ID;
    }

    public void setSALESMAN_ID(String SALESMAN_ID) {
        this.SALESMAN_ID = SALESMAN_ID;
    }

    public String getSALESMAN_NAME() {
        return SALESMAN_NAME;
    }

    public void setSALESMAN_NAME(String SALESMAN_NAME) {
        this.SALESMAN_NAME = SALESMAN_NAME;
    }

    public String getGST_TYPE_ID() {
        return GST_TYPE_ID;
    }

    public void setGST_TYPE_ID(String GST_TYPE_ID) {
        this.GST_TYPE_ID = GST_TYPE_ID;
    }

    public String getGST_TYPE() {
        return GST_TYPE;
    }

    public void setGST_TYPE(String GST_TYPE) {
        this.GST_TYPE = GST_TYPE;
    }

    public String getDISC1() {
        return DISC1;
    }

    public void setDISC1(String DISC1) {
        this.DISC1 = DISC1;
    }

    public String getDISC2() {
        return DISC2;
    }

    public void setDISC2(String DISC2) {
        this.DISC2 = DISC2;
    }

    public String getSP_NAME() {
        return SP_NAME;
    }

    public void setSP_NAME(String SP_NAME) {
        this.SP_NAME = SP_NAME;
    }
}
