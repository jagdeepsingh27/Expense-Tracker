package com.jcoding.expensetracker.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.jcoding.expensetracker.R
import com.jcoding.expensetracker.base.BaseActivity
import com.jcoding.expensetracker.databinding.SettingsActivityBinding
import com.jcoding.expensetracker.ui.currency.CurrencyListActivity

class SettingsActivity : BaseActivity<SettingsActivityBinding>(
    SettingsActivityBinding::inflate
) {
    companion object {
        fun start(activity: FragmentActivity) {
            activity.startActivity(Intent(activity, SettingsActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //click listener
        binding.backArrowImageView.setOnClickListener { finish() }
        binding.currencyOptionTextView.setOnClickListener { CurrencyListActivity.start(this)  }

    }
}