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
import com.stn.carterminal.constant.sharedPreference.SharedPreferenceDataKey;
import com.stn.carterminal.helper.SharedPreferencesHelper;
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
        setOnClickListenerChangeManifestButton();
        setOnClickListenerChangeUHFTagButton();
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
            SharedPreferencesHelper.storeData(SignInActivity.sharedPreferences, SharedPreferenceDataKey.KEY_SHARED_PREFERENCES_USERNAME, Constant.EMPTY_STRING);
            SharedPreferencesHelper.storeData(SignInActivity.sharedPreferences, SharedPreferenceDataKey.KEY_SHARED_PREFERENCES_PASSWORD, Constant.EMPTY_STRING);
            SharedPreferencesHelper.storeData(SignInActivity.sharedPreferences, SharedPreferenceDataKey.KEY_SHARED_PREFERENCES_DATA_USER, Constant.EMPTY_STRING);
            SharedPreferencesHelper.storeData(SignInActivity.sharedPreferences, SharedPreferenceDataKey.KEY_SHARED_PREFERENCES_TOKEN_BEARER, Constant.EMPTY_STRING);

            Intent loginIntent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(loginIntent);
            finish();
        });
    }

    private void setOnClickListenerCheckVehicleButton() {
        Button checkVehicle = findViewById(R.id.btnCheckVehicle);
        checkVehicle.setOnClickListener((View view) -> {
            setupDialog("checkVehicle");
        });
    }

    private void setOnClickListenerChangeManifestButton() {
        Button changeManifest = findViewById(R.id.btnChangeManifest);
        changeManifest.setOnClickListener((View view) -> {
            setupDialog("changeManifest");
        });
    }

    private void setOnClickListenerChangeUHFTagButton() {
        Button changeTag = findViewById(R.id.btnChangeTag);
        changeTag.setOnClickListener((View view) -> {
            changeActivityToScanVehicle("changeUhfTag");
        });
    }

    private void setupDialog(String target) {
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
                changeActivity(spinner, target);
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

    private void changeActivity(Spinner spinner, String target) {
        if (!spinner.getSelectedItem().toString().equalsIgnoreCase("Choose One ...")) {
            if (spinner.getSelectedItem().toString().equals("Scan UHF")) {
                changeActivityToScanVehicle(target);
            } else {
                Intent searchVehicle = new Intent(getApplicationContext(), SearchVehicleActivity.class);
                searchVehicle.putExtra("menu", "home");
                searchVehicle.putExtra("target", target);
                startActivity(searchVehicle);
                finish();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please choose one.", Toast.LENGTH_SHORT).show();
        }
    }

    private void changeActivityToScanVehicle(String target) {
        Intent scanVehicle = new Intent(getApplicationContext(), ScanVehicleActivity.class);
        scanVehicle.putExtra("menu", "home");
        scanVehicle.putExtra("target", target);
        startActivity(scanVehicle);
        finish();
    }
}
