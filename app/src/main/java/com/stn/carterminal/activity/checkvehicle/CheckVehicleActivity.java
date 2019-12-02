package com.stn.carterminal.activity.checkvehicle;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.stn.carterminal.R;
import com.stn.carterminal.activity.HomeActivity;
import com.stn.carterminal.activity.SignInActivity;
import com.stn.carterminal.constant.Constant;
import com.stn.carterminal.helper.TakeruHelper;
import com.stn.carterminal.network.ServiceGenerator;
import com.stn.carterminal.network.response.User;
import com.stn.carterminal.network.response.Vehicle;
import com.stn.carterminal.network.service.VehicleService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckVehicleActivity extends AppCompatActivity {

    private Vehicle vehicle;
    private VehicleService vehicleService;

    private static final String TOOLBAR_TITLE = "Informasi Kendaraan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_vehicle);

        Toolbar toolbar = findViewById(R.id.toolbarDetailVehicle);
        toolbar.setTitle(TOOLBAR_TITLE);

        if (getIntent().getExtras().containsKey("vehicle")) {
            vehicle = (Vehicle) getIntent().getSerializableExtra("vehicle");
            if (vehicle == null) {
                throw new Resources.NotFoundException();
            }
            setData();
        }

        if (getIntent().getExtras().containsKey("EPC")) {
            String EPC = getIntent().getStringExtra("EPC");
            vehicleService = ServiceGenerator.createBaseService(this, VehicleService.class);
            if (EPC != null && !EPC.isEmpty()) {
                requestAPIByTag(EPC);
            }
        }

        setOnClickListenerBackToHomeButton();
    }

    private void setData() {
        TextView vehicleNIK = findViewById(R.id.txtVehicleDetailNIK);
        vehicleNIK.setText(vehicle.getNIK());

        TextView vehicleDescription = findViewById(R.id.txtVehicleDetailDescription);
        vehicleDescription.setText(vehicle.getDescription());

        TextView vehicleClass = findViewById(R.id.txtVehicleDetailClass);
        vehicleClass.setText(vehicle.getVehicleClass());

        TextView vehicleEntryDate = findViewById(R.id.txtVehicleDetailEntryDate);
        String date = TakeruHelper.convertStringDateToPlainDate(vehicle.getEntryDate());
        vehicleEntryDate.setText(date);

        TextView vehicleProductionDay = findViewById(R.id.txtVehicleDetailProductionDay);
        String numDaysBuildUp;
        try {
            numDaysBuildUp = vehicle.getNumDaysBuildUp().toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            numDaysBuildUp = "-";
        }
        vehicleProductionDay.setText(numDaysBuildUp);
    }

    private void setOnClickListenerBackToHomeButton() {
        Button home = findViewById(R.id.btnVehicleDetailBackToHome);
        home.setOnClickListener((View view) -> {
            backToHome();
        });
    }

    @Override
    public void onBackPressed() {
        backToHome();
    }

    private void backToHome() {
        Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
        Gson gson = new Gson();
        String dataUser = SignInActivity.sharedPreferences.getString("user", "");
        User user = gson.fromJson(dataUser, User.class);

        homeIntent.putExtra("user", user);
        startActivity(homeIntent);
        finish();
    }

    private void requestAPIByTag(String EPC) {
        Call<Vehicle> vehicleCall = vehicleService.apiGetVehicleByTag(EPC);
        vehicleCall.enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {
                if (response.code() == 200) {
                    vehicle = response.body();
                    setData();
                } else if (response.code() == 404) {
                    Toast.makeText(getApplicationContext(), Constant.API_ERROR_VEHICLE_NOT_FOUND, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Vehicle> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), Constant.API_ERROR_INVALID_RESPONSE, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
