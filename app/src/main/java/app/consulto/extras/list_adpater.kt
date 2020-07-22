package app.consulto.extras


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.consulto.R
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.NetworkImageView
import com.wang.avi.AVLoadingIndicatorView
import java.io.InputStream


public class list_adapter(rating: Array<String>, enrolls: Array<String>, ik:Handler, con:Context, q:ImageLoader, num:Int, parenttitles:Array<String>, child1:Array<Array<String>>, child2:Array<Array<String>>,child3:Array<Array<String>>,child4:Array<Array<String>>,naam:String):ArrayAdapter<String>(con,num)
{
   lateinit var parenttitles1: Array<String>
    lateinit var childtitles1: Array<Array<String>>
    lateinit var childtitles2: Array<Array<String>>
    lateinit var queue: ImageLoader
    private var k = 0
   lateinit var childtitles3: Array<Array<String>>
    lateinit var qu: RequestQueue
    lateinit var arr: Array<Bitmap>
    lateinit var ih: Handler
    lateinit var childtitles4: Array<Array<String>>
    lateinit var t: Thread
    lateinit var c: Context
    lateinit var ratings: Array<String>
    lateinit var name: String
    lateinit var enrollments: Array<String>
    init {
        childtitles1 = child1;
        queue = q;
        enrollments = enrolls;
        name = naam;
        ratings = rating;
        c = con;
        ih = ik;
        childtitles2 = child2;
        childtitles3 = child3;
        childtitles4 = child4;
        parenttitles1 = parenttitles;
    }
    override fun getCount(): Int {
        return 5
    }
    @SuppressLint("WrongConstant")
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        if(position==0) {
            val holder1: list_holder
            var convertView:View? = null
            if (convertView == null) {
                holder1 = list_holder()
                convertView =
                    LayoutInflater.from(c).inflate(R.layout.hellow_view, null)
                holder1.title = convertView.findViewById(R.id.parenttitle)
                name = name+" ";
                val naam = name.split(" ")[0]
                holder1.title.text = "Hello $naam!"
            }
            else{
                holder1 = convertView.tag as list_holder
            }
            convertView!!.tag = holder1
            return convertView
        }

