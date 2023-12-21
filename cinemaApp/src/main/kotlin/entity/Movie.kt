package entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Movie @JsonCreator constructor(
    @JsonProperty("title") val title: String,
    @JsonProperty("director") val director: String
)