package com.example

import com.example.plugins.configureRouting
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

const val PREFIX = "input = "

class ApplicationTest {
    @Test
    fun testGrigorian() = testApplication {
        application {
            configureRouting()
        }
        var response = client.get("/g_to_j?grigorian=25.03.2021")
        assertEquals(PREFIX + "12.03.2021", response.bodyAsText())

        response = client.get("/g_to_j?grigorian=11.01.2021")
        assertEquals(PREFIX + "29.12.2020", response.bodyAsText())

        response = client.get("/g_to_j?grigorian=11.01.20211")
        assertEquals("Incorrect input", response.bodyAsText())

        response = client.get("/g_to_c?grigorian=11.01.2021")
        assertEquals(PREFIX +  "8,97.12.2020", response.bodyAsText())

        response = client.get("/g_to_c?grigorian=25.01.2021")
        assertEquals(PREFIX +  "22,97.12.2020", response.bodyAsText())
    }

    @Test
    fun testJulian() = testApplication {
        application {
            configureRouting()
        }
        var response = client.get("/j_to_g?julian=12.03.2021")
        assertEquals(PREFIX + "25.03.2021", response.bodyAsText())

        response = client.get("/j_to_c?julian=11.01.2021")
        assertEquals(PREFIX +  "21,97.12.2020", response.bodyAsText())
    }

    @Test
    fun testChinise() = testApplication {
        application {
            configureRouting()
        }
        var response = client.get("/c_to_g?chinese=12.03.2021")
        assertEquals(PREFIX + "15,03.04.2021", response.bodyAsText())

        response = client.get("/c_to_j?chinese=11.01.2021")
        assertEquals(PREFIX +  "14,03.02.2021", response.bodyAsText())
    }
}
