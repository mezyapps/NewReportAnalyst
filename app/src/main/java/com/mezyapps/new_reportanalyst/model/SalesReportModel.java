package com.mezyapps.new_reportanalyst.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SalesReportModel implements Parcelable {

    String entryid, transtyp_id,prefixid,prefixno,vchno,vchdt,vchdt_y_m_d,groupid,groupname,totalcase,totalqty,totalgrossamt,total_td_amt,total_sp_amt,totalnetamt,totalcgst_amt,totalsget_amt,totaligst_amt,totalfinalamt,totalbillamt,narration;


    public static  final Parcelable.Creator CREATOR=new Parcelable.Creator()
    {


        @Override
        public SalesReportModel createFromParcel(Parcel source) {
            return new SalesReportModel(source);
        }

        @Override
        public SalesReportModel[] newArray(int size) {
            return new SalesReportModel[0];
        }
    };

    public SalesReportModel() {
    }

    public SalesReportModel(Parcel source) {
        this.entryid=source.readString();
        this.transtyp_id = source.readString();
        this.prefixid = source.readString();
        this.prefixno = source.readString();
        this.vchno = source.readString();
        this.vchdt = source.readString();
        this.vchdt_y_m_d = source.readString();
        this.groupid = source.readString();
        this.groupname = source.readString();
        this.totalcase = source.readString();
        this.totalqty = source.readString();
        this.totalgrossamt = source.readString();
        this.total_td_amt = source.readString();
        this.total_sp_amt = source.readString();
        this.totalnetamt = source.readString();
        this.totalcgst_amt = source.readString();
        this.totalsget_amt = source.readString();
        this.totaligst_amt = source.readString();
        this.totalfinalamt = source.readString();
        this.totalbillamt = source.readString();
        this.narration = source.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public SalesReportModel(String entryid, String transtyp_id, String prefixid, String prefixno, String vchno, String vchdt, String vchdt_y_m_d, String groupid, String groupname, String totalcase, String totalqty, String totalgrossamt, String total_td_amt, String total_sp_amt, String totalnetamt, String totalcgst_amt, String totalsget_amt, String totaligst_amt, String totalfinalamt, String totalbillamt, String narration) {
        this.entryid = entryid;
        this.transtyp_id = transtyp_id;
        this.prefixid = prefixid;
        this.prefixno = prefixno;
        this.vchno = vchno;
        this.vchdt = vchdt;
        this.vchdt_y_m_d = vchdt_y_m_d;
        this.groupid = groupid;
        this.groupname = groupname;
        this.totalcase = totalcase;
        this.totalqty = totalqty;
        this.totalgrossamt = totalgrossamt;
        this.total_td_amt = total_td_amt;
        this.total_sp_amt = total_sp_amt;
        this.totalnetamt = totalnetamt;
        this.totalcgst_amt = totalcgst_amt;
        this.totalsget_amt = totalsget_amt;
        this.totaligst_amt = totaligst_amt;
        this.totalfinalamt = totalfinalamt;
        this.totalbillamt = totalbillamt;
        this.narration = narration;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.entryid);
        dest.writeString(this.transtyp_id);
        dest.writeString(this.prefixid);
        dest.writeString(this.prefixno);
        dest.writeString(this.vchno);
        dest.writeString(this.vchdt);
        dest.writeString(this.vchdt_y_m_d);
        dest.writeString(this.groupid);
        dest.writeString(this.groupname);
        dest.writeString(this.totalcase);
        dest.writeString(this.totalqty);
        dest.writeString(this.totalgrossamt);
        dest.writeString(this.total_td_amt);
        dest.writeString(this.total_sp_amt);
        dest.writeString(this.totalnetamt);
        dest.writeString(this.totalcgst_amt);
        dest.writeString(this.totalsget_amt);
        dest.writeString(this.totaligst_amt);
        dest.writeString(this.totalfinalamt);
        dest.writeString(this.totalbillamt);
        dest.writeString(this.narration);

    }

    public String getEntryid() {
        return entryid;
    }

    public void setEntryid(String entryid) {
        this.entryid = entryid;
    }

    public String getTranstyp_id() {
        return transtyp_id;
    }

    public void setTranstyp_id(String transtyp_id) {
        this.transtyp_id = transtyp_id;
    }

    public String getPrefixid() {
        return prefixid;
    }

    public void setPrefixid(String prefixid) {
        this.prefixid = prefixid;
    }

    public String getPrefixno() {
        return prefixno;
    }

    public void setPrefixno(String prefixno) {
        this.prefixno = prefixno;
    }

    public String getVchno() {
        return vchno;
    }

    public void setVchno(String vchno) {
        this.vchno = vchno;
    }

    public String getVchdt() {
        return vchdt;
    }

    public void setVchdt(String vchdt) {
        this.vchdt = vchdt;
    }

    public String getVchdt_y_m_d() {
        return vchdt_y_m_d;
    }

    public void setVchdt_y_m_d(String vchdt_y_m_d) {
        this.vchdt_y_m_d = vchdt_y_m_d;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getTotalcase() {
        return totalcase;
    }

    public void setTotalcase(String totalcase) {
        this.totalcase = totalcase;
    }

    public String getTotalqty() {
        return totalqty;
    }

    public void setTotalqty(String totalqty) {
        this.totalqty = totalqty;
    }

    public String getTotalgrossamt() {
        return totalgrossamt;
    }

    public void setTotalgrossamt(String totalgrossamt) {
        this.totalgrossamt = totalgrossamt;
    }

    public String getTotal_td_amt() {
        return total_td_amt;
    }

    public void setTotal_td_amt(String total_td_amt) {
        this.total_td_amt = total_td_amt;
    }

    public String getTotal_sp_amt() {
        return total_sp_amt;
    }

    public void setTotal_sp_amt(String total_sp_amt) {
        this.total_sp_amt = total_sp_amt;
    }

    public String getTotalnetamt() {
        return totalnetamt;
    }

    public void setTotalnetamt(String totalnetamt) {
        this.totalnetamt = totalnetamt;
    }

    public String getTotalcgst_amt() {
        return totalcgst_amt;
    }

    public void setTotalcgst_amt(String totalcgst_amt) {
        this.totalcgst_amt = totalcgst_amt;
    }

    public String getTotalsget_amt() {
        return totalsget_amt;
    }

    public void setTotalsget_amt(String totalsget_amt) {
        this.totalsget_amt = totalsget_amt;
    }

    public String getTotaligst_amt() {
        return totaligst_amt;
    }

    public void setTotaligst_amt(String totaligst_amt) {
        this.totaligst_amt = totaligst_amt;
    }

    public String getTotalfinalamt() {
        return totalfinalamt;
    }

    public void setTotalfinalamt(String totalfinalamt) {
        this.totalfinalamt = totalfinalamt;
    }

    public String getTotalbillamt() {
        return totalbillamt;
    }

    public void setTotalbillamt(String totalbillamt) {
        this.totalbillamt = totalbillamt;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }
}
