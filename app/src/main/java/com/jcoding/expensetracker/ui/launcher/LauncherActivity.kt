package com.jcoding.expensetracker.ui.launcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jcoding.expensetracker.R

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        //stop splash screen
        splashScreen.setKeepOnScreenCondition { true }
    }
}