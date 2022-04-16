package uk.ac.westminster.moviestore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import uk.ac.westminster.moviestore.entities.Actor
import uk.ac.westminster.moviestore.entities.Movie
import uk.ac.westminster.moviestore.entities.relations.MovieActorCrossRef

//import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    lateinit var movieDao: MovieDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addToDb = findViewById<Button>(R.id.addMovies);
        val searchMovie = findViewById<Button>(R.id.searchMovie)


        val db = Room.databaseBuilder(this, MovieDatabase::class.java, "movie_db").build()
        movieDao = db.movieDao()

        addToDb.setOnClickListener {
            populateData()
        }

        searchMovie.setOnClickListener{
            val searchMovieIntent = Intent(this, MovieSearchActivity::class.java)
            startActivity(searchMovieIntent)
        }

    }

    fun populateData() {
//        val dao = MovieDatabase.getInstance(this).movieDao()

        runBlocking {
            launch{
                val movies = listOf(
                    Movie("The Shawshank Redemption","1994","R","14 Oct 1994","142 min","Drama","Frank Darabont","Stephen King, Frank Darabont","Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency."),
                    Movie("Batman: The Dark Knight Returns, Part 1","2012","PG-13","25 Sep 2012","76 min","Animation, Action, Crime, Drama, Thriller","Jay Oliva","Bob Kane (character created by: Batman), Frank Miller (comic book), Klaus Janson (comic book), Bob Goodman","Batman has not been seen for ten years. A new breed of criminal ravages Gotham City, forcing 55-year-old Bruce Wayne back into the cape and cowl. But, does he still have what it takes to fight crime in a new era?"),
                    Movie("The Lord of the Rings: The Return of the King","2003","PG-13","17 Dec 2003","201 min","Action, Adventure, Drama","Peter Jackson","J.R.R. Tolkien, Fran Walsh, Philippa Boyens","Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring."),
                    Movie("Inception","2010","PG-13","16 Jul 2010","148 min","Action, Adventure, Sci-Fi","Christopher Nolan","Christopher Nolan","A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O., but his tragic past may doom the project and his team to disaster."),
                    Movie("The Matrix","1999","R","31 Mar 1999","136 min","Action, Sci-Fi","Lana Wachowski, Lilly Wachowski","Lilly Wachowski, Lana Wachowski","When a beautiful stranger leads computer hacker Neo to a forbidding underworld, he discovers the shocking truth--the life he knows is the elaborate deception of an evil cyber-intelligence.")
                )

                val actors = listOf(
                    Actor("Tim Robbins"),
                    Actor("Morgan Freeman"),
                    Actor("Bob Gunton"),
                    Actor("Peter Weller"),
                    Actor("Ariel Winter"),
                    Actor("David Selby"),
                    Actor("Wade Williams"),
                    Actor("Elijah Wood"),
                    Actor("Viggo Mortensen"),
                    Actor("Ian McKellen"),
                    Actor("Leonardo DiCaprio"),
                    Actor("Joseph Gordon-Levitt"),
                    Actor("Elliot Page"),
                    Actor("Keanu Reeves"),
                    Actor("Laurence Fishburne"),
                    Actor("Carrie-Anne Moss"),
                )

                val movieActorRelationships = listOf(
                    MovieActorCrossRef("The Shawshank Redemption","Tim Robbins"),
                    MovieActorCrossRef("The Shawshank Redemption","Morgan Freeman"),
                    MovieActorCrossRef("The Shawshank Redemption","Bob Gunton"),
                    MovieActorCrossRef("Batman: The Dark Knight Returns, Part 1","Peter Weller"),
                    MovieActorCrossRef("Batman: The Dark Knight Returns, Part 1","Ariel Winter"),
                    MovieActorCrossRef("Batman: The Dark Knight Returns, Part 1","David Selby"),
                    MovieActorCrossRef("Batman: The Dark Knight Returns, Part 1","Wade Williams"),
                    MovieActorCrossRef("The Lord of the Rings: The Return of the King","Elijah Wood"),
                    MovieActorCrossRef("The Lord of the Rings: The Return of the King","Viggo Mortensen"),
                    MovieActorCrossRef("The Lord of the Rings: The Return of the King","Ian McKellen"),
                    MovieActorCrossRef("Inception","Leonardo DiCaprio"),
                    MovieActorCrossRef("Inception","Joseph Gordon-Levitt"),
                    MovieActorCrossRef("Inception","Elliot Page"),
                    MovieActorCrossRef("The Matrix","Keanu Reeves"),
                    MovieActorCrossRef("The Matrix","Laurence Fishburne"),
                    MovieActorCrossRef("The Matrix","Carrie-Anne Moss"),
                )
                
                for(movie in movies) {
                    movieDao.insertMovie(movie)
                }

                for (actor in actors) {
                    movieDao.insertActor(actor)
                }

                for (movieActorRelationship in movieActorRelationships){
                    movieDao.insertMovieActorCrossRef(movieActorRelationship)
                }

                val movie : List<Movie> =  movieDao.getMovie()

                for (film in movie) {
                    Log.d( "insert", "$film")
                }
            }
        }
    }
}


//        lifecycleScope.launch {
//            movies.forEach{dao.insertMovie(it)}
//            actors.forEach{dao.insertActor(it)}
//            movieActorRelationships.forEach{dao.insertMovieActorCrossRef(it)}
//        }