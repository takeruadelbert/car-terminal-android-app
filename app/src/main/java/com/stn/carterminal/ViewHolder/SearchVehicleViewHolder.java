package com.stn.carterminal.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.stn.carterminal.R;
import com.stn.carterminal.listener.ItemClickListener;

public class SearchVehicleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtVehicleNIK;
    public TextView txtVehicleDescription;
    private ItemClickListener itemClickListener;

    public SearchVehicleViewHolder(View view) {
        super(view);

        txtVehicleNIK = view.findViewById(R.id.txtVehicleNIK);
        txtVehicleDescription = view.findViewById(R.id.txtVehicleDescription);

        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
