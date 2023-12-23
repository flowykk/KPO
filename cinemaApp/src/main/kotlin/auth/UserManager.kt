package auth

class UserManager() {
    private val users: MutableList<User> = mutableListOf()

    fun getAll(): List<User> {
        return users
    }

    fun addUser(user: User) {
        users.add(user)
    }

    fun getUserByUsername(username: String): User? {
        return users.find { it.username == username }
    }

    fun isValidCredentials(username: String, password: String): Boolean {
        val user = getUserByUsername(username)
        return user?.password == password
    }
}
