import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
interface Api {
    @GET("/")
    fun getImg(): Call<ResponseBody>
}
