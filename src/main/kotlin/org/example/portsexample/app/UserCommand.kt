package org.example.portsexample.app

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.split
import org.example.portsexample.inbound.AddUser
import org.example.portsexample.model.Role
import org.example.portsexample.model.User
import org.example.portsexample.outbound.UserService

class UserCommand(val service: UserService) : CliktCommand() {

    val username by option(help = "username").required()
    val roles by option(help = "One or more role names to add to").split(",").required()

    override fun run() {
        val action = AddUser {
            var user = User(username)

            roles.forEach { user.roles.add(Role(it)) }

            service.save(user)
            user
        }

        try {
            var result = action.run()
            echo("User '${result.username}' has been created")
        } catch (ex:Exception) {
            error(ex)
        }
    }

}
