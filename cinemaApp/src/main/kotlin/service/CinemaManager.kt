package service

import MovieModes
import entity.Movie
import entity.Seat
import entity.Session
import entity.Ticket
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

class CinemaManager(private val cinemaFileHandler: CinemaFileHandler) {

    private val movies: MutableSet<Movie> = mutableSetOf()
    private val sessions: MutableList<Session> = mutableListOf()
    private val tickets: MutableList<Ticket> = mutableListOf()

    private val dateFormat = SimpleDateFormat("dd/M/yyyy")
    private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

    companion object {
        const val numRows = 6
        const val numColumns = 10
    }
    fun addSession(movie: Movie, date: String, startTime: String, endTime: String) {
        val newSession = Session(movie, date, startTime, endTime)
        sessions.add(newSession)
        sessions.sortWith(compareBy(Session::date, Session::startTime))

        cinemaFileHandler.saveSessions(sessions)

        println("Сеанс успешно добавлен!\n")
    }

    fun sellTicket(ticketSession: Session, ticketSeat: Seat) {
        val ticket = Ticket(ticketSession, ticketSeat)
        tickets.add(ticket)

        for(session in sessions) {
            if (session == ticketSession) {
                session.markSeat(ticketSeat)
            }
        }

        cinemaFileHandler.saveTickets(tickets)
        cinemaFileHandler.saveSessions(sessions)

        println("Билет успешно продан!\n${ticket.getTicketInfo()}")
    }

    fun refundTicket(refTicketSession: Session, refTicketSeat: Seat) {
        val refTicket = Ticket(refTicketSession, refTicketSeat)

        for (ticket in tickets) {
            if (ticket.session == refTicketSession) {
                tickets.remove(ticket)
                break
            }
        }

        for(session in sessions) {
            if (session == refTicketSession) {
                session.releaseSeat(refTicketSeat)
            }
        }

        cinemaFileHandler.saveTickets(tickets)
        cinemaFileHandler.saveSessions(sessions)

        println("Билет успешно возвращён!\n${refTicket.getTicketInfo()}")
    }

    fun editMovie(editingMovie: Movie, mode: MovieModes, data: String) {
        for (movie in movies) {
            if (movie.title == editingMovie.title) {
                if (mode == MovieModes.EDITNAME) {
                    movie.mutableTitle = data
                } else {
                    movie.mutableDirector = data
                }
            }
        }

        // change movie in sessions
    }

    fun addMovie(movie: Movie) {
        movies.add(movie)

        cinemaFileHandler.saveMovies(movies)

        println("Фильм успешно добавлен в прокат!\n")
    }

    fun deleteMovie(movie: Movie) {
        movies.remove(movie)
        deleteSessionsByMovie(movie)

        cinemaFileHandler.saveMovies(movies)
        cinemaFileHandler.saveSessions(sessions)

        println("Фильм успешно удалён из проката!\n")
    }

    private fun deleteSessionsByMovie(movie: Movie) {
        sessions.removeIf { it.movie == movie }
    }

    private fun getSessionsByMovie(movie: Movie): List<Session> {
        return sessions.filter { it.movie.title == movie.title }

//        val movieSessions: MutableList<Session> = mutableListOf()
//
//        for (session in sessions) {
//            if (movie == session.movie)
//            {
//                movieSessions.add(session)
//            }
//        }
//
//        return movieSessions
    }

    // should go SessionHandler
    fun viewSessionsInfo(movie: Movie): Boolean {
        val sessions = getSessionsByMovie(movie)

        if (sessions.isNotEmpty()) {
            println("Сеансы для фильма '${movie.title}':\n")
            sessions.forEach { session ->
                println("Session Id: ${session.id}")
                println("Date: ${session.date}")
                println("Start Time: ${session.startTime}")
                println("End Time: ${session.endTime}")
                println("Duration: ${session.duration} minutes")
                println()
            }
            return true
        } else {
            println("Для фильма \"${movie.title}\" нет доступных сеансов.\n")
        }
        return false
    }

    fun getSessionById(sessionId: Int): Session? {
        return sessions.find { it.id == sessionId }
    }

    fun getMovies(): MutableSet<Movie> {
        return movies
    }

    fun getMovieByName(movieTitle: String): Movie? {
        return movies.find { it.title.lowercase() == movieTitle.lowercase() }
    }

    fun getSessionByTime(startTime: String, movie: Movie): Session? {
        return sessions.find { it.movie.title == movie.title && it.startTime == startTime }

    }

    // ... другие методы ...
}
