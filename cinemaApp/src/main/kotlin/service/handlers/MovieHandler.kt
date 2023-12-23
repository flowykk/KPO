package service.handlers

import MovieModes
import entity.Movie
import service.CinemaManager
import service.ConsoleUI

class MovieHandler(
    private val cinemaManager: CinemaManager,
    private val consoleUI: ConsoleUI
) {

    fun run() {
        displayMenu()
        handleMenuInput()
    }
    private fun displayMenu() {
        println("1. Посмотреть фильмы в прокате")
        println("2. Добавить фильм")
        println("3. Удалить фильм")
        println("4. Изменить называние фильма")
        println("5. Изменить режиссёра фильма")
        println("00. Вернуться в Главное меню")
        println("0. Выход")
    }

    private fun handleMenuInput() {
        while (true) {
            print("Введите число от 0 до 4 или 00: ")
            val userInput: String? = readlnOrNull()

            when (userInput) {
                "1" -> displayMovies()
                "2" -> addMovie()
                "3" -> deleteMovie()
                "4" -> editMovie(MovieModes.EDITNAME) //TODO("editMovieName")
                "5" -> editMovie(MovieModes.EDITDIRECTOR) //TODO("editMovieDirector()")
                "00" -> {
                    println()
                    consoleUI.getMainMenuHandler.run()
                }
                "0" -> consoleUI.exitMenu()
                else -> println("Неверный ввод. Пожалуйста, выберите действие от 0 до 4 или 00.")
            }

            run()
        }
    }

    /*
    mode = add - adding new film
    mode = search - searching for existing film
     */
    fun handleMovieInput(mode: MovieModes): Movie? {
        var movie: Movie? = null

        while (true) {
            print("Введите название фильма: ")
            var title: String = readlnOrNull().orEmpty().trim().lowercase()
            if (title == "00") {
                break
            } else if (title.isEmpty()) {
                println("Название фильма не может быть пустым!\n" +
                        "Повторите ввод ещё раз или введите 00 для выхода в меню.")
                continue
            }
            title = consoleUI.capitalizeFirst(title)

            movie = cinemaManager.getMovieByName(title)
            if (((movie == null) && mode == MovieModes.SEARCH) || ((movie != null) && mode == MovieModes.ADD)) {
                println("Фильм \"$title\" ${if (mode == MovieModes.SEARCH) "пока что отсутсвует" else "уже присутсвует"} в прокате!\n" +
                        "Повторите ввод ещё раз или введите 00 для выхода в меню.")
            } else {
                if ((movie == null) && mode == MovieModes.ADD) {
                    var director: String

                    while (true) {
                        print("Введите режиссёра фильма: ")
                        director = readlnOrNull().orEmpty().trim().lowercase()

                        if (director == "00") {
                            break
                        }

                        if (!consoleUI.isLatin(director)) {
                            println("Режиссёр введён некорректно!\n" +
                                    "Повторите ввод ещё раз или введите 00 для выхода в меню.")
                        } else {
                            break
                        }
                    }

                    director = consoleUI.capitalizeFirst(director)

                    movie = Movie(title, director)
                }

                break
            }
        }

        println()
        return movie
    }

    private fun editMovie(mode: MovieModes) {
        displayMovies()

        println("Введите информацию о фильме для изменения: ")
        val movie = handleMovieInput(MovieModes.SEARCH) ?: return

        var data: String

        while (true) {
            print("Введите ${if (mode == MovieModes.EDITNAME) "новое название" else "нового режиссёра"} фильма:")
            data = readlnOrNull().orEmpty().trim().lowercase()

            if (data == "00") {
                break
            }

            if (!consoleUI.isLatin(data)) {
                println("Режиссёр введён некорректно!\n" +
                        "Повторите ввод ещё раз или введите 00 для выхода в меню.")
            } else {
                break
            }
        }

        cinemaManager.editMovie(movie, mode, data)
    }

    private fun addMovie() {
        displayMovies()

        val movie = handleMovieInput(MovieModes.ADD) ?: return
        cinemaManager.addMovie(movie)
    }

    private fun deleteMovie() {
        displayMovies()

        val movie = handleMovieInput(MovieModes.SEARCH) ?: return
        cinemaManager.deleteMovie(movie)
    }

    fun displayMovies() {
        val movies = cinemaManager.getMovies()

        println("\nСписок фильмов, которые сейчас в прокате:")
        for (movie in movies) {
            println("\"${movie.title}\" by ${movie.director}")
        }
        println()
    }
}