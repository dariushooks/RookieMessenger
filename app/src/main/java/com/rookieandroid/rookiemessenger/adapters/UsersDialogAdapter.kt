package com.rookieandroid.rookiemessenger.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rookieandroid.rookiemessenger.R
import com.rookieandroid.rookiemessenger.User

class UsersDialogAdapter(private val users : ArrayList<User>,
                         private val onItemClick : (position : Int) -> Unit) :RecyclerView.Adapter<UsersDialogAdapter.UsersViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_dialog_users, parent, false)
        return UsersViewHolder(view, users, onItemClick)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int)
    {
        holder.bind(position)
    }

    override fun getItemViewType(position: Int): Int
    {
        return super.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long
    {
        return super.getItemId(position)
    }

    override fun getItemCount(): Int { return users.size }

    class UsersViewHolder(itemView: View, private val users: ArrayList<User>,
                          val onItemClick: (position : Int) -> Unit) : RecyclerView.ViewHolder(itemView), View.OnClickListener
    {
        private val username : TextView = itemView.findViewById(R.id.dialogUsername)

        init { itemView.setOnClickListener(this) }

        fun bind(position: Int)
        {
            username.text = users[position].name
            username.setOnClickListener(this)
        }

        override fun onClick(p0: View?) { onItemClick(adapterPosition) }
    }
}