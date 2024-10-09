package me.leon

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import me.leon.support.*
import org.junit.jupiter.api.Test

class ConnectTest {

    @Test
    fun connect() {
        println("www.tiktok.com".connect())
        println("www.tiktok.com".ping())
        println("www.tiktok.com".connect(443))
    }

    @Test
    fun poolTest() {
        NODE_OK.writeLine()
        runBlocking {
            Parser.parseFromSub(POOL)
                .map { it to async(DISPATCHER) { it.SERVER.quickConnect(it.serverPort, 200) } }
                .filter { it.second.await() > -1 }
                .forEach {
                    println(it.first.info() + ":" + it.second)
                    NODE_OK.writeLine(it.first.toUri())
                }
        }
    }

    @Test
    fun poolPingTest() {
        runBlocking {
            Parser.parseFromSub(POOL)
                .map { it to async(DISPATCHER) { it.SERVER.quickPing(200) } }
                .filter { it.second.await() > -1 }
                .also { println(it.size) }
                .forEach { println(it.first.info() + ":" + it.second) }
        }
    }
}
