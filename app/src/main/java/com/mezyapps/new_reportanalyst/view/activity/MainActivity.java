package com.mezyapps.new_reportanalyst.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.view.fragment.ChancePasswordFragment;
import com.mezyapps.new_reportanalyst.view.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private ImageView iv_drawer;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FrameLayout frameLayout_main;
    private String fragmentName = null;
    Fragment fragmentInstance;
    FragmentManager fragmentManager;
    private boolean doubleBackToExitPressedOnce = false;
    private Dialog dialog_logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        find_View_IdS();
        events();
    }

    private void find_View_IdS() {
        loadFragment(new HomeFragment());
        iv_drawer=findViewById(R.id.iv_drawer);
        drawerLayout=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.navigationView);
    }

    private void events() {
        iv_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.nav_dashboard) {
                    loadFragment(new HomeFragment());
                } else if (id == R.id.nav_change_password) {
                    loadFragment(new ChancePasswordFragment());
                }else if (id == R.id.nav_share_app){
                    shareApplication();
                }else if (id == R.id.nav_logout) {
                    logoutApplication();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (fragmentName.equals(fragmentInstance.getClass().getSimpleName())) {
                if (fragmentName.equals("HomeFragment")) {
                    doubleBackPressLogic();
                } else
                    loadFragment(new HomeFragment());
            }
        }
    }

    // ============ End Double tab back press logic =================
    private void doubleBackPressLogic() {
        if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit !!", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 1000);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void loadFragment(Fragment fragment) {
        fragmentInstance = fragment;
        fragmentName = fragment.getClass().getSimpleName();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout_main, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

    }

    private void logoutApplication() {
        dialog_logout = new Dialog(MainActivity.this);
        dialog_logout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_logout.setContentView(R.layout.dialog_logout);
        TextView txt_cancel = dialog_logout.findViewById(R.id.txt_cancel);
        TextView txt_logout = dialog_logout.findViewById(R.id.txt_logout);
        dialog_logout.setCancelable(false);
        dialog_logout.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog_logout.show();

        Window window = dialog_logout.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_logout.dismiss();
            }
        });
        txt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // SharedLoginUtils.removeLoginSharedUtils(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void shareApplication() {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Biz Protect Application");
        String app_url = "https://play.google.com/store/apps/details?id=com.mezyapps.reportanalyst";
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, app_url);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

}
