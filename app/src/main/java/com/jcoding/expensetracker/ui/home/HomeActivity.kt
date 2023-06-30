package com.jcoding.expensetracker.ui.home

import android.os.Bundle
import com.jcoding.expensetracker.base.BaseActivity
import com.jcoding.expensetracker.databinding.HomeActivityBinding
import com.jcoding.expensetracker.ui.home.list.ExpenseListFragment

class HomeActivity : BaseActivity<HomeActivityBinding>(HomeActivityBinding::inflate) {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        /*##################################################################################*/
        //default fragment
        supportFragmentManager.beginTransaction().add(
            binding.fragmentContainerView.id,
            ExpenseListFragment()
        ).commit()
    }
}