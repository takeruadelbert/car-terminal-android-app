package com.stn.carterminal.network.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserGroup implements Serializable {
    @SerializedName("id")
    private Long userGroupId;
    @SerializedName("name")
    private String name;

    public UserGroup(Long userGroupId, String name) {
        this.userGroupId = userGroupId;
        this.name = name;
    }
}
