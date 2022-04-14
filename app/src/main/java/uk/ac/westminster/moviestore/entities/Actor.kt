package uk.ac.westminster.moviestore.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Actor(
    @PrimaryKey
    val actorName: String
)