package com.jcoding.expensetracker.ui.home.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jcoding.expensetracker.R
import com.jcoding.expensetracker.base.BaseFragment
import com.jcoding.expensetracker.databinding.ExpenseListFragmentBinding


class ExpenseListFragment :  BaseFragment<ExpenseListFragmentBinding>(
    ExpenseListFragmentBinding::inflate
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.expense_list_fragment, container, false)
    }

}