package entity

import com.fasterxml.jackson.annotation.JsonProperty

class Ticket(
    @JsonProperty("session") var session: Session,
    @JsonProperty("seat") val seat: Seat
) : CommonEntity {

    override fun viewInfo() {
        println("Билет: Ряд ${seat.row}, Место ${seat.number}, Фильм: ${session.movie.title}, Время: ${session.startTime}")
    }
}