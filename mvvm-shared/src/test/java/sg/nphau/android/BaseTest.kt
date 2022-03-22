package sg.nphau.android

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.Timeout
import java.util.concurrent.TimeUnit

open class BaseTest {
    @get:Rule
    var globalTimeout = Timeout(if (BuildConfig.LIMIT_UNIT_TEST_TIME) 5L else 0L, TimeUnit.SECONDS)

    protected val mockMainDispatcher = StandardTestDispatcher()

    @Before
    fun setupMainDispatcher() {
        Dispatchers.setMain(mockMainDispatcher)
    }

    @After
    fun tearDownMainDispatcher() {
        Dispatchers.resetMain()
    }
}