package com.example.flightmobileapp

import Api
import android.app.usage.UsageEvents
import android.content.Intent
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightmobileapp.db.Url
import com.example.flightmobileapp.db.UrlRepo
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.lang.System.currentTimeMillis

class UrlViewModel(private val repository: UrlRepo) : ViewModel(), Observable {

    val urls = repository.urls
    private var isUpdateOrDelete = false
    private lateinit var urlToUpdateOrDelete: Url
    private lateinit var urlRepo: UrlRepo

    lateinit var url_for_screenshot : String

    @Bindable
    val inputUrl = MutableLiveData<String>()

    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()

    @Bindable
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()

    var isConnected = MutableLiveData<Boolean>()

    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "Connect"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate() {

        if (inputUrl.value == null) {
            statusMessage.value = Event("Please enter Url")
        } else {

            if (isUpdateOrDelete) {
                urlToUpdateOrDelete.url = inputUrl.value!!

                if (urlToUpdateOrDelete.url != "") {
                    // checkIfExist
                    insert(Url(id = 0, url = urlToUpdateOrDelete.url))
                    connect(urlToUpdateOrDelete.url)
                }
            } else {
                val url = inputUrl.value!!
                if (url != "") {
                    if (checkIfExist(url)) {
                        returnUrl(url)?.let { delete(it) }
                    }

                    insert(Url(id = 0, url = url))
                    connect(url)
                }
            }
        }
        inputUrl.value = null
    }

    // moving here only after i'm sure there is a url
    fun returnUrl(urlAddress : String) : Url? {
        for (url in urls.value!!) {
            if (urlAddress == url.url) {
                return url
            }
        }
        return null
    }

    fun checkIfExist(urlAddress : String) : Boolean {
        for (url in urls.value!!) {
            if (urlAddress == url.url) {
                return true
            }
        }
        return false
    }

    fun clearAllOrDelete() {
        clearAll()
    }

    fun insert(url: Url): Job = viewModelScope.launch {
        if (inputUrl.value != null) {
            repository.insert(url)
        }
    }

    /* fun update(url: Url): Job = viewModelScope.launch {
    repository.update(url)
      inputUrl.value = null
      isUpdateOrDelete = false
      clearAllOrDeleteButtonText.value = "Clear All"
} */

      fun delete(url: Url): Job = viewModelScope.launch {
          repository.delete(url)
       //   inputUrl.value = null
       //   isUpdateOrDelete = false
       //   clearAllOrDeleteButtonText.value = "Clear All"
      }

    fun connect(url: String) {
        try {
            val gson = GsonBuilder()
                .setLenient().create()
            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson)).build()
            val api = retrofit.create(Api::class.java)
            val body = api.getScreenshot().enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    //    success
                    if (response.isSuccessful) {

                        // isConnected = true
                        url_for_screenshot = url
                        isConnected.value = true

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    //    failure

                }
            })
            inputUrl.value = null
        } catch (e: Exception) {

            // could not connect to URL output to screen

        }

    }

    fun clearAll(): Job = viewModelScope.launch {
        repository.deleteAll()
        //index = -0
    }

    fun initUpdateAndDelete(url: Url) {
        inputUrl.value = url.url
        isUpdateOrDelete = true // clicked event
        urlToUpdateOrDelete = url

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}