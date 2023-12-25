package auth

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

interface UserFileHandlerEntity {
    fun saveUsers(movies: List<User>)
    fun saveToFile(content: String)
}

class UserFileHandler : UserFileHandlerEntity {
    private val dataFolderPath = "Data"
    private val usersFilePath = "/users.json"

    private val objectMapper = ObjectMapper()

    override fun saveUsers(movies: List<User>) {
        val data = objectMapper.writeValueAsString(movies)
        saveToFile(data)
    }

    override fun saveToFile(content: String) {
        File("$dataFolderPath/$usersFilePath").writeText(content)
    }
}