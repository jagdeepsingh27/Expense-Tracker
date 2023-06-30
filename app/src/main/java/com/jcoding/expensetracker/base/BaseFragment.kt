package com.jcoding.expensetracker.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.jcoding.expensetracker.util.`typealias`.InflateFragment


abstract class BaseFragment<VB : ViewBinding>(private val inflate: InflateFragment<VB>) : Fragment() {
    private var _binding: VB? = null
    abstract fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, savedInstanceState)
        return binding().root
    }

    fun binding() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}