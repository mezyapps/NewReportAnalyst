package com.mezyapps.new_reportanalyst.connection;

        import android.annotation.SuppressLint;
        import android.os.StrictMode;
        import android.util.Log;

        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.SQLException;

public class ConnectionCommon {

    private String ip_address = "67.211.45.179:1091";
    private String connection_class = "net.sourceforge.jtds.jdbc.Driver";
    private String database = "ANDROID_CONN";
    private String database_user = "JMDINFOTECH";
    private String password = "Jmd&23Info$10Tech~79";


    @SuppressLint("NewApi")
    public Connection connectionDatabase() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection con = null;
        String ConnURL = null;
        try {

            Class.forName(connection_class);
            ConnURL = "jdbc:jtds:sqlserver://" + ip_address + ";" + "databaseName=" + database + ";user=" + database_user + ";password=" + password + ";";
            con = DriverManager.getConnection(ConnURL);

        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return con;
    }
}
