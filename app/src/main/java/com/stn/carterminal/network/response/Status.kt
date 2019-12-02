package com.stn.carterminal.network.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

// this is for Provided Service Status
data class Status(@SerializedName("status") var status: String) : Serializable