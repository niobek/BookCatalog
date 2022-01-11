package com.montismobile.booklibrary.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.montismobile.booklibrary.bookdetail.DATE_CHOOSEN
import com.montismobile.booklibrary.bookdetail.DATE_TYPE_CHOOSEN
import com.montismobile.booklibrary.bookdetail.REQUEST_DATE
import com.montismobile.booklibrary.main.DATE_TYPE
import java.util.*

private const val ARG_DATE = "date"
private const val ARG_DATE_TYPE ="dateType"
class DatePickerFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = arguments?.getSerializable(ARG_DATE) as Date
        val dateType = arguments?.getSerializable(ARG_DATE_TYPE) as DATE_TYPE
        val calendar = Calendar.getInstance()
        calendar.time = date
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        val dateListener = DatePickerDialog.OnDateSetListener{
            _ : DatePicker, year:Int, month:Int, day:Int->
            val resultDate:Date = GregorianCalendar(year,month,day).time
            parentFragmentManager.setFragmentResult(REQUEST_DATE, bundleOf(DATE_CHOOSEN to resultDate, DATE_TYPE_CHOOSEN to dateType))
        }

        return DatePickerDialog(
                requireContext(), // context object
                dateListener, // listener
                initialYear,
                initialMonth,
                initialDay
        )
    }

    companion object {
        fun newInstance(date:Date, dateType:DATE_TYPE) : DatePickerFragment {
            val args = Bundle().apply {  putSerializable(ARG_DATE, date)
            putSerializable(ARG_DATE_TYPE, dateType)}
            return DatePickerFragment().apply { arguments = args }
        }
    }
}