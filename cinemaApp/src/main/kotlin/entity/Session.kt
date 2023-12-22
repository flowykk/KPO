package entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import service.CinemaManager
import java.time.LocalTime
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicInteger

class Session @JsonCreator constructor(
    @JsonProperty("movie") val movie: Movie,
    @JsonProperty("date") val date: String,
    @JsonProperty("startTime") val startTime: String,
    @JsonProperty("endTime") val endTime: String,
) {
    //@JsonIgnore
    @JsonProperty("availableSeats")
    private val availableSeats: MutableList<Seat> = mutableListOf()

    init {
        for (row in 1..CinemaManager.numRows) {
            for (seatNumber in 1..CinemaManager.numColumns) {
                val seat = Seat(row, seatNumber)
                availableSeats.add(seat)
            }
        }
    }
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    companion object {
        private val idCounter = AtomicInteger(1)
    }

    // Автоинкрементируемый Id сеанса
    @JsonProperty("id")
    val id: Int = idCounter.getAndIncrement()

    @get:JsonProperty("duration")
    val duration: Int
        get() {
            val start = LocalTime.parse(startTime, timeFormatter)
            val end = LocalTime.parse(endTime, timeFormatter)
            return Duration.between(start, end).toMinutes().toInt()
        }

    fun markSeat(seat: Seat): Boolean {
        if (availableSeats.contains(seat)) {
            availableSeats.remove(seat)
            return true
        }

        return false
    }

    fun releaseSeat(seat: Seat): Boolean {
        if (!availableSeats.contains(seat)) {
            availableSeats.add(seat)
            return true
        }

        return false
    }

    // либо сделать этот метод методом ConsoleUI
    fun viewSeats() {
        println("      =============ЭКРАН=============")
        for (row in 1..CinemaManager.numRows) {
            print("Ряд $row ")
            for (seatNumber in 1..CinemaManager.numColumns) {
                val seat = Seat(row, seatNumber)
                if (availableSeats.contains(seat)) {
                    print("⚪ ")
                } else {
                    print("\uD83D\uDD34 ")
                }
            }
            println()
        }
        println()
    }

}