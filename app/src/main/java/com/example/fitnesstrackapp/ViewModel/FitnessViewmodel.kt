package com.example.fitnesstrackapp.ViewModel

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstrackapp.Data.HydrationEntry
import com.example.fitnesstrackapp.Data.Workout
import com.example.fitnesstrackapp.Preferences.GoalPreferences
import com.example.fitnesstrackapp.Preferences.ReminderPreferences
import com.example.fitnesstrackapp.Preferences.StepPreferences
import com.example.fitnesstrackapp.Repository.AchievementRepository
import com.example.fitnesstrackapp.Repository.FitnessRepository
import com.example.fitnesstrackapp.Repository.HydrationRepository
import com.example.fitnesstrackapp.Repository.StreakCalculator
import com.example.fitnesstrackapp.Worker.ReminderScheduler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Calendar
import java.util.Locale

private const val TAG = "FitnessViewModel"

class FitnessViewModel(
    private val repository: FitnessRepository,
    private val hydrationRepository: HydrationRepository,
    private val achievementRepository: AchievementRepository,
    private val goalPreferences: GoalPreferences,
    private val stepPreferences: StepPreferences,
    private val reminderPreferences: ReminderPreferences,
    private val appContext: Context
) : ViewModel(), SensorEventListener {

    // ─────────────────────────────── Goals ───────────────────────────────────

    val goals: StateFlow<GoalPreferences.Goals> = goalPreferences.goals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), GoalPreferences.Goals())

    fun saveGoals(goals: GoalPreferences.Goals) {
        viewModelScope.launch { goalPreferences.saveGoals(goals) }
    }

    // ─────────────────────────────── Workouts ────────────────────────────────

    val workouts: StateFlow<List<Workout>> = repository.workouts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addWorkout(name: String, duration: Int, calories: Int, category: String) {
        viewModelScope.launch {
            repository.insertWorkout(
                Workout(
                    name = name,
                    duration = duration.coerceAtLeast(0),
                    category = category,
                    calories = calories.coerceAtLeast(0),
                    date = System.currentTimeMillis()
                )
            )
            recalculateAchievements()
        }
    }

    fun updateWorkout(workout: Workout) {
        viewModelScope.launch {
            repository.updateWorkout(workout)
            recalculateAchievements()
        }
    }

    fun deleteWorkout(workout: Workout) {
        viewModelScope.launch {
            repository.deleteWorkout(workout)
            recalculateAchievements()
        }
    }

    // ─────────────────────────── Today's workouts ────────────────────────────

    private fun todayBounds(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0);      cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.set(Calendar.HOUR_OF_DAY, 23); cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59);      cal.set(Calendar.MILLISECOND, 999)
        return start to cal.timeInMillis
    }

    val todayWorkouts: StateFlow<List<Workout>> = run {
        val (start, end) = todayBounds()
        repository.getWorkoutsBetween(start, end)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    }

    // ─────────────────────────── Weekly analytics ────────────────────────────

    private fun currentWeekBounds(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0);      cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.add(Calendar.DAY_OF_WEEK, 6)
        cal.set(Calendar.HOUR_OF_DAY, 23); cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59);      cal.set(Calendar.MILLISECOND, 999)
        return start to cal.timeInMillis
    }

    private fun currentMonthBounds(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0);      cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        cal.set(Calendar.HOUR_OF_DAY, 23); cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59);      cal.set(Calendar.MILLISECOND, 999)
        return start to cal.timeInMillis
    }

    val weeklyWorkouts: StateFlow<List<Workout>> = run {
        val (start, end) = currentWeekBounds()
        repository.getWorkoutsBetween(start, end)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    }

    val monthlyWorkouts: StateFlow<List<Workout>> = run {
        val (start, end) = currentMonthBounds()
        repository.getWorkoutsBetween(start, end)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    }

    // ─────────────────────────────── Hydration ───────────────────────────────

    private val _todayHydrationMl = MutableStateFlow(0)
    val todayHydrationMl: StateFlow<Int> = _todayHydrationMl.asStateFlow()

    private val _todayHydrationEntries = MutableStateFlow<List<HydrationEntry>>(emptyList())
    val todayHydrationEntries: StateFlow<List<HydrationEntry>> = _todayHydrationEntries.asStateFlow()

    private fun startCollectingHydration() {
        val (start, end) = todayBounds()
        viewModelScope.launch {
            hydrationRepository.getDailyTotalMl(start, end).collect { ml ->
                _todayHydrationMl.value = ml
                checkHydrationAchievements(ml)
            }
        }
        viewModelScope.launch {
            hydrationRepository.getEntriesToday(start, end).collect { entries ->
                _todayHydrationEntries.value = entries
            }
        }
    }

    fun addHydration(amountMl: Int) {
        if (amountMl <= 0) return
        viewModelScope.launch {
            hydrationRepository.addEntry(amountMl)
        }
    }

    /** Removes the most recently added hydration entry (undo). */
    fun undoLastHydration() {
        viewModelScope.launch {
            val last = _todayHydrationEntries.value.firstOrNull() ?: return@launch
            hydrationRepository.removeEntry(last)
        }
    }

    // ─────────────────────────────── Step Tracking ───────────────────────────

    private val _todaySteps     = MutableStateFlow(0)
    val todaySteps: StateFlow<Int> = _todaySteps.asStateFlow()

    private val _stepPermissionGranted = MutableStateFlow(false)
    val stepPermissionGranted: StateFlow<Boolean> = _stepPermissionGranted.asStateFlow()

    private val _stepSensorAvailable = MutableStateFlow(true)
    val stepSensorAvailable: StateFlow<Boolean> = _stepSensorAvailable.asStateFlow()

    private var sensorManager: SensorManager? = null
    private var stepSensor: Sensor? = null
    private var savedBaseline: StepPreferences.StepBaseline? = null

    /** Called from MainActivity/Composable after permission check. */
    fun initStepCounter(context: Context, permissionGranted: Boolean) {
        _stepPermissionGranted.value = permissionGranted
        if (!permissionGranted) return

        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            _stepSensorAvailable.value = false
            Log.w(TAG, "TYPE_STEP_COUNTER not available on this device")
            return
        }
        _stepSensorAvailable.value = true

        // Load cached steps so display doesn't jump to 0 before first sensor event.
        viewModelScope.launch {
            val baseline = stepPreferences.read()
            val today = todayDateString()
            if (baseline.date == today) {
                _todaySteps.value = baseline.todaySteps
            }
            savedBaseline = baseline
        }

        sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun unregisterStepSensor() {
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type != Sensor.TYPE_STEP_COUNTER) return
        val cumulativeSteps = event.values[0].toLong()
        viewModelScope.launch {
            handleNewSensorValue(cumulativeSteps)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { /* no-op */ }

    private suspend fun handleNewSensorValue(cumulativeSteps: Long) {
        val today = todayDateString()
        var baseline = savedBaseline ?: stepPreferences.read()

        when {
            baseline.date.isEmpty() -> {
                // First ever reading – set today's baseline.
                baseline = StepPreferences.StepBaseline(
                    date = today,
                    cumulativeAtDayStart = cumulativeSteps,
                    todaySteps = 0
                )
            }
            baseline.date != today -> {
                // New calendar day – reset baseline.
                baseline = StepPreferences.StepBaseline(
                    date = today,
                    cumulativeAtDayStart = cumulativeSteps,
                    todaySteps = 0
                )
            }
            cumulativeSteps < baseline.cumulativeAtDayStart -> {
                // Counter reset (device reboot). Start fresh for this day.
                baseline = baseline.copy(cumulativeAtDayStart = cumulativeSteps)
            }
        }

        val delta = (cumulativeSteps - baseline.cumulativeAtDayStart).coerceAtLeast(0L).toInt()
        val updated = baseline.copy(todaySteps = delta)
        savedBaseline = updated
        _todaySteps.value = delta
        stepPreferences.save(updated)
        checkStepAchievements(delta)
    }

    private fun todayDateString(): String =
        LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

    // ─────────────────────────────── Reminders ───────────────────────────────

    val reminderSettings: StateFlow<ReminderPreferences.ReminderSettings> =
        reminderPreferences.settings
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ReminderPreferences.ReminderSettings())

    fun saveReminderSettings(settings: ReminderPreferences.ReminderSettings) {
        viewModelScope.launch {
            reminderPreferences.saveSettings(settings)
            ReminderScheduler.applySettings(appContext, settings)
        }
    }

    // ─────────────────────────────── Achievements ────────────────────────────

    val achievements = achievementRepository.achievements
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentStreak: StateFlow<Int> = workouts
        .mapLatest { list ->
            StreakCalculator.currentStreak(list.map { it.date })
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val longestStreak: StateFlow<Int> = workouts
        .mapLatest { list ->
            StreakCalculator.longestStreak(list.map { it.date })
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    private suspend fun recalculateAchievements() {
        val allWorkouts = workouts.value
        val totalWorkouts = allWorkouts.size
        val totalCalories = allWorkouts.sumOf { it.calories }
        val streak = StreakCalculator.currentStreak(allWorkouts.map { it.date })

        // Workout count milestones
        achievementRepository.updateProgress("workouts_10", totalWorkouts)
        achievementRepository.updateProgress("workouts_25", totalWorkouts)
        achievementRepository.updateProgress("workouts_50", totalWorkouts)

        // Calorie milestone
        achievementRepository.updateProgress("calories_5000", totalCalories)

        // Streak milestones
        achievementRepository.updateProgress("streak_3",  streak)
        achievementRepository.updateProgress("streak_7",  streak)
        achievementRepository.updateProgress("streak_14", streak)
        achievementRepository.updateProgress("streak_30", streak)

        // Daily workout goal
        val todayCount = todayWorkouts.value.size
        val goalCount  = goals.value.workoutCountGoal
        if (todayCount >= goalCount) {
            achievementRepository.unlock("workout_goal_first")
        }
    }

    private suspend fun checkStepAchievements(todaySteps: Int) {
        val goal = goals.value.stepGoal
        achievementRepository.updateProgress("step_goal_first", if (todaySteps >= goal) 1 else 0)
        if (todaySteps >= goal) achievementRepository.unlock("step_goal_first")
    }

    private suspend fun checkHydrationAchievements(totalMl: Int) {
        val goal = goals.value.waterGoalMl
        if (totalMl >= goal) {
            achievementRepository.unlock("hydration_goal_first")
            // Re-check 7-day hydration achievement
            val daysMet = hydrationRepository.countDaysMeetingGoal(goal)
            achievementRepository.updateProgress("hydration_7days", daysMet)
        }
    }

    // ─────────────────────────────── Initialisation ──────────────────────────

    init {
        viewModelScope.launch { achievementRepository.seedIfEmpty() }
        startCollectingHydration()
    }

    override fun onCleared() {
        super.onCleared()
        unregisterStepSensor()
    }
}