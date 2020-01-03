package info.safronoff.gpsbeacon

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import info.safronoff.gpsbeacon.utils.GetLocationImpl
import io.reactivex.schedulers.Schedulers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class GetLocationTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun getLocation() {
        val appContext = activityRule.activity
        val getLocation = GetLocationImpl(appContext)
        //val observer = TestObserver<Location>()
        getLocation.exec().subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())
            .subscribe({ println("location received, accuracy ${it.accuracy}") }, {})
        //observer.assertNoErrors()
    }
}