        else {
            val holder3: list_holder
            var convertView:View? = null
            if(convertView==null) {
                holder3 = list_holder()
                convertView = LayoutInflater.from(c).inflate(R.layout.listview, null)
                holder3.title = convertView.findViewById(R.id.parenttitle)
                holder3.linearlay = LinearLayoutManager(c, LinearLayout.HORIZONTAL, false)
                holder3.recycler = convertView.findViewById(R.id.recycler)
                holder3.title.text = parenttitles1[position]
                holder3.viewall = convertView.findViewById(R.id.all)
                when (position) {
                    1 -> {
                        holder3.readapt = recycle_adapter(
                            enrollments,
                            ratings,
                            ih,
                            childtitles1,
                            c,
                            position,
                            queue
                        )
                    }
                    2 -> {
                        holder3.viewall.visibility = View.VISIBLE
                        holder3.viewall.setOnClickListener {
                            val m = Message()
                            val d = Bundle()
                            d.putString("id", "viewall")
                            m.data = d
                            ih.sendMessage(m)
                        }
                        holder3.readapt = recycle_adapter(
                            enrollments,
                            ratings,
                            ih,
                            childtitles2,
                            c,
                            position,
                            queue
                        )
                    }
                    3 -> {
                        holder3.readapt = recycle_adapter(
                            enrollments,
                            ratings,
                            ih,
                            childtitles3,
                            c,
                            position,
                            queue
                        )
                    }
                    4 -> {
                        holder3.readapt = recycle_adapter(
                            enrollments,
                            ratings,
                            ih,
                            childtitles4,
                            c,
                            position,
                            queue
                        )
                    }
                }
                holder3.recycler.layoutManager = holder3.linearlay
                holder3.recycler.recycledViewPool.setMaxRecycledViews(0, 0)
                holder3.recycler.adapter = holder3.readapt
            }
            else{
                holder3 = convertView.tag as list_holder
            }
            convertView!!.tag = holder3
            return convertView
        }
    }

}
class list_holder
{
    lateinit var grid:ListView
    lateinit var readapt:recycle_adapter;
        lateinit var recycler: RecyclerView;
    lateinit var viewall: TextView;
    lateinit var av:AVLoadingIndicatorView
    lateinit var ims:ImageView
    lateinit var title:TextView
    lateinit var linearlay: LinearLayoutManager;
}
class recycle_adapter(enrollments:Array<String>,ratings:Array<String>,ih:Handler,m:Array<Array<String>>,c:Context,posi:Int,queue:ImageLoader):RecyclerView.Adapter<recycle_adapter.viewholder>()
{
    lateinit var enrolls: Array<String>
    lateinit var rating: Array<String>
    private var po = 0
    lateinit var queue1: ImageLoader
    lateinit var con:Context
    private var childlen = 0
    lateinit var itemhandler:Handler
    lateinit var childs:Array<Array<String>>
    init {
        itemhandler = ih
        queue1 = queue
        enrolls = enrollments
        rating = ratings
        con = c
        childlen = m[0].size
        po = posi
        childs = m
    }
    class viewholder(enrolls: Array<String>, rating:Array<String>, ithand:android.os.Handler, v:View, m:Array<Array<String>>, c:ImageLoader):RecyclerView.ViewHolder(v)
    {
        lateinit var enrollments: Array<String>
        lateinit var ratings:Array<String>
        lateinit var itemhandler:android.os.Handler
        lateinit var con:ImageLoader
        lateinit var tit:Array<Array<String>>
        private var view:View? = null
        init {
            enrollments = enrolls
            ratings = rating
         itemhandler = ithand
            con =c
            tit = m
           view = v
        }
        fun bind(posi:Int,globalposi:Int)
        {
                when (globalposi) {
                    3 -> {
                        view!!.findViewById<FrameLayout>(R.id.ll).visibility = View.VISIBLE
                      view!!.findViewById<TextView>(R.id.enrollments).setText(enrollments[posi])
                    }
                    4 -> {
                        view!!.findViewById<RatingBar>(R.id.rating).rating =
                            ((ratings[posi].toInt()) / 5).toFloat();
                    }
                }
            view!!.setOnClickListener(object:View.OnClickListener
            {
                override fun onClick(v: View?) {
                    val m = Message()
                    val bun = Bundle()
                    bun.putString("id",tit[1][posi])
                    m.data = bun
                    itemhandler.sendMessage(m)
                }
            })
            val titve = view!!.findViewById<TextView>(app.consulto.R.id.cattitle)
            titve.setText(tit[0][posi])
            titve.isSelected = true
            val im = view!!.findViewById<NetworkImageView>(app.consulto.R.id.pic)
            im.addOnLayoutChangeListener(object:View.OnLayoutChangeListener
            {
                override fun onLayoutChange(
                    v: View?,
                    left: Int,
                    top: Int,
                    right: Int,
                    bottom: Int,
                    oldLeft: Int,
                    oldTop: Int,
                    oldRight: Int,
                    oldBottom: Int
                ) {
                    if(im.drawable!=null)
                    {
                        view!!.findViewById<AVLoadingIndicatorView>(R.id.av).visibility = View.INVISIBLE
                    }
                }
            })
            im.setImageUrl("https://kikplus.000webhostapp.com/resources/" + tit[1][posi]+".png",con);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        var vi:viewholder? = null
        if(po==4)
        {
            vi = viewholder(enrolls,rating,itemhandler,LayoutInflater.from(parent.context).inflate(app.consulto.R.layout.rating_recycler,parent,false),childs,queue1)
    }
        else if(po==3)
        {
            vi = viewholder(enrolls,rating,itemhandler,LayoutInflater.from(parent.context).inflate(app.consulto.R.layout.trending_recycler_view,parent,false),childs,queue1)
        }
        else
        {
            vi =   viewholder(enrolls,rating,itemhandler,LayoutInflater.from(parent.context).inflate(app.consulto.R.layout.recycler_view,parent,false),childs,queue1)
        }
        return vi
        }

    override fun getItemCount(): Int {
        return childlen
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.bind(position,po)
    }
}
interface inter{
    fun inter(inp:InputStream)
}


