package com.rookieandroid.rookiemessenger.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rookieandroid.rookiemessenger.R
import com.rookieandroid.rookiemessenger.User

class SignUpLoginFragment : Fragment(), View.OnClickListener
{
    private val TAG : String = SignUpLoginFragment::class.java.simpleName
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var name : EditText
    private lateinit var login : Button
    private lateinit var signup : Button
    private lateinit var create : Button
    private lateinit var cancel : Button

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        if(auth.currentUser != null)
            findNavController().navigate(R.id.signup_loginToMainFragment)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_signup_login, container, false)
        email = rootView.findViewById(R.id.userEmail)
        password = rootView.findViewById(R.id.userPassword)
        name = rootView.findViewById(R.id.userName)
        login = rootView.findViewById(R.id.loginButton)
        login.setOnClickListener(this)
        signup = rootView.findViewById(R.id.signUpButton)
        signup.setOnClickListener(this)
        create = rootView.findViewById(R.id.createUserButton)
        create.setOnClickListener(this)
        cancel = rootView.findViewById(R.id.cancelButton)
        cancel.setOnClickListener(this)
        return rootView
    }

    override fun onResume()
    {
        super.onResume()
        clearFields()
    }

    override fun onClick(p0: View?)
    {
        when(p0?.id)
        {
            R.id.loginButton ->
            {
                if(checkTextFields())
                    loginUser()
            }
            R.id.signUpButton ->
            {
                clearFields()
                name.visibility = View.VISIBLE
                create.visibility = View.VISIBLE
                cancel.visibility = View.VISIBLE
                login.visibility = View.GONE
                signup.visibility = View.GONE
            }
            R.id.createUserButton ->
            {createUser()}
            R.id.cancelButton ->
            {
                clearFields()
                name.visibility = View.GONE
                create.visibility = View.GONE
                cancel.visibility = View.GONE
                login.visibility = View.VISIBLE
                signup.visibility = View.VISIBLE
            }
        }
    }

    private fun checkTextFields() : Boolean
    {
        if(email.text.toString() == "" && password.text.toString() == "")
        {
            email.error = "Field cannot be empty"
            password.error = "Field cannot be empty"
            return false
        }
        else if(email.text.toString() == "" && password.text.toString() != "")
        {
            email.error = "Field cannot be empty"
            return false
        }
        else if(email.text.toString() != "" && password.text.toString() == "")
        {
            password.error = "Field cannot be empty"
            return false
        }
        else
            return true
    }

    private fun loginUser()
    {
        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(requireActivity())
            { task ->
                if (task.isSuccessful)
                {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    findNavController().navigate(R.id.signup_loginToMainFragment)
                    //updateUI(user)
                }
                else
                {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
    }

    private fun createUser()
    {
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(requireActivity())
            { task ->
                if (task.isSuccessful)
                {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    addNewUserToDatabase(name.text.toString(), email.text.toString(), auth.currentUser?.uid!!)
                    findNavController().navigate(R.id.signup_loginToMainFragment)
                    //updateUI(user)
                }
                else
                {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, task.exception?.message, Toast.LENGTH_LONG).show()
                    //updateUI(null)
                }
            }
    }

    private fun addNewUserToDatabase(userName : String, userEmail : String, userId : String)
    {
        dbRef.child("users").child(userId).setValue(User(userName, userEmail, userId))
        //dbRef.child("user").child(userId).child("messages").setValue(null)
        //dbRef.child("user").child(userId).child("friends").setValue(null)
    }

    private fun clearFields()
    {
        email.text.clear()
        password.text.clear()
        name.text.clear()
    }
}