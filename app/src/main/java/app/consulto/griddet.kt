package app.consulto

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.*

public class gridet
{
        public lateinit var gri:griddetails
    fun get_details(id:String,username:String,password:String,queue: RequestQueue,con:Context)
    {
        app.consulto.extras.mysubsadapter.num = app.consulto.extras.mysubsadapter.num+1
        lateinit var reviews: Array<Array<String?>>
        reviews = Array(
            2
        ) { arrayOfNulls<String>(4) }
        var sr: StringRequest = object : StringRequest(
            Method.POST,
            "http://consulto.oromap.in/catdetails.php",
            Response.Listener { response ->
                try {
                    val ob = JSONObject(response)
                    val description =
                        ob.getString("description").trim()
                    val rating = ob.getString("rating").trim()
                    val arr = ob.getJSONArray("reviews")
                    val name = ob.getString("name").trim()
                     if (arr.length()!=0) {
                         val reviewscount = arr.length()
                         reviews = Array(
                             2
                         ) { arrayOfNulls<String>(reviewscount) }
                         var childob: JSONObject
                         for (i in 0..reviewscount-1) {
                             childob = arr.getJSONObject(i)
                             reviews[0][i] = childob.getString("review")
                             reviews[1][i] = childob.getString("user")
                         }
                     }
                        gri.griddetails(
                            name,
                            description,
                            rating,reviews)
                     } catch (e: Exception) {
                    Toast.makeText(con,e.localizedMessage,Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener {
                Toast.makeText(con,"sss",Toast.LENGTH_LONG).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val hash =
                    HashMap<String, String>()
                hash.put("id",id)
                hash.put("email",username)
                hash.put("password",password)
                return hash
            }
        }
        queue.add(sr)
    }
}
interface griddetails
{
    fun griddetails(name:String,dess:String,rating:String,reviews:Array<Array<String?>>)
}




