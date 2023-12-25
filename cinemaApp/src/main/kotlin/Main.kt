import auth.*
import entity.Movie

import service.ConsoleUI
import service.CinemaManager

import entity.Seat
import service.CinemaFileHandler
import java.time.LocalDate
import java.time.LocalTime

fun main() {

    val cinemaFileHandler = CinemaFileHandler()
    val userFileHandler = UserFileHandler()

    val cinemaManager = CinemaManager(cinemaFileHandler)
    val userManager = UserManager(userFileHandler)

    val auth = AuthService(userManager)

    val consoleUI = ConsoleUI(cinemaManager)
    val authHandler = AuthHandler(auth, consoleUI)

    val movie = Movie("Meet Joe Black", "Director1")
    val movie2 = Movie("Quite Place", "Director2")
    val movie3 = Movie("Soul", "Director3")

    cinemaManager.addMovie(movie)
    cinemaManager.addMovie(movie2)
    cinemaManager.addMovie(movie3)

    cinemaManager.addSession(
        movie,
        "2004.11.26",
        LocalTime.of(14, 30).toString(),
        LocalTime.of(15, 50).toString()
    )
    cinemaManager.addSession(
        movie2,
        "2004.12.26",
        LocalTime.of(15, 30).toString(),
        LocalTime.of(15, 50).toString()
    )
    cinemaManager.addSession(
        movie,
        "2004.12.26",
        LocalTime.of(13, 30).toString(),
        LocalTime.of(16, 50).toString()
    )
    cinemaManager.addSession(
        movie,
        "2004.12.26",
        LocalTime.of(14, 40).toString(),
        LocalTime.of(16, 50).toString()
    )

    cinemaManager.getSessionById(1)?.markSeat(Seat(1, 6))
    cinemaManager.getSessionById(2)?.markSeat(Seat(2, 6))
    cinemaManager.getSessionById(3)?.markSeat(Seat(3, 6))
    cinemaManager.getSessionById(4)?.markSeat(Seat(4, 6))

    cinemaManager.addSession(
        movie2,
        LocalDate.of(2004, 12, 30).toString(),
        LocalTime.of(20, 30).toString(),
        LocalTime.of(21, 50).toString()
    )

    userManager.addUser(
        User(
            "flowykk", "d74ff0ee8da3b9806b18c877dbf29bbde50b5bd8e4dad7a3a725000feb82e8f1"
            // password to test auth is "pass"
        )
    )

    authHandler.run()
}