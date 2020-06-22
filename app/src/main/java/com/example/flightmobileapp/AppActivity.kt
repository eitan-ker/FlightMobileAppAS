package com.example.flightmobileapp

import Api
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.screenshot.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        
    }

    fun getScreenshot(url: String) {
        val connected = intent.getBooleanExtra("is_connected", false)
        val address = intent.getStringExtra("url_screenshot")
        if (address != null) {
            Thread {
                while (connected) {
                    val gson = GsonBuilder()
                        .setLenient()
                        .create()
                    val retrofit = Retrofit.Builder()
                        .baseUrl(address)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
                    val api = retrofit.create(Api::class.java)
                    val body = api.getScreenshot().enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            val scFromBody = response.body()?.byteStream()
                            val BitmapSc = BitmapFactory.decodeStream(scFromBody)
                            runOnUiThread {
                                screenshot.setImageBitmap(BitmapSc)
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                        }
                    })
                    Thread.sleep(300)
                }
            }.start()
        }
    }
}
