package com.rookieandroid.rookiemessenger.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rookieandroid.rookiemessenger.Message
import com.rookieandroid.rookiemessenger.R
import com.rookieandroid.rookiemessenger.User
import com.rookieandroid.rookiemessenger.adapters.MessageAdapter
import com.rookieandroid.rookiemessenger.architecture.MessagesViewModel
import com.rookieandroid.rookiemessenger.architecture.UserViewModel

class MessagesFragment : Fragment(), View.OnClickListener
{
    private var messages : ArrayList<Message> = ArrayList()
    private val messagesViewModel : MessagesViewModel by viewModels()
    private val userViewModel : UserViewModel by activityViewModels()
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val dbRef : DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true)
        {
            override fun handleOnBackPressed()
            {
                TODO("Not yet implemented")
            }
        })

        Log.i("Checking display name", auth.currentUser?.displayName.toString())
        messagesViewModel.getMessages().observe(this) { messages -> this.messages = messages as ArrayList<Message> }

        //messages.add(Message("Terrance", "null", "Some Text", ""))
        //messages.add(Message("Kobe", "null", "Some Text", ""))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_messages, container, false)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.messagesList)
        val layoutManager = LinearLayoutManager(context)
        val adapter = MessageAdapter(messages, {position -> onItemClick(position)}, {position -> onItemLongClick(position)})
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        val newMessage = rootView.findViewById<FloatingActionButton>(R.id.newMessage)
        newMessage.setOnClickListener(this)
        val settings = rootView.findViewById<ImageButton>(R.id.settingsButton)
        settings.setOnClickListener(this)
        return rootView
    }

    private fun onItemClick(position : Int)
    {
        val name = messages[position].name
        val id = messages[position].uid
        val bundle = bundleOf("name" to name, "uid" to id)
        findNavController().navigate(R.id.mainFragmentToThreadFragment, bundle)

        /*dbRef.child("users").child(id).child("personal")
            .addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot)
            {
                val user = snapshot.getValue(User::class.java)
                val bundle = bundleOf("name" to user?.name, "uid" to user?.uid)
                findNavController().navigate(R.id.mainFragmentToThreadFragment, bundle)
                //Toast.makeText(context, user?.uid.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError)
            {

            }
        })*/
    }

    private fun onItemLongClick(position : Int)
    {

    }

    override fun onClick(p0: View?)
    {
        when(p0?.id)
        {
            R.id.newMessage ->
            {
                findNavController().navigate(R.id.mainFragmentToNewFragment)
                //Toast.makeText(context, "Button Pressed", Toast.LENGTH_SHORT).show()
            }

            R.id.settingsButton ->
            {
                auth.signOut()
                findNavController().navigateUp()
            }
        }

    }
}