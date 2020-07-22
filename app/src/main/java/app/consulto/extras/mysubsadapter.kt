package app.consulto.extras

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import app.consulto.R
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.NetworkImageView
import com.android.volley.toolbox.StringRequest
import com.wang.avi.AVLoadingIndicatorView
import org.json.JSONObject
import org.w3c.dom.Text

class mysubsadapter(
    context: Context,
    resource: Int,
    private val l: Array<String>,
    h: Handler,
    imageLoader:ImageLoader,
    bundles:Array<Bundle>
) : ArrayAdapter<Any?>(context, resource) {
    private val handler: Handler
    private val con: Context
    lateinit var il:ImageLoader
    private lateinit var h:holder
    lateinit var buns:Array<Bundle>
    lateinit var vfinal:View
    override fun getCount(): Int {
        return l.size
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var v:View? = null
        if(v==null)
        {
            v =
                LayoutInflater.from(context).inflate(R.layout.subscription_listview, parent, false)
            h = holder()
            h.t = v.findViewById<TextView>(R.id.title)
           h.im = v.findViewById<View>(R.id.pic) as NetworkImageView
            h.av =
                v.findViewById<View>(R.id.avloads) as AVLoadingIndicatorView
            h.av.visibility = View.VISIBLE
            h.rl =
                v.findViewById<View>(R.id.lefttoright) as RelativeLayout
            h.ini()
        }
        else
        {
            h = v.tag as holder
        }
       val ad = h.rl.background as AnimationDrawable
      ad.setEnterFadeDuration(400)
       ad.setExitFadeDuration(300)
        ad.start()
        if(buns[position]!=null) {
            h.im.setImageUrl(
                "https://kikplus.000webhostapp.com/resources/icons/" + (Integer.parseInt(
                    l[position]
                ) - 1) + "icon.png", il
            )
            h.t.setText(buns[position].getString("title"));
            ad.stop()
            h.rl.setVisibility(View.INVISIBLE);
            v!!.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val m = Message();
                    m.setData(buns[position]);
                    handler.sendMessage(m);
                }
            });
        }
        v!!.setTag(h)
        return v
    }

    companion object {
        var num = 0
    }

    init {
        buns = bundles
        con = context
        il = imageLoader
        handler = h
    }
private class holder
{
    lateinit var im:NetworkImageView
    lateinit var av:AVLoadingIndicatorView
    lateinit var rl:RelativeLayout
lateinit var t:TextView
    fun ini()
    {
        im.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (im.drawable != null) {
                av.visibility = View.INVISIBLE
            }
        }
    }
}
}