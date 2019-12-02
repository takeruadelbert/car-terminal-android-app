package com.stn.carterminal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stn.carterminal.R;
import com.stn.carterminal.network.response.ProvidedService;
import com.stn.carterminal.viewholder.SearchManifestViewHolder;
import com.stn.carterminal.constant.Constant;
import com.stn.carterminal.listener.ItemClickListener;

import java.util.ArrayList;

public class SearchManifestAdapter extends RecyclerView.Adapter<SearchManifestViewHolder> {
    private Context context;
    private ArrayList<ProvidedService> manifests;
    private ItemClickListener itemClickListener;
    private EditText search;
    private ProvidedService providedService;

    public SearchManifestAdapter(Context context, ArrayList<ProvidedService> manifests, EditText search) {
        this.context = context;
        this.manifests = manifests;
        this.search = search;
    }

    @NonNull
    @Override
    public SearchManifestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_search_manifest, parent, false);
        return new SearchManifestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchManifestViewHolder holder, int position) {
        String providedServiceNumberLabel = context.getString(R.string.providedServiceNumber);
        String providedServiceNumber = manifests.get(position).getProvidedServiceNumber();
        String fullProvidedServiceNumber = providedServiceNumberLabel + Constant.WHITESPACE + providedServiceNumber;
        holder.txtProvidedServiceNumber.setText(fullProvidedServiceNumber);

        String vesselName = context.getString(R.string.vesselName) + Constant.WHITESPACE + manifests.get(position).getVesselName();
        holder.txtVesselName.setText(vesselName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                providedService = manifests.get(position);

                search.setText(providedServiceNumber);
                manifests = new ArrayList<>();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return manifests.size();
    }

    public void filterList(ArrayList<ProvidedService> filteredManifests) {
        this.manifests = filteredManifests;
        notifyDataSetChanged();
    }

    public ProvidedService getProvidedService() {
        return this.providedService;
    }
}
