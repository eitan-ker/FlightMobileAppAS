package com.example.flightmobileapp

import Api
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connectButton.setOnClickListener {

            val gson = GsonBuilder()
                .setLenient()
                .create()
            val retrofit = Retrofit.Builder()
                .baseUrl(insertUrl.text.toString())
                .addConverterFactory(GsonConverterFactory.create(gson)).build()
            val api = retrofit.create(Api::class.java)
            val body = api.getImg().enqueue(object : Callback<ResponseBody> {
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
        }
    }



}