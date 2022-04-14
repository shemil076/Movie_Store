package uk.ac.westminster.moviestore

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uk.ac.westminster.moviestore.entities.Actor
import uk.ac.westminster.moviestore.entities.Movie
import uk.ac.westminster.moviestore.entities.relations.MovieActorCrossRef

@Database(
    entities = [
        Movie::class,
        Actor::class,
        MovieActorCrossRef::class
    ],
    version = 1
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

//    companion object {
//        @Volatile
//        private var INSTANCE: MovieDatabase? = null
//
//        fun getInstance(context: Context): MovieDatabase {
//            synchronized(this){
//                return INSTANCE ?: Room.databaseBuilder(
//                    context.applicationContext,
//                    MovieDatabase::class.java,
//                    "movie_db"
//                ).build().also{
//                    INSTANCE = it
//                }
//            }
//        }
//
//    }
}