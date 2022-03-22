package sg.nphau.android.shared

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule
import sg.nphau.android.BaseTest

open class ViewModelBaseTest : BaseTest() {

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()
}