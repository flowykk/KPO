package auth

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.concurrent.atomic.AtomicInteger

data class User(
    val username: String,
    val password: String,
) {
    companion object {
        private val idCounter = AtomicInteger(1)
    }

    // Автоинкрементируемый Id сеанса
    @JsonProperty("id")
    val id: Int = idCounter.getAndIncrement()
}