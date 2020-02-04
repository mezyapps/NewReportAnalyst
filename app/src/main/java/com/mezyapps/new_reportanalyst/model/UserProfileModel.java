package com.mezyapps.new_reportanalyst.model;

public class UserProfileModel {
    private String user_id,user_name, user_pass, db_name, db_user_name, db_user_pass, user_type, SALESMAN_ID,SALESMAN_NAME,INCLU_EXCLU;

    public UserProfileModel(String user_id, String user_name, String user_pass, String db_name, String db_user_name, String db_user_pass, String user_type, String SALESMAN_ID,String SALESMAN_NAME,String INCLU_EXCLU) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_pass = user_pass;
        this.db_name = db_name;
        this.db_user_name = db_user_name;
        this.db_user_pass = db_user_pass;
        this.user_type = user_type;
        this.SALESMAN_ID = SALESMAN_ID;
        this.SALESMAN_NAME = SALESMAN_NAME;
        this.INCLU_EXCLU = INCLU_EXCLU;
    }

    public UserProfileModel() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }

    public String getDb_name() {
        return db_name;
    }

    public void setDb_name(String db_name) {
        this.db_name = db_name;
    }

    public String getDb_user_name() {
        return db_user_name;
    }

    public void setDb_user_name(String db_user_name) {
        this.db_user_name = db_user_name;
    }

    public String getDb_user_pass() {
        return db_user_pass;
    }

    public void setDb_user_pass(String db_user_pass) {
        this.db_user_pass = db_user_pass;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
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

    public String getINCLU_EXCLU() {
        return INCLU_EXCLU;
    }

    public void setINCLU_EXCLU(String INCLU_EXCLU) {
        this.INCLU_EXCLU = INCLU_EXCLU;
    }
}
