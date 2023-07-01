package com.jcoding.expensetracker.ui.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.jcoding.expensetracker.R
import java.util.Calendar

class DatePickerDialogFragment(private val callback: Callback) : DialogFragment(),
    DatePickerDialog.OnDateSetListener {
    interface Callback {
        fun onDateSet(year: Int, month: Int, dayOfMonth: Int, requestCode: Int)
    }

    private var requestCode: Int = 0


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(requireActivity(), R.style.DatePickerStyle,this, year, month, day)
        //for current date
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
        return datePickerDialog
    }


    fun show(fragmentManager: FragmentManager, tag: String?, requestCode: Int = 0) {
        this.requestCode = requestCode
        super.show(fragmentManager, tag)
    }


    override fun onDateSet(
        view: android.widget.DatePicker?, year: Int, month: Int, dayOfMonth: Int
    ) {
        callback.onDateSet(year, month, dayOfMonth, requestCode)
    }

}