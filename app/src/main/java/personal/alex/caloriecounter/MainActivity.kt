package personal.alex.caloriecounter

import android.app.DatePickerDialog
import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.eazegraph.lib.models.PieModel
import java.util.*


class MainActivity : AppCompatActivity() {
        var displayMonth = 0
        var displayDay = 0
        var displayYear = 0
        //Database stuff

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dbHelper = DatabaseRepo.CaloricMetricContract.CaloricMetricDBHelper(this)
        val db = dbHelper.writableDatabase

        //Set Date Related stuff
        val c = Calendar.getInstance()
        setDisplayDate(c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR))
        setDailyMetric(dbHelper.getTodayMetrics(parseDate(getDate())))

        GoToCalendarBtn.setOnClickListener{
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, pickYear, pickMonth, pickDay ->
                    setDisplayDate(pickMonth, pickDay, pickYear)
                    setDailyMetric(dbHelper.getTodayMetrics(parseDate(getDate())))
                },
                displayYear,
                displayMonth,
                displayDay
            )

            dpd.show()
        }


        CalorieIdTV.text = "Calories: "
        CarbIdTV.text = "Carbs: "
        FatIdTV.text = "Fats: "
        ProteinIdTV.text = "Protein: "


        SaveInputBtn.setOnClickListener {
            val input = CaloricMeasurement(
                CalorieInput.text.toString().toInt(),
                CarbInput.text.toString().toInt(),
                FatInput.text.toString().toInt(),
                ProteinInput.text.toString().toInt()
            );

            val values = ContentValues().apply {
                put(
                    DatabaseRepo.CaloricMetricContract.MetricEntry.COLUMN_NAME_CALORIES,
                    CalorieInput.text.toString().toInt()
                )
                put(
                    DatabaseRepo.CaloricMetricContract.MetricEntry.COLUMN_NAME_CARBS,
                    CarbInput.text.toString().toInt()
                )
                put(
                    DatabaseRepo.CaloricMetricContract.MetricEntry.COLUMN_NAME_FATS,
                    FatInput.text.toString().toInt()
                )
                put(
                    DatabaseRepo.CaloricMetricContract.MetricEntry.COLUMN_NAME_PROTEINS,
                    ProteinInput.text.toString().toInt()
                )
                put(
                    DatabaseRepo.CaloricMetricContract.MetricEntry.COLUMN_NAME_DATE, parseDate(
                        getDate()
                    )
                )
            }

            db?.insert(DatabaseRepo.CaloricMetricContract.MetricEntry.TABLE_NAME, null, values)
            setDailyMetric(dbHelper.getTodayMetrics(parseDate(getDate())))
        }


        /* BARCODE SCANNER STUFF
        BarCodeScanBtn.setOnClickListener {
            val intentIntegrator = IntentIntegrator(this@MainActivity)
            intentIntegrator.setBeepEnabled(false)
            intentIntegrator.setCameraId(0)
            intentIntegrator.setPrompt("SCAN")
            intentIntegrator.setBarcodeImageEnabled(false)
            intentIntegrator.initiateScan()
        }
        */
    }


    private fun getDate() : String
    {
        return "${monthConverter(displayMonth)}/$displayDay/$displayYear"
    }

    private fun parseDate(date: String) : String
    {
        return "${date.replace("/", "")}"
    }

    private fun setDisplayDate(month: Int, day: Int, year: Int)
    {
        displayMonth = month
        displayDay = day
        displayYear = year
        SelectedDateTV.text = "${monthConverter(month)}/$day/$year"
    }

    private fun setDailyMetric(dailyMetric: CaloricMeasurement?)
    {
        if (dailyMetric != null) {
            DailyMetricCaloriesTV.text = "Calories: ${dailyMetric.Calories}"
            DailyMetricCarbsTV.text = "Carbs: ${dailyMetric.Carbs}"
            DailyMetricFatsTV.text = "Fats ${dailyMetric.Fats}"
            DailyMetricProteinsTV.text = "Protein ${dailyMetric.Protein}"
            setData(dailyMetric)
        }
        else
        {
            DailyMetricCaloriesTV.text = "Calories: 0"
            DailyMetricCarbsTV.text = "Carbs: 0"
            DailyMetricFatsTV.text = "Fats: 0"
            DailyMetricProteinsTV.text = "Protein 0"

            metric_pie_chart.clearChart()
            metric_pie_chart.startAnimation()
        }
    }

    private fun monthConverter(month: Int) : Int
    {
        return month + 1
    }


    private fun setData(dailyMetric: CaloricMeasurement) {
        metric_pie_chart.clearChart()

        metric_pie_chart.addPieSlice(
            PieModel(
                "Carbs", dailyMetric.Carbs.toFloat(),
                Color.parseColor("#66BB6A")
            )
        )
        metric_pie_chart.addPieSlice(
            PieModel(
                "Fats", dailyMetric.Fats.toFloat(),
                Color.parseColor("#FFA726")
            )
        )
        metric_pie_chart.addPieSlice(
            PieModel(
                "Protein", dailyMetric.Protein.toFloat(),
                Color.parseColor("#EF5350")
            )
        )

        // To animate the pie chart
        metric_pie_chart.startAnimation()
    }

    /* BARCODE SCANNER STUFF
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("MainActivity", "Scanned")
                Toast.makeText(this, "Scanned -> " + result.contents, Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    */


}
