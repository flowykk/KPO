package entity

import com.fasterxml.jackson.annotation.JsonProperty

class Movie(
    @JsonProperty("title") var title: String,
    @JsonProperty("director") var director: String
) : CommonEntity {
    override fun viewInfo() {
        println("\"$title\" by $director")
    }
}
