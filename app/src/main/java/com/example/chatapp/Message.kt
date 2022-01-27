package com.example.chatapp

class Message {
    var message: String? = null
    var senderId: String? = null

    constructor(){}

    constructor(messagesent: String?, senderId: String?) {
        this.message = messagesent
        this.senderId = senderId
    }


}