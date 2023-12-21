package entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalTime
import java.time.Duration
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.concurrent.atomic.AtomicInteger

class Session (
    @JsonProperty("movie") val movie: Movie,
    @JsonProperty("date") val date: String,
    @JsonProperty("startTime") val startTime: String,
    @JsonProperty("endTime") val endTime: String,
) {
    val availibleSeats: MutableList<Seat> = mutableListOf()

    init {
        val numRows = 6
        val numSeatsPerRow = 10

        for (row in 1..numRows) {
            for (seatNumber in 1..numSeatsPerRow) {
                val seat = Seat(row, seatNumber)
                availibleSeats.add(seat)
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

    fun markSeat(row: Int, number: Int) {
        val seat = Seat(row, number)
        if (availibleSeats.contains(seat)) {
            availibleSeats.remove(seat)
        }
    }

    fun releaseSeat(row: Int, number: Int) {
        val seat = Seat(row, number)
        if (!availibleSeats.contains(seat)) {
            availibleSeats.add(seat)
        }
    }


}