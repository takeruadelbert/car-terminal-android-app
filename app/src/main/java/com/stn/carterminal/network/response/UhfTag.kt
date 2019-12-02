package com.stn.carterminal.network.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UhfTag(@SerializedName("status") var status: String) : Serializable