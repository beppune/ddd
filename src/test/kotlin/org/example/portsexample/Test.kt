package org.example.portsexample

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.*
import org.example.portsexample.inbound.AddUser
import org.example.portsexample.model.Right
import org.example.portsexample.model.User
import org.example.portsexample.model.policy.book
import org.example.portsexample.outbound.UserService

class Test : AnnotationSpec() {

    @Test
    fun test() {

        val user = User("USERNAME")

        val service:UserService = mockk()
        every { service.save(any()) } returns Right(user)

        AddUser {
            service.save(user)
            user
        }.run()

        verify { service.save(any()) }

        user.username shouldBe "USERNAME"
    }

    @Test
    fun policyBook() {
        val user = User("toolongusername")

        val usersBook = book<User>("User Creation") {

            description = """
                User Creation Policy Book.
                Refer to Service Manual
            """.trimIndent()

            prefix = "UC"

            policy("length8") {
                description = "Username MUST be 8 characters long"

                testWith {
                    it.username.length == 8
                }
            }

            policy("uppercase") {
                description = "Username MUST be uppercase"

                testWith {
                    it.username.all(Char::isUpperCase)
                }
            }
        }

        val ex = shouldThrowAny {
            usersBook.check(user)
        }

        ex.message shouldContain "Username MUST be 8 characters long"

        val failed = usersBook.checkAll(user)

        failed shouldHaveSize 2
    }

}