package uk.ac.westminster.moviestore

import androidx.room.*
import uk.ac.westminster.moviestore.entities.Actor
import uk.ac.westminster.moviestore.entities.Movie
import uk.ac.westminster.moviestore.entities.relations.ActorMovieCrossRef
import uk.ac.westminster.moviestore.entities.relations.ActorWithMovies
import uk.ac.westminster.moviestore.entities.relations.MovieWithActors

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActor(actor: Actor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActorMovieCrossRef(crossRef: ActorMovieCrossRef)

    @Transaction
    @Query("SELECT * FROM movie WHERE movieTitle = :movieTitle")
    suspend fun getActorsOfMovie(movieTitle: String): List<MovieWithActors>

    @Transaction
    @Query("SELECT * FROM actor WHERE actorName = :actorName")
    suspend fun getMovieWithActors(actorName: String): List<ActorWithMovies>
}