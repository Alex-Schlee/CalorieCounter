package personal.alex.caloriecounter.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import personal.alex.caloriecounter.R
import personal.alex.caloriecounter.helpers.Formatter
import personal.alex.caloriecounter.models.IntakeMetric

class CalorieCounterAdapter(private val context: Context,private val metric: IntakeMetric) : RecyclerView.Adapter<CalorieCounterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calorie_counter_frag, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val total = metric.carbs!! + metric.fats!! + metric.proteins!!
        val formatter = Formatter

        holder.calorieInput.hint = metric.calories?.let { formatter.numberFormatter(it)}
        holder.carbInput.hint = metric.carbs?.let { formatter.numberFormatter(it)} + "g - " + formatter.percentFormatter(metric.carbs.toDouble()/total)
        holder.fatInput.hint = metric.fats?.let { formatter.numberFormatter(it)} + "g - " + formatter.percentFormatter(metric.fats.toDouble()/total)
        holder.proteinInput.hint = metric.proteins?.let { formatter.numberFormatter(it)} + "g - " + formatter.percentFormatter(metric.proteins.toDouble()/total)

        holder.metric_pie_chart.clearChart()

        if (metric.calories != null) {
            holder.metric_pie_chart.addPieSlice(
                PieModel(
                    "Carbs", metric.carbs.toFloat(),
                    Color.parseColor("#66BB6A")
                )
            )
            holder.metric_pie_chart.addPieSlice(
                PieModel(
                    "Fats", metric.fats.toFloat(),
                    Color.parseColor("#FFA726")
                )
            )
            holder.metric_pie_chart.addPieSlice(
                PieModel(
                    "Protein", metric.proteins.toFloat(),
                    Color.parseColor("#EF5350")
                )
            )

        }

                        // To animate the pie chart
        holder.metric_pie_chart.startAnimation()
    }

    override fun getItemCount(): Int {
        return 4
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var calorieInput: TextInputEditText = itemView.findViewById(R.id.CalorieInput)
        var carbInput: TextInputEditText = itemView.findViewById(R.id.CarbInput)
        var fatInput: TextInputEditText = itemView.findViewById(R.id.FatInput)
        var proteinInput: TextInputEditText = itemView.findViewById(R.id.ProteinInput)

        var metric_pie_chart: PieChart = itemView.findViewById(R.id.metric_pie_chart)

        var saveButton: ImageButton = itemView.findViewById(R.id.SaveInputBtn)
    }

}