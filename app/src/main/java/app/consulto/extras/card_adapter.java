package app.consulto.extras;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

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

import org.spongycastle.cert.ocsp.Req;
import org.w3c.dom.Text;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import app.consulto.R;
import app.consulto.all_in;
import jp.wasabeef.blurry.Blurry;

public class card_adapter extends ArrayAdapter
{
 static public int u = 0;
    static public int p = 0;
    private Context con;
    private Handler handler;
    private float x;
    private float y;
    private float inix;
    private ImageLoader imgloader;
    ViewHolder holder;
    private float iniy;
    private String[] catis;
    public card_adapter(@NonNull Context context, int resource,Handler j,String[] cats,ImageLoader im) {
        super(context, resource);
        this.con = context;
        this.imgloader = im;
        this.handler = j;
this.catis = cats;
u= 0;
    }

    @Override
    public int getCount() {
        return catis.length/2;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Drawable d = null;
        Drawable d2 = null;
        int ran = new Random().nextInt(10);
            switch (ran) {
                case 0:
                    d = con.getResources().getDrawable(R.drawable.card_changer1);
                    break;
                case 1:
                    d = con.getResources().getDrawable(R.drawable.card_changer2);
                    break;
                case 2:
                    d = con.getResources().getDrawable(R.drawable.card_changer3);
                    break;
                case 3:
                    d = con.getResources().getDrawable(R.drawable.card_changer4);
                    break;
                case 4:
                    d = con.getResources().getDrawable(R.drawable.card_changer5);
                    break;
                case 5:
                    d = con.getResources().getDrawable(R.drawable.card_changer6);
                    break;
                case 6:
                    d = con.getResources().getDrawable(R.drawable.card_changer7);
                    break;
                case 7:
                    d = con.getResources().getDrawable(R.drawable.card_changer8);
                    break;
                case 8:
                    d = con.getResources().getDrawable(R.drawable.card_changer9);
                    break;
                case 9:
                    d = con.getResources().getDrawable(R.drawable.card_changer10);
                    break;
            }
        ran = new Random().nextInt(10);
        switch (ran) {
            case 0:
                d2 = con.getResources().getDrawable(R.drawable.card_changer1);
                break;
            case 1:
                d2 = con.getResources().getDrawable(R.drawable.card_changer2);
                break;
            case 2:
                d2 = con.getResources().getDrawable(R.drawable.card_changer3);
                break;
            case 3:
                d2 = con.getResources().getDrawable(R.drawable.card_changer4);
                break;
            case 4:
                d2= con.getResources().getDrawable(R.drawable.card_changer5);
                break;
            case 5:
                d2 = con.getResources().getDrawable(R.drawable.card_changer6);
                break;
            case 6:
                d2 = con.getResources().getDrawable(R.drawable.card_changer7);
                break;
            case 7:
                d2 = con.getResources().getDrawable(R.drawable.card_changer8);
                break;
            case 8:
                d2 = con.getResources().getDrawable(R.drawable.card_changer9);
                break;
            case 9:
                d2 = con.getResources().getDrawable(R.drawable.card_changer10);
                break;
        }
        Context conn = getContext();
        if(convertView==null)
        {
            convertView = LayoutInflater.from(con).inflate(R.layout.all_in_listview,null);
            holder = new ViewHolder();
            holder.card1=    convertView.findViewById(R.id.cards1);
            holder.card2 = convertView.findViewById(R.id.cards2);
            holder.back1 = convertView.findViewById(R.id.back1);
            holder.back2= convertView.findViewById(R.id.back2);
            holder.tit1 = holder.card1.findViewById(R.id.title1);
            holder.queue = Volley.newRequestQueue(con);
            holder.tit2 = holder.card2.findViewById(R.id.title2);
            holder.icon1 = holder.card1.findViewById(R.id.icon1);
            holder.icon2 = holder.card2.findViewById(R.id.icon2);
            holder.cardback1 = convertView.findViewById(R.id.cardback1);
            holder.cardback2 = convertView.findViewById(R.id.cardback2);
            holder.handler = handler;
            holder.ini(convertView);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        final ViewGroup forblur1 = (ViewGroup) convertView.findViewById(R.id.forblur1);
       final ViewGroup forblur2 = (ViewGroup) convertView.findViewById(R.id.forblur2);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Blurry.with(con)
                            .radius(20)
                            .sampling(2)
                            .async()
                            .animate()
                            .onto(forblur1);
                    Blurry.with(con)
                            .radius(25)
                            .sampling(2)
                            .async()
                            .animate()
                            .onto(forblur2);
                }
            }).start();
        holder.back1.setBackground(d);
        holder.back2.setBackground(d2);
        final  AnimationDrawable ad2 = (AnimationDrawable)holder.back2.getBackground();
        ad2.setEnterFadeDuration(400);
        ad2.setExitFadeDuration(1000);
        ad2.setOneShot(true);
 inix = holder.back2.getScaleX();
        iniy = holder.back2.getScaleY();
        x = inix+0.04f;
        y = iniy+0.04f;
        final View k = convertView;
        holder.card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.back2.setElevation(20);
                ad2.start();
                holder.back2.animate().setDuration(2000).scaleX(inix).scaleY(iniy).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                       Message m = new Message();
                        Bundle b =new Bundle();
                        b.putInt("posi",position);
                        b.putInt("item",1);
                        m.setData(b);
                        all_in.card = k;
                        handler.sendMessage(m);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
            }
        });
        final  AnimationDrawable ad1 = (AnimationDrawable)holder.back1.getBackground();
        ad1.setEnterFadeDuration(400);
        ad1.setExitFadeDuration(1000);
        ad1.setOneShot(true);
        inix = holder.back1.getScaleX();
        iniy = holder.back1.getScaleY();
        x = inix+0.04f;
        y = iniy+0.04f;
        holder.card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.back1.setElevation(20);
                ad1.start();
                holder.back1.animate().setDuration(2000).scaleX(inix).scaleY(iniy).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                       Message m = new Message();
                        Bundle b =new Bundle();
                        b.putInt("posi",position);
                        b.putInt("item",2);
                        m.setData(b);
                        all_in.card = k;
                        handler.sendMessage(m);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
            }
        });
        holder.tit1.setText(catis[position+u]);
        holder.tit2.setText(catis[position+(u+1)]);
        holder.icon1.setImageUrl("https://kikplus.000webhostapp.com/resources/icons/"+(position+u)+"icon.png",imgloader);
        holder.icon2.setImageUrl("https://kikplus.000webhostapp.com/resources/icons/"+(position+u+1)+"icon.png",imgloader);
        u=u+1;
          convertView.startAnimation(AnimationUtils.loadAnimation(con, R.anim.list_anim));
        convertView.setTag(holder);
        return convertView;
    }
    private static class ViewHolder
    {
        View back1;
        View back2;
        View card1;
        View card2;
        TextView tit1;
        RequestQueue queue;
        TextView tit2;
        private float x;
        private float y;
        ImageView cardback1;
        ImageView cardback2;
        Handler handler;
        private float inix;
        private float iniy;
        NetworkImageView icon1;
        NetworkImageView icon2;
        public void ini(final View k)
        {
            icon1.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if(icon1.getDrawable()!=null)
                    {
                        card1.findViewById(R.id.av1).setVisibility(View.INVISIBLE);
                    }
                }
            });
            icon2.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if(icon2.getDrawable()!=null)
                    {
                        card2.findViewById(R.id.av2).setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }
}
interface in
{
    public void in();
}
