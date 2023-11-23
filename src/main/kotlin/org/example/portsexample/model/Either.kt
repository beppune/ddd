package org.example.portsexample.model


sealed class Either<out L, out R> {

    fun <T> fold(leftfn:(L)->T, rightfn:(R)->T) : T =
        when(this) {
            is Left -> leftfn(value)
            is Right -> rightfn(value)
        }

    fun left() : Projection<L> = when(this) {
        is Left -> ValueProjection(this.value)
        is Right -> EmptyProjection()
    }

    fun right() : Projection<R> = when(this) {
        is Left -> EmptyProjection()
        is Right -> ValueProjection(this.value)
    }

    fun isLeft() = when(this) {
        is Left -> true
        is Right -> false
    }

    fun isRight() = when(this) {
        is Left -> false
        is Right -> true
    }
}

data class Left<L>(val value: L) : Either<L,Nothing>()
data class Right<R>(val value: R) : Either<Nothing, R>()

sealed class Projection<out T> {
    abstract fun <U> map(fn: (T) -> U): Projection<U>
    abstract fun exists(fn: (T)->Boolean = {false}) : Boolean
    abstract fun orNull() : T?
}

class ValueProjection<out T>(val value: T) : Projection<T>() {
    override fun <U> map(fn: (T) -> U): Projection<U> =
        ValueProjection(fn(value))

    override fun exists(fn: (T) -> Boolean): Boolean = fn(this.value)
    override fun orNull(): T? = this.value
}

class EmptyProjection<out T> : Projection<T>() {
    override fun <U> map(fn: (T) -> U): Projection<U> = EmptyProjection()
    override fun exists(fn: (T) -> Boolean): Boolean = false
    override fun orNull(): T? = null
}

fun <T> Projection<T>.getOrElse(or: () -> T): T = when (this) {
    is EmptyProjection -> or()
    is ValueProjection -> this.value
}