package auth

class UserManager(private val userFileHandler: UserFileHandler) {
    private val users: MutableList<User> = mutableListOf()

    fun getAll(): List<User> {
        return users
    }

    fun addUser(user: User) {
        users.add(user)

        userFileHandler.saveUsers(users)
    }

    fun getUserByUsername(username: String): User? {
        return users.find { it.username == username }
    }

    fun isValidCredentials(username: String, password: String): Boolean {
        val user = getUserByUsername(username)
        return user?.password == password
    }
}
