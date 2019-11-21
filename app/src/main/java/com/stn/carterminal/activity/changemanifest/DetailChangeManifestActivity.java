package com.stn.carterminal.activity.changemanifest;

import android.app.ProgressDialog;
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
import com.stn.carterminal.network.ServiceGenerator;
import com.stn.carterminal.network.response.ProvidedService;
import com.stn.carterminal.network.response.User;
import com.stn.carterminal.network.response.Vehicle;
import com.stn.carterminal.network.service.ProvidedServiceService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailChangeManifestActivity extends AppCompatActivity {

    private TextView txtVehicleNik;
    private TextView txtVehicleClass;
    private TextView txtVehicleDescription;
    private TextView txtPreviousManifest;
    private TextView txtCurrentManifest;
    private Vehicle vehicle;
    private String previousManifest;
    private String currentManifest;
    private String EPC;
    private Long originProvidedServiceId;
    private Long targetProvidedServiceId;
    private ProvidedServiceService providedServiceService;
    private ProgressDialog progressDialog;

    private static final String TOOLBAR_TITLE = "Detail Pindah Manifest";
    private static final String PROGRESS_DIALOG_MESSAGE = "Updating ...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_change_manifest);

        Toolbar toolbar = findViewById(R.id.toolbarDetailChangeManifest);
        toolbar.setTitle(TOOLBAR_TITLE);

        vehicle = (Vehicle) getIntent().getSerializableExtra("vehicle");
        previousManifest = getIntent().getStringExtra("previousManifest");
        currentManifest = getIntent().getStringExtra("currentManifest");
        targetProvidedServiceId = getIntent().getLongExtra("targetProvidedServiceId", 0L);
        if (vehicle == null || (previousManifest == null || previousManifest.isEmpty()) || (currentManifest == null || currentManifest.isEmpty()) || targetProvidedServiceId == 0L) {
            throw new Resources.NotFoundException();
        }

        if (getIntent().getExtras().containsKey("EPC")) {
            EPC = getIntent().getStringExtra("EPC");
        }

        if (getIntent().getExtras().containsKey("originProvidedServiceId")) {
            originProvidedServiceId = getIntent().getLongExtra("originProvidedServiceId", 0L);
            if (originProvidedServiceId == 0L) {
                throw new Resources.NotFoundException("Invalid Origin Provided Service ID.");
            }
        }

        providedServiceService = ServiceGenerator.createBaseService(this, ProvidedServiceService.class);

        progressDialog = new ProgressDialog(DetailChangeManifestActivity.this);
        progressDialog.setMessage(PROGRESS_DIALOG_MESSAGE);

        initData();
        setOnClickListenerConfirmationButton();
        setOnClickListenerBackToChangeManifestButton();
    }

    private void initData() {
        txtVehicleNik = findViewById(R.id.txtDetailChangeManifestVehicleNIK);
        txtVehicleNik.setText(vehicle.getNIK());

        txtVehicleDescription = findViewById(R.id.txtDetailChangeManifestVehicleDescription);
        txtVehicleDescription.setText(vehicle.getDescription());

        txtVehicleClass = findViewById(R.id.txtDetailChangeManifestVehicleClass);
        txtVehicleClass.setText(vehicle.getVehicleClass());

        txtPreviousManifest = findViewById(R.id.txtDetailChangeManifestPreviousManifest);
        txtPreviousManifest.setText(previousManifest);

        txtCurrentManifest = findViewById(R.id.txtDetailChangeManifestCurrentManifest);
        txtCurrentManifest.setText(currentManifest);
    }

    private void setOnClickListenerConfirmationButton() {
        Button confirm = findViewById(R.id.btnDetailChangeManifestConfirm);
        confirm.setOnClickListener((View view) -> {
            progressDialog.show();
            changeManifest(vehicle.getVehicleId(), targetProvidedServiceId);
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

    private void setOnClickListenerBackToChangeManifestButton() {
        Button back = findViewById(R.id.btnDetailChangeManifestBack);
        back.setOnClickListener((View view) -> {
            backToChangeManifest();
        });
    }

    public void onBackPressed() {
        backToChangeManifest();
    }

    private void backToChangeManifest() {
        Intent changeManifestIntent = new Intent(getApplicationContext(), ChangeManifestActivity.class);
        changeManifestIntent.putExtra("vehicle", vehicle);
        if (EPC != null && !EPC.isEmpty()) {
            changeManifestIntent.putExtra("EPC", EPC);
        }
        if (originProvidedServiceId != null && originProvidedServiceId != 0L) {
            changeManifestIntent.putExtra("originProvidedServiceId", originProvidedServiceId);
        }
        startActivity(changeManifestIntent);
        finish();
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
}
