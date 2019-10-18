package com.stn.carterminal.network.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class User implements Serializable {
    @SerializedName("id")
    private Long userId;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("biodata")
    private Biodata biodata;
    @SerializedName("userGroup")
    private UserGroup userGroup;

    public User(Long userId, String username, String email, Biodata biodata, UserGroup userGroup) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.biodata = biodata;
        this.userGroup = userGroup;
    }
}
