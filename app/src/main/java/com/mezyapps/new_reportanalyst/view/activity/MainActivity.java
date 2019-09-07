package com.mezyapps.new_reportanalyst.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mezyapps.new_reportanalyst.R;

public class MainActivity extends AppCompatActivity {

    private ImageView iv_drawer;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        find_View_IdS();
        events();
    }

    private void find_View_IdS() {
        iv_drawer=findViewById(R.id.iv_drawer);
        drawerLayout=findViewById(R.id.drawerLayout);
    }

    private void events() {
        iv_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
}
