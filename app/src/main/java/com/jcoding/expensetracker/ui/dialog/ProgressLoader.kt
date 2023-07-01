package com.jcoding.expensetracker.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.jcoding.expensetracker.databinding.ProgressLoaderLayoutBinding

class ProgressLoader(context: Context) : AlertDialog(context) {

    private val binding : ProgressLoaderLayoutBinding by lazy {
        ProgressLoaderLayoutBinding.inflate(layoutInflater)
    }

    init {
        setView(binding.root)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.attributes?.apply {
            gravity = Gravity.CENTER
        }

    }

    override fun hide() {
        if(isShowing){
            super.dismiss()
        }
    }

    override fun show() {
        super.show()
    }

}