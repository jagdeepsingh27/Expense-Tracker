package com.jcoding.expensetracker.util.`typealias`

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup


typealias InflateFragment<T> = (LayoutInflater, ViewGroup?, Bundle?) -> T
typealias InflateActivity<T> = (LayoutInflater) -> T
