package personal.alex.caloriecounter.database

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import personal.alex.caloriecounter.dao.CalorieIntakeDao
import personal.alex.caloriecounter.dao.CurrentDayDao
import personal.alex.caloriecounter.models.CurrentDay
import personal.alex.caloriecounter.models.IntakeMetric

@Database(entities = [IntakeMetric::class, CurrentDay::class], version = 4)
abstract class AppDatabase() : RoomDatabase() {
    abstract fun IntakeDao():CalorieIntakeDao
    abstract fun CurrentDayDao():CurrentDayDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if(instance == null) {
                synchronized(AppDatabase::class.java) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build()
                }
            }
            return instance
        }

        private val roomCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                instance?.let { PopulateDbAsyncTask(it).execute() }
            }
        }

        private open class PopulateDbAsyncTask(db: AppDatabase) :
            AsyncTask<Void?, Void?, Void?>() {
            private val currentDayDao: CurrentDayDao = db.CurrentDayDao()
            override fun doInBackground(vararg params: Void?): Void? {
                currentDayDao.insertCurrentDay(CurrentDay(1,"01011970"))
                return null
            }

        }
    }
}
