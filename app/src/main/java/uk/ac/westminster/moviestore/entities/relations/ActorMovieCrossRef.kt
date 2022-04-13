package uk.ac.westminster.moviestore.entities.relations

import androidx.room.Entity

@Entity(primaryKeys = ["actorName", "movieTitle"])
data class ActorMovieCrossRef(
    val actorName: String,
    val movieTitle: String,
)