package auth

interface UserManagerEntity {
    fun getAll(): List<User>
    fun addUser(user: User)
    fun getUserByUsername(username: String): User?
    fun isValidCredentials(username: String, password: String): Boolean
}

class UserManager(private val userFileHandler: UserFileHandler) : UserManagerEntity {
    private val users: MutableList<User> = mutableListOf()

    override fun getAll(): List<User> {
        return users
    }

    override fun addUser(user: User) {
        users.add(user)

        userFileHandler.saveUsers(users)
    }

    override fun getUserByUsername(username: String): User? {
        return users.find { it.getUsername() == username }
    }

    override fun isValidCredentials(username: String, password: String): Boolean {
        val user = getUserByUsername(username)
        return user?.getPasswordHash() == password
    }
}
