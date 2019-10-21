package com.stn.carterminal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.stn.carterminal.R;
import com.stn.carterminal.network.response.User;

public class HomeActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbarSearchManifest);
        toolbar.setTitle("Home");

        user = (User) getIntent().getSerializableExtra("user");
        ((TextView) findViewById(R.id.fullName)).setText(user.getBiodata().getFullName());

        setOnClickListenerDoServiceButton();
        setOnClickListenerLogoutButton();
    }

    private void setOnClickListenerDoServiceButton() {
        Button doServiceButton = findViewById(R.id.btnDoService);
        doServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doServiceIntent = new Intent(getApplicationContext(), SearchManifestActivity.class);
                startActivity(doServiceIntent);
                finish();
            }
        });
    }

    private void setOnClickListenerLogoutButton() {
        Button logoutButton = findViewById(R.id.btnLogout);
        logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }
}
