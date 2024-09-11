package com.example.ehcf.Notification.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ehcf.CreateSlot.Adapter.CalendarAdapter
import com.example.ehcf.databinding.ActivityCalanderTestBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CalanderTest : AppCompatActivity() {
    private lateinit var binding:ActivityCalanderTestBinding
    private val lastDayInCalendar = Calendar.getInstance(Locale.ENGLISH)
    private val sdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    private val cal = Calendar.getInstance(Locale.ENGLISH)

    // current date
    private val currentDate = Calendar.getInstance(Locale.ENGLISH)
    private val currentDay = currentDate[Calendar.DAY_OF_MONTH]
    private val currentMonth = currentDate[Calendar.MONTH]
    private val currentYear = currentDate[Calendar.YEAR]

    // selected date
    private var selectedDay: Int = currentDay
    private var selectedMonth: Int = currentMonth
    private var selectedYear: Int = currentYear

    // all days in month
    private val dates = ArrayList<Date>()

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalanderTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

       Log.e("Ala","dates$dates")

        /**
         * Adding SnapHelper here, but it is not needed. I add it just to looks better.
         */
//        val snapHelper = LinearSnapHelper()
//        snapHelper.attachToRecyclerView(calre)

        /**
         * This is the maximum month that the calendar will display.
         * I set it for 6 months, but you can increase or decrease as much you want.
         */
        lastDayInCalendar.add(Calendar.MONTH, 6)

        setUpCalendar()

        /**
         * Go to the previous month. First, make sure the current month (cal)
         * is after the current date so that you can't go before the current month.
         * Then subtract  one month from the sludge. Finally, ask if cal is equal to the current date.
         * If so, then you don't want to give @param changeMonth, otherwise changeMonth as cal.
         */
        binding.calendarPrevButton!!.setOnClickListener {
            if (cal.after(currentDate)) {
                cal.add(Calendar.MONTH, -1)
                if (cal == currentDate)
                    setUpCalendar()
                else
                    setUpCalendar(changeMonth = cal)
            }
        }

        /**
         * Go to the next month. First check if the current month (cal) is before lastDayInCalendar,
         * so that you can't go after the last possible month. Then add one month to cal.
         * Then put @param changeMonth.
         */
        binding.calendarNextButton!!.setOnClickListener {
            if (cal.before(lastDayInCalendar)) {
                cal.add(Calendar.MONTH, 1)
                setUpCalendar(changeMonth = cal)
            }
        }
    }

    /**
     * @param changeMonth I am using it only if next or previous month is not the current month
     */
    private fun setUpCalendar(changeMonth: Calendar? = null) {
        binding.txtCurrentMonth!!.text = sdf.format(cal.time)
        val monthCalendar = cal.clone() as Calendar
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        /**
         *
         * If changeMonth is not null, then I will take the day, month, and year from it,
         * otherwise set the selected date as the current date.
         */
        selectedDay =
            when {
                changeMonth != null -> changeMonth.getActualMinimum(Calendar.DAY_OF_MONTH)
                else -> currentDay
            }
        selectedMonth =
            when {
                changeMonth != null -> changeMonth[Calendar.MONTH]
                else -> currentMonth
            }
        selectedYear =
            when {
                changeMonth != null -> changeMonth[Calendar.YEAR]
                else -> currentYear
            }

        var currentPosition = 0
        dates.clear()
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)

        /**
         * Fill dates with days and set currentPosition.
         * currentPosition is the position of first selected day.
         */
        while (dates.size < maxDaysInMonth) {
            // get position of selected day
            if (monthCalendar[Calendar.DAY_OF_MONTH] == selectedDay)
                currentPosition = dates.size
            dates.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        // Assigning calendar view.
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.calendarRecyclerView!!.layoutManager = layoutManager
        val calendarAdapter = CalendarAdapter(this, dates, currentDate, changeMonth)
        binding.calendarRecyclerView!!.adapter = calendarAdapter

        /**
         * If you start the application, it centers the current day, but only if the current day
         * is not one of the first (1, 2, 3) or one of the last (29, 30, 31).
         */
        when {
            currentPosition > 2 ->  binding.calendarRecyclerView!!.scrollToPosition(currentPosition - 3)
            maxDaysInMonth - currentPosition < 2 ->  binding.calendarRecyclerView!!.scrollToPosition(currentPosition)
            else ->  binding.calendarRecyclerView!!.scrollToPosition(currentPosition)
        }


        /**
         * After calling up the OnClickListener, the text of the current month and year is changed.
         * Then change the selected day.
         */
        calendarAdapter.setOnItemClickListener(object : CalendarAdapter.OnItemClickListener {
            @SuppressLint("LogNotTimber")
            override fun onItemClick(position: Int) {
                val clickCalendar = Calendar.getInstance()

                clickCalendar.time = dates[position]
                selectedDay = clickCalendar[Calendar.DAY_OF_MONTH]




            }
        })
    }
}