package info.safronoff.gpsbeacon.tracking

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

interface TrackingStateRepo {

    fun isStarted(): Observable<Boolean>

    fun setStarted(started: Boolean)
}

class TrackingStateRepoImpl : TrackingStateRepo {

    private val subject = BehaviorSubject.create<Boolean>()

    init {
        subject.onNext(TrackingService.isStarted)
    }
    override fun setStarted(started: Boolean) {
        subject.onNext(started)
    }

    override fun isStarted(): Observable<Boolean> {
        return subject
    }

}