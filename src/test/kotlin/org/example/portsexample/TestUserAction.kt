package org.example.portsexample

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.AnnotationSpec
import io.mockk.every
import io.mockk.*
import org.example.portsexample.inbound.AddUser
import org.example.portsexample.inbound.GroupAdd
import org.example.portsexample.model.Role
import org.example.portsexample.model.RoleName
import org.example.portsexample.model.User
import org.example.portsexample.model.policy.PolicyManager
import org.example.portsexample.model.policy.book
import org.example.portsexample.outbound.UserService

class TestUserAction : AnnotationSpec() {

    @Test
    fun addUser() {

        val userBook:PolicyManager<User> = mockk()
        every { userBook.checkAll(any()) } returns mutableListOf(
            userBook.Policy("test", {false}, "test", userBook),
            userBook.Policy("test2", {false}, "test2", userBook)
        )

        val user = User("name")

        val userAdd = AddUser {
            var problems = userBook.checkAll(user)
            var message =
                "User validation failed: [" +
                problems.map {it.name  } .joinToString(", ") +
                "]"

            if(problems.isNotEmpty()) throw Exception(message)

            user
        }

        val ex = shouldThrowAny {
            userAdd.run()
        }

        ex.message.also(::println)
    }

    @Test
    fun groupAdd() {

        val service:UserService = mockk()
        every { service.save(any()) } just runs

        val user = User("uname")

        val action = GroupAdd {
            val groups:List<RoleName> = listOf("group1", "group2")

            groups.map { Role(it) }.forEach{user.roles.add(it)}

            service.save(user)
        }.run()

        verify { service.save(any()) }
    }
}