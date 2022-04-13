package uk.ac.westminster.moviestore.entities.relations

import androidx.room.Entity

@Entity(primaryKeys = ["movieTitle","actorName"])
data class MovieActorCrossRef(
    val movieTitle: String,
    val actorName: String,
)