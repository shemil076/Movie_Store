package uk.ac.westminster.moviestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class findSimilarNameMoviesActivity : AppCompatActivity() {

    lateinit var movieTitle: String
    lateinit var movieDetails: String
    var data = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_similar_name_movies)

        if (supportActionBar != null) {
            supportActionBar?.hide()
        }

        val searchTitle = findViewById<EditText>(R.id.searchTitle)
        val getTitle = findViewById<Button>(R.id.getTitle)

//        val movieSearch : MovieSearchActivity

        getTitle.setOnClickListener {
            val name = searchTitle.text.toString()
            retrieveMovies(name)
        }
    }

    fun retrieveMovies(name: String) {
        val url_string = "http://www.omdbapi.com/?s=*$name*&apikey=c8ae66ce"

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
                Log.d("data", data)
            }
        }
    }

    suspend fun parseJSON(stringBuilder: StringBuilder): String {
        var info:String = ""
        val json = JSONObject(stringBuilder.toString())
        title = json["Title"].toString()

        info = "Title: $title"

        return info
    }
}