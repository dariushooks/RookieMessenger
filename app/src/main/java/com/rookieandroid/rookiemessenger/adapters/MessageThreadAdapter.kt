package com.rookieandroid.rookiemessenger.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rookieandroid.rookiemessenger.MessageThread
import com.rookieandroid.rookiemessenger.R
import com.rookieandroid.rookiemessenger.App

class MessageThreadAdapter(private var thread: ArrayList<MessageThread>,
                           private val onItemClick: (position : Int) -> Unit,
                           private val onItemLongClick: (position : Int) -> Unit) : RecyclerView.Adapter<MessageThreadAdapter.ThreadViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThreadViewHolder
    {
        val view =  if(viewType == App.THREAD_TYPE_SENDER.toInt())
                        LayoutInflater.from(parent.context).inflate(R.layout.list_sender_message, parent, false)
                    else
                        LayoutInflater.from(parent.context).inflate(R.layout.list_receiver_message, parent, false)
        return ThreadViewHolder(view, thread, onItemClick, onItemLongClick)
    }

    override fun onBindViewHolder(holder: ThreadViewHolder, position: Int)
    {
        holder.bind(position)
    }

    override fun getItemViewType(position: Int): Int
    {
        return thread[position].type.toInt()
    }

    override fun getItemCount(): Int
    {
        return thread.size
    }

    class ThreadViewHolder(itemView: View, private val thread: ArrayList<MessageThread>,
                           val onItemClick: (position : Int) -> Unit,
                           val onItemLongClick: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener
    {
        private val date : TextView = itemView.findViewById(R.id.messageDate)
        private val message : TextView = itemView.findViewById(R.id.messageContent)

        fun bind (position: Int)
        {
            date.text = thread[position].date
            message.text = thread[position].content
        }

        override fun onClick(p0: View?)
        {
            TODO("Not yet implemented")
        }

        override fun onLongClick(p0: View?): Boolean
        {
            TODO("Not yet implemented")
        }
    }
}