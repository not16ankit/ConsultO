package app.consulto

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.viewpager.widget.ViewPager
import app.consulto.extras.extrasequence
import app.consulto.extras.pageradapter
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.wang.avi.AVLoadingIndicatorView
import net.kibotu.pgp.Pgp
import org.json.JSONObject
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class login : AppCompatActivity() {
    lateinit var internet:Handler;
    lateinit var gso:GoogleSignInOptions
    private var queue: RequestQueue? = null
    private var name=""
    lateinit var pd:Dialog
    lateinit var al:AlertDialog.Builder
    lateinit var slow_arrive:Animation
    private var username=""
    private var email=""
    private var instacode = ""
    lateinit var go:Animation
    lateinit var vg:ViewGroup;
    lateinit var intro_view:View;
    lateinit var avunderhood:AVLoadingIndicatorView;
    private var process:Boolean = false
    lateinit var cum: Animation
    lateinit var viewPager: ViewPager
    private var type22=0
    lateinit var progd:ProgressDialog
    lateinit var av2:AVLoadingIndicatorView
    lateinit var j:Handler
    private var tokill = false
    lateinit var downloadhandler:Handler
    lateinit var after_view_pgaer_handler:Handler
    private var password=""
    lateinit var gsoclient: GoogleSignInClient
    lateinit var but:Button
    private var  o = 0
    lateinit var avload:LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.foreground)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide()
        findViewById<FrameLayout>(R.id.tool).background = BitmapDrawable(
            resources,
            BitmapFactory.decodeFile(File(filesDir, "logintop.png").absolutePath)
        )
        init()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==2)
        {
            val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            task.addOnCompleteListener(object: OnCompleteListener<GoogleSignInAccount>
            {
                override fun onComplete(p0: Task<GoogleSignInAccount>) {
                    process=false
                }
            })
            task.addOnSuccessListener(object: OnSuccessListener<GoogleSignInAccount> {
                override fun onSuccess(p0: GoogleSignInAccount?) {
                    val acc = p0;
                    name = acc!!.displayName!!.trim()
                    email = acc.email!!.trim()
                    password = acc.id!!.trim()
                    if(name.isNotEmpty()&&email.isNotEmpty()&&password.isNotEmpty())
                    {
                        password = extrasequence(applicationContext).faiza(password)
                        signin()
                    }
                    else
                    {
                        Toast.makeText(applicationContext,"Sign In Failed", Toast.LENGTH_LONG).show()
                    }
                }
            })
            task.addOnFailureListener(object: OnFailureListener
            {
                override fun onFailure(p0: Exception) {
                    but.visibility = View.VISIBLE
                    Toast.makeText(applicationContext,"Sign In Failed", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
    fun signup()
    {
        but.visibility = View.INVISIBLE
        val keygen = Pgp.generateKeyRingGenerator(email.toCharArray())
        val pubkey = Pgp.genPGPPublicKey(keygen)
        val privkey = Pgp.genPGPPrivKey(keygen)
        val sr = object: StringRequest(Method.POST,"http://consulto.oromap.in/signup.php", Response.Listener {
            if(it.trim().equals("200",true))
            {
                after_signin()
            }
            else if(it.trim().equals("na",true))
            {
                val v = viewPager.get(1);
                val firstet =
                    v.findViewById<View>(R.id.firstname) as TextInputEditText
                val lastet =
                    v.findViewById<View>(R.id.lastname) as TextInputEditText
                val emailet =
                    v.findViewById<View>(R.id.email) as TextInputEditText
                emailet.setError("Email Already in use")
                but.visibility = View.VISIBLE
                val passet =
                    v.findViewById<View>(R.id.password) as TextInputEditText
                val checkBox =
                    v.findViewById<View>(R.id.termsandconditions) as CheckBox
                v.findViewById<View>(R.id.loginbut)
                    .setOnClickListener {
                        val name =
                            firstet.text.toString().trim { it <= ' ' } + lastet.text
                                .toString().trim { it <= ' ' }
                        val email =
                            emailet.text.toString().trim { it <= ' ' }
                        val password =
                            passet.text.toString().trim { it <= ' ' }
                        if (name.isEmpty()) {
                            firstet.error = "Field Required"
                        } else {
                            if (email.isEmpty()) {
                                emailet.error = "Field Required"
                            } else {
                                if (password.isEmpty()) {
                                    passet.error = "Field Required"
                                } else {
                                    if (!checkBox.isChecked) {
                                        Toast.makeText(
                                            applicationContext,
                                            "Your consent for terms acceptance is required.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        this.name =  name
                                        this.email =  email
                                        this.password = password
                                        signup()
                                    }
                                }
                            }
                        }
                    }
            }
            else
            {
                Toast.makeText(applicationContext,it,Toast.LENGTH_LONG).show()
                but.visibility = View.VISIBLE
                avload.visibility = View.INVISIBLE
            }
        }, Response.ErrorListener {
            but.visibility = View.VISIBLE
            avload.visibility = View.INVISIBLE
        })
        {
            override fun getParams(): MutableMap<String, String> {
                val hash = HashMap<String,String>()
                hash.put("name",name)
                hash.put("password",password)
                hash.put("email",email)
                hash.put("privkey",privkey)
                hash.put("pubkey",pubkey)
                return hash
            }
        }
        queue!!.add(sr)
    }
    fun google_signin()
    {
        val i = gsoclient.signInIntent
        startActivityForResult(i,2)
    }
    fun init()
    {
        queue = Volley.newRequestQueue(this)
        ini_progres()
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestId()
            .build();
        gsoclient = GoogleSignIn.getClient(this,gso);
        set_handler()
        go = AnimationUtils.loadAnimation(this,R.anim.go);
        slow_arrive = AnimationUtils.loadAnimation(this,R.anim.arrive);
        val text = arrayOf("SignIn","SignUp")
        val sectionsPagerAdapter = pageradapter(this, supportFragmentManager,text.toList(),after_view_pgaer_handler)
        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val anim2 = AlphaAnimation(0f,1f)
        anim2.duration = 1400
        anim2.setAnimationListener(object: Animation.AnimationListener
        {
            override fun onAnimationEnd(animation: Animation?) {
                findViewById<RelativeLayout>(R.id.linearlop).visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
        findViewById<FrameLayout>(R.id.tool).startAnimation(anim2)
    }
    fun after_code()
    {
        progd = ProgressDialog(this,R.style.slert)
        progd.setMessage("Signing in.....")
        progd.setCancelable(false)
        progd.show()
        var sr = object:StringRequest(
            Request.Method.POST,"https://api.instagram.com/oauth/access_token",
            Response.Listener {

                val jsonob = JSONObject(it.trim())
                val userid = jsonob.get("access_token").toString().trim();
                val access_token = jsonob.get("user_id").toString().trim();
                get_insta_details(userid,access_token)
            }, Response.ErrorListener {
                Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val hash = HashMap<String, String>()
                hash.put("code", instacode)
                hash.put("client_id", resources.getString(R.string.insta_id))
                hash.put("client_secret", resources.getString(R.string.insta_secret))
                hash.put("grant_type", "authorization_code")
                hash.put("redirect_uri", "https://kikplus.000webhostapp.com/code.php")
                return hash
            }
        }
        queue!!.add(sr)
    }
    fun get_insta_details(token:String,userid:String)
    {
        var sr = StringRequest(
            Request.Method.GET,"https://graph.instagram.com/"+userid+"?access_token="+token+"&fields=username",
            Response.Listener {
                val jsonob = JSONObject(it.trim())
                name = jsonob.get("username").toString().trim()
                val ob = extrasequence(applicationContext)
                password = ob.faiza(jsonob.get("id").toString().trim())
                email = name
                progd.cancel()
                progd.dismiss()
                signin();
            }, Response.ErrorListener {
                Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
            })
        queue!!.add(sr)
    }
    fun ini_progres()
    {
        pd = Dialog(this,R.style.custom)
        pd.setCancelable(false)
        al = AlertDialog.Builder(this,R.style.custom)
        al.setCancelable(false)
    }
    fun instagram_signin()
    {
        val loading_view:View = layoutInflater.inflate(R.layout.loading,null)
        val alview = layoutInflater.inflate(R.layout.webview,null)
        val dialog = Dialog(this)
        dialog.setContentView(alview)
        dialog.setCanceledOnTouchOutside(false)
        val webview =  alview.findViewById<WebView>(R.id.web)
        val webclient = object: WebViewClient()
        {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                alview as ViewGroup
                pd.cancel()
                pd.dismiss()
                alview.removeView(alview.findViewById(R.id.forhide))
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                super.shouldOverrideUrlLoading(view,url)
                if(url!!.contains("code=")) {
                    instacode = ""
                    for(i in url.indexOf("=")+1..url.length-3)
                    {
                        instacode = instacode+url[i]
                    }
                    dialog.cancel()
                    dialog.dismiss()
                    after_code()
                    return true
                }
                return false
            }
        }
        webview.settings.javaScriptEnabled = true
        dialog.show()
        webview.requestFocus()
        pd.setContentView(loading_view)
        pd.show()
        webview.webViewClient = webclient
        webview.loadUrl(
            "https://api.instagram.com/oauth/authorize?client_id="+resources.getString(R.string.insta_id)+"&redirect_uri=https://kikplus.000webhostapp.com/code.php&scope=user_profile&response_type=code")
    }
    fun login()
    {
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

    override fun onPause() {
        super.onPause()
        if(tokill)
        {
            finish()
        }
    }
    fun after_signin()
    {
        var f1 = File(filesDir,"username.txt");
        var fout = FileOutputStream(f1)
        fout.write(email.toByteArray())
        fout.flush()
        fout.close()
        f1 = File(filesDir,"password.txt");
        fout = FileOutputStream(f1)
        fout.write(password.toByteArray())
        fout.flush()
        fout.close()
        login()
    }
    fun signin()
    {
        val keygen = Pgp.generateKeyRingGenerator(email.toCharArray())
        val pubkey = Pgp.genPGPPublicKey(keygen)
        val privkey = Pgp.genPGPPrivKey(keygen)
        val sr = object:StringRequest(Method.POST,"http://consulto.oromap.in/signin.php", Response.Listener {
            if(it.equals("200",true))
            {
                after_signin()
            }
            else
            {
                val v = viewPager.get(0)
                val emailet: TextInputEditText = v.findViewById(R.id.email)
                val passwordet: TextInputEditText = v.findViewById(R.id.password)
                v.findViewById<View>(R.id.loginbut)
                    .setOnClickListener {
                        but.visibility = View.INVISIBLE
                        email = emailet.text.toString().trim { it <= ' ' }
                        password = passwordet.text.toString().trim { it <= ' ' }
                        if (email.isEmpty()) {
                            emailet.error = "Required Field"
                        } else {
                            if (password.isEmpty()) {
                                passwordet.error = "Required Field"
                            } else {
                                process=true
                                this.email = email
                                this.password = password
                                type22=1
                                signin()
                            }
                        }
                    }
                v.findViewById<View>(R.id.google_but)
                    .setOnClickListener {
                        but.visibility = View.INVISIBLE
                        process=true
                        type22=2
                        google_signin()
                    }
                v.findViewById<View>(R.id.facebook_but)
                    .setOnClickListener {
                        but.visibility = View.INVISIBLE
                        process=true
                        type22=3
                        instagram_signin()
                    }
                but.visibility = View.VISIBLE
                Toast.makeText(applicationContext,"Wrong",Toast.LENGTH_LONG).show()
            }
        }, Response.ErrorListener {
            but.visibility = View.VISIBLE
            avload.visibility = View.INVISIBLE
        })
        {
            override fun getParams(): MutableMap<String, String> {
                val hash = HashMap<String,String>()
                hash.put("email",email)
                hash.put("password",password)
                hash.put("name",name)
                hash.put("type",type22.toString())
                hash.put("privkey",privkey)
                hash.put("pubkey",pubkey)
                return hash
            }
        }
        queue!!.add(sr)
    }
    fun set_handler() {
        after_view_pgaer_handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                val data = msg.data
                val type = data.getInt("type")
                val ripof = data.getInt("ripof")
                if (type != 69) {
                    if (!process) {
                        go.setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationEnd(animation: Animation?) {
                                but.visibility = View.INVISIBLE
                                if (type == 1) {
                                    process = true
                                    email = data.getString("email").toString()
                                    password = data.getString("password").toString()
                                    type22 = 1
                                    signin()
                                } else if (type == 2) {
                                    process = true
                                    email = data.getString("email").toString()
                                    password = data.getString("password").toString()
                                    name = data.getString("name").toString()
                                    signup()
                                } else if (type == 3) {
                                    process = true
                                    type22 = 2
                                    google_signin()
                                } else if (type == 4) {
                                    process = true
                                    type22 = 3
                                    instagram_signin()
                                }
                            }

                            override fun onAnimationRepeat(animation: Animation?) {

                            }

                            override fun onAnimationStart(animation: Animation?) {
                                avload.visibility = View.VISIBLE
                            }
                        })
                        avload = viewPager.get(ripof).findViewById(R.id.avunderthehood)
                        but = viewPager.get(ripof).findViewById<Button>(R.id.loginbut)
                        but.startAnimation(go)
                    }
                } else {
                    if(ripof==0)
                    {
                        viewPager.currentItem = 1
                    }
                    else if(ripof==1)
                    {
                        viewPager.currentItem = 0
                    }
                }
            }
        }
    }
}