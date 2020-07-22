package app.consulto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.consulto.extras.card_adapter;

public class all_in extends AppCompatActivity {
    private BottomSheetBehavior bs;
    private int ii = 0;
    private Handler itemhandler;
    static private int y = 0;
    private RequestQueue queue;
    private String email = "";
    private String password = "";
    private int o =0;
    private String[] ids;
    private app.consulto.extras.card_adapter c;
    static public View card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            getWindow().setStatusBarColor(getResources().getColor(R.color.background));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_in);
        getWindow().setStatusBarColor(getResources().getColor(R.color.back));
        getSupportActionBar().hide();
        Bundle b = getIntent().getExtras();
        email = b.getString("email");
        password = b.getString("password");
        ini();
        queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://consulto.oromap.in/allcats.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                inilist(response.trim());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Erorr", Toast.LENGTH_LONG).show();
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
        queue.add(sr);
    }

    private void ini() {
        itemhandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                int position = msg.getData().getInt("posi")+1;
                int card = msg.getData().getInt("item");
                String id = ids[(2*position)-card];
                final View v = getLayoutInflater().inflate(R.layout.des, null);
                BottomSheetDialog di = new BottomSheetDialog(all_in.this, R.style.bag);
                di.setContentView(v);
                di.show();
                final TextView title = (TextView) v.findViewById(R.id.title);
                final TextView des = (TextView) v.findViewById(R.id.description);
                final RatingBar ratingbar = (RatingBar) v.findViewById(R.id.rating);
                final AVLoadingIndicatorView av = (AVLoadingIndicatorView) v.findViewById(R.id.avav);
                final ViewSwitcher vp = (ViewSwitcher) v.findViewById(R.id.vp);
                av.setVisibility(View.VISIBLE);
                ratingbar.setVisibility(View.INVISIBLE);
                title.setText("");
                des.setText("");
                ratingbar.setRating(0f);
                gridet ob = new gridet();
                ob.gri = new griddetails() {
                    @Override
                    public void griddetails(@NotNull String name, @NotNull String dess, @NotNull String rating, @NotNull final String[][] reviews) {
                        v.findViewById(R.id.parenttobegone).setVisibility(View.VISIBLE);
                        av.setVisibility(View.INVISIBLE);
                        title.setText(name);
                        des.setText(dess);
                        ratingbar.setRating(Float.parseFloat(rating) / 5);
                        ratingbar.setVisibility(View.VISIBLE);
                        vp.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_reviews));
                        vp.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_reviews));
                        ii = 0;
                        TextView textView = vp.getCurrentView().findViewById(R.id.tip);
                        TextView nameet = vp.getCurrentView().findViewById(R.id.name);
                        textView.setText(reviews[0][ii]);
                        nameet.setText("- " + reviews[1][ii]);
                        ii = ii + 1;
                        final Handler h = new Handler() {
                            @Override
                            public void handleMessage(@NonNull Message msg) {
                                super.handleMessage(msg);
                                if (reviews[0].length > ii) {
                                    vp.showNext();
                                    TextView t = vp.getCurrentView().findViewById(R.id.tip);
                                    TextView tt = vp.getCurrentView().findViewById(R.id.name);
                                    t.setText(reviews[0][ii]);
                                    tt.setText("- " + reviews[1][ii]);
                                    ii++;
                                } else {
                                    ii = 0;
                                }
                            }
                        };
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (true) {
                                    try {
                                        Thread.sleep(5000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    h.sendEmptyMessage(0);
                                }
                            }
                        }).start();
                    }
                };
                ob.get_details(id, email, password, queue, getApplicationContext());
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void inilist(final String res) {
        try {
            JSONObject ob = new JSONObject(res);
            JSONArray arr = ob.getJSONArray("cats");
            String[] cats = new String[arr.length()];
            ids = new String[arr.length()];
            JSONObject jsonob;
            for (int i = 0; i < arr.length(); i++) {
                jsonob = arr.getJSONObject(i);
                cats[i] = jsonob.getString("title");
                ids[i] = jsonob.getString("id");
            }
            Toast.makeText(this, ids[7], Toast.LENGTH_LONG).show();
            ImageLoader im = new ImageLoader(queue, new ImageLoader.ImageCache() {
                private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(8);

                @Override
                public Bitmap getBitmap(String url) {
                    return mCache.get(url);
                }

                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                    mCache.put(url, bitmap);
                }
            });
            c = new card_adapter(getApplicationContext(), R.layout.all_in_listview, itemhandler, cats, im);
            ListView ks = (ListView) findViewById(R.id.all_in_list);
            ks.setAdapter(c);
        } catch (Exception e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
interface k
{
    public void k();
}
