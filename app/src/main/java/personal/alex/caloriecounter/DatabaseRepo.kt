package personal.alex.caloriecounter

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class DatabaseRepo {

    object CaloricMetricContract {
        object MetricEntry : BaseColumns {
            const val TABLE_NAME = "IntakeMetric"
            const val COLUMN_NAME_CALORIES = "calories"
            const val COLUMN_NAME_CARBS = "carbs"
            const val COLUMN_NAME_FATS = "fats"
            const val COLUMN_NAME_PROTEINS = "proteins"
            const val COLUMN_NAME_DATE = "date"
        }

    private const val SQL_CREATE_METRICS = "CREATE TABLE ${MetricEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${MetricEntry.COLUMN_NAME_CALORIES} INTEGER," +
            "${MetricEntry.COLUMN_NAME_CARBS} INTEGER," +
            "${MetricEntry.COLUMN_NAME_FATS} INTEGER," +
            "${MetricEntry.COLUMN_NAME_PROTEINS} INTEGER, " +
            "${MetricEntry.COLUMN_NAME_DATE} STRING)"

    private const val SQL_DELETE_METRICS = "DROP TABLE IF EXISTS ${MetricEntry.TABLE_NAME}"

   private const val SQL_SSP_TODAY_METRICS = "SELECT " +
           "SUM(${MetricEntry.COLUMN_NAME_CALORIES}) AS ${MetricEntry.COLUMN_NAME_CALORIES}," +
           "SUM(${MetricEntry.COLUMN_NAME_CARBS}) AS ${MetricEntry.COLUMN_NAME_CARBS}," +
           "SUM(${MetricEntry.COLUMN_NAME_FATS}) AS ${MetricEntry.COLUMN_NAME_FATS}," +
           "SUM(${MetricEntry.COLUMN_NAME_PROTEINS}) AS ${MetricEntry.COLUMN_NAME_PROTEINS}, " +
           "${MetricEntry.COLUMN_NAME_DATE} " +
           "FROM " +
           "${MetricEntry.TABLE_NAME} " +
           "WHERE " +
           "${MetricEntry.COLUMN_NAME_DATE} = ? " +
           "GROUP BY " +
           "${MetricEntry.COLUMN_NAME_DATE}"

    class CaloricMetricDBHelper(context: Context) : SQLiteOpenHelper(context, "CalMetricDB", null, 1) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_METRICS)
        }
        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_METRICS)
            onCreate(db)
        }
        override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            onUpgrade(db, oldVersion, newVersion)
        }

        fun getTodayMetrics(date: String) : CaloricMeasurement?
        {
            val db = this.writableDatabase
            db.rawQuery(SQL_SSP_TODAY_METRICS, arrayOf(date)).use {
                if(it.moveToFirst()) {
                    var metric = CaloricMeasurement()
                    metric.Calories = it.getInt(it.getColumnIndex(MetricEntry.COLUMN_NAME_CALORIES))
                    metric.Carbs = it.getInt(it.getColumnIndex(MetricEntry.COLUMN_NAME_CARBS))
                    metric.Fats = it.getInt(it.getColumnIndex(MetricEntry.COLUMN_NAME_FATS))
                    metric.Protein = it.getInt(it.getColumnIndex(MetricEntry.COLUMN_NAME_PROTEINS))
                    return  metric
                }
            }
            return null
        }

        companion object {
            // If you change the database schema, you must increment the database version.
            const val DATABASE_VERSION = 1
            const val DATABASE_NAME = "CalMetric.db"
        }
    }
    }


}