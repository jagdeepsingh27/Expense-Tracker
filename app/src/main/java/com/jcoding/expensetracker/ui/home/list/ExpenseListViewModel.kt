package com.jcoding.expensetracker.ui.home.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jcoding.expensetracker.AppConstants
import com.jcoding.expensetracker.R
import com.jcoding.expensetracker.base.BaseViewModel
import com.jcoding.expensetracker.data.model.ExpenseItem
import com.jcoding.expensetracker.data.source.AppRepository
import com.jcoding.expensetracker.data.source.local.staticdataprovider.CurrencyItem
import com.jcoding.expensetracker.ui.home.list.adapter.ExpenseListAdapterItem
import com.jcoding.expensetracker.util.ResultState
import com.jcoding.expensetracker.util.resourceprovider.ResourceProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ExpenseListViewModel(
    private val resourceProvider: ResourceProvider,
    private val appRepository: AppRepository
) : BaseViewModel() {
    ///////////////////////////////////////////////////////////////////////////////////////
    private var currencySymbol: String? = null

    //FILTER SECTION
    private var isListFilterEnabled = false
    private var startDate: Date? = null
    private var endDate: Date? = null


    private val _listFilterEnabledStatusLiveData = MutableLiveData<Boolean>()
    fun listFilterEnableStatusLiveData(): LiveData<Boolean> = _listFilterEnabledStatusLiveData

    fun toggleListFilterStatus() {
        isListFilterEnabled = !isListFilterEnabled
        _listFilterEnabledStatusLiveData.value = isListFilterEnabled

        /** if filter disabled **/
        if (isListFilterEnabled.not()) {
            startDate = null
            endDate = null
            setDateFilterData()
        }
    }

    private val _listFilterDateLiveData = MutableLiveData<ListFilterDateData>()
    fun listFilterDateLiveData(): LiveData<ListFilterDateData> = _listFilterDateLiveData

    fun setDate(year: Int, month: Int, dayOfMonth: Int, isStartDate: Boolean) {
        val calender = Calendar.getInstance()
        calender.set(Calendar.YEAR, year)
        calender.set(Calendar.MONTH, month)
        calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        calender.set(Calendar.HOUR_OF_DAY, 0)
        calender.set(Calendar.MINUTE, 0)
        calender.set(Calendar.SECOND, 0)
        if (isStartDate) {
            calender.set(Calendar.HOUR_OF_DAY, 0)
            calender.set(Calendar.MINUTE, 0)
            calender.set(Calendar.SECOND, 0)
            startDate = calender.time
        } else {
            calender.set(Calendar.HOUR_OF_DAY, 23)
            calender.set(Calendar.MINUTE, 59)
            calender.set(Calendar.SECOND, 59)
            endDate = calender.time
        }
        setDateFilterData()
    }

    private fun setDateFilterData() {
        val listFilterDateData = ListFilterDateData()
        val simpleDateFormat = SimpleDateFormat(AppConstants.DATE_FORMAT, Locale.getDefault())
        startDate?.let {
            listFilterDateData.startDate = simpleDateFormat.format(it)
        }
        endDate?.let {
            listFilterDateData.endDate = simpleDateFormat.format(it)
        }
        _listFilterDateLiveData.value = listFilterDateData
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    private var expenseList = ArrayList<ExpenseListAdapterItem>()
    private val _expenseListResultLiveData =
        MutableLiveData<ResultState<List<ExpenseListAdapterItem>>>()

    fun expenseListLiveData(): LiveData<ResultState<List<ExpenseListAdapterItem>>> =
        _expenseListResultLiveData

    private fun fetchExpenseList() {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            _expenseListResultLiveData.value =
                ResultState.Error(resourceProvider.getString(R.string.errorSomethingWentWrong))
        }
        viewModelScope.launch(exceptionHandler) {
            val result = appRepository.fetchExpenseList(startDate, endDate)
            if (result.isSuccess()) {
                expenseList.clear()
                result.requireData().forEach {
                    expenseList.add(ExpenseListAdapterItem.fromExpenseItemModel(it, currencySymbol))
                }
                _expenseListResultLiveData.value = ResultState.Success(
                    expenseList.toMutableList()
                )
                calculateTotalExpenses()
            } else {
                throw Exception()
            }
        }
    }


    fun updateExpenseItem(_item: ExpenseItem) {
        val item = ExpenseListAdapterItem.fromExpenseItemModel(_item, currencySymbol)
        viewModelScope.launch(Dispatchers.Default) {
            for (i in 0 until expenseList.size) {
                val listItem = expenseList[i]
                if (item.id == listItem.id) {
                    expenseList[i] = item
                    calculateTotalExpenses()
                    _expenseListResultLiveData.postValue(
                        ResultState.Success(expenseList.toMutableList())
                    )
                    break
                }
            }
        }
    }


    fun addNewExpenseItem(_item: ExpenseItem) {
        if (isListFilterEnabled) {
            refreshContent()
            return
        }
        val item = ExpenseListAdapterItem.fromExpenseItemModel(_item, currencySymbol)
        expenseList.add(0, item)
        calculateTotalExpenses()
        _expenseListResultLiveData.value = ResultState.Success(expenseList.toMutableList())
    }


    fun deleteExpenseItem(itemId: String) {
        viewModelScope.launch(Dispatchers.Default) {
            for (i in 0 until expenseList.size) {
                val listItem = expenseList[i]
                if (itemId == listItem.id) {
                    expenseList.removeAt(i)
                    break
                }
            }
            calculateTotalExpenses()
            _expenseListResultLiveData.postValue(
                ResultState.Success(expenseList.toMutableList())
            )
        }
    }

    /*##########################################################################*/
    /*##########################################################################*/
    /** total expense calculations **/
    private var totalExpenses: BigDecimal = BigDecimal(0)


    private val _totalExpensesStateFlow = MutableStateFlow<String?>(null)
    fun totalExpensesStateFlow(): StateFlow<String?> = _totalExpensesStateFlow

    private fun calculateTotalExpenses() {
        viewModelScope.launch(Dispatchers.Default) {
            totalExpenses = BigDecimal(0)
            expenseList.forEach {
                val amount = it.amount.toFloat().toBigDecimal()
                totalExpenses = totalExpenses.add(amount)
            }
            _totalExpensesStateFlow.value = getResolvedCurrencyValue(
                currencySymbol,
                totalExpenses.toString()
            )
        }
    }


    /*##########################################################################*/
    ///////////////////////////////////////////////////////////////////////////////////////
    fun applyDateFilter() {
        fetchExpenseList()
    }

    fun retryFetchContent() {
        fetchExpenseList()
    }

    private fun refreshContent() {
        fetchExpenseList()
    }


    private fun updateCurrency(currency: CurrencyItem) {
        if (currencySymbol != currency.symbol) {
            currencySymbol = currency.symbol

            //set symbol on total expense
            _totalExpensesStateFlow.value = getResolvedCurrencyValue(
                currencySymbol,
                totalExpenses.toString()
            )

            ///////////update currency symbol in list
            for (i in 0 until expenseList.size) {
                val listItem = expenseList[i].copy(currencySymbol = currencySymbol)
                expenseList[i] = listItem
            }
            _expenseListResultLiveData.value = ResultState.Success(
                expenseList.toMutableList()
            )
        }
    }
    /*##########################################################################*/

    init {
        _listFilterEnabledStatusLiveData.value = isListFilterEnabled
        fetchExpenseList()
        /** for selecting default currency **/
        viewModelScope.launch {
            appRepository.fetchSelectedCurrency().collect { currencyItem ->
                if (currencyItem == null) {
                    val currencyListResult = appRepository.fetchCurrencyList()
                    if (currencyListResult.isSuccess().not()) {
                        return@collect
                    }
                    appRepository.saveSelectedCurrency(
                        currencyListResult.requireData().first().id.toString()
                    )
                } else {
                    updateCurrency(currencyItem)
                }
            }
        }
    }
}