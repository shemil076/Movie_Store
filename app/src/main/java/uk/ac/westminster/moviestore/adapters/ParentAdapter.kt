package uk.ac.westminster.moviestore.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uk.ac.westminster.moviestore.R
import uk.ac.westminster.moviestore.entities.relations.ActorWithMovies

class ParentAdapter(val activity: Activity, val parentInfo: List<ActorWithMovies>):  RecyclerView.Adapter<ParentAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val actor  = view.findViewById<TextView>(R.id.ActorName)
        val nested_rv = view.findViewById<RecyclerView>(R.id.nested_rv)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val parentInflater = LayoutInflater.from(parent.context).inflate(R.layout.parent_item,parent,false)
        return ViewHolder(parentInflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = parentInfo[position]
        holder.actor.text = item.actor.actorName
        val childAdapter = ChildAdapter(item.movies)
        val layoutManager = LinearLayoutManager(activity)
        holder.nested_rv.layoutManager = layoutManager
        holder.nested_rv.adapter = childAdapter

    }

    override fun getItemCount(): Int {
        return parentInfo.size
    }


}