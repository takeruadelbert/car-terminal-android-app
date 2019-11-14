package com.stn.carterminal.network.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Status implements Serializable {
    @SerializedName("status")
    private String status;
}
