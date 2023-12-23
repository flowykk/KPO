package auth

class AuthService(private val userManager: UserManager) {
    private var currentUser: User? = null
    fun registerUser(username: String, password: String): Boolean {
        // Check if the username is already taken
        if (userManager.getAll().any { it.username == username }) {
            println("Username '$username' is already taken. Please choose another.")
            return false
        }

        // Create a new user and add it to the set of registered users
        val newUser = User(username, password)
        userManager.addUser(newUser)
        println("User '$username' registered successfully.")
        return true
    }

    // Method to authenticate a user
    fun authenticateUser(username: String, password: String): Boolean {
        // Check if the user exists
        val user = userManager.getAll().find { it.username == username }

        if (user != null && user.password == password) {
            // Authentication successful
            currentUser = user
            println("Authentication successful. Welcome, ${user.username}!")
            return true
        } else {
            // Authentication failed
            println("Invalid username or password. Please try again.")
            return false
        }
    }

    // Method to check if a user is currently logged in
    fun isLoggedIn(): Boolean {
        return currentUser != null
    }

    // Method to get the currently logged-in user
    fun getCurrentUser(): User? {
        return currentUser
    }
}
