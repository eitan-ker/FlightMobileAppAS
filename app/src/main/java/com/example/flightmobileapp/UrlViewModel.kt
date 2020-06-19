package com.example.flightmobileapp

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightmobileapp.db.Url
import com.example.flightmobileapp.db.UrlRepo
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UrlViewModel(private val repository: UrlRepo) : ViewModel(), Observable {

    val urls = repository.urls
    private var isUpdateOrDelete = false
    private lateinit var urlToUpdateOrDelete : Url

    @Bindable
    val inputUrl = MutableLiveData<String>()

    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()

    @Bindable
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    init {
        saveOrUpdateButtonText.value = "Connect"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate() {
        if (isUpdateOrDelete) {
            urlToUpdateOrDelete.url = inputUrl.value!!
            update(urlToUpdateOrDelete)
        } else {
            val  url = inputUrl.value!!
            insert(Url(0, url))
            inputUrl.value = null
        }

    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            delete(urlToUpdateOrDelete)
        } else {
            clearAll()
        }

    }

    fun insert(url: Url): Job = viewModelScope.launch {
        repository.insert(url)
    }

    fun update(url: Url): Job = viewModelScope.launch {
        repository.update(url)
        inputUrl.value = null
        isUpdateOrDelete = false
        clearAllOrDeleteButtonText.value = "Clear All"

    }

    fun delete(url: Url): Job = viewModelScope.launch {
        repository.delete(url)
        inputUrl.value = null
        isUpdateOrDelete = false
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun clearAll(): Job = viewModelScope.launch {
        repository.deleteAll()
    }

    fun initUpdateAndDelete(url : Url) {
        inputUrl.value = url.url
        isUpdateOrDelete = true
        urlToUpdateOrDelete = url
        clearAllOrDeleteButtonText.value = "Delete"
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}