package uk.ac.westminster.moviestore.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import uk.ac.westminster.moviestore.entities.Actor
import uk.ac.westminster.moviestore.entities.Movie
import uk.ac.westminster.moviestore.entities.relations.ActorWithMovies

data class MovieWithActors(
    @Embedded val movie: Movie,
    @Relation(
        parentColumn = "movieTitle",
        entityColumn =  "actorName",
        associateBy = Junction(ActorWithMovies::class)
    )
    val actor: List<Actor>
)