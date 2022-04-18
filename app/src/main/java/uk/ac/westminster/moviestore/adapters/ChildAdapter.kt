package uk.ac.westminster.moviestore.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.ac.westminster.moviestore.R
import uk.ac.westminster.moviestore.entities.Movie
import uk.ac.westminster.moviestore.entities.relations.ActorWithMovies

class ChildAdapter(val childInfo: List<Movie>) :  RecyclerView.Adapter<ChildAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val moviesNamame = view.findViewById<TextView>(R.id.MovieName)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val childInflater = LayoutInflater.from(parent.context).inflate(R.layout.child_list,parent,false)
        return ViewHolder(childInflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val childItem = childInfo[position]
        holder.moviesNamame.text = childItem.movieTitle
    }

    override fun getItemCount(): Int {
        return childInfo.size
    }
}