package com.example.flightmobileapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flightmobileapp.db.UrlRepo

class UrlViewModelFactory(private val repository : UrlRepo) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if ( modelClass.isAssignableFrom((UrlViewModel::class.java))) {
            return UrlViewModel(repository) as T
        }
        throw  IllegalArgumentException("Unknown View Model Class")
    }

}