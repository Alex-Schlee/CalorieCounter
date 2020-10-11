package personal.alex.caloriecounter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity

class CalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar_layout)

        val mCalendarActivity = findViewById<CalendarView>(R.id.calendarView)
        mCalendarActivity.setOnDateChangeListener() { _: CalendarView, year: Int, tempMonth: Int, day: Int ->
            val month = tempMonth + 1
            val date = "$month/$day/$year"
            Log.d("SetDate", "Date Set to: $date")
            val intent = Intent(this@CalendarActivity, MainActivity::class.java)
            intent.putExtra("selectDate", date)
            startActivity(intent)
        }
    }
}