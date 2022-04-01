package com.rookieandroid.rookiemessenger.architecture

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.*
import com.rookieandroid.rookiemessenger.User

class UserViewModel : ViewModel()
{
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val dbRef : DatabaseReference = FirebaseDatabase.getInstance().reference
    private val users = MutableLiveData<ArrayList<User>>().also { loadUsers() }
    val addedUser : MutableLiveData<User> = MutableLiveData()

    fun getUsers() : LiveData<ArrayList<User>> { return users }
    fun setAddedUser(user : User) {addedUser.value = user}

    private fun loadUsers()
    {
        dbRef.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot)
            {
                val userList : ArrayList<User> = ArrayList()
                for(postsnap in snapshot.children)
                {
                    val u = postsnap.getValue(User::class.java)
                    if(u?.uid != auth.currentUser?.uid)
                        userList.add(u!!)
                    else
                    {
                        val profile = userProfileChangeRequest { displayName =  u?.name }
                        auth.currentUser?.updateProfile(profile)
                    }
                }

                users.value = userList
                Log.i("UserViewModel", "Users loaded")
            }

            override fun onCancelled(error: DatabaseError)
            {
            }
        })
    }
}