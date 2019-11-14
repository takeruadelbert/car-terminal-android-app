package com.stn.carterminal.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stn.carterminal.R;
import com.stn.carterminal.activity.addnewvehicle.AddNewVehicleActivity;
import com.stn.carterminal.activity.changemanifest.ChangeManifestActivity;
import com.stn.carterminal.activity.checkvehicle.CheckVehicleActivity;
import com.stn.carterminal.adapter.SearchVehicleAdapter;
import com.stn.carterminal.constant.Constant;
import com.stn.carterminal.network.ServiceGenerator;
import com.stn.carterminal.network.response.ProvidedService;
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
    private ProvidedService providedService;
    private String EPC;
    private String menu;
    private String target;
    private ProgressDialog progressDialog;

    private static final String TOOLBAR_TITLE = "Search Kendaraan";
    private static final String PROGRESS_DIALOG_MESSAGE = "Loading ...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_vehicle_activity);

        vehicles = new ArrayList<>();

        menu = getIntent().getStringExtra("menu");
        target = getIntent().getStringExtra("target");
        providedService = (ProvidedService) getIntent().getSerializableExtra("providedService");
        if (menu.equals("scanVehicle") || menu.equals("newVehicle")) {
            providedServiceId = getIntent().getLongExtra("providedServiceId", 0L);
            EPC = getIntent().getStringExtra("EPC");
            if (providedServiceId == 0L || EPC == null || EPC.isEmpty()) {
                Toast.makeText(getApplicationContext(), Constant.ERROR_MESSAGE_EPC_NOT_FOUND, Toast.LENGTH_SHORT).show();
                throw new Resources.NotFoundException();
            }
        }

        progressDialog = new ProgressDialog(SearchVehicleActivity.this);
        progressDialog.setMessage(PROGRESS_DIALOG_MESSAGE);

        recyclerView = findViewById(R.id.recyclerViewVehicle);
        search = findViewById(R.id.inputNIK);

        setupToolbar();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchVehicleAdapter = new SearchVehicleAdapter(this, vehicles, search);
        recyclerView.setAdapter(searchVehicleAdapter);

        vehicleService = ServiceGenerator.createBaseService(this, VehicleService.class);

        setSearchFunctionalityRecyclerView();
        SetOnClickListenerConfirmationButton();
        setOnClickListenerBackToDetailManifestButton();
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbarSearchVehicle);
        toolbar.setTitle(TOOLBAR_TITLE);
        toolbar.inflateMenu(R.menu.additional);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.addNewVehicle:
                        Intent newVehicle = new Intent(getApplicationContext(), AddNewVehicleActivity.class);
                        newVehicle.putExtra("EPC", EPC);
                        newVehicle.putExtra("providedServiceId", providedServiceId);
                        startActivity(newVehicle);
                        finish();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
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
                    if (menu.equals("scanVehicle") || menu.equals("newVehicle")) {
                        vehicles = requestAPISearchVehicleByProvidedServiceIdAndNIK(input);
                    } else {
                        vehicles = requestAPISearchVehicleByNIK(input);
                    }
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

    private ArrayList<Vehicle> requestAPISearchVehicleByProvidedServiceIdAndNIK(String NIK) {
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
                Toast.makeText(getApplicationContext(), Constant.API_ERROR_INVALID_RESPONSE, Toast.LENGTH_SHORT).show();
            }
        });
        return vehicles;
    }

    private ArrayList<Vehicle> requestAPISearchVehicleByNIK(String NIK) {
        Call<ArrayList<Vehicle>> vehicleCall = target.equals("changeManifest") ? vehicleService.apiSearchProvidedServiceConfirmationStatusNotApproved(NIK) : vehicleService.apiGetVehicleByNIK(NIK);
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
                Toast.makeText(getApplicationContext(), Constant.API_ERROR_INVALID_RESPONSE, Toast.LENGTH_SHORT).show();
            }
        });
        return vehicles;
    }

    private void SetOnClickListenerConfirmationButton() {
        Button confirm = findViewById(R.id.btnConfirmSearchVehicle);
        confirm.setOnClickListener((View view) -> {
            Vehicle vehicle = searchVehicleAdapter.getVehicle();
            if (vehicle != null) {
                progressDialog.show();
                changeActivity(vehicle);
            } else {
                Toast.makeText(getApplicationContext(), Constant.ERROR_MESSAGE_SEARCH_PROVIDED_SERVICE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeActivity(Vehicle vehicle) {
        progressDialog.dismiss();
        if (menu.equals("scanVehicle") || menu.equals("newVehicle")) {
            changeActivityToDetailVehicle(vehicle);
        } else {
            if (target.equals("checkVehicle")) {
                Intent checkVehicleIntent = new Intent(getApplicationContext(), CheckVehicleActivity.class);
                checkVehicleIntent.putExtra("vehicle", vehicle);
                checkVehicleIntent.putExtra("providedService", providedService);
                startActivity(checkVehicleIntent);
                finish();
            } else if (target.equals("changeManifest")) {
                Intent changeManifestIntent = new Intent(getApplicationContext(), ChangeManifestActivity.class);
                changeManifestIntent.putExtra("originProvidedServiceId", vehicle.getProvidedServiceId());
                changeManifestIntent.putExtra("vehicleId", vehicle.getVehicleId());
                startActivity(changeManifestIntent);
                finish();
            }
        }
    }

    private void changeActivityToDetailVehicle(Vehicle vehicle) {
        Intent detailVehicleIntent = new Intent(getApplicationContext(), DetailVehicleActivity.class);
        detailVehicleIntent.putExtra("vehicle", vehicle);
        detailVehicleIntent.putExtra("providedServiceId", providedServiceId);
        detailVehicleIntent.putExtra("providedService", providedService);
        detailVehicleIntent.putExtra("EPC", EPC);
        startActivity(detailVehicleIntent);
        finish();
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
