package com.stn.carterminal.activity.changeuhftag;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.stn.carterminal.R;
import com.stn.carterminal.activity.HomeActivity;
import com.stn.carterminal.activity.SignInActivity;
import com.stn.carterminal.adapter.SearchVehicleAdapter;
import com.stn.carterminal.constant.Constant;
import com.stn.carterminal.network.ServiceGenerator;
import com.stn.carterminal.network.request.ChangeUhfTag;
import com.stn.carterminal.network.response.User;
import com.stn.carterminal.network.response.Vehicle;
import com.stn.carterminal.network.service.VehicleService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeUHFTagActivity extends AppCompatActivity {

    private static final String TOOLBAR_TITLE = "Ubah UHF Tag";
    private static final String PROGRESS_DIALOG_MESSAGE = "Updating ...";

    private String EPC;
    private RecyclerView recyclerView;
    private EditText search;
    private ArrayList<Vehicle> vehicles;
    private SearchVehicleAdapter searchVehicleAdapter;
    private VehicleService vehicleService;
    private ProgressDialog progressDialog;
    private TextWatcher textWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_uhftag);

        Toolbar toolbar = findViewById(R.id.toolbarChangeTag);
        toolbar.setTitle(TOOLBAR_TITLE);

        vehicles = new ArrayList<>();
        progressDialog = new ProgressDialog(ChangeUHFTagActivity.this);
        progressDialog.setMessage(PROGRESS_DIALOG_MESSAGE);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString();
                if (!input.isEmpty()) {
                    vehicles = requestAPISearchVehicle(input);
                } else {
                    vehicles = new ArrayList<>();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        recyclerView = findViewById(R.id.recyclerChangeTag);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        search = findViewById(R.id.txtChangeTagInputNIK);
        searchVehicleAdapter = new SearchVehicleAdapter(this, vehicles, search, textWatcher);
        recyclerView.setAdapter(searchVehicleAdapter);

        vehicleService = ServiceGenerator.createBaseService(this, VehicleService.class);

        EPC = getIntent().getStringExtra("EPC");
        if (EPC == null || EPC.isEmpty()) {
            throw new Resources.NotFoundException();
        }

        setSearchFunctionalityRecyclerView();

        setOnClickListenerBackToHomeButton();
        setOnClickListenerConfirmationButton();
    }

    private void setOnClickListenerBackToHomeButton() {
        Button home = findViewById(R.id.btnChangeTagBackToHome);
        home.setOnClickListener((View view) -> {
            backToHome();
        });
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

    public void onBackPressed() {
        backToHome();
    }

    private void setOnClickListenerConfirmationButton() {
        Button confirm = findViewById(R.id.btnChangeTagConfirm);
        confirm.setOnClickListener((View view) -> {
            Vehicle vehicle = searchVehicleAdapter.getVehicle();
            if (vehicle != null) {
                progressDialog.show();
                requestAPIChangeUHFTag(vehicle.getVehicleId());
            } else {
                Toast.makeText(getApplicationContext(), Constant.ERROR_MESSAGE_INVALID_VEHICLE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSearchFunctionalityRecyclerView() {
        search.addTextChangedListener(textWatcher);
    }

    private void filter(String text) {
        ArrayList<Vehicle> filteredVehicles = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getNIK().toLowerCase().contains(text.toLowerCase())) {
                filteredVehicles.add(vehicle);
            }
        }
        searchVehicleAdapter.filterList(filteredVehicles);
    }

    private ArrayList<Vehicle> requestAPISearchVehicle(String NIK) {
        Call<ArrayList<Vehicle>> vehicleCall = vehicleService.apiGetAllVehicleItsTagIsUsed(NIK);
        vehicleCall.enqueue(new Callback<ArrayList<Vehicle>>() {
            @Override
            public void onResponse(Call<ArrayList<Vehicle>> call, Response<ArrayList<Vehicle>> response) {
                if (response.code() == 200) {
                    vehicles = response.body();
                    filter(NIK);
                } else {
                    Toast.makeText(getApplicationContext(), Constant.ERROR_MESSAGE_SEARCH_PROVIDED_SERVICE, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Vehicle>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), Constant.ERROR_MESSAGE_SEARCH_PROVIDED_SERVICE, Toast.LENGTH_SHORT).show();
            }
        });
        return vehicles;
    }

    private void requestAPIChangeUHFTag(Long vehicleId) {
        Call<Vehicle> vehicleCall = vehicleService.apiChangeUHFTag(vehicleId, new ChangeUhfTag(vehicleId, EPC));
        vehicleCall.enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {
                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(), Constant.API_SUCCESS_CHANGE_UHF_TAG, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    backToHome();
                } else {
                    Toast.makeText(getApplicationContext(), Constant.API_ERROR_UHF_TAG_SAME, Toast.LENGTH_SHORT).show();
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
