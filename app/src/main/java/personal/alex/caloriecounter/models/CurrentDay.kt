package personal.alex.caloriecounter.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CurrentDay")
data class CurrentDay(
    @PrimaryKey(autoGenerate = false) val dayId: Int = 1,
    @ColumnInfo(name = "day") val day: String
)
