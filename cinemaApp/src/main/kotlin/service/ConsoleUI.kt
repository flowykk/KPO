package service

import entity.Seat
import entity.Session
import service.handlers.MainMenuHandler
import service.handlers.MovieHandler
import service.handlers.SessionHandler
import service.handlers.TicketHandler
import kotlin.system.exitProcess

interface ConsoleUIEntity {
    fun exitMenu()
    fun handleSeatInput(session: Session, mode: String): Pair<Int?, Int?>
}

class ConsoleUI(cinemaManager: CinemaManager) : ConsoleUIEntity {
    private val mainMenuHandler = MainMenuHandler(this)
    private val movieHandler = MovieHandler(cinemaManager, this)
    private val sessionHandler = SessionHandler(cinemaManager, this)
    private val ticketHandler = TicketHandler(cinemaManager, this)

    val getMainMenuHandler: MainMenuHandler
        get() = mainMenuHandler

    val getMovieHandler: MovieHandler
        get() = movieHandler

    val getSessionHandler: SessionHandler
        get() = sessionHandler

    val getTicketHandler: TicketHandler
        get() = ticketHandler

    override fun exitMenu() {
        exitProcess(0)
    }

    override fun handleSeatInput(session: Session, mode: String): Pair<Int?, Int?> {
        var row: Int?
        var number: Int?

        while (true) {
            print("Введите ряд (число от 1 до 6): ")
            row = readlnOrNull()?.toIntOrNull()

            print("Введите номер места (число от 1 до 10): ")
            number = readlnOrNull()?.toIntOrNull()

            if (row == 0 && number == 0) {
                return Pair(null, null)
            }

            if (row == null || number == null || row !in 1..6 || number !in 1..10 || (if (mode == "buying") !session.markSeat(
                    Seat(row, number)
                ) else !session.releaseSeat(Seat(row, number)))
            ) {
                //val modeBool = if (mode == "buying") !session.markSeat(Seat(row, number)) else !session.markSeat(Seat(row, number))

                println(
                    "Некорректный ввод данных о билете или же выбранное место ${if (mode == "buying") "занятно" else "свободно"}!\n" +
                            "Повторите ввод ещё раз или введите Ряд 0 и Место 0 для выхода в Главное меню."
                )
            } else {
                break
            }
        }

        println()
        return Pair(row, number)
    }
}