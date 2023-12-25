package service.handlers

import service.ConsoleUI

class MainMenuHandler(
    private val consoleUI: ConsoleUI
) : MenuEntity {

    override fun run() {
        displayMenu()
        handleMenuInput()
    }

    override fun displayMenu() {
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

    override fun handleMenuInput() {
        while (true) {
            print("Введите число от 0 до 7: ")
            val userInput: String? = readlnOrNull()

            when (userInput) {
                "1" -> consoleUI.getTicketHandler.sellTicket()
                "2" -> consoleUI.getTicketHandler.refundTicket()
                "3" -> consoleUI.getMovieHandler.displayMovies()
                "4" -> consoleUI.getSessionHandler.displaySessions(consoleUI.getMovieHandler.handleMovieInput(MovieModes.SEARCH))
                "5" -> consoleUI.getSessionHandler.displaySeats()
                "6" -> consoleUI.getMovieHandler.run()
                "7" -> consoleUI.getSessionHandler.run()
                "0" -> consoleUI.exitMenu()
                else -> println("Неверный ввод. Пожалуйста, выберите действие от 0 до 7.")
            }

            run()
        }
    }
}