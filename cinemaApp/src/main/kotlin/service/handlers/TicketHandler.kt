package service.handlers

import entity.Seat
import entity.Session
import service.CinemaManager
import service.ConsoleUI
import service.util.currentDate
import service.util.currentTime
import service.util.dateFormatter
import java.time.LocalDate

interface TicketHandlerEntity {
    fun handleTicketInput(): Session?
    fun sellTicket()
    fun refundTicket()
}

class TicketHandler(
    private val cinemaManager: CinemaManager,
    private val consoleUI: ConsoleUI
) : TicketHandlerEntity {

    override fun handleTicketInput(): Session? {
        consoleUI.getMovieHandler.displayMovies()

        val movie = consoleUI.getMovieHandler.handleMovieInput(MovieModes.SEARCH) ?: return null
        if (!consoleUI.getSessionHandler.displaySessions(movie)) {
            return null
        }

        val session = consoleUI.getSessionHandler.handleSessionInput()
        if (session == null) {
            println("Произошла ошибка\n")
            return null
        }

        session.viewSeats()

        return session
    }

    override fun sellTicket() {
        val session = handleTicketInput() ?: return

        if (session.startTime < currentTime || LocalDate.parse(session.date, dateFormatter) < LocalDate.parse(currentDate, dateFormatter)) {
            println("Извините, билет на данный сеанс недоступен к покупке!\n")
            return
        }

        val (row, number) = consoleUI.handleSeatInput(session, "buying")
        if (row != null && number != null) {
            cinemaManager.sellTicket(session, Seat(row, number))
        }
    }

    override fun refundTicket() {
        val session = handleTicketInput() ?: return

        if (session.startTime < currentTime || LocalDate.parse(session.date, dateFormatter) < LocalDate.parse(currentDate, dateFormatter)) {
            println("Извините, билет на данный сеанс недоступен к возврату!\n")
            return
        }

        val (row, number) = consoleUI.handleSeatInput(session, "refunding")
        if (row != null && number != null) {
            cinemaManager.refundTicket(session, Seat(row, number))
        }
    }
}