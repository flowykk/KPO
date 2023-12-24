package service

import MovieModes
import entity.Movie
import entity.Seat
import entity.Session
import service.handlers.MainMenuHandler
import service.handlers.MovieHandler
import java.time.LocalTime
import java.time.format.DateTimeParseException
import kotlin.system.exitProcess

class ConsoleUI(private val cinemaManager: CinemaManager) {
    private val mainMenuHandler = MainMenuHandler(this)
    private val movieHandler = MovieHandler(cinemaManager, this)

    val getMainMenuHandler: MainMenuHandler
        get() = mainMenuHandler

    val getMovieHandler: MovieHandler
        get() = movieHandler

    private fun displaySessionsMenu() {
        println("1. Изменить сеанс")
        println("0. Выход")
    }

    fun exitMenu() {
        exitProcess(0)
    }

    fun displaySeats() {
        var session: Session? = null
        var sessionId: Int?

        while (true) {
            var flag = true

            print("Введите id сеанса: ")

            sessionId = readlnOrNull()?.toIntOrNull()
            if (sessionId == null) {
                println("ID введён некорректно!")
                flag = false
            } else if (sessionId == 0) {
                break
            }

            // Если что перенести в Session как метод
            session = sessionId?.let { cinemaManager.getSessionById(it) }
            if (session == null && flag) {
                println("Сеанса с ID = $sessionId не существует!")
                flag = false
            }

            if (!flag) {
                println("Повторите ввод ещё раз или введите 0 для выхода в Главное меню.")
            } else {
                break
            }
        }

        if (session != null) {
            println("Информация по выбранному сеансу:\n")
            println("- ID Сеанса: $sessionId")
            println("- Фильм \"${session.movie.title}\" by ${session.movie.director}")
            println("- Дата сеанса: ${session.date}")
            println("- Время начала: ${session.startTime}")
            println("- Время конца: ${session.endTime}")
            println("- Длительность фильма: ${session.duration} минут")
            println("⚪ - свободное место")
            println("\uD83D\uDD34 - занятое место")

            session.viewSeats()
        } else {
            println()
            return
        }
    }

    fun displaySessions(movie: Movie?): Boolean {
        return if (movie == null) false else cinemaManager.viewSessionsInfo(movie)
    }

    private fun handleSeatInput(session: Session, mode: String): Pair<Int?, Int?> {
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

            if (row == null || number == null || row !in 1..6 || number !in 1..10 || (if (mode == "buying") !session.markSeat(Seat(row, number)) else !session.releaseSeat(Seat(row, number)))) {
                //val modeBool = if (mode == "buying") !session.markSeat(Seat(row, number)) else !session.markSeat(Seat(row, number))

                println("Некорректный ввод данных о билете или же выбранное место ${if (mode == "buying") "занятно" else "свободно"}!\n" +
                        "Повторите ввод ещё раз или введите Ряд 0 и Место 0 для выхода в Главное меню.")
            } else {
                break
            }
        }

        println()
        return Pair(row, number)
    }

    private fun handleSessionTimeInput(): String {
        var startTimeString: String = ""
        var startTime: LocalTime? = null

        while (startTime == null && startTimeString != "00") {
            print("Введите время начала интересующего сеанса: ")
            startTimeString = readlnOrNull().orEmpty().trim()

            try {
                startTime = LocalTime.parse(startTimeString)
            } catch (e: DateTimeParseException) {
                println(
                    "Некорректно введено время сеанса!\n" +
                            "Повторите ввод ещё раз или введите 00 для выхода в меню."
                )
            }
        }

        return startTimeString
    }

    private fun handleTicketInput(): Session? {
        // вывод фильмов в прокате ->
        // ввод названия фильма ->
        // вывод сеансов на фильм ->
        // ввод времени начала нужного сеанса ->
        // вывод свободных мест ->
        // ввод ряда и номера места ->
        // успешная продажа

        movieHandler.displayMovies()

        val movie = movieHandler.handleMovieInput(MovieModes.SEARCH) ?: return null
        if (!displaySessions(movie)) {
            return null
        }

        val startTime = handleSessionTimeInput()
        if (startTime == "-1") {
            println()
            return null
        }

        val session = movie.let { cinemaManager.getSessionByTime(startTime, movie) }
        if (session == null) {
            println("Сеансов на фильм  \"${movie.title}\" в $startTime пока нет!\n")
            return null
        }

        session.viewSeats()

        return session
//        val (row, number) = handleSeatInput(session)
//        if (row != null && number != null)
//        {
//            cinemaManager.sellTicket(session, Seat(row, number))
//            println("Место ряд: ${row}, номер: ${number} отмечено как занятое!\n")
//        }
    }

    fun sellTicket() {
        val session = handleTicketInput() ?: return

        val (row, number) = handleSeatInput(session, "buying")
        if (row != null && number != null)
        {
            cinemaManager.sellTicket(session, Seat(row, number))
        }
    }

    fun refundTicket() {
        val session = handleTicketInput() ?: return

        val (row, number) = handleSeatInput(session, "refunding")
        if (row != null && number != null)
        {
            cinemaManager.refundTicket(session, Seat(row, number))
        }
    }
}