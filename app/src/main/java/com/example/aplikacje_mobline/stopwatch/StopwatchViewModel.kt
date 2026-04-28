package com.example.aplikacje_mobline.stopwatch

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class StopwatchViewModel @Inject constructor(
    private val stopwatchManager: StopwatchManager
) : ViewModel() {

    val uiState: StateFlow<StopwatchUiState> = stopwatchManager.uiState

    fun startForTrail(trailId: Int, trailName: String) = stopwatchManager.startForTrail(trailId, trailName)

    fun stopForTrail(trailId: Int) = stopwatchManager.stopForTrail(trailId)

    fun resetForTrail(trailId: Int) = stopwatchManager.resetForTrail(trailId)

    fun forceReset() = stopwatchManager.forceReset()
}
