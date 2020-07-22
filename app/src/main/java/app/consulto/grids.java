package app.consulto;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paytm.pg.merchant.PaytmChecksum;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.wang.avi.AVLoadingIndicatorView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.spongycastle.cert.ocsp.Req;
import org.spongycastle.jcajce.util.MessageDigestUtils;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import app.consulto.extras.*;
import jp.wasabeef.blurry.Blurry;

public class grids extends FragmentActivity {
    static public ViewPager2 lay;
    static public String[] enrollments;
    static public String[] ratings;
    static public int p = 0;
    static public mysubsadapter c;
    static public String mysubscriptions[];
    static public View forremoval;
    static public int ii = 0;
    static public int buncount = 1;
    static public ProgressDialog pd;
    static public ListView ls;
    static public String unicode = "";
    static public RequestQueue queue;
    static public String name="";
    static public String notgood= "";
    static public int verified=0;
    static public DatabaseReference ref;
    static public int type=0;
    static public int lastsynced = 0;
    static public String[][] cat1;
    static public int k = 0;
    static public String[][] cat2;
public static int bundone = 0;
    static public AlertDialog ab;
    static public String[] parenttitles;
    static public AlertDialog ad;
    static public int len=0;
    static public Context con;
static public list_adapter adapter;
    static public String premessages="";
    static public String email="";
    static public Handler logouthandler;
    static public GridView list;
    static public ArrayList<String> messages;
    static public ArrayList<String> username;
    static public View google_view;
    static public BottomNavigationView vie;
    static public View instagram_view;
    static public View normal_view;
    static public int inilen =1;
    static public ImageLoader imageLoader;
    static public String password="";
    static public String privkey = "";
    static public String pubkey = "";
    static public String money = "";
    static public String[][] cat3;
    static public String[][] cat4;
static public  Handler itemhandler;
private int num=0;
static public String usern = "";
static public String[][] reviews;
static public   Bundle[] bun;
public static int o  =0 ;
static public int done = 0;

