package com.mezyapps.new_reportanalyst.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName ="OrderEntryProductDT")
public class OrderEntryProductDT {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "maxID")
    private long maxId;


    @ColumnInfo(name = "product_id")
    private long product_id;

    @ColumnInfo(name = "product_name")
    private String product_name;

    @ColumnInfo(name = "box_pkg")
    private String box_pkg;

    @ColumnInfo(name = "pkg")
    private String pkg;

    @ColumnInfo(name = "qty")
    private String qty;

    @ColumnInfo(name = "rate")
    private String rate;

    @ColumnInfo(name = "sub_total")
    private String sub_total;

    @ColumnInfo(name = "dist_per")
    private String dist_per;

    @ColumnInfo(name = "gst_per")
    private String gst_per;


    @ColumnInfo(name = "dist_amt")
    private String dist_amt;

    @ColumnInfo(name = "gst_amt")
    private String gst_amt;

    @ColumnInfo(name = "final_total")
    private String final_total;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getBox_pkg() {
        return box_pkg;
    }

    public void setBox_pkg(String box_pkg) {
        this.box_pkg = box_pkg;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public String getDist_per() {
        return dist_per;
    }

    public void setDist_per(String dist_per) {
        this.dist_per = dist_per;
    }

    public String getGst_per() {
        return gst_per;
    }

    public void setGst_per(String gst_per) {
        this.gst_per = gst_per;
    }

    public String getDist_amt() {
        return dist_amt;
    }

    public void setDist_amt(String dist_amt) {
        this.dist_amt = dist_amt;
    }

    public String getGst_amt() {
        return gst_amt;
    }

    public void setGst_amt(String gst_amt) {
        this.gst_amt = gst_amt;
    }

    public String getFinal_total() {
        return final_total;
    }

    public void setFinal_total(String final_total) {
        this.final_total = final_total;
    }

    public long getMaxId() {
        return maxId;
    }

    public void setMaxId(long maxId) {
        this.maxId = maxId;
    }
}
