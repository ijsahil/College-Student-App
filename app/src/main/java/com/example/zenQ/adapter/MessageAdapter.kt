package com.example.zenQ.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.titclone.R
import com.example.zenQ.model.Message
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if(viewType == ITEM_RECEIVE){

            //Inflate receive layout
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            ReceiveViewHolder(view)

        }else{

            //Inflate sent layout
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            SendViewHolder(view)

        }

    }

    override fun getItemViewType(position: Int): Int {

        val currentMessage = messageList[position]
        return if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)) {
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }

    }

    override fun getItemCount(): Int {
            return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder.javaClass == SendViewHolder::class.java) {
            // Do the stuff for sent view holder
            val currentMessage = messageList[position]

            val viewHolder = holder as SendViewHolder
            holder.sentMessage.text = currentMessage.message


        } else {  // Do the stuff for receive view holder

            val currentMessage = messageList[position]

            val viewHolder = holder as ReceiveViewHolder
            holder.receiveMessage.text = currentMessage.message

        }

    }

    class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.tvSentMessage)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage = itemView.findViewById<TextView>(R.id.tvReceiveMessage)
    }
}