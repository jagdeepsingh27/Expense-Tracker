package com.jcoding.expensetracker.ui.addeditexpense

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentActivity
import com.jcoding.expensetracker.AppConstants
import com.jcoding.expensetracker.R
import com.jcoding.expensetracker.base.BaseActivity
import com.jcoding.expensetracker.data.model.ExpenseItem
import com.jcoding.expensetracker.data.source.local.staticdataprovider.ExpenseCategory
import com.jcoding.expensetracker.data.source.local.staticdataprovider.ExpensePaymentMethod
import com.jcoding.expensetracker.databinding.AddEditExpenseActivityBinding
import com.jcoding.expensetracker.ui.addeditexpense.expensecategorypicker.ExpenseCategoryPickerBottomSheet
import com.jcoding.expensetracker.ui.addeditexpense.paymentmethodpicker.PaymentMethodPickerBottomSheet
import com.jcoding.expensetracker.ui.dialog.DatePickerDialogFragment
import com.jcoding.expensetracker.ui.dialog.TimePickerDialogFragment
import com.jcoding.expensetracker.util.ResultState
import com.jcoding.expensetracker.util.UtilMethods
import java.text.SimpleDateFormat
import java.util.Locale

class AddEditExpenseActivity : BaseActivity<AddEditExpenseActivityBinding>(
    AddEditExpenseActivityBinding::inflate
) {
    companion object {
        fun start(
            activity: FragmentActivity,
            actionMode: AddEditActivityActionMode,
            expenseId: String? = null
        ) {
            activity.startActivity(Intent(activity, AddEditExpenseActivity::class.java).apply {
                putExtra(AddEditActivityStartUpParamsKey.EXTRA_KEY_ACTION_MODE, actionMode)
                putExtra(AddEditActivityStartUpParamsKey.EXTRA_KEY_EXPENSE_ID, expenseId)
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }

    }

    private val addEditExpenseViewModel: AddEditExpenseViewModel by viewModels {
        AddEditExpenseViewModelFactory(intent)
    }


    private var datePickerDialogFragment: DatePickerDialogFragment? = null
    private var timePickerDialogFragment: TimePickerDialogFragment? = null
    private val categoryPicker: ExpenseCategoryPickerBottomSheet by lazy {
        ExpenseCategoryPickerBottomSheet()
    }
    private val paymentMethodPicker: PaymentMethodPickerBottomSheet by lazy {
        PaymentMethodPickerBottomSheet()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /////////////////////////////////////////////////////////////
        //binding
        setInputFieldFilter()
        //set text watcher
        setInputFieldTextWatcher()
        /*##################################################################################*/
        //init
        datePickerDialogFragment = DatePickerDialogFragment(datePickerDialogFragmentCallback)
        timePickerDialogFragment = TimePickerDialogFragment(timePickerDialogFragmentCallback)
        /*##################################################################################*/
        //set screen title
        binding.screenTitleTextView.text = if(addEditExpenseViewModel.isEditMode()){
            getString(R.string.titleEditExpense)
        }else{
            getString(R.string.titleAddExpense)
        }
        /*##################################################################################*/
        //click listener
        initClickListener()
        /*##################################################################################*/
        /* observer **/
        addEditExpenseViewModel.selectedDateResultLiveData().observe(this) {
            setDate(it)
        }
        addEditExpenseViewModel.selectedTimeResultLiveData().observe(this) {
            setTime(it)
        }
        addEditExpenseViewModel.saveExpenseResultLiveData().observe(this) {
            onSaveResult(it)
        }
        addEditExpenseViewModel.requiredResourceResultLiveData().observe(this) {
            onRequiredResourceResult(it)
        }
        /*##################################################################################*/
    }
    private fun setInputFieldFilter() {
        //set amount input filter
        binding.amountEditText.filters = arrayOf<InputFilter>(
            // DecimalDigitsInputFilter(),
            InputFilter.LengthFilter(AppConstants.MAX_EXPENSE_AMOUNT_DIGIT_INPUT_LIMIT)
        )
        //set description filter
        binding.descriptionEditText.filters = arrayOf<InputFilter>(
            InputFilter.LengthFilter(AppConstants.MAX_EXPENSE_DESCRIPTION_CHAR_INPUT_LIMIT)
        )
        binding.descriptionEditText.maxLines = AppConstants.MAX_EXPENSE_DESCRIPTION_MAX_LINES

        //set title filter
        binding.titleEditText.filters = arrayOf<InputFilter>(
            InputFilter.LengthFilter(AppConstants.MAX_EXPENSE_TITLE_CHAR_INPUT_LIMIT)
        )
    }

    private fun setInputFieldTextWatcher() {
        binding.amountEditText.addTextChangedListener {
            addEditExpenseViewModel.setAmount(it?.toString()?.trim())
        }
        binding.descriptionEditText.addTextChangedListener {
            addEditExpenseViewModel.setDescription(it?.toString()?.trim())
        }
        binding.titleEditText.addTextChangedListener {
            addEditExpenseViewModel.setTitle(it?.toString()?.trim())
        }
    }

    private fun initClickListener() {
        //back arrow click
        binding.backArrowImageView.setOnClickListener { finish() }
        //on date view click
        binding.selectedDateTextView.setOnClickListener { onDateViewClick() }
        //on time view click
        binding.selectedTimeTextView.setOnClickListener { onTimeViewClick() }
        //on save button click
        binding.saveButton.setOnClickListener {
            onSaveClick()
        }
        //category click
        binding.categoryTextView.setOnClickListener {
            categoryPicker.show(supportFragmentManager, "category_picker")
        }
        //category picker listener
        categoryPicker.addCategorySelectedListener { onCategorySelected(it) }
        //payment click
        binding.paymentMethodTextView.setOnClickListener {
            paymentMethodPicker.show(supportFragmentManager, "payment_method")
        }
        //payment picker callback
        paymentMethodPicker.addPaymentMethodSelectedListener { onPaymentMethodSelected(it) }
    }

    /*##################################################################################*/
    //DATE SELECTION
    private fun onDateViewClick() {
        datePickerDialogFragment?.show(
            supportFragmentManager, "datePickerDialog"
        )
    }

    private val datePickerDialogFragmentCallback = object : DatePickerDialogFragment.Callback {
        override fun onDateSet(year: Int, month: Int, dayOfMonth: Int,requestCode: Int) {
            datePickerDialogFragment?.dismiss()
            addEditExpenseViewModel.setDate(year, month, dayOfMonth)
        }
    }

    private fun setDate(text: String) {
        binding.selectedDateTextView.text = text
    }

    /*##################################################################################*/
    //TIME SELECTION
    private fun onTimeViewClick() {
        timePickerDialogFragment?.show(supportFragmentManager, "timePickerDialog")
    }

    private val timePickerDialogFragmentCallback = object : TimePickerDialogFragment.Callback {
        override fun onTimeSet(hourOfDay: Int, minute: Int) {
            timePickerDialogFragment?.dismiss()
            addEditExpenseViewModel.setTime(hourOfDay, minute)
        }
    }

    private fun setTime(text: String) {
        binding.selectedTimeTextView.text = text
    }

    /*##################################################################################*/
    /*##################################################################################*/
    private fun onSaveClick() {
        progressLoader.show()
        addEditExpenseViewModel.saveExpense()
    }

    private fun onSaveResult(resultState: ResultState<Unit>) {
        progressLoader.hide()
        when (resultState) {
            is ResultState.Success -> {
                showToast(resultState.message)
                UtilMethods.hideKeyboard(this)
                finish()
            }
            is ResultState.Error -> {
                showToast(resultState.message)
            }
        }
    }

    /*##################################################################################*/
    private fun onRequiredResourceResult(result: ResultState<AddEditExpenseRequiredResourceResult>) {
        when (result) {
            is ResultState.Success -> {
                categoryPicker.addCategoryList(result.data.expenseCategoryList)
                paymentMethodPicker.addPaymentMethodList(result.data.expensePaymentMethodList)
                if (addEditExpenseViewModel.isEditMode()) {
                    initExistingDetails(requireNotNull(result.data.existingExpenseItem))
                }
            }
            is ResultState.Error -> {
                showToast(result.message)
            }
        }
    }

    /** this method is called for init the existing details in the view in case of edit mode */
    private fun initExistingDetails(expenseItem: ExpenseItem) {
        //set title
        binding.titleEditText.setText(expenseItem.title)
        //set amount
        binding.amountEditText.setText(expenseItem.amount)
        //set description
        binding.descriptionEditText.setText(expenseItem.description)
        //set category
        binding.categoryTextView.text = getString(expenseItem.category.nameStringResId)
        //set category icon
        binding.categoryTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
            ContextCompat.getDrawable(this, expenseItem.category.drawableResId),
            null, null, null
        )
        //set payment method
        binding.paymentMethodTextView.text = getString(expenseItem.paymentMethod.nameResId)
        //set date
        binding.selectedDateTextView.text =
            SimpleDateFormat(AppConstants.DATE_FORMAT, Locale.getDefault()).format(expenseItem.date)

        //set time
        binding.selectedTimeTextView.text =
            SimpleDateFormat(AppConstants.TIME_FORMAT, Locale.getDefault()).format(expenseItem.date)
    }


    private fun onCategorySelected(category: ExpenseCategory) {
        categoryPicker.dismiss()
        binding.categoryTextView.text = getString(category.nameStringResId)
        binding.categoryTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
            ContextCompat.getDrawable(this, category.drawableResId),
            null, null, null
        )
        addEditExpenseViewModel.setExpenseCategory(category)
    }

    private fun onPaymentMethodSelected(item: ExpensePaymentMethod) {
        paymentMethodPicker.dismiss()
        binding.paymentMethodTextView.text = getString(item.nameResId)
        addEditExpenseViewModel.setPaymentMethod(item)
    }

    /*##################################################################################*/
}