package entity

class Ticket (
    val session: Session,
    val seat: Seat
)
{
    fun getTicketInfo(): String {
        return "Билет: Ряд ${seat.row}, Место ${seat.number}, Фильм: ${session.movie.title}, Время: ${session.startTime}"
    }
}