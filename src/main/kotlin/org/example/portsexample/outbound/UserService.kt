package org.example.portsexample.outbound

import org.example.portsexample.model.Either
import org.example.portsexample.model.User

interface UserService {
    fun save(u: User) : Either<String,User>
}