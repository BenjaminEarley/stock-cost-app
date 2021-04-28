package com.benjaminearley.stockcost.util

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach
import kotlin.random.Random

typealias Randomizer<A> = (Random) -> A

fun String.Companion.randomizer(from: Int, until: Int): Randomizer<String> =
    { random ->
        (from until until)
            .map { random.nextInt(32, 127).toChar() }
            .joinToString(separator = "")
    }

fun Int.Companion.randomizer(from: Int, until: Int): Randomizer<Int> =
    { random -> random.nextInt(from, until) }

fun Long.Companion.randomizer(from: Long, until: Long): Randomizer<Long> =
    { random -> random.nextLong(from, until) }

fun <A, E> Randomizer<A>.eitherRandomizer(error: E, errorChance: Int = 2)
        : Randomizer<Either<E, A>> = { random ->
    if (random.nextInt(0, 1) % errorChance == 0) error.left() else invoke(random).right()
}

fun <A> Randomizer<A>.flowRandomizer(from: Int, until: Int, delay: Long = 0): Randomizer<Flow<A>> =
    { random ->
        (0..Int.randomizer(from, until)(random))
            .map { invoke(random) }
            .asFlow()
            .onEach { delay(delay) }
    }

fun <A> Randomizer<A>.listRandomizer(from: Int, until: Int): Randomizer<List<A>> =
    { random -> (0..Int.randomizer(from, until)(random)).map { invoke(random) } }

fun <A : Any> Randomizer<A>.sequenceRandomizer(): Randomizer<Sequence<A>> =
    { random -> generateSequence { invoke(random) } }