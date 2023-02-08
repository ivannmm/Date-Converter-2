package com.example.plugins

import com.example.ClassDateEnum
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

const val GRIGORIAN = "grigorian"
const val CHINESE = "chinese"
const val JULIAN = "julian"
val DAYS = intArrayOf( 31 , 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

fun Application.configureRouting() {
    routing {
        get("/g_to_j") {
            val input = call.parameters[GRIGORIAN]
            if (input != null) {
                if (input.matches(Regex("\\d{2}.\\d{2}.\\d{4}"))) {
                    val dates = input.split(".")
                    val result = getJulianDate(dates[0].toInt(), dates[1].toInt(), dates[2].toInt(), ClassDateEnum.GRIGORIAN)
                    call.respondText("input = $result")
                } else
                    call.respondText("Incorrect input")
            }
        }

        get("/j_to_g") {
            val input = call.parameters[JULIAN]
            if (input != null) {
                if (input.matches(Regex("\\d{2}.\\d{2}.\\d{4}"))) {
                    val dates = input.split(".")
                    val result = getGrigorianDate(dates[0].toInt(), dates[1].toInt(), dates[2].toInt(), ClassDateEnum.JULIAN)
                    call.respondText("input = $result")
                } else
                    call.respondText("Incorrect input")
            }
        }
    }
}

fun checkCorrectDay(day: Int, month: Int): Boolean {
    if (month !in 1..12)
        return false
    if (day < 1 || day > DAYS[month - 1])
        return false
    return true
}

fun isBissextile(year : Int) : Boolean {
    return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0
}

fun getJulianDate (day : Int, month : Int, year : Int, from : ClassDateEnum) : String {
    if (!checkCorrectDay(day, month))
        return "Incorrect date"
    when(from) {
        ClassDateEnum.GRIGORIAN -> {
            return if (day > 13)
                String.format("%02d.%02d.%d", day - 13, month, year)
            else {
                val takeDays = day - 13
                val newMonth = if (month == 1) 12 else month - 1
                val newYear = year - if (newMonth == 12) 1 else 0
                val newDay = DAYS[newMonth - 1] + takeDays + if (isBissextile(newYear) && newMonth == 2) 1 else 0

                String.format("%02d.%02d.%d", newDay, newMonth, newYear)
            }
        }
        else -> return String.format("%02d.%02d.%d", day, month, year)
    }
}

fun getGrigorianDate (day : Int, month : Int, year : Int, from : ClassDateEnum) : String {
    if (!checkCorrectDay(day, month))
        return "Incorrect date"
    when (from) {
        ClassDateEnum.JULIAN -> {
            return if (day + 13 <= DAYS[month])
                String.format("%02d.%02d.%d", day + 13, month, year)
            else {
                val giveDays = 13 - (DAYS[month] - day)
                val newMonth = if (month == 12) 1 else month - 1
                val newYear = year + if (newMonth == 1) 1 else 0
                String.format("%02d.%02d.%d", giveDays, newMonth, newYear)
            }
        }
        else -> return String.format("%02d.%02d.%d", day, month, year)
    }
}
