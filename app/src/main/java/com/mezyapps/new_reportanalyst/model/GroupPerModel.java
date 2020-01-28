package com.mezyapps.new_reportanalyst.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName ="GroupPer")
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

    @ColumnInfo(name = "group_id")
    private String GROUPID;
    @ColumnInfo(name = "group_code")
    private String GROUPCODE;
    @ColumnInfo(name = "group_name")
    private String GROUPNAME;
    @ColumnInfo(name = "add1")
    private String ADD1;
    @ColumnInfo(name = "add2")
    private String ADD2;
    @ColumnInfo(name = "add3")
    private String ADD3;
    @ColumnInfo(name = "add4")
    private String ADD4;
    @ColumnInfo(name = "grp_info")
    private String GRP_INFO;
    @ColumnInfo(name = "area_name")
    private String AREANAME;
    @ColumnInfo(name = "state_name")
    private String STATE_NAME;
    @ColumnInfo(name = "gst_no")
    private String GSTNO;
    @ColumnInfo(name = "mobile_no")
    private String MOBILENO;
    @ColumnInfo(name = "email")
    private String E_MAIL;
    @ColumnInfo(name = "salesman")
    private String SALESMAN;


    public String getGROUPID() {
        return GROUPID;
    }

    public void setGROUPID(String GROUPID) {
        this.GROUPID = GROUPID;
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

    public String getGRP_INFO() {
        return GRP_INFO;
    }

    public void setGRP_INFO(String GRP_INFO) {
        this.GRP_INFO = GRP_INFO;
    }

    public String getAREANAME() {
        return AREANAME;
    }

    public void setAREANAME(String AREANAME) {
        this.AREANAME = AREANAME;
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

    public String getMOBILENO() {
        return MOBILENO;
    }

    public void setMOBILENO(String MOBILENO) {
        this.MOBILENO = MOBILENO;
    }

    public String getE_MAIL() {
        return E_MAIL;
    }

    public void setE_MAIL(String e_MAIL) {
        E_MAIL = e_MAIL;
    }

    public String getSALESMAN() {
        return SALESMAN;
    }

    public void setSALESMAN(String SALESMAN) {
        this.SALESMAN = SALESMAN;
    }
}
