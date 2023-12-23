package service

import com.fasterxml.jackson.databind.ObjectMapper
import entity.Movie
import entity.Session
import entity.Ticket
import java.io.File

class CinemaFileHandler() {
    private val dataFolderPath = "Data" // Папка для хранения файлов данных
    private val objectMapper = ObjectMapper()

    private val sessionsFilePath = "/sessions.json"
    private val ticketsFilePath = "/tickets.json"
    private val moviesFilePath = "/movies.json"

    fun saveSessions(movies: List<Session>) {
        // Сохранить список фильмов в файл
        val data = objectMapper.writeValueAsString(movies)
        saveToFile(sessionsFilePath, data)
    }

    fun saveTickets(movies: List<Ticket>) {
        // Сохранить список фильмов в файл
        val data = objectMapper.writeValueAsString(movies)
        saveToFile(ticketsFilePath, data)
    }

    fun saveMovies(movies: Set<Movie>) {
        // Сохранить список фильмов в файл
        val data = objectMapper.writeValueAsString(movies)
        saveToFile(moviesFilePath, data)
    }

    private fun saveToFile(fileName: String, content: String) {
        // Записать содержимое в файл
        File("$dataFolderPath/$fileName").writeText(content)
    }


    private fun readFromFile(fileName: String): String {
        // Прочитать содержимое из файла
        return File("$dataFolderPath/$fileName").readText()
    }
}
