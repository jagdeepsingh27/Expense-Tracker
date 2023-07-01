package com.jcoding.expensetracker.custom.view.expenseitemview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.jcoding.expensetracker.AppConstants
import com.jcoding.expensetracker.databinding.ExpenseItemViewLayoutBinding
import com.jcoding.expensetracker.ui.home.list.adapter.ExpenseListAdapterItem
import com.jcoding.expensetracker.ui.home.list.adapter.ExpenseListPayloadDiff
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ExpenseItemView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding: ExpenseItemViewLayoutBinding =
        ExpenseItemViewLayoutBinding.inflate(LayoutInflater.from(context), this)

    fun setContent(expenseItem: ExpenseListAdapterItem, payloadDiff: ExpenseListPayloadDiff) {
        //set title
        if(payloadDiff.titleChanged){
            binding.titleTextView.text = expenseItem.title
        }
        //set category
        if(payloadDiff.categoryChanged){
            binding.categoryTextView.text = context.getString(expenseItem.category.nameStringResId)
            //set category image
            binding.categoryIconImageView.setImageResource(expenseItem.category.drawableResId)
        }

        //set date
        if(payloadDiff.dateChanged){
            setDate(expenseItem.date)
        }
        //set amount
        if(payloadDiff.amountChanged || payloadDiff.currencySymbolChanged){
            val amount = expenseItem.amount.toBigDecimal().toString()
            val text =  "${expenseItem.currencySymbol ?: ""} $amount"
            binding.amountTextView.text = text
        }





        //set transaction type
        if(payloadDiff.paymentMethodChanged){
            binding.paymentMethodTypeTextView.text =
                context.getString(expenseItem.paymentMethod.nameResId)
        }
    }

    private fun setDate(date: Date){
        val simpleDateFormat = SimpleDateFormat(AppConstants.DATE_TIME_FORMAT, Locale.getDefault())
        val dateString = simpleDateFormat.format(date)
        binding.dateTextView.text = dateString
    }

}