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

class HomeActivity : BaseActivity<HomeActivityBinding>(HomeActivityBinding::inflate) {

    private val expenseListViewModel: ExpenseListViewModel by viewModels {
        ExpenseListViewModelFactory()
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

    private fun onAddFabButtonClick() {
        AddEditExpenseActivity.start(this, AddEditActivityActionMode.ADD)
    }

    /////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////
}