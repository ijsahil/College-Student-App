package com.example.zenQ

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.titclone.R
import com.example.zenQ.adapter.MessageAdapter
import com.example.zenQ.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class ChatTextActivity : AppCompatActivity() {

    private lateinit var messageBox: EditText
    private lateinit var messageSendBtn: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDatabaseReference: DatabaseReference

    private var senderRoom: String? = null
    private var receiverRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_text)


        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid


        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = name


        messageBox = findViewById(R.id.etMessage)
        messageSendBtn = findViewById(R.id.btnSend)
        recyclerView = findViewById(R.id.rv_ChatText)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this@ChatTextActivity, messageList)
        mDatabaseReference = FirebaseDatabase.getInstance().reference
        recyclerView.layoutManager = LinearLayoutManager(this@ChatTextActivity)
        recyclerView.adapter = messageAdapter


        // Logic for adding message to the recycler view

        mDatabaseReference.child("chats").child(senderRoom!!).child("messages").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                messageList.clear()

                for(postSnapShot in snapshot.children){
                    val message = postSnapShot.getValue(Message::class.java)
                    messageList.add(message!!)
                }
                messageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


        // Adding the message to the DataBase
        messageSendBtn.setOnClickListener {
            val message = messageBox.text.toString()
            if(message.isNotEmpty()) {
                val messageObject = Message(message, senderUid)

                mDatabaseReference.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        mDatabaseReference.child("chats").child(receiverRoom!!).child("messages")
                            .push()
                            .setValue(messageObject)
                    }


                messageBox.setText("")

            }
        }

    }
}