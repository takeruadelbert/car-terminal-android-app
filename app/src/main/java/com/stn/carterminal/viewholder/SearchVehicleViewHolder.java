package com.stn.carterminal.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.stn.carterminal.R;
import com.stn.carterminal.listener.ItemClickListener;

public class SearchVehicleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtVehicleNIK;
    public TextView txtVehicleDescription;
    public TextView txtVehicleProvidedServiceNumber;
    private ItemClickListener itemClickListener;

    public SearchVehicleViewHolder(View view) {
        super(view);

        txtVehicleNIK = view.findViewById(R.id.txtVehicleNIK);
        txtVehicleDescription = view.findViewById(R.id.txtVehicleDescription);
        txtVehicleProvidedServiceNumber = view.findViewById(R.id.txtVehicleProvidedServiceNumber);

        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
