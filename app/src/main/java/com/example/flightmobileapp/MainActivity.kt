package com.example.flightmobileapp

import Api
import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flightmobileapp.databinding.ActivityMainBinding
import com.example.flightmobileapp.db.Url
import com.example.flightmobileapp.db.UrlDataBase
import com.example.flightmobileapp.db.UrlRepo


import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var urlViewModel : UrlViewModel
    private lateinit var adapter : MyRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val dao = UrlDataBase.getInstance(application).urlDAO
        val repository = UrlRepo(dao)
        val factory = UrlViewModelFactory(repository)
        urlViewModel = ViewModelProvider(this, factory).get(UrlViewModel::class.java)
        binding.myViewModel = urlViewModel
        binding.lifecycleOwner = this
        initRecycleView()



        /*connectButton.setOnClickListener {

            val gson = GsonBuilder()
                .setLenient().create()
            val retrofit = Retrofit.Builder()
                .baseUrl(insertUrl.text.toString())
                .addConverterFactory(GsonConverterFactory.create(gson)).build()
            val api = retrofit.create(Api::class.java)
            val body = api.getScreenshot().enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    //    success
                    if (response.isSuccessful) {



                        val intent = Intent(this@MainActivity, AppActivity::class.java)
                        startActivity(intent)
                    }
                    }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    //    failure
                }
            })
        }*/
    }

    private fun initRecycleView() {
        binding.urlsRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyRecyclerViewAdapter({selectedItem : Url->listItemClicked(selectedItem)})
        binding.urlsRecyclerView.adapter = adapter
        displayUrlsList()


    }

    private fun displayUrlsList() {
        urlViewModel.urls.observe(this, Observer {
            Log.i("MYTAG", it.toString())
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(url : Url) {
       // Toast.makeText(this, "selected url is ${url.url}", Toast.LENGTH_LONG).show()
        urlViewModel.initUpdateAndDelete(url)
    }

}