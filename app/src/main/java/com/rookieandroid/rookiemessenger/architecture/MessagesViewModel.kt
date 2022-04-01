package com.rookieandroid.rookiemessenger.architecture

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rookieandroid.rookiemessenger.App
import com.rookieandroid.rookiemessenger.Message
import com.rookieandroid.rookiemessenger.MessageThread

class MessagesViewModel : ViewModel()
{
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val dbRef : DatabaseReference = FirebaseDatabase.getInstance().reference
    private val messages : MutableLiveData<List<Message>> = MutableLiveData<List<Message>>()

    init {
        loadMessages()
        Log.i("MessagesViewModel", "Being Created")
    }

    fun getMessages() : LiveData<List<Message>> { return messages }

    private fun loadMessages()
    {
        dbRef.child("messages").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot)
                {
                    val messageList : ArrayList<Message> = ArrayList()
                    for(postsnap in snapshot.children)
                    {

                        if (postsnap.key.toString().startsWith(auth.currentUser?.uid.toString()))
                        {
                            val m = postsnap.children.last().getValue(MessageThread::class.java)
                            val msg =
                                if(m?.type!! == App.THREAD_TYPE_RECEIVER)
                                {
                                    val name = getUserName(m.senderId)
                                    Message(name, m.date, m.content, m.senderId)
                                }
                                else
                                {
                                    val name = getUserName(m.receiverId)
                                    Message(name, m.date, m.content, m.receiverId)
                                }
                            messageList.add(msg)
                        }
                    }
                    messages.value = messageList
                }

                override fun onCancelled(error: DatabaseError)
                {
                }
            })
    }

    private fun getUserName(id: String): String
    {
        val list = dbRef.child("users").child(id).child("name").get()
        if(list.isComplete)
            return list.result.value.toString()
        return id
    }
}