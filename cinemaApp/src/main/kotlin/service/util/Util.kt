package service.util

import InfoModes
import java.security.MessageDigest
import java.time.format.DateTimeFormatter

const val currentTime = "14:00"
const val currentDate = "2004.12.26"
val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

fun handleStringInput(message: String, errorMessage: String, mode: InfoModes): String {
    var data: String

    while (true) {
        print(message)
        data = readlnOrNull().orEmpty().trim().lowercase()

        if (data == "00") {
            return ""
        }

        if (
            (mode == InfoModes.USERNAME && !isValidUsername(data)) ||
            (mode == InfoModes.PASSWORD && !isValidPassword(data))
        ) {
            println("$errorMessage\nПовторите ввод ещё раз или введите 00 для выхода в меню.")
        } else {
            break
        }
    }

    return data
}

fun sha256(input: String): String {
    val bytes = input.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.joinToString("") { "%02x".format(it) }
}

fun capitalizeFirst(data: String): String {
    return data.substring(0, 1).uppercase() + data.substring(1)
}

fun isDirector(data: String): Boolean {
    val regex = Regex("^\\s*[a-zA-Z]+(\\s+[a-zA-Z]+){0,2}\\s*$")
    return data.matches(regex)
}

fun isMovieTitle(title: String): Boolean {
    val regex = Regex("^[a-zA-Z0-9\\s]+$")
    return title.matches(regex)
}

fun isValidPassword(password: String): Boolean {
    val regex = Regex("[a-zA-Z0-9]+")
    return password.matches(regex) && password.length >= 4
}

fun isValidUsername(username: String): Boolean {
    val regex = Regex("^[a-zA-Z0-9_]+$")
    return username.matches(regex) && username.length >= 4
}