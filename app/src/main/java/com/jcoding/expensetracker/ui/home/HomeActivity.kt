package com.jcoding.expensetracker.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import com.jcoding.expensetracker.base.BaseActivity
import com.jcoding.expensetracker.databinding.HomeActivityBinding
import com.jcoding.expensetracker.ui.addeditexpense.AddEditActivityActionMode
import com.jcoding.expensetracker.ui.addeditexpense.AddEditExpenseActivity
import com.jcoding.expensetracker.ui.home.list.ExpenseListFragment
import com.jcoding.expensetracker.ui.home.list.ExpenseListViewModel
import com.jcoding.expensetracker.ui.home.list.ExpenseListViewModelFactory
import com.jcoding.expensetracker.util.EventBusWork
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HomeActivity : BaseActivity<HomeActivityBinding>(HomeActivityBinding::inflate) {

    private val expenseListViewModel: ExpenseListViewModel by viewModels {
        ExpenseListViewModelFactory()
    }

    override fun onStart() {
        super.onStart()
        //event bus register
        EventBus.getDefault().register(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*##################################################################################*/
        //click listener and callback
        binding.addExpenseFloatingActionButton.setOnClickListener {
            onAddFabButtonClick()
        }
        /*##################################################################################*/
        //init view model
        expenseListViewModel
        /*##################################################################################*/
        //default fragment
        supportFragmentManager.beginTransaction().add(
            binding.fragmentContainerView.id,
            ExpenseListFragment()
        ).commit()
    }
    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
    private fun onAddFabButtonClick() {
        AddEditExpenseActivity.start(this, AddEditActivityActionMode.ADD)
    }

    /////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////
    //event bus subscribers
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onExpenseItemUpdateEvent(event: EventBusWork.UpdateExpenseItem) {
        val stickyEvent =
            EventBus.getDefault().getStickyEvent(EventBusWork.UpdateExpenseItem::class.java)
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent)
            expenseListViewModel.updateExpenseItem(event.item)
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onExpenseItemAddEvent(event: EventBusWork.AddsNewExpenseItem) {
        val stickyEvent =
            EventBus.getDefault().getStickyEvent(EventBusWork.AddsNewExpenseItem::class.java)
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent)
            expenseListViewModel.addNewExpenseItem(event.item)
        }
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onExpenseItemDeleteEvent(event: EventBusWork.DeleteExpenseItem) {
        val stickyEvent =
            EventBus.getDefault().getStickyEvent(EventBusWork.DeleteExpenseItem::class.java)
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent)
            expenseListViewModel.deleteExpenseItem(event.id)
        }
    }
    /////////////////////////////////////////////////////////////////////////////////
}