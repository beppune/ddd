package org.example.portsexample.model.policy

abstract class PolicyManager<T>(val name: String) {

    var description:String?=null
    lateinit var prefix:String

    val policies:MutableList<Policy> = mutableListOf()

    inner class Policy(val name:String, val validator:T.()->Boolean, val description:String, val manager: PolicyManager<T>)

    inner class PolicyBuilder{
        lateinit var description:String
        lateinit var test:T.()->Boolean

        fun testWith(t:(T)->Boolean) {
            test = t
        }

        fun build(builder:PolicyBuilder.()->Unit) {
            builder()
        }
    }

    fun policy(name: String, b:PolicyBuilder.()->Unit): Policy {
        val builder = PolicyBuilder()
        builder.build(b)
        val pol = Policy(name, builder.test, builder.description, this)
        policies.add(pol)
        return pol
    }

    fun check(t:T) {
        var it = policies.iterator()
        while (it.hasNext() ) {
            val pol = it.next()
            if (pol.validator(t).not())
                throw Exception("${prefix}:${pol.name.uppercase()}: ${pol.description}")
        }
    }

    fun checkAll(t:T): List<Policy> {
        return policies.filter { pol -> pol.validator(t).not() }
    }
}

fun <T> book(bookName:String, p:PolicyManager<T>.()->Unit ) = object : PolicyManager<T>(bookName){}.apply(p)