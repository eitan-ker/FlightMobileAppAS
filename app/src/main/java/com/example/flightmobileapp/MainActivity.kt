package com.example.flightmobileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        connectButton.setOnClickListener() {
            webView.loadUrl("https://" + insertUrl.text.toString())
            //  connect()
        }

        fun connect(view: View): Unit {
            connectButton.setOnClickListener() {
                webView.loadUrl("https://" + insertUrl.text.toString())
            }
        }
    }
}