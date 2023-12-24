package entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class Movie @JsonCreator constructor(
    @JsonProperty("title") var title: String,
    @JsonProperty("director") var director: String
)
{
//    var mutableTitle: String
//        get() = title
//        set(value) {
//            // Выполняйте здесь необходимые действия перед установкой значения
//            title = value
//        }

    @JsonIgnore
    var mutableDirector: String = "default"
        set(value) {
            // Выполняйте здесь необходимые действия перед установкой значения
            field = value
        }
}
