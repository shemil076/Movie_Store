package uk.ac.westminster.moviestore.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.ac.westminster.moviestore.R
import uk.ac.westminster.moviestore.entities.Movie

class ChildAdapter(val childInfo: List<Movie>) :  RecyclerView.Adapter<ChildAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val moviesName = view.findViewById<TextView>(R.id.MovieName)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val childInflater = LayoutInflater.from(parent.context).inflate(R.layout.child_list,parent,false)
        return ViewHolder(childInflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val childItem = childInfo[position]
        holder.moviesName.text = childItem.movieTitle
    }

    override fun getItemCount(): Int {
        return childInfo.size
    }
}