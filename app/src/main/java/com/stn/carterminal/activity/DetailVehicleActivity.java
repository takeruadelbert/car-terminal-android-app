package com.stn.carterminal.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.stn.carterminal.R;
import com.stn.carterminal.network.response.Vehicle;

public class DetailVehicleActivity extends AppCompatActivity {

    private Vehicle vehicle;
    private TextView txtVehicleNIK;
    private TextView txtVehicleDescription;
    private TextView txtVehicleClass;
    private Long providedServiceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_vehicle);

        vehicle = (Vehicle) getIntent().getSerializableExtra("vehicle");
        providedServiceId = getIntent().getLongExtra("providedServiceId", 0L);
        if (vehicle == null) {
            throw new Resources.NotFoundException();
        }

        setData();
        setOnClickListenerBackToSearchVehicleButton();
        setOnClickListenerConfirmDetailVehicleButton();
    }

    private void setData() {
        txtVehicleNIK = findViewById(R.id.txtVehicleDetailNIK);
        txtVehicleNIK.setText(vehicle.getNIK());

        txtVehicleDescription = findViewById(R.id.txtVehicleDetailDescription);
        txtVehicleDescription.setText(vehicle.getDescription());

        txtVehicleClass = findViewById(R.id.txtVehicleDetailClass);
        txtVehicleClass.setText(vehicle.getVehicleClass());

    }

    private void setOnClickListenerBackToSearchVehicleButton() {
        Button backToSearchVehicle = findViewById(R.id.btnBackToSearchVehicle);
        backToSearchVehicle.setOnClickListener((View v) -> {
            Intent searchVehicleIntent = new Intent(getApplicationContext(), SearchVehicleActivity.class);
            searchVehicleIntent.putExtra("providedServiceId", providedServiceId);
            startActivity(searchVehicleIntent);
            finish();
        });
    }

    private void setOnClickListenerConfirmDetailVehicleButton() {
        Button confirmDetailVehicle = findViewById(R.id.btnConfirmDetailVehicle);
        confirmDetailVehicle.setOnClickListener((View v) -> {

        });
    }
}
