package com.storex.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.storex.app.fragments.CartFragment;
import com.storex.app.fragments.SettingsFragment;
import com.storex.app.fragments.ShopFragment;
import com.storex.app.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(this);

        if (!session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        TextView toolbarUserName = findViewById(R.id.toolbarUserName);
        toolbarUserName.setText(session.getLoggedInName());

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        // الشاشة الافتراضية: المتجر
        loadFragment(new ShopFragment());

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_shop) {
                loadFragment(new ShopFragment());
                return true;
            } else if (id == R.id.nav_cart) {
                loadFragment(new CartFragment());
                return true;
            } else if (id == R.id.nav_settings) {
                loadFragment(new SettingsFragment());
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    public void logoutAndRestart() {
        session.logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
