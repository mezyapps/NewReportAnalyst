package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.utils.DatabaseConfiguration;
import com.mezyapps.new_reportanalyst.utils.SharedLoginUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

public class SplashActivity extends AppCompatActivity {

    String is_login="";
    Handler handler;
    ImageView iv_splash_image;
    private String is_config="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        iv_splash_image=findViewById(R.id.iv_splash_image);
        is_config= DatabaseConfiguration.getDatabaseConfiguration(getApplicationContext());

        is_login = SharedLoginUtils.getLoginSharedUtils(getApplicationContext());
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        } else {
            handlerCall();
        }
    }
    private void checkPermissions() {
        RxPermissions.getInstance(SplashActivity.this)
                .request(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        initialize(aBoolean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    private void initialize(Boolean isAppInitialized) {
        if (isAppInitialized) {
            handlerCall();

        } else {
            /* If one Of above permission not grant show alert (force to grant permission)*/
            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
            builder.setTitle("Alert");
            builder.setMessage("All permissions necessary");

            builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checkPermissions();
                }
            });

            builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.show();
        }
    }

    private void handlerCall() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(is_config.equalsIgnoreCase("") || is_config.equalsIgnoreCase("false"))
                {
                    Intent intent = new Intent(SplashActivity.this, DatabaseConfigActivity.class);
                    startActivity(intent);
                }else if (is_login.equalsIgnoreCase("") || is_login.equalsIgnoreCase("false")) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else  {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 3000);
    }
}
