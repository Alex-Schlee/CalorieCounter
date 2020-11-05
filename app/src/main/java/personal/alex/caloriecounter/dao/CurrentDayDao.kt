package personal.alex.caloriecounter.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import personal.alex.caloriecounter.models.CurrentDay

@Dao
interface CurrentDayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentDay(day:CurrentDay)

    @Query("SELECT * FROM CurrentDay")
    fun getCurrentDay() : LiveData<CurrentDay>
}