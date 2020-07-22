package app.consulto

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import app.consulto.extras.card_adapter
import app.consulto.extras.get_icons
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream


class MainActivity : AppCompatActivity() {
    lateinit var downloadhandler:Handler
    private var email = ""
    private var tokill = false
    private var password = ""
    companion object
    {
        lateinit var anim:LottieAnimationView
        var list:ArrayList<String> = ArrayList<String>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            window.statusBarColor = resources.getColor(R.color.background)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
       check();
    }
    fun set_up_pager()
    {
        val pager :  ViewPager = vp;
        val tabs : TabLayout = tabs;
        val nextbut : Button = loginbut;
        pager.addOnPageChangeListener(object:ViewPager.OnPageChangeListener
        {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if(position==1)
                {
                    anim.playAnimation()
                }
                else if(anim.isAnimating)
                {
                    anim.pauseAnimation()
                }
            }
        })
        nextbut.setOnClickListener(object:View.OnClickListener
        {
            override fun onClick(v: View?) {
                if(pager.currentItem==0)
                {
                    pager.currentItem = 1
                }
                else if(pager.currentItem==2)
                {
                    if(list.size>2)
                    {
                        arrow.visibility = View.INVISIBLE
                        val av = avunderthehood
                        val size = AnimationUtils.loadAnimation(applicationContext,R.anim.go)
                        size.setAnimationListener(object:Animation.AnimationListener
                        {
                            override fun onAnimationEnd(animation: Animation?) {
                                nextbut.visibility = View.INVISIBLE
                                av.visibility = View.VISIBLE
                                get_icons()
                            }

                            override fun onAnimationRepeat(animation: Animation?) {

                            }

                            override fun onAnimationStart(animation: Animation?) {

                            }
                        })
                        nextbut.startAnimation(size)
                    }
                }
                else if(pager.currentItem==1)
                {
                    pager.currentItem = 2
                }
            }
        })
        pager.offscreenPageLimit = 3
        val adapt = pager_adapter_intro(supportFragmentManager)
        pager.adapter = adapt
        tabs.setupWithViewPager(pager)
    }
    fun get_icons()
    {
        val i = Intent(this,login::class.java)
        downloadhandler = object:Handler()
        {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if(msg.data.getBoolean("done"))
                {
                  startActivity(i)
                }
            }
        }
        val ob = get_icons(this,downloadhandler,resources.getStringArray(R.array.icons))
        ob.execute()
    }
    fun login()
    {
        tokill = true
        var f1 = File(filesDir,"username.txt")
        var fin = DataInputStream(FileInputStream(f1))
        email = fin.readLine().trim()
        fin.close()
        f1 = File(filesDir,"password.txt")
        fin = DataInputStream(FileInputStream(f1))
        password = fin.readLine().trim()
        fin.close()
        if(email.equals("educon@gm.in",true)||email.equals("careercon@gm.in",true)||email.equals("groomcon@gm.in",true)||email.equals("healthcon@gm.in",true)||email.equals("marketcon@gm.in",true)||email.equals("comcon@gm.in",true)||email.equals("relationcon@gm.in",true)||email.equals("fundcon@gm.in",true))
        {
            val intent = Intent(this, app.consulto.seller.chats::class.java)
            val bun = Bundle()
            tokill=true
            bun.putString("email",email)
            bun.putString("password",password)
            if(email.equals("educon@gm.in",true))
            {
                bun.putInt("id",1)
                bun.putString("title","Education")
            }
            else if(email.equals("careercon@gm.in",true))
            {
                bun.putInt("id",2)
                bun.putString("title","Career")
            }
            else if(email.equals("groomcon@gm.in",true))
            {
                bun.putInt("id",3)
                bun.putString("title","Self-Grooming")
            }
            else if(email.equals("healthcon@gm.in",true))
            {
                bun.putInt("id",4)
                bun.putString("title","Health & Nutrition")
            }
            else if(email.equals("marketcon@gm.in",true))
            {
                bun.putInt("id",5)
                bun.putString("title","Marketing")
            }
            else if(email.equals("comcon@gm.in",true))
            {
                bun.putInt("id",6)
                bun.putString("title","Communication")
            }
            else if(email.equals("relationcon@gm.in",true))
            {
                bun.putInt("id",7)
                bun.putString("title","Relationship")
            }
            else if(email.equals("fundcon@gm.in",true))
            {
                bun.putInt("id",8)
                bun.putString("title","Fund Management")
            }
            intent.putExtras(bun)
            startActivity(intent)
        }
        else {
            val intent = Intent(this, grids::class.java)
            val bun = Bundle()
            bun.putString("email", email)
            bun.putString("password", password)
            intent.putExtras(bun)
            tokill = true
            startActivity(intent)
        }
    }
    fun check()
    {
        if(File(filesDir,"username.txt").exists())
        {
            login()
        }
        else{
          set_up_pager()
        }
    }

    override fun onPause() {
        super.onPause()
        if(tokill)
        {
            finish()
        }
    }
}
class pager_adapter_intro(fm:FragmentManager) : FragmentPagerAdapter(fm)
{
    override fun getItem(position: Int): Fragment {
     var frag:Fragment = page_one();
        when(position)
        {
            0 ->
            {
              frag = page_one();
            }
            1->
            {
                frag = page_three()
            }
            2->
            {
                frag = page_two();
            }
        }
        return frag
    }

