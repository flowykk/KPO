package auth

import service.ConsoleUI
import service.util.handleStringInput
import org.mindrot.jbcrypt.BCrypt

class AuthService(private val userManager: UserManager) {
    private var currentUser: User? = null

    fun registerUser(): Boolean {
        println("Введите данные для регистрации.")
        val username = handleStringInput("Введите имя пользователя: ", "Имя пользователя введено некорректно!\n" +
                "Имя пользователя должно состоять из не менее чем 6-ти латинских букв или цифр!", InfoModes.USERNAME)

        if (username.isEmpty()) {
            return false
        }

        if (userManager.getAll().any { it.username == username }) {
            println("Пользователь с именем \"$username\" уже существует.")
            return false
        }

        val password = handleStringInput("Придумайте свой пароль: ", "Пароль введён некорректно!\n" +
                "Пароль должен состоять из не менее чем 4-ёх латинских букв или цифр!", InfoModes.PASSWORD)
        //val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())

        val newUser = User(username, password)
        userManager.addUser(newUser)
        println("Пользователь \"$username\" registered successfully.")
        return true
    }

    // Method to authenticate a user
    fun authenticateUser(): Boolean {
        println("Введите данные для авторизации.")
        val username = handleStringInput("Введите имя пользователя: ", "Имя пользователя введено некорректно!\n" +
                "Имя пользователя должно состоять из не менее чем 6-ти латинских букв или цифр!", InfoModes.USERNAME)

        if (username.isEmpty()) {
            return false
        }

        // Check if the user exists
        val user = userManager.getAll().find { it.username == username }

        val password = handleStringInput("Введите пароль: ", "Пароль введён некорректно!\n" +
                "Пароль должен состоять из не менее чем 4-ёх латинских букв или цифр!", InfoModes.PASSWORD)
        //val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())

        if (user?.password == password) {
            // Authentication successful
            currentUser = user
            println("Авторизация произошла успешна. Добро пожаловать, ${user.username}!")
            return true
        } else {
            // Authentication failed
            println("Неправильно введено имя пользователя или пароль. Попробуйте ещё раз.")
            return false
        }
    }

    // Method to get the currently logged-in user
    fun getCurrentUser(): User? {
        return currentUser
    }
}
