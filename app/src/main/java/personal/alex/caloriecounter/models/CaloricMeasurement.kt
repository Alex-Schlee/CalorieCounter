package personal.alex.caloriecounter.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "IntakeMetric")
data class IntakeMetric(
    @PrimaryKey(autoGenerate = true) val intakeId: Int = 0,
    @ColumnInfo(name = "calories") val calories: Int?,
    @ColumnInfo(name = "carbs") val carbs: Int?,
    @ColumnInfo(name = "fats") val fats: Int?,
    @ColumnInfo(name = "proteins") val proteins: Int?,
    @ColumnInfo(name = "date") val date: String?
)