    override fun getCount(): Int {
        return 3
    }
}
class page_one : Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.view_one,null);
        val icon = v.findViewById<ImageView>(R.id.icon);
        val rel = v.findViewById<RelativeLayout>(R.id.welcomerel);
        val alpha = AlphaAnimation(0f,1f)
        alpha.duration = 500
        alpha.setAnimationListener(object:Animation.AnimationListener
        {
            override fun onAnimationEnd(animation: Animation?) {
                rel.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
        icon.startAnimation(alpha)
        return v
    }
}
class page_three : Fragment()
{
    lateinit var anim:LottieAnimationView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.view_three,null);
        MainActivity.Companion.anim = v.findViewById(R.id.icon)
            return v
    }
}
class page_two() : Fragment()
{
    private var ids:Array<String?>? = null
    lateinit var grids:GridView
    lateinit var v:View
    lateinit var arrr:Array<Bitmap?>
    lateinit var rq:RequestQueue
    lateinit var cats:Array<String?>
    lateinit var im:ImageLoader
    private var k = 0;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.view_two,null)
        rq = Volley.newRequestQueue(context)
        grids = v.findViewById(R.id.listview)
        return v
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser&&ids==null)
        {
            val sr = StringRequest(Request.Method.POST,"http://consulto.oromap.in/allcats.php",Response.Listener {
                inilist(it.trim())
            }, Response.ErrorListener {

            })
            rq.add(sr)
        }
    }
    private fun inilist(res: String) {
        try {
            val ob = JSONObject(res)
            val arr = ob.getJSONArray("cats")
            cats = arrayOfNulls<String>(arr.length())
            ids = arrayOfNulls<String>(arr.length())
            var jsonob: JSONObject
            for (i in 0 until arr.length()) {
                jsonob = arr.getJSONObject(i)
                cats[i] = jsonob.getString("title")
                ids!![i] = jsonob.getString("id")
            }
            arrr = arrayOfNulls<Bitmap>(arr.length())
            iniimages()
        } catch (e: Exception) {
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
    private fun iniimages()
    {
        val ir = ImageRequest("https://kikplus.000webhostapp.com/resources/icons/"+k+"icon.png", Response.Listener {
            arrr[k] = it
            k++
            if(k<arrr.size)
            {
                iniimages()
            }
            else{
                v.findViewById<LottieAnimationView>(R.id.pref_loading).visibility = View.INVISIBLE
                val adapt = grids_adapter(cats,ids!!,context as Context,arrr)
                grids.adapter = adapt
                grids.visibility = View.VISIBLE
            }
        },100,100,ImageView.ScaleType.FIT_XY,Bitmap.Config.RGB_565,
            Response.ErrorListener {

            })
        rq.add(ir)
    }
}
class grids_adapter(cat:Array<String?>,id:Array<String?>,con:Context,arr:Array<Bitmap?>) : BaseAdapter()
{
    lateinit var iconarr:Array<Bitmap?>
    lateinit var cats:Array<String?>
    lateinit var ids:Array<String?>
    lateinit var c:Context
    lateinit var h:ViewHolder
    init {
        this.iconarr = arr
        this.cats = cat
        this.c = con
        this.ids = id
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var v: View? = null
        if(v==null)
        {
            h = ViewHolder();
            v = LayoutInflater.from(c).inflate(R.layout.grid_card,null)
            h.text = v.findViewById(R.id.title)
            h.imageView = v.findViewById(R.id.icon)
            h.checkbox = v.findViewById(R.id.check)
            h.x = v.scaleX.toDouble()
            h.y = v.scaleY.toDouble()
            h.ini(v,position,c)
        }
        else{
            h = v.tag as ViewHolder
        }
        h.text.setText(cats[position])
        h.imageView.setImageBitmap(iconarr[position])
        v!!.setTag(h)
        return v
    }

    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getItemId(position: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int {
       return ids.size
    }
class ViewHolder
{
        lateinit var text:TextView
        lateinit var imageView: ImageView
        lateinit var checkbox:CheckBox
    var x = 0.0
    var y = 0.0
   fun ini(v:View,position: Int,c:Context)
    {
        v.setOnClickListener(object:View.OnClickListener
        {
            override fun onClick(v: View?) {
                if(checkbox.isChecked)
                {
                    checkbox.isChecked = false
                    v!!.scaleX = x.toFloat()
                    v.scaleY = y.toFloat()
                    MainActivity.Companion.list.remove("s"+position)
                }
                else if(!checkbox.isChecked)
                {
                    v!!.scaleX = (x-(0.04)).toFloat()
                    v.scaleY = (y-0.04).toFloat()
                    checkbox.isChecked  = true
                    MainActivity.Companion.list.add("s"+position)
                }
            }
        })
    }
}
}