import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface Api {
    @GET("/screenshot")
    fun getScreenshot(): Call<ResponseBody>
}
