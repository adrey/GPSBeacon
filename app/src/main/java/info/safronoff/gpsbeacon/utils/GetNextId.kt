package info.safronoff.gpsbeacon.utils

object GetNextId {
    private var value = 1000

    fun exec(): Int {
        return value++
    }
}