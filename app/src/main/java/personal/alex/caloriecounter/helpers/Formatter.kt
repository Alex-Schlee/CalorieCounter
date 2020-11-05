package personal.alex.caloriecounter.helpers

import java.text.DecimalFormat

object Formatter {
    fun numberFormatter(number: Int) : String
    {
        return "%,d".format(number)
    }

    fun percentFormatter(number: Double) : String
    {
        return DecimalFormat("##.#%").format(number).toString()
    }

    fun monthConverterPlus(month: Int) : Int
    {
        return month + 1
    }

    fun monthConverterMinus(month: Int) : Int
    {
        return month - 1
    }

    fun concatCalenderDate(month: String, day: String, year: String) : String
    {
        return StringBuilder()
            .append(monthConverterPlus(formatDayMonth(month).toInt()))
            .append(formatDayMonth(day))
            .append(year).toString()
    }

    private fun formatDayMonth(dateNum: String) : String
    {
        return if(dateNum.length < 2)
        {
            StringBuilder().append("0").append(dateNum).toString()
        } else
        {
            dateNum
        }
    }

}