package com.benjaminearley.stockcost

class TestCurrentTime(private val time: Long = System.currentTimeMillis()) : CurrentTime {
    override fun get(): Long = time
}