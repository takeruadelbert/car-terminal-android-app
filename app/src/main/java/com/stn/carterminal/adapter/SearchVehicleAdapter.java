package com.stn.carterminal.adapter;

import android.content.Context;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stn.carterminal.R;
import com.stn.carterminal.constant.Constant;
import com.stn.carterminal.listener.ItemClickListener;
import com.stn.carterminal.network.response.Vehicle;
import com.stn.carterminal.viewholder.SearchVehicleViewHolder;

import java.util.ArrayList;

public class SearchVehicleAdapter extends RecyclerView.Adapter<SearchVehicleViewHolder> {
    private Context context;
    private ArrayList<Vehicle> vehicles;
    private ItemClickListener itemClickListener;
    private EditText search;
    private Vehicle vehicle;
    private TextWatcher textWatcher;

    public SearchVehicleAdapter(Context context, ArrayList<Vehicle> vehicles, EditText search, TextWatcher textWatcher) {
        this.context = context;
        this.vehicles = vehicles;
        this.search = search;
        this.textWatcher = textWatcher;
    }

    @NonNull
    @Override
    public SearchVehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_search_vehicle, parent, false);
        return new SearchVehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchVehicleViewHolder holder, int position) {
        String vehicleNIKLabel = context.getString(R.string.vehicleNIK);
        String vehicleNIK = vehicles.get(position).getNIK();
        String fullVehicleNIK = vehicleNIKLabel + Constant.WHITESPACE + vehicleNIK;
        holder.txtVehicleNIK.setText(fullVehicleNIK);

        String vehicleDescription = context.getString(R.string.vehicleDescription) + Constant.WHITESPACE + vehicles.get(position).getDescription();
        holder.txtVehicleDescription.setText(vehicleDescription);

        String providedServiceNumber = context.getString(R.string.providedServiceNumber) + Constant.WHITESPACE + vehicles.get(position).getProvidedServiceNumber();
        holder.txtVehicleProvidedServiceNumber.setText(providedServiceNumber);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicle = vehicles.get(position);
                search.removeTextChangedListener(textWatcher);
                search.setText(vehicleNIK);
                vehicles = new ArrayList<>();
                notifyDataSetChanged();

                search.addTextChangedListener(textWatcher);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    public void filterList(ArrayList<Vehicle> filteredVehicles) {
        this.vehicles = filteredVehicles;
        notifyDataSetChanged();
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }
}
