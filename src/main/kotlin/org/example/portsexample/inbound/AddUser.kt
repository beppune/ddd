package org.example.portsexample.inbound

import org.example.portsexample.model.User

fun interface AddUser {
    fun run() : User
}