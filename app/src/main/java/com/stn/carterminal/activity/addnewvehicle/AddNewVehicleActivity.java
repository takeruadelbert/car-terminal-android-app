package com.stn.carterminal.activity.addnewvehicle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.stn.carterminal.R;
import com.stn.carterminal.activity.SearchVehicleActivity;
import com.stn.carterminal.constant.Constant;
import com.stn.carterminal.network.ServiceGenerator;
import com.stn.carterminal.network.request.NewVehicle;
import com.stn.carterminal.network.response.ProvidedService;
import com.stn.carterminal.network.response.Vehicle;
import com.stn.carterminal.network.response.VehicleClass;
import com.stn.carterminal.network.service.VehicleService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewVehicleActivity extends AppCompatActivity {

    private static final String TOOLBAR_TITLE = "Input Kendaraan Tambahan";
    private static final String PROGRESS_BAR_MESSAGE = "Saving ...";

    private ArrayList<VehicleClass> vehicleClasses = new ArrayList<>();
    private ArrayList<String> vehicleClassSpinner = new ArrayList<>();
    private Map<Long, String> dataResponse = new HashMap<>();
    private VehicleService vehicleService;
    private String EPC;
    private Long providedServiceId;
    private Long vehicleClassId;
    private ProvidedService providedService;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_vehicle);

        EPC = getIntent().getStringExtra("EPC");
        providedServiceId = getIntent().getLongExtra("providedServiceId", 0L);
        providedService = (ProvidedService) getIntent().getSerializableExtra("providedService");
        if (EPC == null || EPC.isEmpty() || providedServiceId == 0L || providedService == null) {
            throw new Resources.NotFoundException();
        }

        vehicleService = ServiceGenerator.createBaseService(this, VehicleService.class);

        progressDialog = new ProgressDialog(AddNewVehicleActivity.this);
        progressDialog.setMessage(PROGRESS_BAR_MESSAGE);

        Toolbar toolbar = findViewById(R.id.toolbarAddNewVehicle);
        toolbar.setTitle(TOOLBAR_TITLE);

        apiRequestGetVehicleClass();
        setOnClickListenerBackToSearchVehicleButton();
        setOnClickListenerConfirmButton();
    }

    private void setupVehicleClasses(ArrayList<VehicleClass> vehicleClasses) {
        Spinner spinner = findViewById(R.id.addNewVehicleClassSpinner);

        if (vehicleClasses.isEmpty()) {
            throw new Resources.NotFoundException();
        }

        for (VehicleClass vehicleClass : vehicleClasses) {
            vehicleClassSpinner.add(vehicleClass.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vehicleClassSpinner);
        spinner.setAdapter(adapter);
    }

    private void setOnClickListenerConfirmButton() {
        Button confirm = findViewById(R.id.btnAddNewVehicleConfirm);
        confirm.setOnClickListener((View view) -> {
            EditText editTextNIK = findViewById(R.id.txtAddNewVehicleInputNIK);
            String NIK = editTextNIK.getText().toString();

            EditText editTextDescription = findViewById(R.id.txtAddNewVehicleDescription);
            String description = editTextDescription.getText().toString();

            Spinner spinnerVehicleClass = findViewById(R.id.addNewVehicleClassSpinner);
            String vehicleClass = spinnerVehicleClass.getSelectedItem().toString();
            vehicleClassId = getVehicleClassIdByName(vehicleClass);

            if (validate(NIK, description, vehicleClassId)) {
                progressDialog.show();
                requestAPIAddNewVehicle(NIK, description, vehicleClassId);
            }
        });
    }

    private void setOnClickListenerBackToSearchVehicleButton() {
        Button back = findViewById(R.id.btnAddNewVehicleBackToSearchVehicle);
        back.setOnClickListener((View view) -> {
            backToSearchVehicle();
        });
    }

    @Override
    public void onBackPressed() {
        backToSearchVehicle();
    }

    private void backToSearchVehicle() {
        Intent searchVehicle = new Intent(getApplicationContext(), SearchVehicleActivity.class);
        searchVehicle.putExtra("EPC", EPC);
        searchVehicle.putExtra("menu", "newVehicle");
        searchVehicle.putExtra("providedServiceId", providedServiceId);
        searchVehicle.putExtra("providedService", providedService);
        startActivity(searchVehicle);
        finish();
    }

    private void requestAPIAddNewVehicle(String vehicleIdNumber, String description, Long vehicleClassId) {
        NewVehicle newVehicle = new NewVehicle(vehicleIdNumber, description, vehicleClassId, providedServiceId);
        Call<Vehicle> vehicleCall = vehicleService.apiAddNewVehicle(newVehicle);
        vehicleCall.enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {
                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(), Constant.API_SUCCESS_ADD_NEW_VEHICLE, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    backToSearchVehicle();
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

    private void apiRequestGetVehicleClass() {
        Call<Map<Long, String>> vehicleClasscall = vehicleService.apiGetVehicleClass();
        vehicleClasscall.enqueue(new Callback<Map<Long, String>>() {
            @Override
            public void onResponse(Call<Map<Long, String>> call, Response<Map<Long, String>> response) {
                if (response.code() == 200) {
                    dataResponse = response.body();
                    vehicleClasses = convertHashMapToListVehicleClass(dataResponse);
                    if (vehicleClasses.isEmpty()) {
                        Toast.makeText(getApplicationContext(), Constant.ERROR_MESSAGE_INVALID_VEHICLE_CLASS, Toast.LENGTH_SHORT).show();
                    }

                    setupVehicleClasses(vehicleClasses);
                } else {
                    Toast.makeText(getApplicationContext(), Constant.API_ERROR_INVALID_RESPONSE, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<Long, String>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), Constant.API_ERROR_INVALID_RESPONSE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<VehicleClass> convertHashMapToListVehicleClass(Map<Long, String> data) {
        ArrayList<VehicleClass> result = new ArrayList<>();
        if (!data.isEmpty()) {
            for (Map.Entry<Long, String> entry : data.entrySet()) {
                Long vehicleClassId = entry.getKey();
                String vehicleClassName = entry.getValue();
                result.add(new VehicleClass(vehicleClassId, vehicleClassName));
            }
        }
        return result;
    }

    private Long getVehicleClassIdByName(String name) {
        if (!dataResponse.isEmpty()) {
            for (Map.Entry<Long, String> entry : dataResponse.entrySet()) {
                if (entry.getValue().equals(name)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    private boolean validate(String NIK, String description, Long vehicleClassId) {
        if (NIK == null || NIK.isEmpty()) {
            Toast.makeText(getApplicationContext(), Constant.WARNING_MESSAGE_VEHICLE_ID_NUMBER, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (description == null || description.isEmpty()) {
            Toast.makeText(getApplicationContext(), Constant.WARNING_MESSAGE_VEHICLE_DESCRIPTION, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (vehicleClassId == null || vehicleClassId == 0L) {
            Toast.makeText(getApplicationContext(), Constant.WARNING_MESSAGE_VEHICLE_CLASS, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
