package info.safronoff.gpsbeacon.api

data class DeviceData(val lat: Double, val lng: Double, val accuracy: Float, val datetime: Long, val battery: Int)