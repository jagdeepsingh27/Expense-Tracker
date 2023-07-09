package com.jcoding.expensetracker.ui.addeditexpense

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.jcoding.expensetracker.AppConstants
import com.jcoding.expensetracker.R
import com.jcoding.expensetracker.base.BaseViewModel
import com.jcoding.expensetracker.data.model.ExpenseItem
import com.jcoding.expensetracker.data.model.SaveExpenseItemResponse
import com.jcoding.expensetracker.data.source.AppRepository
import com.jcoding.expensetracker.data.source.local.staticdataprovider.ExpenseCategory
import com.jcoding.expensetracker.data.source.local.staticdataprovider.ExpensePaymentMethod
import com.jcoding.expensetracker.util.EventBusWork
import com.jcoding.expensetracker.util.ResultState
import com.jcoding.expensetracker.util.resourceprovider.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddEditExpenseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val resourceProvider: ResourceProvider,
    private val appRepository: AppRepository
) : BaseViewModel() {
    private var actionMode: AddEditActivityActionMode =
        savedStateHandle.get<AddEditActivityActionMode>(
            AddEditActivityStartUpParamsKey.EXTRA_KEY_ACTION_MODE
        ) ?: throw RuntimeException("action mode required")
    private var expenseId: String? =
        savedStateHandle.get<String?>(AddEditActivityStartUpParamsKey.EXTRA_KEY_EXPENSE_ID)


    private var selectedDateCalendar: Calendar = Calendar.getInstance()
    private var title: String? = null
    private var amount: String? = null
    private var description: String? = null
    private var expenseCategory: ExpenseCategory? = null
    private var paymentMethod: ExpensePaymentMethod? = null

    fun setAmount(value: String?) {
        this.amount = value
    }

    fun setDescription(text: String?) {
        this.description = text
    }

    fun setTitle(text: String?) {
        this.title = text
    }

    fun setExpenseCategory(_category: ExpenseCategory?) {
        this.expenseCategory = _category
    }

    fun setPaymentMethod(_paymentMethod: ExpensePaymentMethod?) {
        this.paymentMethod = _paymentMethod
    }


    /*##########################################################################*/
    //DATE SECTION
    private val _selectedDateLiveData = MutableLiveData<String>()
    fun selectedDateResultLiveData(): LiveData<String> = _selectedDateLiveData

    fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        selectedDateCalendar.set(Calendar.YEAR, year)
        selectedDateCalendar.set(Calendar.MONTH, month)
        selectedDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateDate()
    }

    private fun updateDate() {
        val simpleDateFormat = SimpleDateFormat(AppConstants.DATE_FORMAT, Locale.getDefault())
        val dateString = simpleDateFormat.format(requireNotNull(selectedDateCalendar.time))
        _selectedDateLiveData.value = dateString
    }

    /*##########################################################################*/
    //TIME SECTION
    private val _selectedTimeLiveData = MutableLiveData<String>()
    fun selectedTimeResultLiveData(): LiveData<String> = _selectedTimeLiveData

    fun setTime(hour: Int, minute: Int) {
        selectedDateCalendar.set(Calendar.HOUR_OF_DAY, hour)
        selectedDateCalendar.set(Calendar.MINUTE, minute)
        updateTime()
    }

    private fun updateTime() {
        val simpleDateFormat = SimpleDateFormat(AppConstants.TIME_FORMAT, Locale.getDefault())
        val dateString = simpleDateFormat.format(requireNotNull(selectedDateCalendar.time))
        _selectedTimeLiveData.value = dateString
    }

    /*##########################################################################*/
    /*##########################################################################*/
    /* ######################################################################################### */
    /** FOR EDIT MODE fetch existing details **/
    var existingExpenseItem: ExpenseItem? = null

    private val _requiredResourceResultLiveData =
        MutableLiveData<ResultState<AddEditExpenseRequiredResourceResult>>()

    fun requiredResourceResultLiveData(): LiveData<ResultState<AddEditExpenseRequiredResourceResult>> =
        _requiredResourceResultLiveData


    private fun fetchRequiredResources() {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            _requiredResourceResultLiveData.value = ResultState.Error(
                resourceProvider.getString(R.string.errorSomethingWentWrong)
            )
        }
        viewModelScope.launch(exceptionHandler) {
            //fetch category list first
            val categoryListResult = appRepository.fetchExpenseCategoryList()
            if (categoryListResult.isSuccess().not()) {
                throw Exception("fetch category list result error")
            }
            //fetch payment method list
            val paymentMethodListResult = appRepository.fetchPaymentMethodList()
            if (paymentMethodListResult.isSuccess().not()) {
                throw Exception("fetch payment method list result error")
            }

            /*##########################################################################*/
            /** fetch existing expense item is performed in case of edit mode */
            if (isEditMode()) {
                val existingExpenseItemResponse = appRepository.fetchExpenseDetails(
                    requireNotNull(expenseId)
                )
                if (existingExpenseItemResponse.isSuccess().not()) {
                    throw Exception("fetch existing expenseItem  result error")
                }
                existingExpenseItem = existingExpenseItemResponse.requireData()
                initExistingExpenseItemDetails()
            }
            /*##########################################################################*/

            _requiredResourceResultLiveData.value = ResultState.Success(
                AddEditExpenseRequiredResourceResult(
                    expenseCategoryList = categoryListResult.requireData(),
                    expensePaymentMethodList = paymentMethodListResult.requireData(),
                    existingExpenseItem = existingExpenseItem
                )
            )


        }
    }

    /*##########################################################################*/
    /*##########################################################################*/
    /*##########################################################################*/
    /*##########################################################################*/
    /*##########################################################################*/
    /*##########################################################################*/
    //SAVE OPERATION
    private val _saveExpenseResultLiveData = MutableLiveData<ResultState<Unit>>()
    fun saveExpenseResultLiveData(): LiveData<ResultState<Unit>> = _saveExpenseResultLiveData


    private var saveExpenseJob: Job? = null
    fun saveExpense() {
        saveExpenseJob?.cancel()
        if (title.isNullOrBlank()) {
            _saveExpenseResultLiveData.value =
                ResultState.Error(resourceProvider.getString(R.string.errorAddTitle))
            return
        }

        if (amount.isNullOrBlank()) {
            _saveExpenseResultLiveData.value =
                ResultState.Error(resourceProvider.getString(R.string.errorAddAmount))
            return
        }

        if (description.isNullOrBlank()) {
            _saveExpenseResultLiveData.value =
                ResultState.Error(resourceProvider.getString(R.string.errorAddDescription))
            return
        }

        if (expenseCategory == null) {
            _saveExpenseResultLiveData.value =
                ResultState.Error(resourceProvider.getString(R.string.errorSelectExpenseCategory))
            return
        }


        if (paymentMethod == null) {
            _saveExpenseResultLiveData.value =
                ResultState.Error(
                    resourceProvider.getString(R.string.errorSelectExpensePaymentMethod)
                )
            return
        }

        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            _saveExpenseResultLiveData.value =
                ResultState.Error(resourceProvider.getString(R.string.errorSomethingWentWrong))
        }

        saveExpenseJob = viewModelScope.launch(exceptionHandler) {
            val date = selectedDateCalendar.time

            val expenseItem = ExpenseItem(
                title = requireNotNull(title),
                amount = requireNotNull(amount),
                description = requireNotNull(description),
                date = date,
                category = requireNotNull(expenseCategory),
                paymentMethod = requireNotNull(paymentMethod)
            )


            val response = if (isEditMode()) {
                expenseItem.id = requireNotNull(existingExpenseItem).id
                appRepository.updateExpenseItem(expenseItem)
            } else {
                appRepository.saveExpenseItem(expenseItem)
            }

            if (response.isSuccess()) {
                if (isEditMode()) {
                    EventBus.getDefault()
                        .postSticky(EventBusWork.UpdateExpenseItem(expenseItem))
                } else {
                    EventBus.getDefault().postSticky(
                        EventBusWork.AddsNewExpenseItem(
                            expenseItem.apply {
                                id = (response.requireData() as SaveExpenseItemResponse).id.toInt()
                            }
                        )
                    )
                }
                _saveExpenseResultLiveData.value =
                    ResultState.Success(
                        data = Unit,
                        message = resourceProvider.getString(R.string.messageExpenseItemSavedSuccess)
                    )
            } else {
                throw Exception()
            }
        }
    }

    /*##########################################################################*/
    /*##########################################################################*/
    /** init the existing expense item details **/
    private fun initExistingExpenseItemDetails() {
        val expenseItem = requireNotNull(existingExpenseItem)
        title = expenseItem.title
        amount = expenseItem.amount
        description = expenseItem.description
        expenseCategory = expenseItem.category
        paymentMethod = expenseItem.paymentMethod
        selectedDateCalendar = Calendar.getInstance().apply {
            time = expenseItem.date
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////
    fun isEditMode() = actionMode == AddEditActivityActionMode.EDIT

    /*##########################################################################*/
    /*##########################################################################*/
    init {

        if (actionMode == AddEditActivityActionMode.EDIT && expenseId.isNullOrBlank()) {
            throw RuntimeException("Expense Id required for Edit Mode")
        }
        //set default  date and time for add mode
        if (actionMode == AddEditActivityActionMode.ADD) {
            updateDate()
            updateTime()
        }
        fetchRequiredResources()
    }
}