package com.rookieandroid.rookiemessenger.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rookieandroid.rookiemessenger.App
import com.rookieandroid.rookiemessenger.MessageThread
import com.rookieandroid.rookiemessenger.R
import com.rookieandroid.rookiemessenger.User
import com.rookieandroid.rookiemessenger.architecture.UserViewModel
import com.rookieandroid.rookiemessenger.dialogs.UsersDialog
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class NewMessageFragment : Fragment(), View.OnClickListener
{
    private lateinit var cancelMessage : TextView
    private lateinit var sendMessage: Button
    private lateinit var messageBox : EditText
    private lateinit var addContact: EditText
    private lateinit var addedContact : TextView
    private lateinit var addToMessage : ImageButton
    private lateinit var addResources : ImageButton
    private lateinit var addedUser: User
    private var users : ArrayList<User> = ArrayList()
    private val userViewModel : UserViewModel by activityViewModels()
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val dbRef : DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        userViewModel.getUsers().observe(this) {users -> this.users = users}
    }

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
        addedContact = rootView.findViewById(R.id.addToMessageAdded)
        addToMessage = rootView.findViewById(R.id.addToMessageButton)
        addToMessage.setOnClickListener(this)
        addResources = rootView.findViewById(R.id.addResources)
        addResources.setOnClickListener(this)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        userViewModel.addedUser.observe(viewLifecycleOwner) {user ->
            addedUser = user
            addedContact.text = user.name
        }
    }

    override fun onClick(p0: View?)
    {
        when(p0?.id)
        {
            R.id.cancelMessage -> {findNavController().navigateUp()}
            R.id.sendMessage -> {sendMessage()}
            R.id.addToMessageButton -> {openDialog()}
            R.id.addResources -> {}
        }
    }

    private fun sendMessage()
    {
        val senderRoom = auth.currentUser?.uid + addedUser.uid
        val receiverRoom = addedUser.uid + auth.currentUser?.uid

        val dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        val timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss")
        val currentDate = LocalDate.now().format(dateFormat)
        val currentTime = LocalTime.now().format(timeFormat)

        val senderMessage = MessageThread(currentDate.toString(), currentTime.toString(), messageBox.text.toString(),
            auth.currentUser?.uid.toString(), addedUser.uid, App.THREAD_TYPE_SENDER)
        dbRef.child("messages").child(senderRoom).push().setValue(senderMessage)
            .addOnSuccessListener{
                val receiverMessage = MessageThread(currentDate.toString(), currentTime.toString(), messageBox.text.toString(),
                    auth.currentUser?.uid.toString(), addedUser.uid, App.THREAD_TYPE_RECEIVER)
                dbRef.child("messages").child(receiverRoom).push().setValue(receiverMessage)
                messageBox.text.clear()
            }
        val name = addedUser.name
        val id = addedUser.uid
        val bundle = bundleOf("name" to name, "uid" to id)
        findNavController().navigate(R.id.newMessageFragmentToThreadFragment, bundle)
    }

    private fun  openDialog()
    {
        val dialog = UsersDialog(users)
        dialog.show(parentFragmentManager, "New Message Fragment")
    }

}