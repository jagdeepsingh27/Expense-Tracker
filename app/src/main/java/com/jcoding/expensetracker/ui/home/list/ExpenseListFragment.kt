package com.jcoding.expensetracker.ui.home.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jcoding.expensetracker.R
import com.jcoding.expensetracker.base.BaseFragment
import com.jcoding.expensetracker.custom.view.contentlayout.ContentStatus
import com.jcoding.expensetracker.databinding.ExpenseListFragmentBinding
import com.jcoding.expensetracker.ui.dialog.DatePickerDialogFragment
import com.jcoding.expensetracker.ui.expensedetails.ExpenseDetailsActivity
import com.jcoding.expensetracker.ui.home.list.adapter.ExpenseListAdapter
import com.jcoding.expensetracker.ui.home.list.adapter.ExpenseListAdapterItem
import com.jcoding.expensetracker.util.ResultState
import com.jcoding.expensetracker.util.VerticalSpaceRecyclerItemDecoration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ExpenseListFragment :  BaseFragment<ExpenseListFragmentBinding>(
    ExpenseListFragmentBinding::inflate
) {
    companion object {
        private const val startDatePickerRequestCode = 1
        private const val endDatePickerRequestCode = 2
    }
    private val expenseListAdapter by lazy { ExpenseListAdapter() }
    private val expenseListViewModel: ExpenseListViewModel by activityViewModels()
    private var datePickerDialogFragment: DatePickerDialogFragment? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /////////////////////////////////////////////////////////////////////
        //init
        datePickerDialogFragment = DatePickerDialogFragment(datePickerDialogFragmentCallback)

        initRecyclerView()
        /////////////////////////////////////////////////////////////////////
        initClickListeners()
        /////////////////////////////////////////////////////////////////////
        //observer
        expenseListViewModel.expenseListLiveData().observe(viewLifecycleOwner) {
            onExpenseListResult(it)
        }
        //total expense
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                expenseListViewModel.totalExpensesStateFlow().collectLatest {
                    onTotalExpenseResult(it)
                }
            }
        }


        //list filter status
        expenseListViewModel.listFilterEnableStatusLiveData().observe(viewLifecycleOwner) {
            onListFilterStatusChanged(it)
        }
        //date result
        expenseListViewModel.listFilterDateLiveData().observe(viewLifecycleOwner) {
            onDateChangedResult(it)
        }

        //start progress
        binding().contentLayout.setContentStatus(ContentStatus.Loading(true))


    }

    /////////////////////////////////////////////////////////////////////////////////////////
    private fun initRecyclerView() {
        binding().listRecyclerView.addItemDecoration(VerticalSpaceRecyclerItemDecoration(8))
        binding().listRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding().listRecyclerView.adapter = expenseListAdapter
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    private fun initClickListeners() {
        //content layout callback
        binding().contentLayout.setOnRetryClickListener { onRetryClick() }
        //adapter callback
        expenseListAdapter.setItemClickListener { onExpenseListItemClick(it) }
        //on filter list option click
        binding().listFilterOptionImageView.setOnClickListener { onFilterOptionClick() }
        //on start date click
        binding().startDateTextView.setOnClickListener { onStartDateClick() }
        //on end date click
        binding().endDateTextView.setOnClickListener { onEndDateClick() }
    }


    /////////////////////////////////////////////////////////////////////////////////////////

    private fun onExpenseListResult(result: ResultState<List<ExpenseListAdapterItem>>) {
        when (result) {
            is ResultState.Success -> {
                if (result.data.isEmpty()) {
                    binding().contentLayout.setContentStatus(
                        ContentStatus.DataNotAvailable(getString(R.string.noDataAvailable))
                    )
                } else {
                    binding().contentLayout.setContentStatus(ContentStatus.DataAvailable)
                }
                expenseListAdapter.submitList(result.data)

                binding().listRecyclerView.post {
                    binding().listRecyclerView.scrollToPosition(0)
                }
            }
            is ResultState.Error -> {
                binding().contentLayout.setContentStatus(ContentStatus.Error(result.message))
            }
        }
    }


    private fun onRetryClick() {
        binding().contentLayout.setContentStatus(ContentStatus.Loading(true))
        expenseListViewModel.retryFetchContent()
    }


    private fun onExpenseListItemClick(item: ExpenseListAdapterItem) {
        ExpenseDetailsActivity.start(requireActivity(), item.id)
    }


    private fun onTotalExpenseResult(value: String?) {
        binding().totalExpenseAmountTextView.text = value
    }

    //////////////////////////////////////////////////////////////////////////////////////
    private fun onFilterOptionClick() {
        expenseListViewModel.toggleListFilterStatus()
    }

    private fun onListFilterStatusChanged(status: Boolean) {
        if (status) {
            binding().listFilterOptionImageView.setImageResource(R.drawable.ic_filte_list_on)
            binding().startDateTextView.isEnabled = true
            binding().endDateTextView.isEnabled = true
        } else {
            binding().listFilterOptionImageView.setImageResource(R.drawable.ic_filter_list_off)
            binding().startDateTextView.isEnabled = false
            binding().endDateTextView.isEnabled = false
        }
    }


    private fun onStartDateClick() {
        datePickerDialogFragment?.show(
            requireActivity().supportFragmentManager,
            "datePicker",
            startDatePickerRequestCode
        )
    }

    private fun onEndDateClick() {
        datePickerDialogFragment?.show(
            requireActivity().supportFragmentManager,
            "datePicker",
            endDatePickerRequestCode
        )
    }


    private val datePickerDialogFragmentCallback = object : DatePickerDialogFragment.Callback {
        override fun onDateSet(year: Int, month: Int, dayOfMonth: Int, requestCode: Int) {
            datePickerDialogFragment?.dismiss()
            when (requestCode) {
                startDatePickerRequestCode -> {
                    expenseListViewModel.setDate(year, month, dayOfMonth, true)
                }
                endDatePickerRequestCode -> {
                    expenseListViewModel.setDate(year, month, dayOfMonth, false)
                }
                else -> throw RuntimeException("invalid request code")
            }
        }
    }


    private fun onDateChangedResult(data: ListFilterDateData) {
        binding().startDateTextView.text = data.startDate ?: getString(R.string.labelStartDate)
        binding().endDateTextView.text = data.endDate ?: getString(R.string.labelEndDate)
        //start progress
        binding().contentLayout.setContentStatus(ContentStatus.Loading(true))
        expenseListViewModel.applyDateFilter()
    }

}