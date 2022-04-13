package uk.ac.westminster.moviestore

import androidx.room.Database
import androidx.room.RoomDatabase
import uk.ac.westminster.moviestore.entities.Actor
import uk.ac.westminster.moviestore.entities.Movie
import uk.ac.westminster.moviestore.entities.relations.ActorMovieCrossRef

@Database(
    entities = [
        Movie::class,
        Actor::class,
        ActorMovieCrossRef::class
    ],
    version = 1
)
abstract class MovieDatabase : RoomDatabase() {
}