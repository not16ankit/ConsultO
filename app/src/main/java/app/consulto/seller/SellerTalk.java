package app.consulto.seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.kibotu.pgp.Pgp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.spongycastle.openpgp.PGPException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.consulto.R;
import app.consulto.TalkActivity;
import app.consulto.extras.chat_adapter;
import app.consulto.extras.chronometer;

public class SellerTalk extends AppCompatActivity {
    private String id = "";
    private String to = "";
    private int len = 0;
    private String email = "";
    private String password = "";
    private int inilen = 0;
    private chat_adapter c;
    private int p = 0;
    private String premessages = "";
    private int k = 0;
    private DatabaseReference ref;
    private String pubkey = "";
    private RequestQueue queue;
    private String privkey = "";
    private FirebaseDatabase fire;
    private DatabaseReference attendingref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_talk);
        Bundle b = getIntent().getExtras();
        getWindow().setStatusBarColor(getResources().getColor(R.color.blue));
        id = b.getString("id");
        to = b.getString("to");
        email = b.getString("email");
        queue = Volley.newRequestQueue(this);
        password = b.getString("password");
        fire = FirebaseDatabase.getInstance();
        attendingref = fire.getReference("attending"+id);
        attendingref.setValue(to);
        get_keys();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        attendingref.setValue("");
    }

    public void get_keys()
{
    StringRequest sr = new StringRequest(Request.Method.POST, "http://consulto.oromap.in/get_keys.php", new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject object = new JSONObject(response);
                pubkey = object.getString("pubkey");
                privkey = object.getString("privkey");
                get_chats();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
        }
    })
    {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map hash = new HashMap<String,String>();
            hash.put("email",email);
            hash.put("password",password);
            hash.put("query",to);
            return hash;
        }
    };
    queue.add(sr);
}
    @Override
    public void onBackPressed() {
        finish();
    }

    public void get_chats() {
        Pgp.setPublicKey(pubkey);
        Pgp.setPrivateKey(privkey);
        len = 0;
        inilen = 0;
        final ArrayList<String> messages = new ArrayList<String>();
        final ArrayList<String> username = new ArrayList<String>();
        p = 0;
        k = 0;
        final ListView ls = (ListView) findViewById(R.id.chat_list);
        ImageView im = (ImageView) findViewById(R.id.send);
        final EditText messaget = (EditText) findViewById(R.id.message);
        messaget.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ls.setSelection(c.getCount() - 1);
            }
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        char[] car = {'.', '$', '#', '[', ']'};
        String forusemeial = to;
        for (int io = 0; io < car.length; io++) {
            forusemeial = forusemeial.replace(String.valueOf(car[io]), "");
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
                                messages.add(Pgp.decrypt(childob.getString("message"), to));
                                username.add(childob.getString("username"));
                            }
                        } else {
                            if (inilen != len) {
                                for (int i = inilen; i < len; i++) {
                                    childob = arr.getJSONObject(i);
                                    if (!childob.getString("username").equalsIgnoreCase(email)) {
                                        messages.add(Pgp.decrypt(childob.getString("message"), to));
                                        username.add(childob.getString("username"));
                                        p = 2;
                                    }
                                }
                                inilen = len;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage()+"decrypt", Toast.LENGTH_LONG).show();
                    }
                    if (k == 0) {
                        c = new chat_adapter(SellerTalk.this, R.layout.sent, messages, username, email);
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
}
