package com.nullgr.androidcore.date

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nullgr.androidcore.R
import com.nullgr.core.date.CommonFormats
import com.nullgr.core.date.isToday
import com.nullgr.core.date.minusDay
import com.nullgr.core.date.plusMonths
import com.nullgr.core.date.toDate
import com.nullgr.core.date.toStringWithFormat
import com.nullgr.core.date.withoutTime
import kotlinx.android.synthetic.main.activity_date_utils_example.*
import java.util.*

/**
 * Created by Grishko Nikita on 01.02.18.
 */
class DateUtilsExampleActivity : AppCompatActivity() {

    private var selectedDate = System.currentTimeMillis().toDate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_utils_example)

        bindSelectedDate()

        buttonSelectDate.setOnClickListener {
            val initialCalendar = Calendar.getInstance()
            initialCalendar.time = selectedDate

            val datePickerDialog = DatePickerDialog(this,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        calendar.set(Calendar.MONTH, month)
                        calendar.set(Calendar.YEAR, year)
                        selectedDate = calendar.time
                        bindSelectedDate()

                    }, initialCalendar[Calendar.YEAR], initialCalendar[Calendar.MONTH], initialCalendar[Calendar.DAY_OF_MONTH])
            datePickerDialog.show()
        }

        buttonFormat1.setOnClickListener {
            showResult(selectedDate.toStringWithFormat(CommonFormats.FORMAT_TIME))
        }

        buttonFormat2.setOnClickListener {
            showResult(selectedDate.toStringWithFormat(CommonFormats.FORMAT_SIMPLE_DATE_TIME))
        }

        buttonFormat3.setOnClickListener {
            showResult(selectedDate.toStringWithFormat(CommonFormats.FORMAT_DATE_WITH_MONTH_NAME))
        }

        buttonIsToday.setOnClickListener {
            showResult(selectedDate.isToday().toString())
        }

        buttonPlusMonth.setOnClickListener {
            val newDate = selectedDate plusMonths 1
            showResult(newDate.toStringWithFormat(CommonFormats.FORMAT_STANDARD_DATE_TIME))
        }

        buttonMinus3Days.setOnClickListener {
            val newDate = selectedDate minusDay 3
            showResult(newDate.toStringWithFormat(CommonFormats.FORMAT_STANDARD_DATE_TIME))
        }

        buttonWithoutTime.setOnClickListener {
            val newDate = selectedDate.withoutTime()
            showResult(newDate.toStringWithFormat(CommonFormats.FORMAT_STANDARD_DATE_TIME))
        }
    }

    private fun bindSelectedDate() {
        selectedDateTitle.text = selectedDate.toStringWithFormat(CommonFormats.FORMAT_SIMPLE_DATE_TIME)
    }

    private fun showResult(result: String) {
        AlertDialog.Builder(this)
                .setMessage(result)
                .setPositiveButton(R.string.btn_ok, { dialog, _ -> dialog?.dismiss() })
                .show()
    }
}