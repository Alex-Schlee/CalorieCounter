package personal.alex.caloriecounter.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import personal.alex.caloriecounter.models.IntakeMetric

@Dao
interface CalorieIntakeDao {
    @Insert
    fun insert(metric: IntakeMetric)

    @Update
    fun update(metric: IntakeMetric)

    @Delete
    fun delete(metric: IntakeMetric)

    @Query("SELECT SUM(calories) AS calories, SUM(carbs) AS carbs, (fats) AS fats, SUM(proteins) AS proteins, :date as date, intakeId as intakeId FROM IntakeMetric WHERE date = :date" )
    fun getTodayMetrics(date: String): LiveData<IntakeMetric>

    @Query("SELECT count(*) intakeId FROM IntakeMetric WHERE date = :date GROUP BY date")
    fun getTodayCount(date: String): Int

    @Query("SELECT SUM(calories) FROM IntakeMetric WHERE date = :date")
    fun getTodayCalories(date: String): LiveData<Int>
}