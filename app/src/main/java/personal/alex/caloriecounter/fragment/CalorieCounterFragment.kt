package personal.alex.caloriecounter.fragment

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.calorie_counter_frag.*
import org.eazegraph.lib.models.PieModel
import personal.alex.caloriecounter.R
import personal.alex.caloriecounter.helpers.Formatter
import personal.alex.caloriecounter.helpers.Formatter.percentFormatter
import personal.alex.caloriecounter.models.CurrentDay
import personal.alex.caloriecounter.models.IntakeMetric
import personal.alex.caloriecounter.viewModels.CalorieCounterViewModel
import java.util.*

class CalorieCounterFragment() : Fragment() {
    private var displayMonth = 0
    private var displayDay = 0
    private var displayYear = 0

    private val formatter = Formatter

    private var calorieCounterViewModel: CalorieCounterViewModel? = null

    fun newInstance(page: Int, title: String) : CalorieCounterFragment
    {
        val frag = CalorieCounterFragment()
        val args = Bundle()
        args.putInt("1", page)
        args.putString("CalorieCounterFragment", title)
        frag.arguments = args
        return frag
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.calorie_counter_frag, container, false)

        calorieCounterViewModel = ViewModelProvider(this).get(CalorieCounterViewModel::class.java)

        //Date Display
        val selectedDate = view.findViewById<TextView>(R.id.SelectedDateTV)
        //Buttons
        val calendarButton = view.findViewById<ImageButton>(R.id.GoToCalendarBtn)
        val saveInputBtn = view.findViewById<ImageButton>(R.id.SaveInputBtn)
        //Input Layouts
        val calorieInputLayout = view.findViewById<TextInputLayout>(R.id.CalorieInputLayout)
        val carbInputLayout  = view.findViewById<TextInputLayout>(R.id.CarbInputLayout)
        val fatInputLayout = view.findViewById<TextInputLayout>(R.id.FatInputLayout)
        val proteinInputLayout = view.findViewById<TextInputLayout>(R.id.ProteinInputLayout)
        val inputLayouts : List<TextInputLayout> = listOf(calorieInputLayout, carbInputLayout, fatInputLayout, proteinInputLayout)




        //Set Date Related stuff
        val c = Calendar.getInstance()
        calorieCounterViewModel!!.insertCurrentDay(
            CurrentDay(1, formatter.concatCalenderDate(c.get(Calendar.MONTH).toString(),c.get(Calendar.DAY_OF_MONTH).toString(),c.get(Calendar.YEAR).toString()) )
        )

        calorieCounterViewModel!!.getDay()!!.observe(viewLifecycleOwner,  androidx.lifecycle.Observer { day ->
            Log.d("CC Date Observed",day.day)

            setDisplayDate(selectedDate, day)

            calorieCounterViewModel!!.getTodayMetrics(day.day)!!.observe(viewLifecycleOwner,  androidx.lifecycle.Observer { metric ->
                Log.d("CC Calories Observed",metric.calories.toString())
                Log.d("CC Carbs Observed",metric.carbs.toString())
                Log.d("CC Fats Observed",metric.fats.toString())
                Log.d("CC Protein Observed",metric.proteins.toString())
                Log.d("CC Metric Date Observed",metric.date.toString())
                setDailyMetric(inputLayouts, metric)
            })
        })


        calendarButton.setOnClickListener {

            context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    { _, pickYear, pickMonth, pickDay ->
                        calorieCounterViewModel!!.insertCurrentDay(CurrentDay(1,
                            formatter.concatCalenderDate(pickMonth.toString(),pickDay.toString(),pickYear.toString())
                            ))
                        Log.d("CC Current Date: ", calorieCounterViewModel!!.getDay()?.value.toString())
                    },
                    displayYear,
                    formatter.monthConverterMinus(displayMonth),
                    displayDay
                )
            }?.show()
        }


        saveInputBtn.setOnClickListener {
            val input = IntakeMetric(
                0,
                checkEmpty(CalorieInput.text.toString()),
                checkEmpty(CarbInput.text.toString()),
                checkEmpty(FatInput.text.toString()),
                checkEmpty(ProteinInput.text.toString()),
                getDate()
            )

            calorieCounterViewModel!!.insertMetric(input)

            resetInputs()
        }

        Log.d("Current Date: ", calorieCounterViewModel!!.getCurrentDay()?.toString())
        return view
    }

    private fun resetInputs() {
        CalorieInput.text?.clear()
        CarbInput.text?.clear()
        FatInput.text?.clear()
        ProteinInput.text?.clear()
    }

    private fun checkEmpty(value: String): Int {
        return if (value == "") {
            0
        } else {
            value.toInt()
        }
    }


    private fun getDate(): String {
        return "${displayMonth}$displayDay$displayYear"
    }

    private fun setDisplayDate(selectedDate: TextView, day: CurrentDay) {
        displayMonth = day.day.take(2).toInt()
        displayDay = day.day.take(4).takeLast(2).toInt()
        displayYear = day.day.takeLast(4).toInt()
        val setDate =  StringBuilder().append(displayMonth).append("/").append(displayDay).append("/").append(displayYear)
        selectedDate.text = setDate
    }

    private fun setDailyMetric(inputLayouts: List<TextInputLayout>, dailyMetric: IntakeMetric)
    {


        if (dailyMetric.calories != null) {
            val total = dailyMetric.carbs!! + dailyMetric.fats!! + dailyMetric.proteins!!

            inputLayouts[0].hint = dailyMetric.calories?.let { formatter.numberFormatter(it) }
            inputLayouts[1].hint = dailyMetric.carbs?.let { formatter.numberFormatter(it) } + "g - " + formatter.percentFormatter((dailyMetric.carbs!!.toDouble() / total))
            inputLayouts[2].hint = dailyMetric.fats?.let { formatter.numberFormatter(it) } + "g - " + percentFormatter((dailyMetric.fats!!.toDouble() / total))
            inputLayouts[3].hint = dailyMetric.proteins?.let { formatter.numberFormatter(it) } + "g - " + percentFormatter(dailyMetric.proteins!!.toDouble() / total)


            setData(dailyMetric)
        }
        else
        {
            CalorieInputLayout.hint = "0"
            CarbInputLayout.hint = "0"
            FatInputLayout.hint = "0"
            ProteinInputLayout.hint = "0"

            metric_pie_chart.clearChart()
        }
    }



    private fun setData(dailyMetric: IntakeMetric) {
        metric_pie_chart.clearChart()

        Log.d("MainActivity","SetData")

        if (dailyMetric != null) {
                metric_pie_chart.addPieSlice(
                    dailyMetric.carbs?.toFloat()?.let {
                        PieModel(
                            "carbs", it,
                            Color.parseColor("#66BB6A")
                        )
                    }
                )
                metric_pie_chart.addPieSlice(
                    dailyMetric.fats?.toFloat()?.let {
                        PieModel(
                            "Fats", it,
                            Color.parseColor("#FFA726")
                        )
                    }
                )
                metric_pie_chart.addPieSlice(
                    dailyMetric.proteins?.toFloat()?.let {
                        PieModel(
                            "Protein", it,
                            Color.parseColor("#EF5350")
                        )
                    }
                )

        }

        // To animate the pie chart
        metric_pie_chart.startAnimation()
    }


}
