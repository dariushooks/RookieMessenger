package com.rookieandroid.rookiemessenger.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rookieandroid.rookiemessenger.R

class NewMessageFragment : Fragment(), View.OnClickListener
{
    private lateinit var cancelMessage : TextView
    private lateinit var sendMessage: Button
    private lateinit var messageBox : EditText
    private lateinit var addContact: EditText
    private lateinit var addToMessage : ImageButton
    private lateinit var addResources : ImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_new_message, container, false)
        cancelMessage = rootView.findViewById(R.id.cancelMessage)
        cancelMessage.setOnClickListener(this)
        sendMessage = rootView.findViewById(R.id.sendMessage)
        sendMessage.setOnClickListener(this)
        messageBox = rootView.findViewById(R.id.messageBox)
        messageBox.setOnClickListener(this)
        addContact = rootView.findViewById(R.id.addToMessage)
        addContact.setOnClickListener(this)
        addToMessage = rootView.findViewById(R.id.addToMessageButton)
        addToMessage.setOnClickListener(this)
        addResources = rootView.findViewById(R.id.addResources)
        addResources.setOnClickListener(this)
        return rootView
    }

    override fun onClick(p0: View?)
    {
        when(p0?.id)
        {
            R.id.cancelMessage -> {findNavController().navigateUp()}
            R.id.sendMessage -> {sendMessage()}
            R.id.addToMessageButton -> {}
            R.id.addResources -> {}
        }
    }

    private fun sendMessage()
    {

    }

}