package com.jcoding.expensetracker.ui.expensedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jcoding.expensetracker.R
import com.jcoding.expensetracker.base.BaseViewModel
import com.jcoding.expensetracker.data.model.ExpenseItem
import com.jcoding.expensetracker.data.source.AppRepository
import com.jcoding.expensetracker.util.EventBusWork
import com.jcoding.expensetracker.util.ResultState
import com.jcoding.expensetracker.util.resourceprovider.ResourceProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus


class ExpenseDetailsViewModel(
    private val expenseId: String,
    private val resourceProvider: ResourceProvider,
    private val appRepository: AppRepository
) : BaseViewModel() {

    private var expenseItem: ExpenseItem? = null

    fun getExpenseItem() = requireNotNull(expenseItem)
    /////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////
    private val _expenseDetailsResultLiveData = MutableLiveData<ResultState<ExpenseItem>>()
    fun expenseDetailsResultLiveData(): LiveData<ResultState<ExpenseItem>> =
        _expenseDetailsResultLiveData

    fun fetchExpenseDetails() {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            _expenseDetailsResultLiveData.value = ResultState.Error(
                resourceProvider.getString(R.string.errorSomethingWentWrong)
            )
        }
        viewModelScope.launch(exceptionHandler) {
            val response = appRepository.fetchExpenseDetails(expenseId)
            if (response.isSuccess()) {
                expenseItem = response.requireData()
                _expenseDetailsResultLiveData.value = ResultState.Success(
                    requireNotNull(expenseItem)
                )
            } else {
                throw Exception()
            }
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////
    private val _deleteExpenseResultLiveData = MutableLiveData<ResultState<Unit>>()
    fun deleteExpenseResultLiveData(): LiveData<ResultState<Unit>> = _deleteExpenseResultLiveData

    var deleteJob : Job? =null
    fun deleteExpense() {
        deleteJob?.cancel()

        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            _deleteExpenseResultLiveData.value = ResultState.Error(
                resourceProvider.getString(R.string.errorSomethingWentWrong)
            )
        }
        deleteJob = viewModelScope.launch(exceptionHandler) {
            val response = appRepository.deleteExpenseItem(requireNotNull(expenseItem))
            if (response.isSuccess()) {
                //post event
                EventBus.getDefault().postSticky(EventBusWork.DeleteExpenseItem(expenseId))
                _deleteExpenseResultLiveData.value = ResultState.Success(Unit,
                    message = resourceProvider.getString(R.string.messageExpenseItemDeletedSuccess))
            } else {
                throw Exception()
            }
        }
    }



    /////////////////////////////////////////////////////////////////////////////////////////
    fun refreshData(){
        fetchExpenseDetails()
    }

    init {
        fetchExpenseDetails()
    }
}