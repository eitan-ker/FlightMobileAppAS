import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
interface Api {
    @GET("/screenshot")
    fun getImg(): Call<ResponseBody>
}
