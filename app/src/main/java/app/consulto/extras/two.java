package app.consulto.extras;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;
import org.spongycastle.cert.ocsp.Req;

import app.consulto.R;
import app.consulto.griddetails;
import app.consulto.gridet;
import app.consulto.grids;

public class two extends Fragment {
    private Bundle[] bun;
    private int buncount = 1;
    private RequestQueue q;
    private mysubsadapter adap;
    private String email;
    private String password;
    private String id[];
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_subscriptions,container,false);
        ListView ls  = (ListView)v.findViewById(R.id.sub_list);
        bun = new Bundle[id.length];
        Handler j =new Handler()
        {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
            }
        };
        ImageLoader il = new ImageLoader(q, new ImageLoader.ImageCache() {
            LruCache<String,Bitmap> cache = new LruCache<String,Bitmap>(id.length);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url,bitmap);
            }
        });
        adap = new mysubsadapter(getContext(),R.layout.subscription_listview,id,j,il,bun);
        get_bundles();
        ls.setAdapter(adap);
        return v;
    }
    public two(String ids[],RequestQueue qe,String em,String pas)
    {
        this.email = em;
        this.password = pas;
        this.q = qe;
        this.id = ids;
    }
    private void get_bundles()
    {
        gridet ob = new gridet();
        ob.gri = new griddetails() {
            @Override
            public void griddetails(@NotNull String name, @NotNull String dess, @NotNull String rating, @NotNull String[][] reviews) {
                Bundle b = new Bundle();
                b.putString("title",name);
                b.putString("id", String.valueOf(buncount));
                bun[buncount-1] = b;
                buncount++;
                if(buncount<=id.length)
                {
                    get_bundles();
                }
                else
                {
                    adap.notifyDataSetChanged();
                    buncount = 1;
                }
            }
        };
        ob.get_details(String.valueOf(buncount),email,password, q,getContext());
    }
}
