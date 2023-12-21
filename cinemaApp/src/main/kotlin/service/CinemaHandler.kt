package service

import com.fasterxml.jackson.databind.ObjectMapper
import entity.Movie
import entity.Session
import java.io.File

class CinemaHandler() {
    private val dataFolderPath = "Data" // Папка для хранения файлов данных
    private val sessionsFilePath = "/sessions.json";
    private val objectMapper = ObjectMapper()

    fun saveSessions(sessions: List<Session>) {
        val json = objectMapper.writeValueAsString(sessions)

        saveToFile(sessionsFilePath, json)
    }

    private fun saveToFile(fileName: String, content: String) {
        File("$dataFolderPath/$fileName").writeText(content)
    }

    private fun readFromFile(fileName: String): String {
        // Прочитать содержимое из файла
        return File("$dataFolderPath/$fileName").readText()
    }
}
