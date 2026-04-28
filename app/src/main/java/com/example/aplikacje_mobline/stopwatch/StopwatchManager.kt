package com.example.aplikacje_mobline.stopwatch

import android.content.Context
import android.os.SystemClock
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopwatchManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _uiState = MutableStateFlow(StopwatchUiState())
    val uiState: StateFlow<StopwatchUiState> = _uiState.asStateFlow()

    private var tickerJob: Job? = null
    private var isRunning = prefs.getBoolean(KEY_RUNNING, false)
    private var startedAtElapsedMs = prefs.getLong(KEY_STARTED_AT, 0L).takeIf { it > 0L }
    private var accumulatedMs = prefs.getLong(KEY_ACCUMULATED, 0L)
    private var activeTrailId = prefs.getInt(KEY_TRAIL_ID, NO_TRAIL_ID).takeIf { it != NO_TRAIL_ID }
    private var activeTrailName = prefs.getString(KEY_TRAIL_NAME, null)

    init {
        if (isRunning && startedAtElapsedMs == null) {
            // Recovery path for stale persisted state.
            isRunning = false
        }
        emitCurrentState()
        if (isRunning) {
            startTicker()
        }
    }

    fun startForTrail(trailId: Int, trailName: String) {
        if (activeTrailId != null && activeTrailId != trailId) return
        if (isRunning) return
        activeTrailId = trailId
        activeTrailName = trailName
        startedAtElapsedMs = SystemClock.elapsedRealtime()
        isRunning = true
        persistState()
        emitCurrentState()
        startTicker()
    }

    fun stopForTrail(trailId: Int) {
        if (activeTrailId != trailId) return
        if (!isRunning) return
        accumulatedMs = currentElapsedMs()
        isRunning = false
        startedAtElapsedMs = null
        stopTicker()
        persistState()
        emitCurrentState()
    }

    fun resetForTrail(trailId: Int) {
        if (activeTrailId != trailId) return
        clearStopwatchState()
    }

    fun forceReset() {
        clearStopwatchState()
    }

    private fun clearStopwatchState() {
        isRunning = false
        accumulatedMs = 0L
        startedAtElapsedMs = null
        activeTrailId = null
        activeTrailName = null
        stopTicker()
        persistState()
        emitCurrentState()
    }

    private fun currentElapsedMs(now: Long = SystemClock.elapsedRealtime()): Long {
        val runningPart = if (isRunning) {
            val startedAt = startedAtElapsedMs ?: now
            (now - startedAt).coerceAtLeast(0L)
        } else {
            0L
        }
        return (accumulatedMs + runningPart).coerceAtLeast(0L)
    }

    private fun emitCurrentState() {
        val elapsed = currentElapsedMs()
        _uiState.value = StopwatchUiState(
            isRunning = isRunning,
            elapsedMs = elapsed,
            formattedElapsed = formatElapsed(elapsed),
            activeTrailId = activeTrailId,
            activeTrailName = activeTrailName
        )
    }

    private fun startTicker() {
        if (tickerJob?.isActive == true) return
        tickerJob = scope.launch {
            while (isRunning) {
                emitCurrentState()
                delay(1000L)
            }
        }
    }

    private fun stopTicker() {
        tickerJob?.cancel()
        tickerJob = null
    }

    private fun persistState() {
        prefs.edit()
            .putBoolean(KEY_RUNNING, isRunning)
            .putLong(KEY_ACCUMULATED, accumulatedMs)
            .putLong(KEY_STARTED_AT, startedAtElapsedMs ?: 0L)
            .putInt(KEY_TRAIL_ID, activeTrailId ?: NO_TRAIL_ID)
            .putString(KEY_TRAIL_NAME, activeTrailName)
            .apply()
    }

    private fun formatElapsed(elapsedMs: Long): String {
        val totalSeconds = elapsedMs / 1000L
        val hours = totalSeconds / 3600L
        val minutes = (totalSeconds % 3600L) / 60L
        val seconds = totalSeconds % 60L
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    companion object {
        private const val PREFS_NAME = "stopwatch_prefs"
        private const val KEY_RUNNING = "running"
        private const val KEY_STARTED_AT = "started_at_elapsed"
        private const val KEY_ACCUMULATED = "accumulated_ms"
        private const val KEY_TRAIL_ID = "trail_id"
        private const val KEY_TRAIL_NAME = "trail_name"
        private const val NO_TRAIL_ID = -1
    }
}

data class StopwatchUiState(
    val isRunning: Boolean = false,
    val elapsedMs: Long = 0L,
    val formattedElapsed: String = "00:00:00",
    val activeTrailId: Int? = null,
    val activeTrailName: String? = null
)
