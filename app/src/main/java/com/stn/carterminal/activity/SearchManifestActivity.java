package com.stn.carterminal.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stn.carterminal.R;
import com.stn.carterminal.adapter.SearchManifestAdapter;
import com.stn.carterminal.network.response.ProvidedService;

import java.util.ArrayList;

public class SearchManifestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText search;
    private Toolbar toolbar;
    private ArrayList<ProvidedService> manifests;
    private SearchManifestAdapter searchManifestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_manifest);

        manifests = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerViewManifest);
        search = findViewById(R.id.searchServiceNumber);

        toolbar = findViewById(R.id.toolbarSearchManifest);
        toolbar.setTitle("Search Manifest");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchManifestAdapter = new SearchManifestAdapter(this, manifests, search);
        recyclerView.setAdapter(searchManifestAdapter);

        setSearchFunctionalityRecyclerView();
    }

    private void setSearchFunctionalityRecyclerView() {
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    manifests.add(new ProvidedService(1L, "PS-123", "Kapal-1", "Takeru", "T.K."));
                    manifests.add(new ProvidedService(2L, "PS-123", "Kapal-2", "Takeru", "T.K."));
                    manifests.add(new ProvidedService(3L, "PS-123", "Kapal-3", "Takeru", "T.K."));
                    manifests.add(new ProvidedService(4L, "PS-123", "Kapal-4", "Takeru", "T.K."));
                    manifests.add(new ProvidedService(5L, "PS-123", "Kapal-5", "Takeru", "T.K."));
                    manifests.add(new ProvidedService(6L, "PS-123", "Kapal-6", "Takeru", "T.K."));
                    manifests.add(new ProvidedService(7L, "PS-123", "Kapal-7", "Takeru", "T.K."));
                    manifests.add(new ProvidedService(8L, "PS-123", "Kapal-8", "Takeru", "T.K."));
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
}
