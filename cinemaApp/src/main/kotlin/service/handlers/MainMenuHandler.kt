package service.handlers

import service.CinemaManager
import service.ConsoleUI

class MainMenuHandler(
    private val consoleUI: ConsoleUI
) {

    fun run() {
        displayMenu()
        handleMenuInput()
    }

    private fun displayMenu() {
        println("Добро пожаловать в главное меню!")
        println("1. Осуществить продажу билета")
        println("2. Осуществить возврат билета")
        println("3. Посмотреть фильмы в прокате")
        println("4. Посмотреть сеансы на фильм")
        println("5. Посмотреть свободные места на сеанс")
        println("6. Изменить данные о фильмах")
        println("7. Изменить данные о сеансах")
        println("0. Выход")
    }

    private fun handleMenuInput() {
        while (true) {
            print("Введите число от 0 до 7: ")
            val userInput: String? = readlnOrNull()

            when (userInput) {
                "1" -> consoleUI.sellTicket()
                "2" -> consoleUI.refundTicket()
                "3" -> consoleUI.getMovieHandler.displayMovies()
                "4" -> consoleUI.displaySessions(consoleUI.getMovieHandler.handleMovieInput(MovieModes.SEARCH))
                "5" -> consoleUI.displaySeats()
                "6" -> {
                    consoleUI.getMovieHandler.run()
                } //editSchedule()
                "7" -> {
                    //displaySessionsMenu()
                    //handleSessionsMenuInput()
                }

                "0" -> consoleUI.exitMenu()
                else -> println("Неверный ввод. Пожалуйста, выберите действие от 0 до 7.")
            }

            run()
        }
    }
}