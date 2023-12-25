package auth

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.concurrent.atomic.AtomicInteger

interface UserEntity {
    fun getId(): Int
    fun getUsername(): String
    fun getPasswordHash(): String
}

data class User(
    private val username: String,
    private val password: String,
) : UserEntity {
    @JsonProperty("id")
    private val id: Int = idCounter.getAndIncrement()

    override fun getId(): Int {
        return id
    }

    override fun getUsername(): String {
        return username
    }

    override fun getPasswordHash(): String {
        return password
    }

    companion object {
        private val idCounter = AtomicInteger(1)
    }
}