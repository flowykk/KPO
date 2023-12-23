package auth

import com.fasterxml.jackson.databind.ObjectMapper
import entity.Session
import java.io.File

class UserFileHandler {
    private val dataFolderPath = "Data" // Папка для хранения файлов данных
    private val objectMapper = ObjectMapper()

    private val usersFilePath = "/sessions.json"

    fun saveUsers(movies: List<Session>) {
        // Сохранить список фильмов в файл
        val data = objectMapper.writeValueAsString(movies)
        saveToFile(usersFilePath, data)
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