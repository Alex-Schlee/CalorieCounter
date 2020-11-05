package personal.alex.caloriecounter.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import personal.alex.caloriecounter.models.CurrentDay
import personal.alex.caloriecounter.models.IntakeMetric
import personal.alex.caloriecounter.repos.CalorieIntakeRepo

class CalorieCounterViewModel(application: Application) : AndroidViewModel(application) {

    private var repo:CalorieIntakeRepo = CalorieIntakeRepo(application)

    private var currentDay: LiveData<CurrentDay>? = null

    init {
        currentDay = getCurrentDay()
    }

    fun getTodayMetrics(date: String) = repo.getTodayMetrics(date)

    fun getTodayCount(date: String) = repo.getTodayCount(date)

    fun getTodayCaloires(date: String) = repo.getTodayCalories(date)

    fun insertMetric(metric: IntakeMetric) = repo.insertMetric(metric)

    fun getDay(): LiveData<CurrentDay>?
    {
        return currentDay
    }

    fun getCurrentDay() = repo.getCurrentDay()

    fun insertCurrentDay(date: CurrentDay) = repo.insertCurrentDay(date)
}
