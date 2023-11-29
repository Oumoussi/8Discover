package com.univ.paris8discover.screens;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

import com.univ.paris8discover.R;

public class LoginActivity extends AppCompatActivity {
    private TextView bookITextView;
    private ProgressBar loadingProgressBar;
    private RelativeLayout rootView;
    private ImageView bookIconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);

    }

    public static class onActivityResult {
    }
}
