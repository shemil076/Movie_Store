package uk.ac.westminster.moviestore

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import uk.ac.westminster.moviestore.entities.Actor
import uk.ac.westminster.moviestore.entities.Movie
import uk.ac.westminster.moviestore.entities.relations.MovieActorCrossRef
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MovieSearchActivity : AppCompatActivity() {
    lateinit var imageUrl :String
    var data = ""
    var bitMapIcon: Bitmap? = null
    lateinit var movieDao: MovieDao

    lateinit var title :String
    lateinit var year :String
    lateinit var rated :String
    lateinit var released :String
    lateinit var runtime :String
    lateinit var genre :String
    lateinit var director :String
    lateinit var writer :String
    lateinit var actors :String
    lateinit var plot :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_search)

        val searchView = findViewById<EditText>(R.id.movieSearchView)
        val retrieveMovieButton = findViewById<Button>(R.id.movieRetrieveButton)
        val showInfo = findViewById<TextView>(R.id.showData)
        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        val coverImage = findViewById<ImageView>(R.id.coverImage)
        val saveCoverImage = findViewById<ImageView>(R.id.saveCoverImage)
        val movieSaveButton = findViewById<Button>(R.id.movieSaveButton)

        saveCoverImage.visibility = View.GONE
        scrollView.visibility = View.GONE
        coverImage.visibility = View.GONE


        val db = Room.databaseBuilder(this, MovieDatabase::class.java, "movie_db").build()
        movieDao = db.movieDao()

        if (supportActionBar != null) {
            supportActionBar?.hide()
        }
        retrieveMovieButton.setOnClickListener {

            saveCoverImage.visibility = View.GONE

            val nameRetrieve = searchView.text.toString()
            if (nameRetrieve.isNotEmpty() ) {
                if (nameRetrieve != " "){
                    scrollView.visibility = View.VISIBLE
                    coverImage.visibility = View.VISIBLE

                    val remove = " "
                    val replace = "%20"
                    val query = nameRetrieve.replace(remove,replace)

                    getMovie(query)

                    showInfo.text = data
                    coverImage.setImageBitmap(bitMapIcon)
                }else{
                    val toast = Toast.makeText(applicationContext, "Nothing to search", Toast.LENGTH_LONG)
                    toast.show()
                }
            }else{

                val toast = Toast.makeText(applicationContext, "Nothing to search", Toast.LENGTH_LONG)
                toast.show()
            }

        }

        movieSaveButton.setOnClickListener {
            scrollView.visibility = View.GONE
            coverImage.visibility = View.GONE

            val nameRetrieve = searchView.text.toString()
            if (nameRetrieve.isNotEmpty()){
                if(nameRetrieve != " "){
                    saveCoverImage.visibility = View.VISIBLE

                    val remove = " "
                    val replace = "%20"
                    val query = nameRetrieve.replace(remove,replace)
                    getMovie(query)

                    saveToDB()
                    saveCoverImage.setImageBitmap(bitMapIcon)
                }
            }else{

                val toast = Toast.makeText(applicationContext, "Nothing to search", Toast.LENGTH_LONG)
                toast.show()
            }
        }


    }

    fun getMovie(name: String) {
        val url_string = "http://www.omdbapi.com/?t=$name&apikey=c8ae66ce"
        runBlocking{
            withContext(Dispatchers.IO){
                val stringBuilder = StringBuilder("")
                val url = URL(url_string)
                val con = url.openConnection() as HttpURLConnection
                val reader : BufferedReader
                try{
                    reader = BufferedReader(InputStreamReader(con.inputStream))
                }catch(e: IOException){
                    e.printStackTrace()
                    return@withContext
                }
                var line = reader.readLine()
                while (line != null){
                    stringBuilder.append(line)
                    line = reader.readLine()
                }
                data = parseJSON(stringBuilder)
                bitMapIcon = getCoverImage()
            }
        }

    }
    suspend fun parseJSON(stringBuilder: StringBuilder): String{

        var info:String = ""

         val json = JSONObject(stringBuilder.toString())
         title = json["Title"].toString()
         year = json["Year"].toString()
         rated = json["Rated"].toString()
         released = json["Released"].toString()
         runtime = json["Runtime"].toString()
         genre = json["Genre"].toString()
         director = json["Director"].toString()
         writer = json["Writer"].toString()
         actors = json["Actors"].toString()
        plot = json["Plot"].toString()
        imageUrl = json["Poster"].toString()

        info = "Title: $title,\n Year: $year,\n Rated: $rated,\n Released: $released,\n Runtime: $runtime,\n Genre: $genre,\n Director: $director,\n Writer: $writer,\n Actors: $actors,\n Plot: $plot"
        return info
    }


    fun getCoverImage(): Bitmap {
        var bitmap : Bitmap? = null
        val url = URL(imageUrl)
        val con = url.openConnection() as HttpURLConnection
        val bfstream = BufferedInputStream(con.inputStream)
        bitmap = BitmapFactory.decodeStream(bfstream)
        return bitmap
    }

     @SuppressLint("ShowToast")
     fun saveToDB(){

        runBlocking{
            val newMovie = Movie(title,year,rated,released,runtime,genre,director,writer,plot)
            movieDao.insertMovie(newMovie)

            val actorsList = actors.split(",")
            for(actor in actorsList) {
                val newActor = Actor(actor)
                val newMovieActorRelationship  = MovieActorCrossRef(title,actor)
                movieDao.insertActor(newActor)
                movieDao.insertMovieActorCrossRef(newMovieActorRelationship)
            }

            val toast = Toast.makeText(applicationContext, "Details of movie $title save to the database.", Toast.LENGTH_LONG)
            toast.show()

            val movie : List<Movie> =  movieDao.getMovie()

            for (film in movie) {
                Log.d( "insert", "$film")
            }
        }
    }

}

