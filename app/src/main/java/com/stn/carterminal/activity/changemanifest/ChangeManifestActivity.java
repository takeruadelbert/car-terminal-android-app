package com.stn.carterminal.activity.changemanifest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stn.carterminal.R;
import com.stn.carterminal.activity.HomeActivity;
import com.stn.carterminal.activity.SignInActivity;
import com.stn.carterminal.adapter.SearchManifestAdapter;
import com.stn.carterminal.constant.Constant;
import com.stn.carterminal.network.ServiceGenerator;
import com.stn.carterminal.network.response.ProvidedService;
import com.stn.carterminal.network.response.Status;
import com.stn.carterminal.network.response.User;
import com.stn.carterminal.network.response.Vehicle;
import com.stn.carterminal.network.service.ProvidedServiceService;
import com.stn.carterminal.network.service.VehicleService;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeManifestActivity extends AppCompatActivity {

    private static final String TOOLBAR_TITLE = "Pindah Manifest";
    private static final String PROGRESS_DIALOG_MESSAGE = "Updating ...";

    private ProvidedService providedService;
    private ArrayList<ProvidedService> manifests;
    private SearchManifestAdapter searchManifestAdapter;
    private ProvidedServiceService providedServiceService;
    private EditText search;

    private Long vehicleId;
    private Long originProvidedServiceId;
    private Long targetProvidedServiceId;
    private VehicleService vehicleService;
    private ProgressDialog progressDialog;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_manifest);

        Toolbar toolbar = findViewById(R.id.toolbarChangeManifest);
        toolbar.setTitle(TOOLBAR_TITLE);

        manifests = new ArrayList<>();
        gson = new Gson();

        progressDialog = new ProgressDialog(ChangeManifestActivity.this);
        progressDialog.setMessage(PROGRESS_DIALOG_MESSAGE);

        RecyclerView recyclerView = findViewById(R.id.recyclerChangeManifest);
        search = findViewById(R.id.changeManifestSearchServiceNumber);
        providedServiceService = ServiceGenerator.createBaseService(this, ProvidedServiceService.class);
        if (getIntent().getExtras().containsKey("EPC")) {
            String EPC = getIntent().getStringExtra("EPC");
            vehicleService = ServiceGenerator.createBaseService(this, VehicleService.class);
            if (EPC != null && !EPC.isEmpty()) {
                checkProvidedServiceConfirmationStatusByTag(EPC);
            }
        }

        if (getIntent().getExtras().containsKey("originProvidedServiceId")) {
            originProvidedServiceId = getIntent().getLongExtra("originProvidedServiceId", 0L);
            vehicleId = getIntent().getLongExtra("vehicleId", 0L);
            if (originProvidedServiceId == 0L || vehicleId == 0L) {
                throw new Resources.NotFoundException();
            }
            requestAPIByProvidedServiceId(originProvidedServiceId);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchManifestAdapter = new SearchManifestAdapter(this, manifests, search);
        recyclerView.setAdapter(searchManifestAdapter);

        setSearchFunctionalityRecyclerView();

        setOnClickListenerConfirmationButton();
        setOnClickListenerBackToHomeButton();
    }

    private void setData() {
        TextView txtProvidedServiceNumber = findViewById(R.id.txtChangeManifestProvidedServiceNumber);
        txtProvidedServiceNumber.setText(providedService.getProvidedServiceNumber());

        TextView txtVesselName = findViewById(R.id.txtChangeManifestVesselName);
        txtVesselName.setText(providedService.getVesselName());

        TextView txtVesselOwner = findViewById(R.id.txtChangeManifestVesselOwner);
        txtVesselOwner.setText(providedService.getVesselOwner());

        TextView txtCompanyName = findViewById(R.id.txtChangeManifestCompanyName);
        txtCompanyName.setText(providedService.getCompanyName());
    }

    private void setOnClickListenerConfirmationButton() {
        Button confirm = findViewById(R.id.btnConfirmChangeManifest);
        confirm.setOnClickListener((View view) -> {
            ProvidedService providedService = searchManifestAdapter.getProvidedService();
            if (providedService != null) {
                targetProvidedServiceId = providedService.getProvidedServiceId();
                if (targetProvidedServiceId != null && vehicleId != null) {
                    progressDialog.show();
                    changeManifest(vehicleId, targetProvidedServiceId);
                } else {
                    if (targetProvidedServiceId == null) {
                        Toast.makeText(getApplicationContext(), Constant.ERROR_MESSAGE_SEARCH_PROVIDED_SERVICE, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), Constant.ERROR_MESSAGE_INVALID_VEHICLE, Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), Constant.ERROR_MESSAGE_SEARCH_PROVIDED_SERVICE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setOnClickListenerBackToHomeButton() {
        Button back = findViewById(R.id.btnChangeManifestBackToHome);
        back.setOnClickListener((View view) -> {
            backToHome();
        });
    }

    @Override
    public void onBackPressed() {
        backToHome();
    }

    private void backToHome() {
        Intent home = new Intent(getApplicationContext(), HomeActivity.class);

        Gson gson = new Gson();
        String dataUser = SignInActivity.sharedPreferences.getString("user", "");
        User user = gson.fromJson(dataUser, User.class);

        home.putExtra("user", user);
        startActivity(home);
        finish();
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
                    manifests = requestAPISearchProvidedServiceWithExcludedId(input);
                } else {
                    manifests = new ArrayList<>();
                }
                searchManifestAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<ProvidedService> filteredManifests = new ArrayList<>();
        for (ProvidedService providedService : manifests) {
            if (providedService.getProvidedServiceNumber().toLowerCase().contains(text.toLowerCase())) {
                filteredManifests.add(providedService);
            }
        }

        searchManifestAdapter.filterList(filteredManifests);
    }

    private ArrayList<ProvidedService> requestAPISearchProvidedServiceWithExcludedId(String providedServiceNumber) {
        Call<ArrayList<ProvidedService>> providedServiceCall = providedServiceService.apiSearchProvidedServiceWithExcludedId(originProvidedServiceId, providedServiceNumber);
        providedServiceCall.enqueue(new Callback<ArrayList<ProvidedService>>() {
            @Override
            public void onResponse(Call<ArrayList<ProvidedService>> call, Response<ArrayList<ProvidedService>> response) {
                if (response.code() == 200) {
                    manifests = response.body();
                } else {
                    Toast.makeText(getApplicationContext(), Constant.API_ERROR_INVALID_RESPONSE, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProvidedService>> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return manifests;
    }

    private void requestAPIByProvidedServiceId(Long providedServiceId) {
        Call<ProvidedService> providedServiceCall = providedServiceService.apiGetProvidedServiceById(providedServiceId);
        providedServiceCall.enqueue(new Callback<ProvidedService>() {
            @Override
            public void onResponse(Call<ProvidedService> call, Response<ProvidedService> response) {
                if (response.code() == 200) {
                    providedService = response.body();
                    setData();
                } else {
                    Toast.makeText(getApplicationContext(), Constant.API_ERROR_INVALID_RESPONSE, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProvidedService> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), Constant.API_ERROR_INVALID_RESPONSE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestAPIByTag(String EPC) {
        Call<Vehicle> vehicleCall = vehicleService.apiGetVehicleByTag(EPC);
        vehicleCall.enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {
                if (response.code() == 200) {
                    Vehicle vehicle = response.body();
                    vehicleId = vehicle.getVehicleId();
                    originProvidedServiceId = vehicle.getProvidedServiceId();
                    requestAPIByProvidedServiceId(originProvidedServiceId);
                } else if (response.code() == 404) {
                    Toast.makeText(getApplicationContext(), Constant.ERROR_MESSAGE_EPC_NOT_FOUND, Toast.LENGTH_SHORT).show();
                    backToHome();
                } else {
                    Toast.makeText(getApplicationContext(), Constant.API_ERROR_INVALID_RESPONSE, Toast.LENGTH_SHORT).show();
                    backToHome();
                }
            }

            @Override
            public void onFailure(Call<Vehicle> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), Constant.API_ERROR_INVALID_RESPONSE, Toast.LENGTH_SHORT).show();
                backToHome();
            }
        });
    }

    private void checkProvidedServiceConfirmationStatusByTag(String EPC) {
        Call<Status> vehicleCall = vehicleService.apiCheckProvidedConfirmationStatusByUhfTag(EPC);
        vehicleCall.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.code() == 200) {
                    requestAPIByTag(EPC);
                } else {
                    Type type = new TypeToken<Status>() {}.getType();
                    Status status = gson.fromJson(response.errorBody().charStream(), type);
                    if (status.getStatus().equals("active")) {
                        Toast.makeText(getApplicationContext(), Constant.ERROR_MESSAGE_UHF_TAG_ALREADY_USED, Toast.LENGTH_SHORT).show();
                        backToHome();
                    } else {
                        Toast.makeText(getApplicationContext(), Constant.API_ERROR_PROVIDED_SERVICE_STATUS_ALREADY_APPROVED, Toast.LENGTH_SHORT).show();
                        backToHome();
                    }
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), Constant.API_ERROR_INVALID_RESPONSE, Toast.LENGTH_SHORT).show();
                backToHome();
            }
        });
    }

    private void changeManifest(Long vehicleId, Long providedServiceId) {
        Call<ProvidedService> providedServiceCall = providedServiceService.apiChangeDataManifest(vehicleId, providedServiceId);
        providedServiceCall.enqueue(new Callback<ProvidedService>() {
            @Override
            public void onResponse(Call<ProvidedService> call, Response<ProvidedService> response) {
                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(), Constant.API_SUCCESS_CHANGE_DATA_MANIFEST, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    backToHome();
                } else {
                    Toast.makeText(getApplicationContext(), Constant.API_ERROR_CHANGE_DATA_MANIFEST, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ProvidedService> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), Constant.API_ERROR_INVALID_RESPONSE, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}
