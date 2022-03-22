package com.rookieandroid.rookiemessenger.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rookieandroid.rookiemessenger.Message
import com.rookieandroid.rookiemessenger.R

class MessageAdapter(private var messages: ArrayList<Message>,
                     private val onItemClick: (position : Int) -> Unit,
                     private val onItemLongClick: (position: Int) -> Unit) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_messages, parent, false)
        return MessageViewHolder(view, messages, onItemClick, onItemLongClick)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int)
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

    override fun getItemCount(): Int
    {
        return messages.size
    }

    class MessageViewHolder(itemView: View, private val messages: ArrayList<Message>,
                            val onItemClick: (position : Int) -> Unit,
                            val onItemLongClick: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener
   {
       private val name: TextView = itemView.findViewById(R.id.messageName)
       private val date: TextView = itemView.findViewById(R.id.messageDate)
       private val content: TextView = itemView.findViewById(R.id.messageContent)
       init
       {
           itemView.setOnClickListener(this)
           itemView.setOnLongClickListener(this)
       }
       fun bind(position: Int)
       {
           name.text = messages[position].name
           date.text = messages[position].date
           content.text = messages[position].preview
       }

       override fun onClick(p0: View?) { onItemClick(adapterPosition) }

       override fun onLongClick(p0: View?): Boolean
       {
           onItemLongClick(adapterPosition)
           return true
       }
   }
}