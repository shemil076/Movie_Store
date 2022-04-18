package uk.ac.westminster.moviestore

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import uk.ac.westminster.moviestore.adapters.ParentAdapter
import uk.ac.westminster.moviestore.entities.relations.ActorWithMovies

class ActorSearchActivity : AppCompatActivity() {

    lateinit var movieDao: MovieDao
    lateinit var films: List<ActorWithMovies>
    lateinit var showCards : RecyclerView
     var name =  "!@$%^&*()"

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
        showCards = findViewById<RecyclerView>(R.id.showData)


        searchActors.setOnClickListener {
            name = movieSearchView.getText().toString()
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
        }

        val parentAdapter = ParentAdapter(this,films)
        val linearLayout = LinearLayoutManager(this)
        showCards.layoutManager = linearLayout
        showCards.adapter = parentAdapter
    }

    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)

        // how to save the state of a custom  listOf<ActorWithMovies>
//        outState.putParcelableArrayList("films", ArrayList(films))

        outState.putString("name", name)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle){
        super.onRestoreInstanceState(savedInstanceState)

        name = savedInstanceState.getString("name"," " )

        if(name != "!@$%^&*()") {
            getActorsMovieDetails(name)
        }else{

        getActorsMovieDetails(name)
        }
    }
}

