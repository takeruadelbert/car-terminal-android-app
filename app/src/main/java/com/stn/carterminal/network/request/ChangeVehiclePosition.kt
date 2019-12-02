package com.stn.carterminal.network.request

class ChangeVehiclePosition(var id: Long, var uhfTag: String, var vehicleIdNumber: String, var description: String, var vehicleClassId: Long, var vehicleManifestStatus: String, var isDataVehicleChanged: Boolean) {
    val vehiclePosition: String = "TERMINAL_IN"

    init {
        isDataVehicleChanged = setDataVehicleChanged(isDataVehicleChanged)
    }

    private fun setDataVehicleChanged(isDataVehicleChanged: Boolean): Boolean {
        when (vehicleManifestStatus) {
            "ADD" -> return false
            else -> return isDataVehicleChanged
        }
    }
}