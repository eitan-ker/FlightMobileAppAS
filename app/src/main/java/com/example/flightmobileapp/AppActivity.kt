package com.example.flightmobileapp

import Api
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlinx.android.synthetic.main.activity_app.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Url

class AppActivity : AppCompatActivity() {
    private lateinit var joystickView: JoystickView
    private lateinit var seekBar: SeekBar
    private lateinit var verticalSeekBar: VerticalSeekBar
    private var aileron: Float = 50.0f
    private var elevator: Float = 50.0f
    private var rudder: Float = 0.0f
    private var throttle: Float = 0.0f
    private val MIN_VALUE: Int = 0
    private val MAX_VALUE: Int = 100
    private val SMALL_LIM: Int = -1
    private val BIG_LIM: Int = 1
    private val THROTTLE_SMALL_LIM: Int = 0
    private var url : String = ""
    private var isConnected : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        joystickView = findViewById(R.id.joystick)
        joystickView.setOnMoveListener {
                angle: Int, strength: Int ->
            onMoveEvent(angle, strength)
        }
        url = getIntent().getExtras()?.get("url_screenshot").toString()
        //isConnected = getIntent().getExtras()?.get("isConnected") as Boolean
        getScreenshot(url, isConnected);
        initSeekBars()
    }

    private fun initSeekBars() {
        seekBar = findViewById(R.id.seekBar)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                rudder = normalize(progress, SMALL_LIM, BIG_LIM, MIN_VALUE, MAX_VALUE)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        verticalSeekBar = findViewById(R.id.mySeekBar)
        verticalSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                throttle = normalize(progress, THROTTLE_SMALL_LIM, BIG_LIM, MIN_VALUE, MAX_VALUE)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    fun onMoveEvent(angle: Int, strength: Int) {
        elevator = normalize(joystickView.normalizedY, SMALL_LIM, BIG_LIM, MIN_VALUE, MAX_VALUE)
        aileron = normalize(joystickView.normalizedX, SMALL_LIM, BIG_LIM, MIN_VALUE, MAX_VALUE)
        sendCommand()
    }

    private fun sendCommand() {

    }

    fun normalize(value: Int, smallLim: Int, bigLim: Int, min: Int, max: Int): Float {
        return (bigLim - smallLim) * ((value.toFloat() - min) / (max - min)) + smallLim
    }

    fun getScreenshot(url : String, isConnected : Boolean){
        Thread {
            while(true) {
                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                val retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                val api = retrofit.create(Api::class.java)
                val body = api.getScreenshot().enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        val scFromBody = response.body()?.byteStream()
                        val bitmapSc = BitmapFactory.decodeStream(scFromBody)
                        runOnUiThread {
                            screenshot.setImageBitmap(bitmapSc)
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        //On Failure
                        Toast.makeText(applicationContext, "Could not retrieve image from server", Toast.LENGTH_LONG).show()
                    }
                })
                Thread.sleep(500)
            }
        }.start()
       //  Toast.makeText(applicationContext, "Exiting getScreenshot method", Toast.LENGTH_LONG).show()
    }
}
