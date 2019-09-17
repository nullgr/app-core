package com.nullgr.androidcore.date

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nullgr.androidcore.R
import com.nullgr.core.date.CommonFormats
import com.nullgr.core.date.atStartOfDay
import com.nullgr.core.date.isToday
import com.nullgr.core.date.toStringWithFormat
import kotlinx.android.synthetic.main.activity_date_utils_example.*
import org.threeten.bp.ZonedDateTime

/**
 * Created by Grishko Nikita on 01.02.18.
 */
class DateUtilsExampleActivity : AppCompatActivity() {

    private var selectedDate = ZonedDateTime.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_utils_example)

        bindSelectedDate()

        buttonSelectDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    selectedDate = selectedDate.withYear(year).withMonth(month).withDayOfMonth(dayOfMonth)
                    bindSelectedDate()

                }, selectedDate.year, selectedDate.month.value, selectedDate.dayOfMonth)
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
            showResult(selectedDate.toLocalDate().isToday().toString())
        }

        buttonPlusMonth.setOnClickListener {
            val newDate = selectedDate.plusMonths(1)
            showResult(newDate.toStringWithFormat(CommonFormats.FORMAT_STANDARD_DATE_TIME))
        }

        buttonMinus3Days.setOnClickListener {
            val newDate = selectedDate.minusDays(3)
            showResult(newDate.toStringWithFormat(CommonFormats.FORMAT_STANDARD_DATE_TIME))
        }

        buttonWithoutTime.setOnClickListener {
            val newDate = selectedDate.atStartOfDay()
            showResult(newDate.toStringWithFormat(CommonFormats.FORMAT_STANDARD_DATE_TIME))
        }
    }

    private fun bindSelectedDate() {
        selectedDateTitle.text = selectedDate.toStringWithFormat(CommonFormats.FORMAT_SIMPLE_DATE_TIME)
    }

    private fun showResult(result: String) {
        AlertDialog.Builder(this)
            .setMessage(result)
            .setPositiveButton(R.string.btn_ok) { dialog, _ -> dialog?.dismiss() }
            .show()
    }
}