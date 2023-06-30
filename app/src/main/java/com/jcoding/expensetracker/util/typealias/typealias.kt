package com.jcoding.expensetracker.util.`typealias`

import android.view.LayoutInflater
import android.view.ViewGroup


typealias InflateFragment<T> = (LayoutInflater, ViewGroup?, Boolean) -> T
typealias InflateActivity<T> = (LayoutInflater) -> T
