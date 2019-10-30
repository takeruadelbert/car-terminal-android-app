package com.stn.carterminal.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.stn.carterminal.R;
import com.stn.carterminal.listener.ItemClickListener;

public class SearchManifestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProvidedServiceNumber;
    public TextView txtVesselName;
    private ItemClickListener itemClickListener;

    public SearchManifestViewHolder(View view) {
        super(view);

        txtProvidedServiceNumber = view.findViewById(R.id.txtProvidedServiceNumber);
        txtVesselName = view.findViewById(R.id.txtVesselName);

        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
