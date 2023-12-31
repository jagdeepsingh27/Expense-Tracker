package com.jcoding.expensetracker.ui.dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.jcoding.expensetracker.R
import java.util.Calendar

class TimePickerDialogFragment(private val callback: Callback) : DialogFragment(),
    TimePickerDialog.OnTimeSetListener {
    interface Callback {
        fun onTimeSet(hourOfDay: Int, minute: Int)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(
            requireActivity(), R.style.TimePickerStyle,
            this,
            hour,
            minute,
            DateFormat.is24HourFormat(requireActivity())
        )
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        callback.onTimeSet(hourOfDay,minute)
    }
}