    public grids() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            getWindow().setStatusBarColor(getResources().getColor(R.color.background));
        }
        setContentView(R.layout.activity_grids);
        getWindow().setStatusBarColor(getResources().getColor(R.color.green));
        queue = Volley.newRequestQueue(this);
        getWindow().setStatusBarColor(getResources().getColor(R.color.back));
        Bundle bun = getIntent().getExtras();
        assert bun != null;
        con = grids.this;
        email = bun.getString("email");
        password = bun.getString("password");
        usern = email;
        vie = (BottomNavigationView)findViewById(R.id.nav_view);
        vie.setClickable(false);
        pd = new ProgressDialog(this,R.style.slert);
        pd.setMessage("Connecting to Paytm.....");
        pd.setCancelable(false);
        lay=(ViewPager2)findViewById(R.id.container);
        lay.setOffscreenPageLimit(1);
        lay.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position)
                {
                    case 0:
                        vie.setSelectedItemId(R.id.home);
                        break;
                    case 1:
                        vie.setSelectedItemId(R.id.wallet);
                        break;
                    case 2:
                        vie.setSelectedItemId(R.id.chat);
                        break;
                    case 3:
                        vie.setSelectedItemId(R.id.noti);
                        break;
                    case 4:
                        vie.setSelectedItemId(R.id.acc);
                        break;
                }
            }
        });
        normal_view = getLayoutInflater().inflate(R.layout.normal,null);
        google_view = getLayoutInflater().inflate(R.layout.google,null);
        instagram_view = getLayoutInflater().inflate(R.layout.instagram,null);
        ini();
    }
    public void logout()
    {
        new File(getFilesDir(),"username.txt").delete();
        new File(getFilesDir(),"password.txt").delete();
        finish();
    }

    private void ini()
    {
        final Bundle b = new Bundle();
        b.putString("email",email);
        b.putString("password",password);
        itemhandler = new Handler()
        {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                final String id = msg.getData().getString("id").trim();
                if(id.equalsIgnoreCase("viewall"))
                {
                    Intent i = new Intent(getApplicationContext(),all_in.class);
                    i.putExtras(b);
                    startActivity(i);
                }
                else {
                    final View v = getLayoutInflater().inflate(R.layout.des,null);
                    BottomSheetDialog di =new BottomSheetDialog(grids.this,R.style.bag);
                    di.setContentView(v);
                    di.show();
                    final TextView title = (TextView) v.findViewById(R.id.title);
                    final TextView des = (TextView) v.findViewById(R.id.description);
                    final RatingBar ratingbar = (RatingBar)v.findViewById(R.id.rating);
                    final AVLoadingIndicatorView av = (AVLoadingIndicatorView)v.findViewById(R.id.avav);
                    final ViewSwitcher vp = (ViewSwitcher) v.findViewById(R.id.vp);
                    final Button paybut = (Button)v.findViewById(R.id.pay);
                    final LottieAnimationView icon = (LottieAnimationView) v.findViewById(R.id.icon);
                    icon.setAnimationFromUrl("https://kikplus.000webhostapp.com/resources/"+id+".json");
                    icon.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            v.findViewById(R.id.loading).setVisibility(View.INVISIBLE);
                        }
                    });
                    av.setVisibility(View.VISIBLE);
                    ratingbar.setVisibility(View.INVISIBLE);
                    title.setText("");
                    des.setText("");
                    ratingbar.setRating(0f);
                                gridet ob = new gridet();
                                ob.gri = new griddetails() {
                                    @Override
                                    public void griddetails(@NotNull final String name, @NotNull String dess, @NotNull String rating, @NotNull final String[][] reviews) {
                                        v.findViewById(R.id.parenttobegone).setVisibility(View.VISIBLE);
                                        av.setVisibility(View.INVISIBLE);
                                        title.setText(name);
                                        des.setText(dess);
                                        icon.playAnimation();
                                        paybut.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                    Toast.makeText(getApplicationContext(),"Pay",Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        ratingbar.setRating(Float.parseFloat(rating)/5);
                                        ratingbar.setVisibility(View.VISIBLE);
                                        vp.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_reviews));
                                        vp.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_reviews));
                                        ii=0;
                                        TextView textView = vp.getCurrentView().findViewById(R.id.tip);
                                        TextView nameet = vp.getCurrentView().findViewById(R.id.name);
                                        textView.setText(reviews[0][ii]);
                                        nameet.setText("- "+reviews[1][ii]);
                                        ii=ii+1;
                                        final Handler h =new Handler()
                                        {
                                            @Override
                                            public void handleMessage(@NonNull Message msg) {
                                                super.handleMessage(msg);
                                                if(reviews[0].length>ii) {
                                                    vp.showNext();
                                                    TextView t = vp.getCurrentView().findViewById(R.id.tip);
                                                    TextView tt = vp.getCurrentView().findViewById(R.id.name);
                                                    t.setText(reviews[0][ii]);
                                                    tt.setText("- "+reviews[1][ii]);
                                                    ii++;
                                                }
                                                else
                                                {
                                                    ii=0;
                                                }
                                            }
                                        };
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                while (true)
                                                {
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
                                ob.get_details(id,usern,password,queue,getApplicationContext());
                }
            }
        };
        Intent k = new Intent(this,settings.class);
        logouthandler = new Handler()
        {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
               int m = msg.getData().getInt("code");
                if(m==0)
               {
                   logout();
               }
               else
               {
                   startActivity(k);
               }
            }
        };
        vie.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(done==1) {
                    switch (item.getItemId()) {
                        case R.id.home:
                            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                            {
                                getWindow().setStatusBarColor(getResources().getColor(R.color.background));
                            }
                            lay.setCurrentItem(0);
                            break;
                        case R.id.chat:
                            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                            {
                                getWindow().setStatusBarColor(getResources().getColor(R.color.background));
                            }
                            lay.setCurrentItem(2);
                            break;
                        case R.id.noti:
                            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                            {
                                getWindow().setStatusBarColor(getResources().getColor(R.color.background));
                            }
                            lay.setCurrentItem(3);
                            break;
                        case R.id.acc:
                            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                            {
                                getWindow().setStatusBarColor(getResources().getColor(R.color.background));
                            }
                            lay.setCurrentItem(4);
                            break;
                        case R.id.wallet:
                            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                            {
                                getWindow().setStatusBarColor(getResources().getColor(R.color.foreground));
                            }
                            lay.setCurrentItem(1);
                            break;
                    }
                }
                return true;
            }
        });
       getgrids();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==44&&Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
        if (checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this,"Restart Transaction",Toast.LENGTH_LONG).show();
        }
        }
    }
    private void getgrids()
    {
        StringRequest sr = new StringRequest(Request.Method.POST, "http://consulto.oromap.in/grids.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response.trim());
                    JSONArray child;
                    JSONObject childob;
                    num = Integer.parseInt(object.getString("num"));
                     parenttitles = new String[num+1];
                    int u = 1;
                    for(int i=1;i<=num;i++)
                    {
                        parenttitles[u]=object.getString(String.valueOf(i));
                        u++;
                        child = object.getJSONArray(String.valueOf(i)+"child");
                        switch (i)
                        {
                        case 1:
                                cat1 = new String[2][child.length()];
                                for(int s=0;s<child.length();s++)
                                {
                                    childob = child.getJSONObject(s);
                                    cat1[0][s]=childob.getString("title");
                                    cat1[1][s]=childob.getString("id");
                                }
                                break;
                            case 2:
                                cat2 = new String[2][child.length()];
                                for(int s=0;s<child.length();s++)
                                {
                                    childob = child.getJSONObject(s);
                                    cat2[0][s]=childob.getString("title");
                                    cat2[1][s]=childob.getString("id");
                                }
                                break;
                            case 3:
                                cat3 = new String[2][child.length()];
                                enrollments = new String[child.length()];
                                for(int s=0;s<child.length();s++)
                                {
                                    childob = child.getJSONObject(s);
                                    cat3[0][s]=childob.getString("title");
                                    cat3[1][s]=childob.getString("id");
                                    enrollments[s] = childob.getString("enrollments");
                                }
                                break;
                            case 4:
                                cat4 = new String[2][child.length()];
                               ratings = new String[child.length()];
                                for(int s=0;s<child.length();s++)
                                {
                                    childob = child.getJSONObject(s);
                                    cat4[0][s]=childob.getString("title");
                                    cat4[1][s]=childob.getString("id");
                                    ratings[s] = childob.getString("rating");
                                }
                                break;
                        }
                    }
                    get_info();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hash = new HashMap<String,String>();
                hash.put("email",email);
                hash.put("password",password);
                return hash;
            }
        };
        queue.add(sr);
    }
    private void get_info()
    {
        StringRequest sr = new StringRequest(Request.Method.POST, "http://consulto.oromap.in/aftersignin.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response.trim());
                    name = object.getString("name");
                    verified = Integer.parseInt(object.getString("verified"));
                    type = Integer.parseInt(object.getString("type"));
                    privkey = object.getString("privkey");
                    pubkey = object.getString("pubkey");
                    money = object.getString("wallet");
                    JSONArray arr = object.getJSONArray("mysubscriptions");
                    JSONObject child_ob;
                    mysubscriptions = new String[arr.length()];
                    for(int i=0;i<arr.length();i++)
                    {
                        child_ob = arr.getJSONObject(i);
                        mysubscriptions[i] = child_ob.getString("id");
                    }
                    done = 1;
                    main_pager adap = new main_pager(grids.this);
                    lay.setAdapter(adap);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hash = new HashMap<String,String>();
                hash.put("email",email);
                hash.put("password",password);
                return hash;
            }
        };
        queue.add(sr);
    }
}

