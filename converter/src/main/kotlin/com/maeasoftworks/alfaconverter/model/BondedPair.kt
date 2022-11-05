package com.maeasoftworks.alfaconverter.model

import com.maeasoftworks.alfaconverter.annotations.UsedExternally

class BondedPair<T>() : Iterable<T> {
	@UsedExternally
	var first: T? = null
	@UsedExternally
	var second: T? = null

	@UsedExternally
	constructor(first: T, second: T) : this() {
		this.first = first
		this.second = second
	}

	var dependence: Headship = Headship.FIRST

	val master: T
		get() = if (dependence == Headship.FIRST) first!! else second!!

	val slave: T
		get() = if (dependence == Headship.SECOND) first!! else second!!

	val size: Int
		get() = if (second != null) 2 else if (first != null) 1 else 0

	@UsedExternally
	fun add(value: T) {
		if (first == null) {
			first = value
			return
		}
		if (second == null) {
			second = value
			return
		}
		throw Exception("Both fields was already set")
	}

	operator fun get(pos: Int): T {
		return when (pos) {
			0 -> first!!
			1 -> second!!
			else -> throw IndexOutOfBoundsException()
		}
	}

	operator fun plusAssign(value: T) = add(value)

	override fun iterator(): Iterator<T> {
		return PairIterator(this)
	}

	enum class Headship {
		FIRST,
		SECOND
	}

	private class PairIterator<T>(private val pair: BondedPair<T>) : Iterator<T> {
		private var pos = 0

		override fun hasNext() = pos < 2

		override fun next(): T {
			return pair[pos++]
		}
	}
}