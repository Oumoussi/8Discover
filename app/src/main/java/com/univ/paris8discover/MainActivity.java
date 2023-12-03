package com.univ.paris8discover;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.univ.paris8discover.screens.ArNavigation;
import com.univ.paris8discover.screens.LoginActivity;
import com.univ.paris8discover.screens.MapActivity;

public class MainActivity extends AppCompatActivity {

    private Button switchButton;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initializeUI();
    }
    private void initializeUI() {

        switchButton = (Button) findViewById(R.id.demarrer);
        switchButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        });
        login = (Button) findViewById(R.id.auth);
        login.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}