package com.rookieandroid.rookiemessenger.dialogs

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rookieandroid.rookiemessenger.R
import com.rookieandroid.rookiemessenger.User
import com.rookieandroid.rookiemessenger.adapters.UsersDialogAdapter
import com.rookieandroid.rookiemessenger.architecture.UserViewModel

class UsersDialog(private val users : ArrayList<User>) : DialogFragment(), View.OnClickListener
{
    private lateinit var search : SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var cancel : TextView
    private val userViewModel : UserViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogFullScreen)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val rootView = inflater.inflate(R.layout.dialog_users, container, false)
        search = rootView.findViewById(R.id.dialogSearch)
        cancel = rootView.findViewById(R.id.dialogCancelText)
        cancel.setOnClickListener(this)
        recyclerView = rootView.findViewById(R.id.dialogUserlist)

        val adapter = UsersDialogAdapter(users) { position -> onListItemClick(position) }
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        return rootView
    }

    override fun onClick(p0: View?)
    {
        when(p0?.id)
        {
            R.id.dialogCancelText -> {dismiss()}
        }
    }

    private fun onListItemClick(position : Int)
    {
        userViewModel.setAddedUser(users[position])
        dismiss()
    }
}