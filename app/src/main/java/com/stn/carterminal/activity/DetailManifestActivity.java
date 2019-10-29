package com.stn.carterminal.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.stn.carterminal.R;
import com.stn.carterminal.network.response.ProvidedService;

public class DetailManifestActivity extends AppCompatActivity {
    private ProvidedService providedService;
    private Toolbar toolbar;
    private TextView providedServiceNumber;
    private TextView vesselName;
    private TextView vesselOwner;
    private TextView companyName;
    private Long providedServiceId;

    private static final String TOOLBAR_TITLE = "Detail Manifest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_manifest);

        toolbar = findViewById(R.id.toolbarDetailManifest);
        toolbar.setTitle(TOOLBAR_TITLE);

        providedService = (ProvidedService) getIntent().getSerializableExtra("providedService");
        if (providedService == null) {
            throw new Resources.NotFoundException();
        }
        providedServiceId = providedService.getProvidedServiceId();

        setData(providedService);
        setOnClickListenerBackToSearchManifestButton();
        setOnClickListenerGoToSearchVehicleButton();
    }

    private void setData(ProvidedService providedService) {
        providedServiceNumber = findViewById(R.id.txtProvidedServiceNumberDetailManifest);
        providedServiceNumber.setText(providedService.getProvidedServiceNumber());

        vesselName = findViewById(R.id.txtVesselNameDetailManifest);
        vesselName.setText(providedService.getVesselName());

        vesselOwner = findViewById(R.id.txtVesselOwnerDetailManifest);
        vesselOwner.setText(providedService.getVesselOwner());

        companyName = findViewById(R.id.txtCompanyNameDetailManifest);
        companyName.setText(providedService.getCompanyName());
    }

    private void setOnClickListenerBackToSearchManifestButton() {
        Button backToSearchManifest = findViewById(R.id.btnBackToSearchManifest);
        backToSearchManifest.setOnClickListener((View view) -> {
            backToSearchManifest();
        });
    }

    private void setOnClickListenerGoToSearchVehicleButton() {
        Button goToSearchVehicle = findViewById(R.id.btnConfirmDetailManifest);
        goToSearchVehicle.setOnClickListener((View view) -> {
            Intent scanVehicle = new Intent(getApplicationContext(), ScanVehicleActivity.class);
            scanVehicle.putExtra("providedServiceId", providedServiceId);
            startActivity(scanVehicle);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        backToSearchManifest();
    }

    private void backToSearchManifest() {
        Intent searchManifest = new Intent(getApplicationContext(), SearchManifestActivity.class);
        startActivity(searchManifest);
        finish();
    }
}
