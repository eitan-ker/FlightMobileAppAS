package com.example.flightmobileapp.db

class UrlRepo(private val dao : UrlDAO) {

    val urls = dao.getAllUrls()

    suspend fun insert (url : Url) {
        dao.insertUrl(url)
    }

    suspend fun update (url : Url) {
        dao.updateUrl(url)
    }

    suspend fun delete (url : Url) {
        dao.deleteUrl(url)
    }

    suspend fun deleteAll () {
        dao.deleteAll()
    }
}