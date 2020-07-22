package app.consulto.extras;

import android.widget.Chronometer;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class chronometer {
    private Chronometer c;
    private int secs=0;
    private String email = "";
    private String password = "";
    private RequestQueue queue;
    private int tempsecs = 0;
    private StringRequest srr;
    public chronometer(Chronometer chronometer, RequestQueue q,String em,String pas)
    {
        this.email = em;
        this.password = pas;
        this.queue = q;
        this.c = chronometer;
       srr = new StringRequest(Request.Method.POST, "http://consulto.oromap.in/plus.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("200")) {
                    Toast.makeText(c.getContext(),"10Rs",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map hash = new HashMap<String, String>();
                hash.put("email", email);
                hash.put("password", password);
                return hash;
            }
        };
    }
    public void start()
    {
        c.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                secs = secs+1;
                if((secs%60)==0)
                {
                    queue.add(srr);
                }
            }
        });
        c.start();
    }
    public void stop()
    {
        c.stop();
    }
}
