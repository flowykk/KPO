package service

import entity.Movie
import entity.Seat
import entity.Session
import entity.Ticket
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

import java.time.LocalTime

class CinemaManager(private val cinemaHandler: CinemaHandler) {

    private val movies: MutableSet<Movie> = mutableSetOf()
    private val sessions: MutableList<Session> = mutableListOf()
    private val tickets: MutableList<Ticket> = mutableListOf()

    private val dateFormat = SimpleDateFormat("dd/M/yyyy")

    fun addSession(movie: Movie, date: String, startTime: String, endTime: String) {
        val newSession = Session(movie, date, startTime, endTime)
        sessions.add(newSession)
        movies.add(movie)
        sessions.sortWith(compareBy(Session::date, Session::startTime))
        //fileHandler.saveSessions(sessions)
    }

    fun saveSessionsToFile() {
        cinemaHandler.saveSessions(sessions)
    }

    fun getSessionsByMovie(movie: Movie): List<Session> {
        var movieSessions: MutableList<Session> = mutableListOf()

        for (session in sessions) {
            if (movie == session.movie)
            {
                movieSessions.add(session)
            }
        }

        return movieSessions
    }

    fun getSessionById(sessionId: Int): Session? {
//        println("111" + sessions.find { it.id == 2 }?.availableSeats)
//        println("111" + sessions.find { it.id == 3 }?.availableSeats)
//        println("111" + sessions.find { it.id == 4 }?.availableSeats)
        return sessions.find { it.id == sessionId }
    }

    fun getMovies(): MutableSet<Movie> {
        return movies
    }

    fun getMovieByName(movieTitle: String): Movie? {
        return movies.find { it.title == movieTitle }
    }

    // ... другие методы ...
}
