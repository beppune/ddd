package org.example.portsexample

import com.github.ajalt.clikt.testing.test
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.example.portsexample.app.UserCommand
import org.example.portsexample.outbound.UserService

class TestUserCommands:AnnotationSpec() {

    @Test
    fun testAddUserCommand() {

        val service:UserService = mockk()
        every { service.save(any()) } just runs

        var cmd = UserCommand(service)

        val result = cmd.test("--username beppune --roles rol1,rol2")

        verify(exactly = 1) { service.save(any()) }

        result.stdout shouldBe "User 'beppune' has been created\n"
    }


}