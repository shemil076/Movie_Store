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
     var imageUrl = "https://uwaterphoto.com/wp-content/uploads/2013/10/placeholder_image2.png"
    var data = ""
    var bitMapIcon: Bitmap? = null
    lateinit var movieDao: MovieDao
    var info:String = ""
    lateinit var showInfo: TextView
    lateinit var coverImage : ImageView
    lateinit var saveCoverImage : ImageView
     var query: String = " "
     var nameRetrieve :  String =  " "
    lateinit var scrollView : ScrollView

    var retrieveMovieButtonPressed = false
    var movieSaveButtonPressed = false

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
        showInfo = findViewById<TextView>(R.id.showData)
        scrollView = findViewById<ScrollView>(R.id.scrollView)
        coverImage = findViewById<ImageView>(R.id.coverImage)
        saveCoverImage = findViewById<ImageView>(R.id.saveCoverImage)
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
            movieSaveButtonPressed = false
            retrieveMovieButtonPressed = true

            saveCoverImage.visibility = View.GONE

            nameRetrieve = searchView.text.toString()
            if (nameRetrieve.isNotEmpty() ) {
                if (nameRetrieve != " "){
                    scrollView.visibility = View.VISIBLE
                    coverImage.visibility = View.VISIBLE

                    val remove = " "
                    val replace = "%20"
                    query = nameRetrieve.replace(remove,replace)

                    getMovie(query)


                    if(data == "404"){
                        val toast = Toast.makeText(applicationContext, "Nothing to search", Toast.LENGTH_LONG)
                        toast.show()
                        scrollView.visibility = View.GONE
                        coverImage.setImageBitmap(bitMapIcon)
                    }else{
                        showInfo.text = data
                        coverImage.setImageBitmap(bitMapIcon)
                    }

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
            movieSaveButtonPressed = true
            retrieveMovieButtonPressed = false


            scrollView.visibility = View.GONE
            coverImage.visibility = View.GONE

            nameRetrieve = searchView.text.toString()
            if (nameRetrieve.isNotEmpty()){
                if(nameRetrieve != " "){
                    saveCoverImage.visibility = View.VISIBLE

                    val remove = " "
                    val replace = "%20"
                    query = nameRetrieve.replace(remove,replace)
                    getMovie(query)


                    if (data == "404"){
                        val toast = Toast.makeText(applicationContext, "$nameRetrieve not found" , Toast.LENGTH_LONG)
                        toast.show()
                        saveCoverImage.setImageBitmap(bitMapIcon)
                    }else{
                        saveToDB()
                        saveCoverImage.setImageBitmap(bitMapIcon)
                    }

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



         val json = JSONObject(stringBuilder.toString())

        if(json.has("Error")){
            imageUrl = "https://ih0.redbubble.net/image.183170713.4213/flat,800x800,075,f.u3.jpg"
            return "404"
        }else{
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


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("info", info)
        outState.putString("data", data )
        outState.putString("image", imageUrl)
        outState.putString("query",query)
        outState.putString("nameRetrieve",nameRetrieve)
        outState.putBoolean("retrieveMovieButtonPressed",retrieveMovieButtonPressed)
        outState.putBoolean("movieSaveButtonPressed",movieSaveButtonPressed)


        Log.d("imageUrl", imageUrl)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle){
        super.onRestoreInstanceState(savedInstanceState)

        data = savedInstanceState.getString("data", null)
        imageUrl = savedInstanceState.getString("image", null)
        query = savedInstanceState.getString("query", null)
        nameRetrieve = savedInstanceState.getString("nameRetrieve", null)
        retrieveMovieButtonPressed = savedInstanceState.getBoolean("retrieveMovieButtonPressed", false)
        movieSaveButtonPressed = savedInstanceState.getBoolean("movieSaveButtonPressed", false)


        if(query == " "){
            scrollView.visibility = View.GONE
            coverImage.visibility = View.GONE
            saveCoverImage.visibility = View.GONE
        }else {

            if (retrieveMovieButtonPressed) {
                showInfo.text = data
                scrollView.visibility = View.VISIBLE
                coverImage.visibility = View.VISIBLE
                saveCoverImage.visibility = View.GONE
                runBlocking {
                    withContext(Dispatchers.IO) {
                        bitMapIcon = getCoverImage()
                    }
                }
                coverImage.setImageBitmap(bitMapIcon)
            } else if (movieSaveButtonPressed) {
                scrollView.visibility = View.GONE
                coverImage.visibility = View.GONE
                saveCoverImage.visibility = View.VISIBLE
                runBlocking {
                    withContext(Dispatchers.IO) {
                        bitMapIcon = getCoverImage()
                        saveCoverImage.setImageBitmap(bitMapIcon)
                    }
                }
            }
        }
        Log.d("onRestore", data)

    }
}

