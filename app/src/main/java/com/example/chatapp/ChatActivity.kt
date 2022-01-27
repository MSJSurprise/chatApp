package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var edtChat: EditText
    private lateinit var imgvSend: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var message: ArrayList<Message>
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    var senderRoom: String? = null
    var receiverRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatRecyclerView = findViewById(R.id.chat_recyclerview)
        edtChat = findViewById(R.id.edt_chat)
        imgvSend = findViewById(R.id.imgv_send)


        message = ArrayList()
        messageAdapter = MessageAdapter(this, message)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference()
        val senderId = auth.currentUser?.uid!!
        val senderName = intent.getStringExtra("name")
        val receiverId = intent.getStringExtra("uid")

        senderRoom = senderId + receiverId
        receiverRoom = receiverId + senderId

        imgvSend.setOnClickListener {
            val messageBox = edtChat.text.toString()
            val messageObject = Message(messageBox, senderId)
            addMessage(messageObject)
        }

        getMessage()

    }

    private fun addMessage(messageObject: Message) {
        dbRef.child("chats").child(senderRoom!!).child("messages").push()
            .setValue(messageObject).addOnSuccessListener {
                dbRef.child("chats").child(receiverRoom!!).child("messages").push()
                    .setValue(messageObject)

            }
        edtChat.setText("")
    }

    private fun getMessage() {
        dbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    message.clear()
                    for (postSnapshot in snapshot.children) {
                        val currentUser = postSnapshot.getValue(Message::class.java)
                        message.add(currentUser!!)

                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}