package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.ui.IstighfarViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("الاستغفار", appName)
    }

    @Test
    fun `counter decrements by one on tap`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = IstighfarViewModel(app)
        viewModel.setTargetCount(99)
        assertEquals(99, viewModel.uiState.value.currentCount)

        viewModel.decrement()
        assertEquals(98, viewModel.uiState.value.currentCount)

        viewModel.resetCount()
        assertEquals(99, viewModel.uiState.value.currentCount)
    }

    @Test
    fun `reaching zero triggers completion alert`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = IstighfarViewModel(app)
        viewModel.setTargetCount(1)
        assertEquals(1, viewModel.uiState.value.currentCount)

        viewModel.decrement()
        assertEquals(0, viewModel.uiState.value.currentCount)
        assertTrue(viewModel.uiState.value.isZeroReached)
    }
}
