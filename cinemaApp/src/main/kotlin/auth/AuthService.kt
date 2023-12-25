package auth

import service.util.handleStringInput
import service.util.sha256

interface AuthServiceEntity {
    fun registerUser(): Boolean
    fun authenticateUser(): Boolean
}

class AuthService(private val userManager: UserManager) : AuthServiceEntity {
    override fun registerUser(): Boolean {
        println("Введите данные для регистрации.")
        val username = handleStringInput(
            "Введите имя пользователя: ", "Имя пользователя введено некорректно!\n" +
                    "Имя пользователя должно состоять из не менее чем 6-ти латинских букв или цифр!", InfoModes.USERNAME
        )

        if (username.isEmpty()) {
            return false
        }

        if (userManager.getAll().any { it.getUsername() == username }) {
            println("Пользователь с именем \"$username\" уже существует.")
            return false
        }

        val password = handleStringInput(
            "Придумайте свой пароль: ", "Пароль введён некорректно!\n" +
                    "Пароль должен состоять из не менее чем 4-ёх латинских букв или цифр!", InfoModes.PASSWORD
        )
        val hashedPassword = sha256(password)

        val newUser = User(username, hashedPassword)
        userManager.addUser(newUser)
        println("Пользователь \"$username\" registered successfully.")
        return true
    }

    override fun authenticateUser(): Boolean {
        println("Введите данные для авторизации.")
        val username = handleStringInput(
            "Введите имя пользователя: ", "Имя пользователя введено некорректно!\n" +
                    "Имя пользователя должно состоять из не менее чем 6-ти латинских букв или цифр!", InfoModes.USERNAME
        )

        if (username.isEmpty()) {
            return false
        }

        val user = userManager.getAll().find { it.getUsername() == username }

        val password = handleStringInput(
            "Введите пароль: ", "Пароль введён некорректно!\n" +
                    "Пароль должен состоять из не менее чем 4-ёх латинских букв или цифр!", InfoModes.PASSWORD
        )
        val hashedPassword = sha256(password)

        if (user?.getPasswordHash() == hashedPassword) {
            println("Авторизация произошла успешна. Добро пожаловать, ${user.getUsername()}!")
            return true
        } else {
            println("Неправильно введено имя пользователя или пароль. Попробуйте ещё раз.")
            return false
        }
    }
}
