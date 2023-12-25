package entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import service.CinemaManager
import java.time.LocalTime
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicInteger

interface SessionEntity {
    fun getId(): Int
    fun markSeat(seat: Seat): Boolean
    fun releaseSeat(seat: Seat): Boolean
    fun viewSeats()
}

class Session @JsonCreator constructor(
    @JsonProperty("movie") val movie: Movie,
    @JsonProperty("date") var date: String,
    @JsonProperty("startTime") var startTime: String,
    @JsonProperty("endTime") var endTime: String,
) : SessionEntity, CommonEntity {
    @JsonProperty("availableSeats")
    private val availableSeats: MutableList<Seat> = mutableListOf()
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    // Автоинкрементируемый Id сеанса
    @JsonProperty("id")
    private val id: Int = idCounter.getAndIncrement()

    @get:JsonProperty("duration")
    private val duration: Int
        get() {
            val start = LocalTime.parse(startTime, timeFormatter)
            val end = LocalTime.parse(endTime, timeFormatter)
            return Duration.between(start, end).toMinutes().toInt()
        }

    init {
        for (row in 1..CinemaManager.numRows) {
            for (seatNumber in 1..CinemaManager.numColumns) {
                val seat = Seat(row, seatNumber)
                availableSeats.add(seat)
            }
        }
    }

    override fun getId(): Int {
        return id
    }

    override fun markSeat(seat: Seat): Boolean {
        if (availableSeats.contains(seat)) {
            availableSeats.remove(seat)
            return true
        }

        return false
    }

    override fun releaseSeat(seat: Seat): Boolean {
        if (!availableSeats.contains(seat)) {
            availableSeats.add(seat)
            return true
        }

        return false
    }

    // либо сделать этот метод методом ConsoleUI
    override fun viewSeats() {
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

    override fun viewInfo() {
        println("- ID Сеанса: $id")
        println("- Фильм \"${movie.title}\" by ${movie.director}")
        println("- Дата сеанса: $date")
        println("- Время начала: $startTime")
        println("- Время конца: $endTime")
        println("- Длительность фильма: $duration минут")
        println()
    }

    companion object {
        private val idCounter = AtomicInteger(1)
    }
}