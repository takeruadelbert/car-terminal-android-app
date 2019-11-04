package com.stn.carterminal.activity.editvehicle;

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
import com.stn.carterminal.activity.DetailVehicleActivity;
import com.stn.carterminal.constant.Constant;
import com.stn.carterminal.network.ServiceGenerator;
import com.stn.carterminal.network.response.Vehicle;
import com.stn.carterminal.network.response.VehicleClass;
import com.stn.carterminal.network.service.VehicleService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditVehicleActivity extends AppCompatActivity {

    private Vehicle vehicle;
    private VehicleService vehicleService;
    private ArrayList<String> vehicleClassSpinner = new ArrayList<>();
    private ArrayList<VehicleClass> vehicleClasses = new ArrayList<>();
    private Map<Long, String> dataResponse = new HashMap<>();
    private ArrayAdapter<String> adapter;
    private Spinner spinner;
    private EditText editTextNIK;
    private EditText editTextDescription;
    private String EPC;
    private Long providedServiceId;

    private static final String TOOLBAR_TITLE = "Edit Detail Kendaraan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle);

        Toolbar toolbar = findViewById(R.id.toolbarEditVehicle);
        toolbar.setTitle(TOOLBAR_TITLE);

        vehicle = (Vehicle) getIntent().getSerializableExtra("vehicle");
        EPC = getIntent().getStringExtra("EPC");
        providedServiceId = getIntent().getLongExtra("providedServiceId", 0L);
        if (vehicle == null || EPC == null || EPC.isEmpty() || providedServiceId == 0L) {
            throw new Resources.NotFoundException();
        }

        editTextNIK = findViewById(R.id.txtEditVehicleInputNIK);
        editTextDescription = findViewById(R.id.txtEditVehicleDescription);
        spinner = findViewById(R.id.editVehicleClassSpinner);

        vehicleService = ServiceGenerator.createBaseService(this, VehicleService.class);

        apiRequestGetVehicleClass();

        setOnClickListenerConfirmationButton();
        setOnClickListenerBackToDetailVehicleButton();
    }

    private void setData() {
        editTextNIK.setText(vehicle.getNIK());
        editTextDescription.setText(vehicle.getDescription());

        String vehicleClass = vehicle.getVehicleClass();
        int selectedPosition = getSelectedSpinnerPosition(vehicleClass);
        if (selectedPosition == -1) {
            throw new Resources.NotFoundException();
        }

        spinner.setSelection(selectedPosition);
    }

    private void setupVehicleClasses(ArrayList<VehicleClass> vehicleClasses) {
        if (vehicleClasses.isEmpty()) {
            throw new Resources.NotFoundException();
        }

        for (VehicleClass vehicleClass : vehicleClasses) {
            vehicleClassSpinner.add(vehicleClass.getName());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vehicleClassSpinner);
        spinner.setAdapter(adapter);
    }

    private void apiRequestGetVehicleClass() {
        Call<Map<Long, String>> vehicleClassCall = vehicleService.apiGetVehicleClass();
        vehicleClassCall.enqueue(new Callback<Map<Long, String>>() {
            @Override
            public void onResponse(Call<Map<Long, String>> call, Response<Map<Long, String>> response) {
                if (response.code() == 200) {
                    dataResponse = response.body();
                    vehicleClasses = convertHashMapToListVehicleClass(dataResponse);
                    if (vehicleClasses.isEmpty()) {
                        Toast.makeText(getApplicationContext(), Constant.ERROR_MESSAGE_INVALID_VEHICLE_CLASS, Toast.LENGTH_SHORT).show();
                    } else {
                        setupVehicleClasses(vehicleClasses);
                        setData();
                    }
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

    private int getSelectedSpinnerPosition(String selectedItem) {
        for (int i = 0; i < vehicleClasses.size(); i++) {
            if (vehicleClasses.get(i).getName().equals(selectedItem)) {
                return i;
            }
        }
        return -1;
    }

    private Long getVehicleClassIdByName(String vehicleClassName) {
        for (VehicleClass vehicleClass : vehicleClasses) {
            if (vehicleClass.getName().equals(vehicleClassName)) {
                return vehicleClass.getId();
            }
        }
        return null;
    }

    private void setOnClickListenerConfirmationButton() {
        Button confirm = findViewById(R.id.btnEditVehicleConfirm);
        confirm.setOnClickListener((View view) -> {
            String editedNIK = editTextNIK.getText().toString();
            String editedDescription = editTextDescription.getText().toString();
            String editedVehicleClass = spinner.getSelectedItem().toString();

            vehicle.setNIK(editedNIK);
            vehicle.setDescription(editedDescription);
            vehicle.setVehicleClass(editedVehicleClass);
            vehicle.setVehicleClassId(getVehicleClassIdByName(editedVehicleClass));

            Intent detailVehicleIntent = new Intent(getApplicationContext(), DetailVehicleActivity.class);
            detailVehicleIntent.putExtra("vehicle", vehicle);
            detailVehicleIntent.putExtra("EPC", EPC);
            detailVehicleIntent.putExtra("providedServiceId", providedServiceId);
            startActivity(detailVehicleIntent);
            finish();
        });
    }

    private void setOnClickListenerBackToDetailVehicleButton() {
        Button back = findViewById(R.id.btnEditVehicleBackToDetailVehicle);
        back.setOnClickListener((View view) -> {
            backToDetailManifest();
        });
    }

    private void backToDetailManifest() {
        Intent detailManifestIntent = new Intent(getApplicationContext(), DetailVehicleActivity.class);
        detailManifestIntent.putExtra("EPC", EPC);
        detailManifestIntent.putExtra("providedServiceId", providedServiceId);
        detailManifestIntent.putExtra("vehicle", vehicle);
        startActivity(detailManifestIntent);
        finish();
    }
}
