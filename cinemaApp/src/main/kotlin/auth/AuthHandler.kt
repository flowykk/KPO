package auth

import service.ConsoleUI

class AuthHandler(
    private val authService: AuthService,
    private val consoleUI: ConsoleUI
) {

    fun run() {
        displayMenu()
        handleMenuInput()
    }
    private fun displayMenu() {
        println("Войдите или зарегистрируйтесь в аккаунт")
        println("1. Войти в существующий аккаунт")
        println("2. Создать новый аккаунт")
        println("0. Выход")
    }

    private fun handleMenuInput() {
        while (true) {
            print("Введите число от 0 до 2: ")
            val userInput: String? = readlnOrNull()

            when (userInput) {
                "1" -> {
                    if (authService.authenticateUser()) {
                        consoleUI.getMainMenuHandler.run()
                    } else {
                        println("Авторизация не удалась!")
                    }
                }
                "2" -> {
                    if (authService.registerUser()) {
                        run()
                    } else {
                        println("Регистрация не удалась!")
                    }
                }

                "0" -> consoleUI.exitMenu()
                else -> println("Неверный ввод. Пожалуйста, выберите действие от 0 до 2.")
            }

            run()
        }
    }
}