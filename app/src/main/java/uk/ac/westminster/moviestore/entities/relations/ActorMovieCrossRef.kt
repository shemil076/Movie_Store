package uk.ac.westminster.moviestore.entities.relations

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["actorId", "movieId"])
data class ActorMovieCrossRef(
    val actorId: Int,
    val movieId: Int,
)