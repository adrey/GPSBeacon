package info.safronoff.gpsbeacon.tracking

import io.reactivex.Observable

interface IsTrackingStarted {
    fun exec(): Observable<Boolean>
}

class IsTrackinStartedImpl(private val trackingStateRepo: TrackingStateRepo) : IsTrackingStarted {
    override fun exec(): Observable<Boolean> {
        return trackingStateRepo.isStarted()
    }

}