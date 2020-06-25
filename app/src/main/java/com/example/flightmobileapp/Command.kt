package com.example.flightmobileapp

import com.google.gson.annotations.SerializedName

data class Command(
    @SerializedName("aileron")val aileron: Float?,
    @SerializedName("elevator")val elevator: Float?,
    @SerializedName("rudder")val rudder: Float?,
    @SerializedName("throttle")val throttle: Float?
)