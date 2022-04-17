package uk.ac.westminster.moviestore

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import uk.ac.westminster.moviestore.entities.relations.ActorWithMovies

class ActorSearchActivity : AppCompatActivity() {

    lateinit var movieDao: MovieDao
    lateinit var showInfo: TextView
    lateinit  var filmName: String
    lateinit var films: List<ActorWithMovies>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actor_search)

        if (supportActionBar != null) {
            supportActionBar?.hide()
        }

        val db = Room.databaseBuilder(this, MovieDatabase::class.java, "movie_db").build()
        movieDao = db.movieDao()

        val movieSearchView = findViewById<EditText>(R.id.movieSearchView)
        val searchActors = findViewById<Button>(R.id.searchActors)
        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        showInfo = findViewById<TextView>(R.id.showInfo)


        searchActors.setOnClickListener {
            val name = movieSearchView.getText().toString()
            getActorsMovieDetails(name)

//            showInfo.text = filmName

        }

    }

    @SuppressLint("SetTextI18n")
    fun getActorsMovieDetails(query : String) {
        runBlocking {
            withContext(Dispatchers.IO){
                films = movieDao.getMoviesOfActors(query)
                val size = films.size
                Log.d("size", "$size")
                for (film in films) {
                    Log.d("insert", "$film")
                }




            }
            if(films.size > 1){


                for (element in films) {
                    for(movie in element.movies) {
                        val mName = movie.movieTitle
                        val actor = element.actor.actorName
                        Log.d("Actor Details", " $mName + $actor")
//                        showInfo.text = "Actor Name: $actor\nMovie Name: $mName "

                        showInfo.append("\n Actor Name: $actor\n " +
                                "Movie Name: $mName")
                    }
                }
            }else if(films.size == 1){
                val name = films[0].movies[0].movieTitle
                showInfo.text = "Movie Name: $name "
                Log.d("actor", name)
            }
        }
    }
}