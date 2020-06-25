import com.example.flightmobileapp.Command
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface Api {
    @GET("/screenshot")
    fun getScreenshot(): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("/api/command")
    fun postCommand(@Body command: Command): Call<Command>
}
