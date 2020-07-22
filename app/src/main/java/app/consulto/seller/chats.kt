package app.consulto.seller

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.consulto.R
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.File
import java.util.*

class chats : AppCompatActivity() {
    private var id = 0
    private var title = ""
    private var email = ""
    lateinit var queue: RequestQueue
    lateinit var c:listadapt
    lateinit var firebaseDatabase:FirebaseDatabase
    private var password= ""
    lateinit var users:List<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)
        setSupportActionBar(findViewById(R.id.toolbar))
        val bun = intent.extras
        queue = Volley.newRequestQueue(this)
        id = bun!!.getInt("id")
        title = bun.getString("title").toString()
        email = bun.getString("email").toString()
        password = bun.getString("password").toString()
        supportActionBar!!.setTitle(title)
       get_live_users()
        val ser = Intent(this,customer_service::class.java)
       startService(ser)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
      menuInflater.inflate(R.menu.seller_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        logout()
        return true
    }
    fun logout() {
        File(filesDir, "username.txt").delete()
        File(filesDir, "password.txt").delete()
        finish()
    }
    private fun make_list()
    {
        val i = Intent(this,SellerTalk::class.java)
        val h = object:Handler()
        {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                val bun  =Bundle();
                bun.putString("email",email)
                bun.putString("password",password)
                bun.putString("id",id.toString())
                bun.putString("to",msg.data.getString("user"))
                i.putExtras(bun)
                startActivity(i)
            }
        }
        val listview = findViewById<ListView>(R.id.listvoew);
        c = listadapt(this,R.layout.live_user_listview,users,h)
        listview.adapter = c;
    }
    private fun get_live_users()
    {
     firebaseDatabase = FirebaseDatabase.getInstance()
        val ref = firebaseDatabase.getReference("live"+id)
        ref.addValueEventListener(object:ValueEventListener
        {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.getValue()!=null)
                {
                    val usrs = snapshot.getValue().toString();
                    users = usrs.split(",")
                    make_list()
                }
            }
        })
    }
}
class listadapt(con: Context,res:Int,usrs:List<String>,h:Handler) : ArrayAdapter<String>(con,res)
{
    lateinit var handler: Handler
    lateinit var users:List<String>
    init {
        this.handler = h
        this.users = usrs
    }
    override fun getCount(): Int {
        return users.size-1
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v:View = LayoutInflater.from(context).inflate(R.layout.live_user_listview,null)
        v.findViewById<TextView>(R.id.user).setText("User "+(position+1))
        v.setOnClickListener(object :View.OnClickListener
        {
            override fun onClick(v: View?) {
                val b = Bundle();
                val m = Message();
                b.putString("user",users[position])
                m.data = b
                handler.sendMessage(m)
            }
        })
        return v;
    }
}