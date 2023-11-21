package org.example.portsexample.model

data class User(val username:UserName) {

    val roles:MutableList<Role> = mutableListOf()

}

data class Role(val rolename:RoleName)