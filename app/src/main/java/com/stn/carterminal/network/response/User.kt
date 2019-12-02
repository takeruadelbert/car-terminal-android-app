package com.stn.carterminal.network.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
        @SerializedName("id") var userId: Long,
        @SerializedName("username") var username: String,
        @SerializedName("email") var email: String,
        @SerializedName("biodata") var biodata: Biodata,
        @SerializedName("roleStrings") var roleStrings: List<String>,
        @SerializedName("roles") var roles: List<Role>
) : Serializable