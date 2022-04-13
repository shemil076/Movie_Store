package uk.ac.westminster.moviestore.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Movie(
    @PrimaryKey(autoGenerate = false)
    val movieTitle: String,

    val year: String,
    val rated: String,
    val released: String,
    val runtime: String,
    val genre: String,
    val director: String,
    val writer: String,
    val plot: String,

)