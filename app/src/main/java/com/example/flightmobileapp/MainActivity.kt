package com.example.flightmobileapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flightmobileapp.databinding.ActivityMainBinding
import com.example.flightmobileapp.db.Url
import com.example.flightmobileapp.db.UrlDataBase
import com.example.flightmobileapp.db.UrlRepo

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

        urlViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        urlViewModel.isConnected.observe(this, Observer {
            if(it) {
                val url_screenShot = urlViewModel.url_for_screenshot
                val intent = Intent(this@MainActivity, AppActivity::class.java)
                intent.putExtra("url_screenshot", url_screenShot)
                intent.putExtra("is_connected", it)
                startActivity(intent)
            }
        })
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