package uk.ac.westminster.moviestore.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import kotlinx.coroutines.NonDisposableHandle.parent
import uk.ac.westminster.moviestore.entities.Actor
import uk.ac.westminster.moviestore.entities.Movie

data class ActorWithMovies(
    @Embedded val actor: Actor,
    @Relation(
        parentColumn = "actorId",
        entityColumn =  "movieId",
        associateBy = Junction(ActorWithMovies::class)
    )
    val movies: List<Movie>
)