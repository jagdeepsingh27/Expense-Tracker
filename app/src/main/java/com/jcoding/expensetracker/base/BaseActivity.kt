package com.jcoding.expensetracker.base

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.jcoding.expensetracker.util.`typealias`.InflateActivity


abstract class BaseActivity<VB : ViewBinding>(private val inflate: InflateActivity<VB>) :
    AppCompatActivity() {

    private var toast: Toast? = null
    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate.invoke(layoutInflater)
        setContentView(binding.root)
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT)
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        currentFocus?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }


    fun showToast(resId: Int) {
        showToast(getString(resId))
    }

    fun showToast(resourceString: String?) {
        if (resourceString.isNullOrBlank()) {
            return
        }
        if (toast != null) {
            toast?.cancel()
        }

        toast = Toast.makeText(this, resourceString, Toast.LENGTH_SHORT)
        toast?.show()
    }

}