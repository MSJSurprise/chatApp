package com.example.chatapp

import androidx.recyclerview.widget.RecyclerView

class UserList {
    private var name: String? = null
    private var email: String? = null
    private var uid: String? = null

    constructor(){}

    constructor(name: String, email: String, uid: String){
        this.name = name
        this.email = email
        this.uid = uid

    }

}