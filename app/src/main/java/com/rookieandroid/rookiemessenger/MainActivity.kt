package com.rookieandroid.rookiemessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rookieandroid.rookiemessenger.fragments.MessagesFragment

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val fragment = MessagesFragment()
        transaction.add(R.id.fragmentContainer, fragment, "").commit()
    }
}