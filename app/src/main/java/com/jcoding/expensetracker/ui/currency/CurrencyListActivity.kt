package com.jcoding.expensetracker.ui.currency

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jcoding.expensetracker.R
import com.jcoding.expensetracker.base.BaseActivity
import com.jcoding.expensetracker.custom.view.contentlayout.ContentStatus
import com.jcoding.expensetracker.databinding.CurrencyListActivityBinding
import com.jcoding.expensetracker.ui.currency.list.CurrencyListAdapter
import com.jcoding.expensetracker.ui.currency.list.CurrencyListAdapterItem
import com.jcoding.expensetracker.util.ResultState
import com.jcoding.expensetracker.util.VerticalSpaceRecyclerItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyListActivity : BaseActivity<CurrencyListActivityBinding>(
    CurrencyListActivityBinding::inflate
) {
    companion object {
        fun start(activity: FragmentActivity) {
            activity.startActivity(Intent(activity, CurrencyListActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }
    }

    private val currencyListAdapter by lazy { CurrencyListAdapter() }
    private val currencyListViewModel: CurrencyListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ////////////////////////////////////////////////////////////////////////////////
        initRecyclerView()
        ////////////////////////////////////////////////////////////////////////////////
        //click listener
        binding.backArrowImageView.setOnClickListener { finish() }
        currencyListAdapter.setItemClickListener { onCurrencyListItemClick(it) }
        //content layout callback
        binding.contentLayout.setOnRetryClickListener { onRetryClick() }
        ////////////////////////////////////////////////////////////////////////////////


        ////////////////////////////////////////////////////////////////////////////////
        //observer
        currencyListViewModel.currencyListLiveData().observe(this) {
            onCurrencyListResult(it)
        }

        ///////////////////////////////////////////////////////////////////////////////////////////
        //start progress
        binding.contentLayout.setContentStatus(ContentStatus.Loading(true))

    }
    /////////////////////////////////////////////////////////////////////////////////////////
    private fun initRecyclerView() {
        binding.listRecyclerView.addItemDecoration(VerticalSpaceRecyclerItemDecoration(8))
        binding.listRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.listRecyclerView.adapter = currencyListAdapter
    }


    private fun onCurrencyListItemClick(item: CurrencyListAdapterItem) {
        currencyListViewModel.selectCurrency(item)
    }

    private fun onRetryClick() {
        binding.contentLayout.setContentStatus(ContentStatus.Loading(true))
        currencyListViewModel.fetchCurrencyDetails()
    }


    private fun onCurrencyListResult(result: ResultState<List<CurrencyListAdapterItem>>) {
        when (result) {
            is ResultState.Success -> {
                if (result.data.isEmpty()) {
                    binding.contentLayout.setContentStatus(
                        ContentStatus.DataNotAvailable(getString(R.string.noDataAvailable))
                    )
                } else {
                    binding.contentLayout.setContentStatus(ContentStatus.DataAvailable)
                }
                currencyListAdapter.submitList(result.data)
            }
            is ResultState.Error -> {
                binding.contentLayout.setContentStatus(ContentStatus.Error(result.message))
            }
        }
    }
}