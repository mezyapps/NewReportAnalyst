package com.mezyapps.new_reportanalyst.model;

public class DatabaseConfigModel {
    private String ip_address;
    private String username;
    private String database;
    private String password;

    public DatabaseConfigModel() {
    }

    public DatabaseConfigModel(String ip_address, String username, String database, String password) {
        this.ip_address = ip_address;
        this.username = username;
        this.database = database;
        this.password = password;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
