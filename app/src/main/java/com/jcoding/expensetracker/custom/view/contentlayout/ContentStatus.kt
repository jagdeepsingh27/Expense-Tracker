package com.jcoding.expensetracker.custom.view.contentlayout

sealed class ContentStatus{
    object DataAvailable : ContentStatus()
    class  DataNotAvailable(val text : String?) : ContentStatus()
    class Loading(val status : Boolean = false) : ContentStatus()
    class Error(val text:String?) : ContentStatus()
}