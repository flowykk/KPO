package service

import com.fasterxml.jackson.databind.ObjectMapper
import entity.Movie
import entity.Session
import entity.Ticket
import java.io.File

interface CinemaFileHandlerEntity {
    fun saveSessions(movies: List<Session>)
    fun saveTickets(movies: List<Ticket>)
    fun saveMovies(movies: Set<Movie>)
    fun saveToFile(fileName: String, content: String)
}

class CinemaFileHandler : CinemaFileHandlerEntity {
    private val dataFolderPath = "Data" // Папка для хранения файлов данных
    private val sessionsFilePath = "/sessions.json"
    private val ticketsFilePath = "/tickets.json"
    private val moviesFilePath = "/movies.json"

    private val objectMapper = ObjectMapper()

    override fun saveSessions(movies: List<Session>) {
        // Сохранить список фильмов в файл
        val data = objectMapper.writeValueAsString(movies)
        saveToFile(sessionsFilePath, data)
    }

    override fun saveTickets(movies: List<Ticket>) {
        // Сохранить список фильмов в файл
        val data = objectMapper.writeValueAsString(movies)
        saveToFile(ticketsFilePath, data)
    }

    override fun saveMovies(movies: Set<Movie>) {
        // Сохранить список фильмов в файл
        val data = objectMapper.writeValueAsString(movies)
        saveToFile(moviesFilePath, data)
    }

    override fun saveToFile(fileName: String, content: String) {
        // Записать содержимое в файл
        File("$dataFolderPath/$fileName").writeText(content)
    }
}
