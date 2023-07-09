package com.jcoding.expensetracker.ui.expensedetails

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.jcoding.expensetracker.AppConstants
import com.jcoding.expensetracker.R
import com.jcoding.expensetracker.base.BaseActivity
import com.jcoding.expensetracker.custom.view.contentlayout.ContentStatus
import com.jcoding.expensetracker.data.model.ExpenseItem
import com.jcoding.expensetracker.databinding.ExpenseDetailsActivityBinding
import com.jcoding.expensetracker.ui.addeditexpense.AddEditActivityActionMode
import com.jcoding.expensetracker.ui.addeditexpense.AddEditExpenseActivity
import com.jcoding.expensetracker.util.EventBusWork
import com.jcoding.expensetracker.util.ResultState
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class ExpenseDetailsActivity :  BaseActivity<ExpenseDetailsActivityBinding>(
    ExpenseDetailsActivityBinding::inflate
) {
    companion object {
         const val EXTRA_KEY_EXPENSE_ID = "extra.expense.id"

        fun start(activity: FragmentActivity, expenseItemId: String) {
            activity.startActivity(Intent(activity, ExpenseDetailsActivity::class.java).apply {
                putExtra(EXTRA_KEY_EXPENSE_ID, expenseItemId)
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }
    }


    private val expenseDetailsViewModel: ExpenseDetailsViewModel by viewModels()
    override fun onStart() {
        super.onStart()
        //event bus register
        EventBus.getDefault().register(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ///////////////////////////////////////////////////////////////////////////////////////////
        //click listener
        binding.backArrowImageView.setOnClickListener { finish() }
        //content layout callback
        binding.contentLayout.setOnRetryClickListener { onRetryClick() }
        //on delete button click
        binding.deleteButton.setOnClickListener { onDeleteClick() }
        //on edit click
        binding.editButton.setOnClickListener { onEditClick() }
        //////////////////////////////////////////////////////////////////////////////////////////
        //observer
        expenseDetailsViewModel.expenseDetailsResultLiveData().observe(this) {
            onExpenseDetailsResult(it)
        }
        expenseDetailsViewModel.deleteExpenseResultLiveData().observe(this) {
            onExpenseDeleteResult(it)
        }
        ///////////////////////////////////////////////////////////////////////////////////////////
        //start progress
        binding.contentLayout.setContentStatus(ContentStatus.Loading(true))

    }


    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


    private fun onRetryClick() {
        binding.contentLayout.setContentStatus(ContentStatus.Loading(true))
        expenseDetailsViewModel.fetchExpenseDetails()
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //DETAILS SECTION
    private fun onExpenseDetailsResult(result: ResultState<ExpenseItem>) {
        when (result) {
            is ResultState.Success -> {
                setDetails(result.data)
                binding.contentLayout.setContentStatus(ContentStatus.DataAvailable)
            }

            is ResultState.Error -> {
                binding.contentLayout.setContentStatus(ContentStatus.Error(result.message))
            }
        }
    }


    private fun setDetails(item: ExpenseItem) {
        //set title
        binding.expenseTitleTextView.text = item.title
        //set amount
        binding.expenseAmountTextView.text = item.amount
        //set description
        binding.expenseDescriptionTextView.text = item.description
        //set category
        binding.categoryTextView.text = getString(item.category.nameStringResId)
        binding.categoryTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
            ContextCompat.getDrawable(this, item.category.drawableResId),
            null, null, null
        )
        //set payment method
        binding.paymentMethodTextView.text = getString(item.paymentMethod.nameResId)
        //set date
        setDate(item.date)
        //set time
        setTime(item.date)
    }

    private fun setDate(date: Date) {
        val simpleDateFormat = SimpleDateFormat(AppConstants.DATE_FORMAT, Locale.getDefault())
        val dateString = simpleDateFormat.format(date)
        binding.expenseDateTextView.text = dateString
    }

    private fun setTime(date: Date) {
        val simpleDateFormat = SimpleDateFormat(AppConstants.TIME_FORMAT, Locale.getDefault())
        val dateString = simpleDateFormat.format(date)
        binding.expenseTimeTextView.text = dateString
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //delete section
    /* this will returns the delete confirmation alert dialog */
    private fun getDeleteConfirmationAlertDialog(onPositiveClick: () -> Unit): AlertDialog {
        return AlertDialog.Builder(this).apply {
            setTheme(R.style.CommonAlertDialogStyle)
            title = getString(R.string.deleteConfirmationDialogTitle)
            setMessage(getString(R.string.deleteConfirmationDialogMessage))
                .setPositiveButton(
                    getString(R.string.deleteConfirmationDialogPositiveActionLabel)
                ) { dialog, _ ->
                    dialog.dismiss()
                    onPositiveClick.invoke()
                }
                .setNegativeButton(
                    getString(R.string.deleteConfirmationDialogNegativeActionLabel)
                ) { dialog, _ ->
                    dialog.dismiss()
                }
        }.create()
    }

    private fun onDeleteClick() {
        getDeleteConfirmationAlertDialog {
            progressLoader.show()
            expenseDetailsViewModel.deleteExpense()
        }.show()
    }

    private fun onExpenseDeleteResult(result: ResultState<Unit>) {
        progressLoader.hide()
        when (result) {
            is ResultState.Success -> {
                showToast(result.message)
                finish()
            }

            is ResultState.Error -> {
                showToast(result.message)
            }
        }
    }


    private fun onEditClick() {
        AddEditExpenseActivity.start(
            this, AddEditActivityActionMode.EDIT,
            expenseDetailsViewModel.getExpenseItem().id.toString()
        )
    }

    /////////////////////////////////////////////////////////////////////////////////
    //event bus subscribers
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onExpenseItemUpdateEvent(event: EventBusWork.UpdateExpenseItem) {
        //start progress
        binding.contentLayout.setContentStatus(ContentStatus.Loading(true))
        expenseDetailsViewModel.refreshData()
    }

}