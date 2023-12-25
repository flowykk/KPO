package service.handlers

import MovieModes
import SessionModes
import entity.Movie
import entity.Session
import service.CinemaManager
import service.ConsoleUI
import service.util.currentDate
import service.util.currentTime
import service.util.dateFormatter
import service.util.timeFormatter
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeParseException

interface SessionHandlerEntity {
    fun handleSessionInput(): Session?
    fun addSession()
    fun deleteSession()
    fun editTime()
    fun editDate()
    fun displaySessions(movie: Movie?): Boolean
    fun displaySeats()
    fun handleSessionTimeInput(mode: SessionModes): String
    fun handleSessionDateInput(): String
    fun handleTimeInput(): Pair<String, String>
}

class SessionHandler(
    private val cinemaManager: CinemaManager,
    private val consoleUI: ConsoleUI
) : MenuEntity, SessionHandlerEntity {
    override fun run() {
        displayMenu()
        handleMenuInput()
    }

    override fun displayMenu() {
        println("1. Посмотреть сеансы на фильм")
        println("2. Добавить сеанс")
        println("3. Удалить сеанс")
        println("4. Изменить дату сеанса")
        println("5. Изменить временной промежуток сеанса")
        println("00. Вернуться в Главное меню")
        println("0. Выход")
    }

    override fun handleMenuInput() {
        while (true) {
            print("Введите число от 0 до 5 или 00: ")
            val userInput: String? = readlnOrNull()

            when (userInput) {
                "1" -> displaySessions(consoleUI.getMovieHandler.handleMovieInput(MovieModes.SEARCH))
                "2" -> addSession()
                "3" -> deleteSession()
                "4" -> editDate()
                "5" -> editTime()
                "00" -> {
                    println()
                    consoleUI.getMainMenuHandler.run()
                }
                "0" -> consoleUI.exitMenu()
                else -> println("Неверный ввод. Пожалуйста, выберите действие от 0 до 5 или 00.")
            }

            run()
        }
    }


    override fun handleSessionInput(): Session? {
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

        return session
    }

    override fun displaySessions(movie: Movie?): Boolean {
        return if (movie == null) false else cinemaManager.viewSessionsInfo(movie)
    }

    override fun addSession() {
        consoleUI.getMovieHandler.displayMovies()

        val movie = consoleUI.getMovieHandler.handleMovieInput(MovieModes.SEARCH)
        if (movie == null) {
            println("Произошла ошибка!\n")
            return
        }
        displaySessions(movie)

        val date = handleSessionDateInput()
        if (date == "00") {
            return
        }

        val (startTime, endTime) = handleTimeInput()
        if (startTime == "" && endTime == "") {
            return
        }

        if (startTime < currentTime) {
            println("Извините, данный сеанс уже нельзя изменить!\n")
            return
        }

        cinemaManager.addSession(movie, date, startTime, endTime)
    }

    override fun deleteSession() {
        consoleUI.getMovieHandler.displayMovies()

        val movie = consoleUI.getMovieHandler.handleMovieInput(MovieModes.SEARCH)
        if (movie == null) {
            println("Произошла ошибка!\n")
            return
        }
        if (!displaySessions(movie)) {
            return
        }

        val session = handleSessionInput()
        if (session == null) {
            println()
            return
        }

        cinemaManager.deleteSession(session)
    }

    override fun editTime() {
        consoleUI.getMovieHandler.displayMovies()

        val movie = consoleUI.getMovieHandler.handleMovieInput(MovieModes.SEARCH)
        if (movie == null) {
            println("Произошла ошибка!\n")
            return
        }
        if (!displaySessions(movie)) {
            return
        }

        val session = handleSessionInput()
        if (session == null) {
            println()
            return
        }

        val (startTime, endTime) = handleTimeInput()
        if (startTime == "" && endTime == "") {
            return
        }

        if (startTime < currentTime) {
            println("Извините, данный сеанс уже нельзя изменить!\n")
            return
        }

        cinemaManager.editSessionTime(session, startTime, endTime)
    }

    override fun editDate() {
        consoleUI.getMovieHandler.displayMovies()

        val movie = consoleUI.getMovieHandler.handleMovieInput(MovieModes.SEARCH)
        if (movie == null) {
            println("Произошла ошибка!\n")
            return
        }
        if (!displaySessions(movie)) {
            return
        }

        val session = handleSessionInput()
        if (session == null) {
            println()
            return
        }

        if (LocalDate.parse(session.date, dateFormatter) < LocalDate.parse(currentDate, dateFormatter)) {
            println("Извините, данный сеанс уже нельзя изменить!\n")
            return
        }

        val date = handleSessionDateInput()
        if (date == "00") {
            return
        }

        cinemaManager.editSessionDate(session, date)

    }

    override fun displaySeats() {
        val session = handleSessionInput()

        if (session != null) {
            println("Информация по выбранному сеансу:\n")
            session.viewInfo()
            println("⚪ - свободное место")
            println("\uD83D\uDD34 - занятое место")

            session.viewSeats()
        } else {
            println()
            return
        }
    }

    override fun handleSessionTimeInput(mode: SessionModes): String {
        var startTimeString = ""
        var startTime: LocalTime? = null

        while (startTime == null && startTimeString != "00") {
            print("Введите время ${if (mode == SessionModes.END) "конца" else "начала"} сеанса: ")
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

    override fun handleSessionDateInput(): String {
        var dateString = ""
        var date: LocalDate? = null

        while (date == null && dateString != "00") {
            print("Введите дату сеанса: ")
            dateString = readlnOrNull().orEmpty().trim()

            try {
                date = LocalDate.parse(dateString, dateFormatter)
            } catch (e: DateTimeParseException) {
                println(
                    "Некорректно введена дата сеанса! Дата должна соответствовать формату гггг.мм.дд\n" +
                            "Повторите ввод ещё раз или введите 00 для выхода в меню."
                )
            }
        }

        return dateString
    }

    override fun handleTimeInput(): Pair<String, String> {
        var startTime: String
        var endTime: String
        do {
            println("Введите время начала и конца сеанса (время начала должнл быть меньше времени конца)")
            startTime = handleSessionTimeInput(SessionModes.START)
            if (startTime == "00") {
                return Pair("", "")
            }

            endTime = handleSessionTimeInput(SessionModes.END)
            if (endTime == "00") {
                return Pair("", "")
            }

            val startLocalTime = LocalTime.parse(startTime, timeFormatter)
            val endLocalTime = LocalTime.parse(endTime, timeFormatter)
        } while (startLocalTime >= endLocalTime)

        return Pair(startTime, endTime)
    }
}