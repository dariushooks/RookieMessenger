package com.rookieandroid.rookiemessenger

import android.app.Application

class App : Application()
{
    companion object
    {
        const val THREAD_TYPE_SENDER : String = "0"
        const val THREAD_TYPE_RECEIVER: String = "1"
    }
}