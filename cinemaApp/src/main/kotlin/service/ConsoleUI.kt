package service

import entity.Movie
import entity.Seat
import entity.Session
import java.time.LocalTime
import java.time.format.DateTimeParseException
import kotlin.system.exitProcess

class ConsoleUI(private val cinemaManager: CinemaManager) {

    fun run() {
        displayMenu()
        handleUserInput()
    }

    fun displayMenu() {
        println("Добро пожаловать в главное меню!")
        println("1. Осуществить продажу билета")
        println("2. Осуществить возврат билета")
        println("3. Посмотреть фильмы в прокате")
        println("4. Посмотреть сеансы на фильм")
        println("5. Посмотреть свободные места на сеанс")
//        println("1. Продажа билетов")
//        println("2. Возврат билетов")
//        println("3. Отображение свободных и проданных мест для выбранного сеанса")
//        println("4. Редактирование данных о фильмах и расписании сеансов")
//        println("5. Отметка занятых мест посетителями конкретного сеанса")
        println("0. Выход")
    }

    private fun handleUserInput() {
        print("Введите цифру от 0 до 7: ")
        var userInput: String? = readlnOrNull()

        while (true) {
            when (userInput) {
                "1" -> sellTicket() //handleTicketInput()
                "2" -> refundTicket()
                "3" -> displayMovies() //sellTickets()
                "4" -> displaySessions(handleMovieInput()) //returnTickets()
                "5" -> displaySeats() //displaySeats()
                "6" -> println(6) //editSchedule()
                "7" -> println(7) //markOccupiedSeats()
                "0" -> exitMenu()
                else -> println("Неверный ввод. Пожалуйста, выберите действие от 0 до 7.\n")
            }

            run()
        }
    }

    private fun handleMovieInput(): Movie? {
        print("Введите название фильма: ")
        val movieTitle: String = readlnOrNull().orEmpty().trim()

        val movie = cinemaManager.getMovieByName(movieTitle)

        if (movie == null) {
            println("Фильма $movieTitle пока нет в прокате!\n")
            return null
        }

        return movie
    }

    private fun exitMenu() {
        exitProcess(0)
    }

    private fun displayMovies() {
        val movies = cinemaManager.getMovies()

        println("\nСписок фильмов, которые сейчас в прокате:")
        for (movie in movies) {
            println("\"${movie.title}\" by ${movie.director}")
        }
        println()
    }

    private fun displaySeats() {
        print("Введите id сеанса: ")
        val sessionId = readLine()?.toIntOrNull()

        if (sessionId == null) {
            println("ID введён некорректно!\n")
            return;
        }

        // Если что перенести в Session как метод
        val session = sessionId.let { cinemaManager.getSessionById(it) }

        if (session == null) {
            println("Сеанса с ID = $sessionId не существует!\n")
            return;
        }

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
    }

    private fun displaySessions(movie: Movie?): Boolean {
        return if (movie == null) false else cinemaManager.viewSessionsInfo(movie)
    }

    private fun handleSeatInput(session: Session, mode: String): Pair<Int?, Int?> {
        var row: Int?
        var number: Int?

        while (true) {
            print("Введите ряд (число от 1 до 6): ")
            row = readLine()?.toIntOrNull()

            print("Введите номер места (число от 1 до 10): ")
            number = readLine()?.toIntOrNull()

            if (row == 0 && number == 0) {
                println()
                return Pair(null, null)
            }

            if (row == null || number == null || row !in 1..6 || number !in 1..10 || (if (mode == "buying") !session.markSeat(Seat(row, number)) else !session.releaseSeat(Seat(row, number)))) {
                //val modeBool = if (mode == "buying") !session.markSeat(Seat(row, number)) else !session.markSeat(Seat(row, number))

                println("Некорректный ввод данных о билете или же выбранное место ${if (mode == "buying") "занятно" else "свободно"}!\n" +
                        "Для выхода в Главное меню введите Ряд 0 и Место 0. ")
            } else {
                break
            }
        }

        return Pair(row, number)
    }

    private fun handleSessionTimeInput(): String {
        print("Введите время начала интересующего сеанса: ")
        var startTimeString = readlnOrNull().orEmpty().trim()
        var startTime: LocalTime? = null

        while (startTime == null && startTimeString != "-1") {
            try {
                startTime = LocalTime.parse(startTimeString)
            } catch (e: DateTimeParseException) {
                print(
                    "Некорректно введено время сеанса! Попробуйте ввести время ещё раз или\n" +
                            "или введите -1, чтобы вернуться в Главное меню: "
                )
                startTimeString = readlnOrNull().orEmpty().trim()
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

        displayMovies()

        val movie = handleMovieInput() ?: return null
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

    private fun sellTicket() {
        val session = handleTicketInput() ?: return

        val (row, number) = handleSeatInput(session, "buying")
        if (row != null && number != null)
        {
            cinemaManager.sellTicket(session, Seat(row, number))
            println("Место ряд: ${row}, номер: ${number} отмечено как занятое!\n")
        }
    }

    private fun refundTicket() {
        val session = handleTicketInput() ?: return

        val (row, number) = handleSeatInput(session, "refunding")
        if (row != null && number != null)
        {
            cinemaManager.refundTicket(session, Seat(row, number))
            println("Место ряд: ${row}, номер: ${number} отмечено как свободное!\n")
        }
    }
}