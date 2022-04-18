package uk.ac.westminster.moviestore.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.ac.westminster.moviestore.R


class MovieAdapter(val movieInfo: List<String>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val movieName = view.findViewById<TextView>(R.id.MovieName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val movieInflater = LayoutInflater.from(parent.context).inflate(R.layout.film_name_card,parent,false)
        return ViewHolder(movieInflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movieItem = movieInfo[position]
        holder.movieName.text = movieItem
    }

    override fun getItemCount(): Int {
        return movieInfo.size
    }


}