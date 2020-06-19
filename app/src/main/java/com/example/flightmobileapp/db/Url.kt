package com.example.flightmobileapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URL

@Entity(tableName = "URL_data_table")
data class Url (

    @PrimaryKey(autoGenerate =  true)
    @ColumnInfo(name = "Url_Id")
    var id : Int,

    @ColumnInfo(name = "Url")
    var url : String
)
