package com.jcoding.expensetracker.ui.currency

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jcoding.expensetracker.R
import com.jcoding.expensetracker.base.BaseViewModel
import com.jcoding.expensetracker.data.source.AppRepository
import com.jcoding.expensetracker.ui.currency.list.CurrencyListAdapterItem
import com.jcoding.expensetracker.util.ResultState
import com.jcoding.expensetracker.util.resourceprovider.ResourceProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrencyListViewModel(
    private val resourceProvider: ResourceProvider,
    private val appRepository: AppRepository
) : BaseViewModel() {
    //////////////////////////////////////////////////////////////////////////////////////
    private var selectedCurrencyItem: CurrencyListAdapterItem? = null

    //////////////////////////////////////////////////////////////////////////////////////
    private val currencyListAdapterItemList = ArrayList<CurrencyListAdapterItem>()
    private val _currencyListLiveData =
        MutableLiveData<ResultState<List<CurrencyListAdapterItem>>>()

    fun currencyListLiveData(): LiveData<ResultState<List<CurrencyListAdapterItem>>> =
        _currencyListLiveData

    fun fetchCurrencyDetails() {


        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            _currencyListLiveData.value = ResultState.Error(
                resourceProvider.getString(R.string.errorSomethingWentWrong)
            )
        }

        viewModelScope.launch(exceptionHandler) {
            val response = appRepository.fetchCurrencyList()
            if (response.isSuccess()) {
                val resultList = response.requireData()
                appRepository.fetchSelectedCurrency().first().let { selectedItem ->
                    selectedItem?.let {
                        selectedCurrencyItem = CurrencyListAdapterItem.fromCurrencyItem(it)
                        resultList.forEach { listItem ->
                            currencyListAdapterItemList.add(
                                CurrencyListAdapterItem.fromCurrencyItem(listItem).apply {
                                    isSelected =
                                        listItem.id == requireNotNull(selectedCurrencyItem).id
                                }
                            )
                        }
                        _currencyListLiveData.value = ResultState.Success(
                            currencyListAdapterItemList.toMutableList()
                        )
                    }
                }

            } else {
                throw Exception()
            }
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////

    fun selectCurrency(item: CurrencyListAdapterItem) {
        if (item.isSelected) {
            return
        }
        selectedCurrencyItem = item
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                for (i in 0 until currencyListAdapterItemList.size) {
                    val listItem = currencyListAdapterItemList[i]
                    if (listItem.isSelected) {
                        currencyListAdapterItemList[i] = listItem.copy(isSelected = false)
                        continue
                    }
                    if (item.id == listItem.id) {
                        currencyListAdapterItemList[i] = listItem.copy(isSelected = !item.isSelected)
                    }
                }

                appRepository.saveSelectedCurrency(
                    requireNotNull(selectedCurrencyItem).id.toString()
                )
                _currencyListLiveData.postValue(
                    ResultState.Success(currencyListAdapterItemList.toMutableList())
                )
            }
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////


    init {
        fetchCurrencyDetails()
    }
}