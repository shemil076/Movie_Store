package uk.ac.westminster.moviestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import uk.ac.westminster.moviestore.adapters.MovieAdapter
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class findSimilarNameMoviesActivity : AppCompatActivity() {

    lateinit var movieTitle: String
    lateinit var movieDetails: String
    var data = ""
    lateinit var movieNames: List<String>
    lateinit var movieNameCard : RecyclerView
    var query: String = " "
    var foundError = false
    var name = " "


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_similar_name_movies)

        if (supportActionBar != null) {
            supportActionBar?.hide()
        }

        val searchTitle = findViewById<EditText>(R.id.searchTitle)
        val getTitle = findViewById<Button>(R.id.getTitle)
        movieNameCard = findViewById<RecyclerView>(R.id.showData)

//        val movieSearch : MovieSearchActivity

        getTitle.setOnClickListener {

             name = searchTitle.text.toString()
            if(name.isNotEmpty()){
                if(name != " "){
                    val remove = " "
                    val replace = "%20"
                    query = name.replace(remove,replace)
                    retrieveMovies(query)
                }else{
                    val toast = Toast.makeText(applicationContext, "Nothing to search", Toast.LENGTH_LONG)
                    toast.show()
                }
            }else{
                val toast = Toast.makeText(applicationContext, "Nothing to search", Toast.LENGTH_LONG)
                toast.show()
            }

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
                 parseJSON(stringBuilder)
            }
        }
        if(!foundError){
            val movieAdapter = MovieAdapter(movieNames)
            val linearLayout = LinearLayoutManager(this)
            movieNameCard.layoutManager = linearLayout
            movieNameCard.adapter = movieAdapter
            movieNameCard.visibility= View.VISIBLE
        }else{
            val toast = Toast.makeText(applicationContext, "$name not found", Toast.LENGTH_LONG)
            movieNameCard.visibility= View.GONE
            toast.show()
        }
    }


    suspend fun parseJSON(stringBuilder:StringBuilder){
        val json = JSONObject(stringBuilder.toString())
        val allMovies = java.lang.StringBuilder()

        if(json.has("Error")){
            foundError = true
        }else{
            foundError = false
            var jsonArray : JSONArray = json.getJSONArray("Search")

            for(i in 0..jsonArray.length()-1){
                val movie: JSONObject = jsonArray[i] as JSONObject


                val title = movie["Title"] as String
                allMovies.append(title)

                if(i < jsonArray.length()-1){

                    allMovies.append(",,")
                }
                movieNames = allMovies.toString().split(",,")
            }
            Log.d("movies", "$movieNames")
        }
    }
}