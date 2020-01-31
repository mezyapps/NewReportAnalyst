package com.mezyapps.new_reportanalyst.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "OrderEntryProductHD")
public class OrderEntryProductHD {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "maxID")
    private long maxID;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "order_no")
    private String order_no;

    @ColumnInfo(name = "party_id")
    private String party_id;

    @ColumnInfo(name = "party_name")
    private String party_name;

    @ColumnInfo(name = "balance")
    private String balance;

    @ColumnInfo(name = "total_qty")
    private String total_qty;

    @ColumnInfo(name = "total_amt")
    private String total_amt;

    @ColumnInfo(name = "date_y_m_d")
    private String date_y_m_d;

    @ColumnInfo(name = "salesman_id")
    private String salesman_id;

    @ColumnInfo(name = "saleman_name")
    private String salesman_name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getParty_id() {
        return party_id;
    }

    public void setParty_id(String party_id) {
        this.party_id = party_id;
    }

    public String getParty_name() {
        return party_name;
    }

    public void setParty_name(String party_name) {
        this.party_name = party_name;
    }

    public long getMaxID() {
        return maxID;
    }

    public void setMaxID(long maxID) {
        this.maxID = maxID;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTotal_qty() {
        return total_qty;
    }

    public void setTotal_qty(String total_qty) {
        this.total_qty = total_qty;
    }

    public String getTotal_amt() {
        return total_amt;
    }

    public void setTotal_amt(String total_amt) {
        this.total_amt = total_amt;
    }


    public String getDate_y_m_d() {
        return date_y_m_d;
    }

    public void setDate_y_m_d(String date_y_m_d) {
        this.date_y_m_d = date_y_m_d;
    }

    public String getSalesman_id() {
        return salesman_id;
    }

    public void setSalesman_id(String salesman_id) {
        this.salesman_id = salesman_id;
    }

    public String getSalesman_name() {
        return salesman_name;
    }

    public void setSalesman_name(String salesman_name) {
        this.salesman_name = salesman_name;
    }
}
