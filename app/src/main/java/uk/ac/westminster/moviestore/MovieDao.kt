package uk.ac.westminster.moviestore

import androidx.room.*
import uk.ac.westminster.moviestore.entities.Actor
import uk.ac.westminster.moviestore.entities.Movie
import uk.ac.westminster.moviestore.entities.relations.MovieActorCrossRef
import uk.ac.westminster.moviestore.entities.relations.ActorWithMovies
import uk.ac.westminster.moviestore.entities.relations.MovieWithActors

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActor(actor: Actor)
//
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieActorCrossRef(actorCrossRef: MovieActorCrossRef)

    @Query("Select * from movie")
    suspend fun getMovie(): List<Movie>


    @Transaction
    @Query("SELECT * FROM actor WHERE actorName LIKE '%' || :query || '%'")
    fun getMoviesOfActors(query: String): List<ActorWithMovies>

//    @Query("SELECT * FROM movie WHERE movieTitle = :movieTitle")
//    suspend fun getActorsOfMovie(movieTitle: String): List<MovieWithActors>

//    @Query("SELECT * FROM actor WHERE actorName = :actorName")
//    suspend fun getMovieWithActors(actorName: String): List<ActorWithMovies>
}