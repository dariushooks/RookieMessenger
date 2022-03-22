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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rookieandroid.rookiemessenger.App
import com.rookieandroid.rookiemessenger.MessageThread
import com.rookieandroid.rookiemessenger.R
import com.rookieandroid.rookiemessenger.User
import com.rookieandroid.rookiemessenger.adapters.MessageThreadAdapter
import com.rookieandroid.rookiemessenger.architecture.MessageThreadViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MessageThreadFragment : Fragment(), View.OnClickListener
{
    private lateinit var contact: TextView
    private lateinit var receiver : User
    private lateinit var senderRoom : String
    private lateinit var receiverRoom : String
    private lateinit var messageBox : EditText
    private lateinit var sendMessage : Button
    private lateinit var addResource : ImageButton
    private val threadViewModel : MessageThreadViewModel by viewModels()
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val dbRef : DatabaseReference = FirebaseDatabase.getInstance().reference
    private var thread : ArrayList<MessageThread> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        receiver = User(arguments?.getString("name")!!, "", arguments?.getString("uid")!!)
        threadViewModel.setReceiver(receiver)
        senderRoom = auth.currentUser?.uid + receiver.uid
        receiverRoom = receiver.uid + auth.currentUser?.uid
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_message_thread, container, false)
        contact = rootView.findViewById(R.id.contactName)
        contact.text = receiver.name
        messageBox = rootView.findViewById(R.id.messageBox)
        sendMessage = rootView.findViewById(R.id.sendMessage)
        sendMessage.setOnClickListener(this)
        addResource = rootView.findViewById(R.id.addResources)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.messageThread)
        val layoutManager = LinearLayoutManager(context)
        val adapter = MessageThreadAdapter(thread, {position -> onItemClick(position)}, {position -> onItemLongClick(position)})
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        threadViewModel.getMessages().observe(viewLifecycleOwner) {thread ->
            this.thread = thread as ArrayList<MessageThread>
            adapter.notifyItemInserted(thread.size)
        }
        /*dbRef.child("messages").child(senderRoom)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot)
                {
                    thread.clear()
                    for(snap in snapshot.children)
                    {
                        val message = snap.getValue(MessageThread::class.java)
                        thread.add(message!!)
                    }
                    adapter.notifyItemInserted(thread.size)
                }

                override fun onCancelled(error: DatabaseError)
                {
                    TODO("Not yet implemented")
                }
            })*/
        return rootView
    }

    private fun onItemClick(position : Int)
    {

    }

    private fun onItemLongClick(position : Int)
    {

    }


    override fun onClick(p0: View?)
    {
        when(p0?.id)
        {
            R.id.sendMessage -> {sendMessage()}
        }
    }

    private fun sendMessage()
    {
        val dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        val timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss")
        val currentDate = LocalDate.now().format(dateFormat)
        val currentTime = LocalTime.now().format(timeFormat)

        val senderMessage = MessageThread(currentDate.toString(), currentTime.toString(), messageBox.text.toString(),
            auth.currentUser?.uid.toString(), receiver.uid, App.THREAD_TYPE_SENDER)
        messageBox.text.clear()
        dbRef.child("messages").child(senderRoom).push().setValue(senderMessage)
            .addOnSuccessListener{
                val receiverMessage = MessageThread(currentDate.toString(), currentTime.toString(), messageBox.text.toString(),
                    auth.currentUser?.uid.toString(), receiver.uid, App.THREAD_TYPE_RECEIVER)
                dbRef.child("messages").child(receiverRoom).push().setValue(receiverMessage)
            }
    }
}