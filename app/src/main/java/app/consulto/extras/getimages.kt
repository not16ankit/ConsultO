package app.consulto.extras

import android.graphics.BitmapFactory
import android.media.Image
import android.os.AsyncTask
import android.widget.ImageView
import java.net.URL

class geticons(im:ImageView) : AsyncTask<ImageView,String,String>(){
    lateinit var image:ImageView
    public var ob:inter? = null
    init {
        image = im
    }
    override fun doInBackground(vararg params: ImageView?): String {
        val u = URL("http://consulto.oromap.in/resources/career.png")
        val con = u.openConnection()
        con.setRequestProperty("Referer","http://consulto.oromap.in")
        val inp = con.getInputStream()
        ob!!.inter(inp)
        return ""
    }
}