package com.stn.carterminal.network.response;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Biodata {
    @SerializedName("id")
    private Long biodataId;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("fullName")
    private String fullName;

    public Biodata(Long biodataId, String firstName, String lastName, String fullName) {
        this.biodataId = biodataId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
    }
}
