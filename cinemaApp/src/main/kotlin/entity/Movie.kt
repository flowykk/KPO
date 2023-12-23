package entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class Movie @JsonCreator constructor(
    @JsonProperty("title") val title: String,
    @JsonProperty("director") val director: String
)
{
    @JsonIgnore
    var mutableTitle: String = "default"
        set(value) {
            // Выполняйте здесь необходимые действия перед установкой значения
            field = value
        }

    @JsonIgnore
    var mutableDirector: String = "default"
        set(value) {
            // Выполняйте здесь необходимые действия перед установкой значения
            field = value
        }
}
