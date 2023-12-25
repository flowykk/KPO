package service

import MovieModes
import entity.Movie
import entity.Seat
import entity.Session
import entity.Ticket

interface CinemaManagerMovieEntity {
    fun getMovies(): MutableSet<Movie>
    fun getMovieByName(movieTitle: String): Movie?
    fun editMovie(editingMovie: Movie, mode: MovieModes, data: String): Boolean
    fun addMovie(movie: Movie)
    fun deleteMovie(movie: Movie)
}

interface CinemaManagerSessionEntity {
    fun addSession(movie: Movie, date: String, startTime: String, endTime: String)
    fun deleteSession(deletingSession: Session)
    fun editSessionDate(editingSession: Session, date: String)
    fun editSessionTime(editingSession: Session, newStartTime: String, newEndTime: String)
    fun deleteSessionsByMovie(movie: Movie)
    fun getSessionsByMovie(movie: Movie): List<Session>
    fun viewSessionsInfo(movie: Movie): Boolean
    fun getSessionById(sessionId: Int): Session?
    fun getSessionByTime(startTime: String, movie: Movie): Session?
}

interface CinemaManagerTicketEntity {
    fun sellTicket(ticketSession: Session, ticketSeat: Seat)
    fun refundTicket(refTicketSession: Session, refTicketSeat: Seat)
}

class CinemaManager(private val cinemaFileHandler: CinemaFileHandler) : CinemaManagerMovieEntity,
    CinemaManagerSessionEntity, CinemaManagerTicketEntity {

    private val movies: MutableSet<Movie> = mutableSetOf()
    private val sessions: MutableList<Session> = mutableListOf()
    private val tickets: MutableList<Ticket> = mutableListOf()

    override fun getMovies(): MutableSet<Movie> {
        return movies
    }

    override fun getMovieByName(movieTitle: String): Movie? {
        return movies.find { it.title.lowercase() == movieTitle.lowercase() }
    }

    override fun editMovie(editingMovie: Movie, mode: MovieModes, data: String): Boolean {
        for (movie in movies) {
            if (movie.title.lowercase() == editingMovie.title.lowercase()) {
                if (mode == MovieModes.EDITNAME) {
                    if (movie.title.lowercase() == data.lowercase()) {
                        return false
                    }
                    movie.title = data
                } else {
                    if (movie.director == data.lowercase()) {
                        return false
                    }
                    movie.director = data
                }
            }
        }

        cinemaFileHandler.saveSessions(sessions)
        cinemaFileHandler.saveMovies(movies)

        return true
    }

    override fun addMovie(movie: Movie) {
        movies.add(movie)

        cinemaFileHandler.saveMovies(movies)

        println("Фильм успешно добавлен в прокат!\n")
    }

    override fun deleteMovie(movie: Movie) {
        movies.remove(movie)
        deleteSessionsByMovie(movie)

        cinemaFileHandler.saveMovies(movies)
        cinemaFileHandler.saveSessions(sessions)

        println("Фильм успешно удалён из проката!\n")
    }

    override fun getSessionsByMovie(movie: Movie): List<Session> {
        return sessions.filter { it.movie.title == movie.title }
    }

    override fun getSessionById(sessionId: Int): Session? {
        return sessions.find { it.getId() == sessionId }
    }

    override fun getSessionByTime(startTime: String, movie: Movie): Session? {
        return sessions.find { it.movie.title == movie.title && it.startTime == startTime }
    }

    override fun viewSessionsInfo(movie: Movie): Boolean {
        val sessions = getSessionsByMovie(movie)

        if (sessions.isNotEmpty()) {
            println("Сеансы для фильма '${movie.title}':\n")
            for (session in sessions) session.viewInfo()
            return true
        } else {
            println("Для фильма \"${movie.title}\" нет доступных сеансов.\n")
        }
        return false
    }

    override fun addSession(movie: Movie, date: String, startTime: String, endTime: String) {
        val newSession = Session(movie, date, startTime, endTime)
        sessions.add(newSession)
        sessions.sortWith(compareBy(Session::date, Session::startTime))

        cinemaFileHandler.saveSessions(sessions)

        println("Сеанс успешно добавлен!\n")
    }

    override fun deleteSession(deletingSession: Session) {
        for (session in sessions) {
            if (deletingSession.getId() == session.getId()) {
                sessions.remove(session)
                break
            }
        }

        cinemaFileHandler.saveSessions(sessions)

        println("Сеанс успешно удалён!\n")
    }

    override fun editSessionDate(editingSession: Session, date: String) {
        for (session in sessions) {
            if (session.getId() == editingSession.getId()) {
                session.date = date
            }
        }

        cinemaFileHandler.saveSessions(sessions)

        println("Изменения произошли успешно!\n")
    }

    override fun editSessionTime(editingSession: Session, newStartTime: String, newEndTime: String) {
        for (session in sessions) {
            if (session.getId() == editingSession.getId()) {
                session.startTime = newStartTime
                session.endTime = newEndTime
            }
        }

        cinemaFileHandler.saveSessions(sessions)

        println("Изменения произошли успешно!\n")
    }

    override fun deleteSessionsByMovie(movie: Movie) {
        sessions.removeIf { it.movie == movie }
    }

    override fun sellTicket(ticketSession: Session, ticketSeat: Seat) {
        val ticket = Ticket(ticketSession, ticketSeat)
        tickets.add(ticket)

        for (session in sessions) {
            if (session == ticketSession) {
                session.markSeat(ticketSeat)
            }
        }

        cinemaFileHandler.saveTickets(tickets)
        cinemaFileHandler.saveSessions(sessions)

        println("Билет успешно продан!")
        ticket.viewInfo()
    }

    override fun refundTicket(refTicketSession: Session, refTicketSeat: Seat) {
        val refTicket = Ticket(refTicketSession, refTicketSeat)

        for (ticket in tickets) {
            if (ticket.session == refTicketSession) {
                tickets.remove(ticket)
                break
            }
        }

        for (session in sessions) {
            if (session == refTicketSession) {
                session.releaseSeat(refTicketSeat)
            }
        }

        cinemaFileHandler.saveTickets(tickets)
        cinemaFileHandler.saveSessions(sessions)

        println("Билет успешно возвращён!")
        refTicket.viewInfo()
    }

    companion object {
        const val numRows = 6
        const val numColumns = 10
    }
}
