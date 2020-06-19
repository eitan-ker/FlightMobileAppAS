package com.example.flightmobileapp.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UrlDAO {

    @Insert
    suspend fun insertUrl(url : Url) : Long

    @Update
    suspend fun updateUrl(url : Url)

    @Delete
    suspend fun deleteUrl(url : Url)

    @Query("DELETE FROM URL_data_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM URL_data_table")
    fun getAllUrls() : LiveData<List<Url>>
}