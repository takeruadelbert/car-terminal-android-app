package com.stn.carterminal.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.Toolbar;

import com.stn.carterminal.R;
import com.stn.carterminal.activity.editvehicle.EditVehicleActivity;
import com.stn.carterminal.constant.Constant;
import com.stn.carterminal.network.ServiceGenerator;
import com.stn.carterminal.network.request.ChangeVehiclePosition;
import com.stn.carterminal.network.response.ProvidedService;
import com.stn.carterminal.network.response.Vehicle;
import com.stn.carterminal.network.service.VehicleService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailVehicleActivity extends AppCompatActivity {

    private Vehicle vehicle;
    private TextView txtVehicleNIK;
    private TextView txtVehicleDescription;
    private TextView txtVehicleClass;
    private Long providedServiceId;
    private ProvidedService providedService;
    private String EPC;
    private VehicleService vehicleService;
    private ProgressDialog progressDialog;

    private final static String TOOLBAR_TITLE = "Detail Kendaraan";
    private final static String PROGRESS_DIALOG_MESSAGE = "Updating ...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_vehicle);

        Toolbar toolbar = findViewById(R.id.toolbarDetailVehicle);
        toolbar.setTitle(TOOLBAR_TITLE);

        vehicle = (Vehicle) getIntent().getSerializableExtra("vehicle");
        providedServiceId = getIntent().getLongExtra("providedServiceId", 0L);
        providedService = (ProvidedService) getIntent().getSerializableExtra("providedService");
        EPC = getIntent().getStringExtra("EPC");
        if (vehicle == null || EPC == null || EPC.isEmpty()) {
            throw new Resources.NotFoundException();
        }

        progressDialog = new ProgressDialog(DetailVehicleActivity.this);
        progressDialog.setMessage(PROGRESS_DIALOG_MESSAGE);

        setData();
        setOnClickListenerBackToSearchVehicleButton();
        setOnClickListenerConfirmDetailVehicleButton();
        setOnClickListenerEditDataVehicleButton();

        vehicleService = ServiceGenerator.createBaseService(this, VehicleService.class);
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
            backToDetailManifest();
        });
    }

    private void setOnClickListenerConfirmDetailVehicleButton() {
        Button confirmDetailVehicle = findViewById(R.id.btnConfirmDetailVehicle);
        confirmDetailVehicle.setOnClickListener((View v) -> {
            if (validate()) {
                progressDialog.show();
                apiChangeDataVehiclePosition(vehicle.getVehicleId());
            } else {
                Toast.makeText(getApplicationContext(), Constant.ERROR_MESSAGE_DETAIL_VEHICLE_CHECKBOX, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setOnClickListenerEditDataVehicleButton() {
        Button editVehicle = findViewById(R.id.btnEdit);
        editVehicle.setOnClickListener((View view) -> {
            Intent editVehicleIntent = new Intent(getApplicationContext(), EditVehicleActivity.class);
            editVehicleIntent.putExtra("vehicle", vehicle);
            editVehicleIntent.putExtra("providedServiceId", providedServiceId);
            editVehicleIntent.putExtra("providedService", providedService);
            editVehicleIntent.putExtra("EPC", EPC);
            startActivity(editVehicleIntent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        backToDetailManifest();
    }

    private void backToDetailManifest() {
        Intent scanVehicleIntent = new Intent(getApplicationContext(), DetailManifestActivity.class);
        scanVehicleIntent.putExtra("providedService", providedService);
        startActivity(scanVehicleIntent);
        finish();
    }

    private boolean validate() {
        AppCompatCheckBox checkBoxNIK = findViewById(R.id.checkboxVehicleNIK);
        AppCompatCheckBox checkBoxDescription = findViewById(R.id.checkboxVehicleDescription);
        AppCompatCheckBox checkBoxClass = findViewById(R.id.checkboxVehicleDetailClass);
        return checkBoxNIK.isChecked() && checkBoxDescription.isChecked() && checkBoxClass.isChecked();
    }

    private void apiChangeDataVehiclePosition(Long vehicleId) {
        ChangeVehiclePosition changeVehiclePosition = new ChangeVehiclePosition(vehicleId, EPC, vehicle.getNIK(), vehicle.getDescription(), vehicle.getVehicleClassId(), vehicle.isDataVehicleChanged());
        Call<Vehicle> vehicleCall = vehicleService.apiChangeVehiclePosition(vehicleId, changeVehiclePosition);
        vehicleCall.enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {
                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(), Constant.API_SUCCESS, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    backToDetailManifest();
                } else {
                    Toast.makeText(getApplicationContext(), Constant.API_ERROR_INVALID_RESPONSE, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Vehicle> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), Constant.API_ERROR_INVALID_RESPONSE, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}
