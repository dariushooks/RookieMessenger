package com.rookieandroid.rookiemessenger.architecture

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rookieandroid.rookiemessenger.MessageThread
import com.rookieandroid.rookiemessenger.User

class MessageThreadViewModel : ViewModel()
{
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val dbRef : DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var receiver : User
    private val messages : MutableLiveData<List<MessageThread>> = MutableLiveData<List<MessageThread>>()

    fun getMessages() : LiveData<List<MessageThread>> {return messages}

    fun setReceiver(receiver : User)
    {
        this.receiver = receiver
        loadMessages()
    }

    private fun loadMessages()
    {
        val senderRoom = auth.currentUser?.uid + receiver.uid
        val thread : ArrayList<MessageThread> = ArrayList()
        dbRef.child("messages").child(senderRoom)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot)
                {
                    thread.clear()
                    for(snap in snapshot.children)
                    {
                        val message = snap.getValue(MessageThread::class.java)
                        thread.add(message!!)
                    }
                    messages.value = thread
                }

                override fun onCancelled(error: DatabaseError)
                {
                    
                }
            })
    }
}