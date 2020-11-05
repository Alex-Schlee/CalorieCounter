package personal.alex.caloriecounter.repos

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import personal.alex.caloriecounter.dao.CalorieIntakeDao
import personal.alex.caloriecounter.dao.CurrentDayDao
import personal.alex.caloriecounter.database.AppDatabase
import personal.alex.caloriecounter.models.CurrentDay
import personal.alex.caloriecounter.models.IntakeMetric
import kotlin.coroutines.CoroutineContext

class CalorieIntakeRepo(application: Application) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var intakeDao : CalorieIntakeDao?
    private var currentDayDao: CurrentDayDao?

    init {
        val db = AppDatabase.getInstance(application)
        intakeDao = db?.IntakeDao()
        currentDayDao = db?.CurrentDayDao()
    }

    fun getTodayMetrics(date: String) = intakeDao?.getTodayMetrics(date)

    fun getTodayCalories(date: String) = intakeDao?.getTodayCalories(date)

    fun getTodayCount(date: String) = intakeDao?.getTodayCount(date)

    fun insertMetric(metric: IntakeMetric){
        launch { insertMetricBG(metric) }
    }


    private suspend fun insertMetricBG(metric: IntakeMetric){
        withContext(Dispatchers.IO){
            intakeDao?.insert(metric)
        }
    }


    fun getCurrentDay() = currentDayDao?.getCurrentDay()

    fun insertCurrentDay(day: CurrentDay){
        launch { insertDayBG(day) }
    }

    private suspend fun insertDayBG(day: CurrentDay){
        withContext(Dispatchers.IO){
            currentDayDao?.insertCurrentDay(day)
        }
    }

}
