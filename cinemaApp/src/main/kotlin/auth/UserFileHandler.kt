package auth

import com.fasterxml.jackson.databind.ObjectMapper
import entity.Session
import java.io.File

class UserFileHandler {
    private val dataFolderPath = "Data" // Папка для хранения файлов данных
    private val objectMapper = ObjectMapper()

    private val usersFilePath = "/users.json"

    fun saveUsers(movies: List<User>) {
        val data = objectMapper.writeValueAsString(movies)
        saveToFile(data)
    }

    private fun saveToFile(content: String) {
        // Записать содержимое в файл
        File("$dataFolderPath/$usersFilePath").writeText(content)
    }

    private fun readFromFile(fileName: String): String {
        // Прочитать содержимое из файла
        return File("$dataFolderPath/$fileName").readText()
    }
}