package org.example.portsexample

import com.github.ajalt.clikt.testing.test
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.example.portsexample.app.UserCommand
import org.example.portsexample.model.Right
import org.example.portsexample.model.Role
import org.example.portsexample.model.User
import org.example.portsexample.outbound.UserService

class TestUserCommands:AnnotationSpec() {

    @Test
    fun testAddUserCommand() {

        val user = User("beppune").apply {
            roles.add(Role("rol1"))
            roles.add(Role("rol2"))
        }

        val service:UserService = mockk()
        every { service.save(any()) } returns Right(user)

        var cmd = UserCommand(service)

        val result = cmd.test("--username beppune --roles rol1,rol2")

        verify(exactly = 1) { service.save(any()) }

        result.stdout shouldBe "User 'beppune' has been created\n"
    }


}