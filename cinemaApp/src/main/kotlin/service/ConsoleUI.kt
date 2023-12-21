package service

import entity.Movie
import entity.Seat
import entity.Session
import entity.Ticket
import kotlin.system.exitProcess

class ConsoleUI(val cinemaManager: CinemaManager) {

    fun run() {
        displayMenu()
        handleUserInput()
    }

    fun displayMenu() {
        println("1. Посмотреть фильмы в прокате")
        println("2. Посмотреть сеансы на фильм")
        println("3. Посмотреть свободные места на сеанс")
//        println("1. Продажа билетов")
//        println("2. Возврат билетов")
//        println("3. Отображение свободных и проданных мест для выбранного сеанса")
//        println("4. Редактирование данных о фильмах и расписании сеансов")
//        println("5. Отметка занятых мест посетителями конкретного сеанса")
        println("0. Выход")
    }

    private fun handleUserInput() {
        var userInput: String? = readLine()

        while (userInput != "6") {
            when (userInput) {
                "1" -> displayMovies() //sellTickets()
                "2" -> displaySessions() //returnTickets()
                "3" -> displaySeats() //displaySeats()
                "4" -> print(4) //editSchedule()
                "5" -> print(5) //markOccupiedSeats()
                "0" -> exitMenu()
                else -> println("Неверный ввод. Пожалуйста, выберите действие от 0 до 5.")
            }

            displayMenu()
            userInput = readLine()
        }

        println("Выход из программы.")
    }

    fun exitMenu() {
        exitProcess(0)
    }

    fun displayMovies() {
        val movies = cinemaManager.getMovies()

        println("\nСписок фильмов, которые сейчас в прокате:")
        for (movie in movies) {
            println("${movie.title} by ${movie.director}")
        }
        println()
    }

    fun displaySeats() {
        print("Введите id сеанса: ")
        var sessionId = readLine()?.toIntOrNull()

        var session = sessionId?.let { cinemaManager.getSessionById(it) }

        if (session == null) {
            println("Сеанса с Id = $sessionId не существует!")
            return;
        }

        val numRows = 6
        val numSeatsPerRow = 10

        println(
            "\nИнформация по свободным местам выбранного фильма:\n" +
                    "- Фильм ${session.movie.title} by ${session.movie.director}\n" +
                    "- Дата сеанса: ${session.date}\n" +
                    "- Время начала: ${session.startTime}\n" +
                    "- Время конца: ${session.endTime}\n" +
                    "- Длительность фильма: ${session.duration} минут\n" +
                    "⚪ - свободное место\n" +
                    "\uD83D\uDD34 - занятое место"
        )

        for (row in 1..numRows) {
            print("Ряд $row ")
            for (seatNumber in 1..numSeatsPerRow) {
                val seat = Seat(row, seatNumber)
                if (session.availibleSeats.contains(seat)) {
                    print("⚪ ")
                } else {
                    print("\uD83D\uDD34 ")
                }
            }
            println()
        }

    }

    fun displaySessions() {
        var movieTitle: String
        do {
            println("Введите название фильма:")
            movieTitle = readLine().orEmpty().trim()
        } while (movieTitle.isBlank())

        val movie = cinemaManager.getMovieByName(movieTitle)
        val sessions = movie?.let { cinemaManager.getSessionsByMovie(it) }

        if (sessions == null) {
            println("Такого фильма пока нет в прокате!")
            return
        }

        if (sessions.isNotEmpty()) {
            println("Сеансы для фильма '${movie.title}':")
            sessions.forEach { session ->
                println("Session Id: ${session.id}")
                println("Date: ${session.date}")
                println("Start Time: ${session.startTime}")
                println("End Time: ${session.endTime}")
                println("Duration: ${session.duration} minutes")
                println()
            }
        } else {
            println("Для фильма '${movie.title}' нет доступных сеансов.")
        }

    }

    fun displayTicketInfo(ticket: Ticket) {

    }


}