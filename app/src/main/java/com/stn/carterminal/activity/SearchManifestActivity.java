package com.stn.carterminal.activity;

import android.content.Intent;
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
import com.stn.carterminal.adapter.SearchManifestAdapter;
import com.stn.carterminal.constant.Constant;
import com.stn.carterminal.network.ServiceGenerator;
import com.stn.carterminal.network.response.ProvidedService;
import com.stn.carterminal.network.service.ProvidedServiceService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchManifestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText search;
    private Toolbar toolbar;
    private ArrayList<ProvidedService> manifests;
    private SearchManifestAdapter searchManifestAdapter;
    private ProvidedServiceService providedServiceService;

    private static final String TOOLBAR_TITLE = "Search Manifest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_manifest);

        manifests = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerViewManifest);
        search = findViewById(R.id.searchServiceNumber);

        toolbar = findViewById(R.id.toolbarSearchManifest);
        toolbar.setTitle(TOOLBAR_TITLE);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchManifestAdapter = new SearchManifestAdapter(this, manifests, search);
        recyclerView.setAdapter(searchManifestAdapter);

        setSearchFunctionalityRecyclerView();

        providedServiceService = ServiceGenerator.createBaseService(this, ProvidedServiceService.class);
        setOnClickListenerConfirmationButton();
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
                    manifests = requestAPISearchProvidedService(input);
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

    private ArrayList<ProvidedService> requestAPISearchProvidedService(String providedServiceNumber) {
        Call<ArrayList<ProvidedService>> providedServiceCall = providedServiceService.apiGetProvidedService(providedServiceNumber);
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

    private void setOnClickListenerConfirmationButton() {
        Button confirm = findViewById(R.id.btnConfirmSearchManifest);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProvidedService providedService = searchManifestAdapter.getProvidedService();
                if (providedService != null) {
                    Intent detailManifestIntent = new Intent(getApplicationContext(), DetailManifestActivity.class);
                    detailManifestIntent.putExtra("providedService", providedService);
                    startActivity(detailManifestIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), Constant.ERROR_MESSAGE_SEARCH_PROVIDED_SERVICE, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
