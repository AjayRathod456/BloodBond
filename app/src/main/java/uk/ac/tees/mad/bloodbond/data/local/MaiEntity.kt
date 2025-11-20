package uk.ac.tees.mad.bloodbond.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "mainDb")
data class MainEntity(

    @PrimaryKey
    val id: String,
    val uid: String,
    val name: String,
    val email: String,
    val passkey: String,
    val title: String,
    val bloodGroup: String,
    val mobNumber: String,
    val idImageUrl: String,
    val profileImageUrl: String
)

