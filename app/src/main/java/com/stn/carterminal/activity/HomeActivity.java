package com.stn.carterminal.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.stn.carterminal.R;
import com.stn.carterminal.constant.Constant;
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
        setOnClickListenerCheckVehicleButton();
    }

    private void setOnClickListenerDoServiceButton() {
        Button doServiceButton = findViewById(R.id.btnDoService);
        doServiceButton.setOnClickListener((View view) -> {
            Intent doServiceIntent = new Intent(getApplicationContext(), SearchManifestActivity.class);
            startActivity(doServiceIntent);
            finish();
        });
    }

    private void setOnClickListenerLogoutButton() {
        Button logoutButton = findViewById(R.id.btnLogout);
        logoutButton.setOnClickListener((View view) -> {
            Intent loginIntent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(loginIntent);
            finish();
        });
    }

    private void setOnClickListenerCheckVehicleButton() {
        Button checkVehicle = findViewById(R.id.btnCheckVehicle);
        checkVehicle.setOnClickListener((View view) -> {
            setupDialog();
        });
    }

    private void setupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.MyDialogTheme);
        View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
        builder.setTitle(Constant.DIALOG_SPINNER_TITLE);
        Spinner spinner = mView.findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(HomeActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.methodList));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!spinner.getSelectedItem().toString().equalsIgnoreCase("Choose One ...")) {
                    if (spinner.getSelectedItem().toString().equals("Scan UHF")) {
                        Toast.makeText(getApplicationContext(), "Scan UHF", Toast.LENGTH_SHORT).show();

                        Intent scanVehicle = new Intent(getApplicationContext(), ScanVehicleActivity.class);
                        scanVehicle.putExtra("menu", "home");
                        startActivity(scanVehicle);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Input NIK", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please choose one.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setView(mView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
