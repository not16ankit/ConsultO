package app.consulto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.kibotu.pgp.Pgp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.spongycastle.openpgp.PGPException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.consulto.extras.chat_adapter;
import app.consulto.extras.chronometer;

public class TalkActivity extends AppCompatActivity {
    private String email="";
    private int len;
    private chat_adapter c;
    private int cross = 0;
    private int inilen;
    private int p;
    private int k;
    private String password;
    private chronometer chrono;
    private DatabaseReference ref;
    private String premessages="";
    private int tempsecs = 0;
    private int secs = 0;
    private DatabaseReference reff;
    private String prev = "";
    private String id = "";
    private int kk = 0;
    private ArrayList<String> username;
    private ArrayList<String> messages;
    private FirebaseDatabase f;
    private String name = "";
    private String privkey = "";
    private TextView t;
    private int o  =0;
    private ListView ls;
    private AlertDialog.Builder ab;
    private Thread thread;
    private View v;
    private AlertDialog ad;
    private String pubkey = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            getWindow().setStatusBarColor(getResources().getColor(R.color.foreground));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk);
        Bundle b = getIntent().getExtras();
        getWindow().setStatusBarColor(getResources().getColor(R.color.blue));
        id = b.getString("id");
        privkey = b.getString("privkey");
        pubkey = b.getString("pubkey");
        name = b.getString("title");
        email = b.getString("email");
        password = b.getString("password");
        Pgp.setPrivateKey(privkey);
        Pgp.setPublicKey(pubkey);
        t = (TextView) findViewById(R.id.connecttext);
        f = FirebaseDatabase.getInstance();
        getSupportActionBar().setTitle(name);
        ab = new AlertDialog.Builder(TalkActivity.this,R.style.dialog);
        v = getLayoutInflater().inflate(R.layout.cost_alert,null);
        ab.setView(v);
        ab.setCancelable(false);
        ad = ab.create();
        ad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        t.setText("Waiting for consultant");
       ini();
    }
    private void ini() {
        final Button but = v.findViewById(R.id.loginbut);
        final TextView buttext = v.findViewById(R.id.buttontext);
        final Handler h = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                int secs = msg.getData().getInt("secs");
                if(secs==10)
                {
                    get_chats();
                }
                else {
                    if (secs == 0) {
                        but.setBackground(getResources().getDrawable(R.drawable.button_back));
                        buttext.setText("OK");
                        but.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ad.cancel();
                                ad.dismiss();
                                findViewById(R.id.rel).setVisibility(View.VISIBLE);
                                ls.setVisibility(View.VISIBLE);
                                findViewById(R.id.ll).setVisibility(View.VISIBLE);
                                start();
                            }
                        });
                    } else {
                        buttext.setText("OK  (" + secs + ")");
                    }
                }
            }
        };
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i1 = 10; i1 >= 0; i1--) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message m = new Message();
                    Bundle b = new Bundle();
                    b.putInt("secs", i1);
                    m.setData(b);
                    h.sendMessage(m);
                }
            }
        });
        go_live();
    }

private void start_consultant_listener()
{
    DatabaseReference reference = f.getReference("attending"+id);
    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            String data = snapshot.getValue().toString();
            if(data.contains(email))
            {
                t.setText("Connected");
                findViewById(R.id.connecting).setVisibility(View.INVISIBLE);
                t.setText("Exchanging PGP Keys");
                ad.show();
                thread.start();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
}
    @Override
    protected void onDestroy() {
        super.onDestroy();
        reff.setValue(prev.replace(email+",",""));
    }

    private void go_live()
{
   reff = f.getReference("live"+id);
    reff.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            prev = snapshot.getValue().toString()+email+",";
            reff.setValue(prev);
            start_consultant_listener();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stop_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(cross==1) {
            chrono.stop();
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void get_chats() {
        len = 0;
        inilen = 0;
         messages = new ArrayList<String>();
         username = new ArrayList<String>();
        p = 0;
        k = 0;
         ls = (ListView) findViewById(R.id.chat_list);
        FloatingActionButton im = (FloatingActionButton) findViewById(R.id.send);
        final EditText messaget = (EditText) findViewById(R.id.message);
        messaget.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(c!=null) {
                    ls.setSelection(c.getCount() - 1);
                }
                }
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        char[] car = {'.', '$', '#', '[', ']'};
        String forusemeial = email;
        for(int io=0;io<car.length;io++)
        {
            forusemeial = forusemeial.replace(String.valueOf(car[io]),"");
        }
        ref = database.getReference(forusemeial);
        ref = ref.child(id.trim().toLowerCase());
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messaget.getText().toString().trim().isEmpty()) {
                    String message = "{'chat':[";
                    try {
                    for (int i = 0; i < len; i++) {
                        message = message + "{'message':'" + Pgp.encrypt(messages.get(i)) + "','username':'" + username.get(i) + "'}";
                        message = message + ",";
                    }
                        message = message + "{'message':'" + Pgp.encrypt(messaget.getText().toString().trim()) + "','username':'" + email + "'}]}";
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (PGPException e) {
                        e.printStackTrace();
                    }
                    messages.add(messaget.getText().toString().trim());
                    username.add(email);
                    messaget.setText("");
                    c.notifyDataSetChanged();
                    ls.smoothScrollToPosition(c.getCount() - 1);
                    ref.setValue(message);
                }
            }
        });
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.getValue() == null)) {
                    p = 0;
                    premessages = dataSnapshot.getValue().toString();
                    try {
                        JSONObject childob;
                        JSONObject json = new JSONObject(premessages);
                        JSONArray arr = json.getJSONArray("chat");
                        len = arr.length();
                        if (k == 0) {
                            inilen = len;
                            for (int i = 0; i < len; i++) {
                                childob = arr.getJSONObject(i);
                                messages.add(Pgp.decrypt(childob.getString("message"),email));
                                username.add(childob.getString("username"));
                            }
                        } else {
                            if (inilen != len) {
                                for (int i = inilen; i < len; i++) {
                                    childob = arr.getJSONObject(i);
                                    if (!childob.getString("username").equalsIgnoreCase(email)) {
                                        messages.add(Pgp.decrypt(childob.getString("message"),email));
                                        username.add(childob.getString("username"));
                                        p = 2;
                                    }
                                }
                                inilen = len;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                    if (k == 0) {
                        c = new chat_adapter(TalkActivity.this, R.layout.sent, messages, username, email);
                        ls.setAdapter(c);
                        k = 2;
                        ls.setSelection(c.getCount() - 1);
                    } else {
                        if (p != 0) {
                            c.notifyDataSetChanged();
                            ls.smoothScrollToPosition(c.getCount() - 1);
                        }
                    }
                } else {
                    ref.setValue("{'chat':[]}");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void start() {
        Chronometer chronome = (Chronometer) findViewById(R.id.chronometer);
        chronome.setBase(SystemClock.elapsedRealtime());
        RequestQueue q = Volley.newRequestQueue(this);
        chrono = new chronometer(chronome,q,email,password);
        chrono.start();
        cross = 1;
    }

    public void stop() {
        chrono.stop();
    }
}