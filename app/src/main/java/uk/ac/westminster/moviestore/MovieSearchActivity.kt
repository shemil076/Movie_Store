package uk.ac.westminster.moviestore

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_search)

        val searchView = findViewById<EditText>(R.id.movieSearchView)
        val retrieveMovieButton = findViewById<Button>(R.id.movieRetrieveButton)
        val showInfo = findViewById<TextView>(R.id.showData)
        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        val coverImage = findViewById<ImageView>(R.id.coverImage)


        if (supportActionBar != null) {
            supportActionBar?.hide()
        }
        retrieveMovieButton.setOnClickListener {
            val name = searchView.text.toString()
            val remove = " "
            val replace = "%20"
            val query = name.replace(remove,replace)
            showInfo.text = query

            val url_string = "http://www.omdbapi.com/?t=$query&apikey=c8ae66ce"

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
            showInfo.text = data
            coverImage.setImageBitmap(bitMapIcon)
        }
    }
    suspend fun parseJSON(stringBuilder: StringBuilder): String{

        var info:String = ""

        val json = JSONObject(stringBuilder.toString())
        val title = json["Title"].toString()
        val year = json["Year"].toString()
        val rated = json["Rated"]
        val released = json["Released"]
        val runtime = json["Runtime"]
        val genre = json["Genre"]
        val director = json["Director"]
        val writer = json["Writer"]
        val actors = json["Actors"]
        val plot = json["Plot"]
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
}