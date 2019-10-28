package com.stn.carterminal.activity;

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

import com.stn.carterminal.R;
import com.stn.carterminal.adapter.SearchVehicleAdapter;
import com.stn.carterminal.constant.Constant;
import com.stn.carterminal.network.ServiceGenerator;
import com.stn.carterminal.network.response.Vehicle;
import com.stn.carterminal.network.service.VehicleService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchVehicleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText search;
    private Toolbar toolbar;
    private ArrayList<Vehicle> vehicles;
    private SearchVehicleAdapter searchVehicleAdapter;
    private VehicleService vehicleService;
    private Long providedServiceId;

    private static final String TOOLBAR_TITLE = "Search Kendaraan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_vehicle_activity);

        vehicles = new ArrayList<>();

        providedServiceId = getIntent().getLongExtra("providedServiceId", 0L);
        if (providedServiceId == 0L) {
            throw new Resources.NotFoundException();
        }

        recyclerView = findViewById(R.id.recyclerViewVehicle);
        search = findViewById(R.id.inputNIK);

        toolbar = findViewById(R.id.toolbarSearchVehicle);
        toolbar.setTitle(TOOLBAR_TITLE);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchVehicleAdapter = new SearchVehicleAdapter(this, vehicles, search);
        recyclerView.setAdapter(searchVehicleAdapter);

        vehicleService = ServiceGenerator.createBaseService(this, VehicleService.class);

        setSearchFunctionalityRecyclerView();
        SetOnClickListenerConfirmationButton();
        setOnClickListenerBackToDetailManifestButton();
    }

    private void setSearchFunctionalityRecyclerView() {
        search.addTextChangedListener(new TextWatcher() {
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
                searchVehicleAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
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
        Call<ArrayList<Vehicle>> vehicleCall = vehicleService.apiGetVehicle(providedServiceId, NIK);
        vehicleCall.enqueue(new Callback<ArrayList<Vehicle>>() {
            @Override
            public void onResponse(Call<ArrayList<Vehicle>> call, Response<ArrayList<Vehicle>> response) {
                if (response.code() == 200) {
                    vehicles = response.body();
                } else {
                    Toast.makeText(getApplicationContext(), Constant.API_ERROR_INVALID_RESPONSE, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Vehicle>> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return vehicles;
    }

    private void SetOnClickListenerConfirmationButton() {
        Button confirm = findViewById(R.id.btnConfirmSearchVehicle);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vehicle vehicle = searchVehicleAdapter.getVehicle();
                if (vehicle != null) {
                    Intent detailVehicleIntent = new Intent(getApplicationContext(), DetailVehicleActivity.class);
                    detailVehicleIntent.putExtra("vehicle", vehicle);
                    detailVehicleIntent.putExtra("providedServiceId", providedServiceId);
                    startActivity(detailVehicleIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), Constant.ERROR_MESSAGE_SEARCH_PROVIDED_SERVICE, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setOnClickListenerBackToDetailManifestButton() {
        Button back = findViewById(R.id.btnBackToDetailManifest);
        back.setOnClickListener((View view) -> {
            backToSearchManifest();
        });
    }

    @Override
    public void onBackPressed() {
        backToSearchManifest();
    }

    private void backToSearchManifest() {
        Intent backToSearchManifest = new Intent(getApplicationContext(), SearchManifestActivity.class);
        startActivity(backToSearchManifest);
        finish();
    }
}
