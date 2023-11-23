package org.example.portsexample

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
}

data class Left<L>(val value: L) : Either<L,Nothing>()
data class Right<R>(val value: R) : Either<Nothing, R>()

sealed class Projection<out T> {
    abstract fun <U> map(fn: (T) -> U): Projection<U>
}

class ValueProjection<out T>(val value: T) : Projection<T>() {
    override fun <U> map(fn: (T) -> U): Projection<U> =
        ValueProjection(fn(value))
}

class EmptyProjection<out T> : Projection<T>() {
    override fun <U> map(fn: (T) -> U): Projection<U> = EmptyProjection()
}

fun <T> Projection<T>.getOrElse(or: () -> T): T = when (this) {
    is EmptyProjection -> or()
    is ValueProjection -> this.value
}

class TestEither {
}