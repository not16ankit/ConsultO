/*package app.consulto.extras;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import app.consulto.R;

public class list_adapter extends ArrayAdapter
{
    private String[] parenttitles1;
    private String[][] childtitles1;
    private String[][] childtitles2;
    private ImageLoader queue;
    private int k = 0;
    private FragmentManager fm;
    private String[][] childtitles3;
    private RequestQueue qu;
    private Bitmap[] arr;
    private Handler ih;
    private String[][] childtitles4;
    private Thread t;
    private Context c;
    private String[] ratings;
    private String name;
    private String[] enrollments;
    public list_adapter(String[] rating,String[] enrolls,FragmentManager fragment,Handler ik,@Nullable Context con,ImageLoader q,int num,String[] parenttitles,String[][] child1,String[][] child2,String[][] child3,String[][] child4,String naam) {
        super(con,num);
        childtitles1 = child1;
        queue = q;
        enrollments = enrolls;
        name = naam;
        ratings = rating;
        c = con;
        fm = fragment;
        ih = ik;
        childtitles2 = child2;
        childtitles3 = child3;
        childtitles4 = child4;
        parenttitles1 = parenttitles;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @SuppressLint("WrongConstant")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        list_holder holder;
        if (convertView == null) {
            holder = new list_holder();
            if(position==0) {
                convertView = LayoutInflater.from(c).inflate(app.consulto.R.layout.hellow_view, null);
                holder.title = convertView.findViewById(R.id.parenttitle);
                        holder.title.setText("Hello "+name+"!");
            }
            else if(position==1)
            {
                convertView = LayoutInflater.from(c).inflate(app.consulto.R.layout.gift_view, null);
            }
            else {
                convertView = LayoutInflater.from(c).inflate(app.consulto.R.layout.listview, null);
                holder.title = convertView.findViewById(R.id.parenttitle);
                        holder.linearlay = new LinearLayoutManager(c, LinearLayout.HORIZONTAL, false);
                holder.recycler = convertView.findViewById(app.consulto.R.id.recycler);
                        holder.viewall = convertView.findViewById(R.id.viewall);
                        holder.title.setText(parenttitles1[position]);
            }
        } else {
            holder = (list_holder) convertView.getTag();
        }
        if (position == 3) {
            holder.viewall.setVisibility(View.VISIBLE);
            holder.viewall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message m = new Message();
                    Bundle d = new Bundle();
                    d.putString("id", "viewall");
                    m.setData(d);
                    ih.sendMessage(m);
                }
            });
        }
        int oo = 0;
        switch (position) {
            case 2:
                oo = 1;
                holder.readapt = new recycle_adapter(
                        enrollments,
                        ratings,
                        ih,
                        childtitles1,
                        c,
                        position,
                        queue
                );
                break;
            case 3:
                oo = 1;
                holder.readapt = new  recycle_adapter(
                        enrollments,
                        ratings,
                        ih,
                        childtitles2,
                        c,
                        position,
                        queue
                );
                break;
            case 4:
                oo = 1;
                holder.readapt = new  recycle_adapter(
                        enrollments,
                        ratings,
                        ih,
                        childtitles3,
                        c,
                        position,
                        queue
                );
                break;
            case 5:
                oo = 1;
                holder.readapt = new recycle_adapter(
                        enrollments,
                        ratings,
                        ih,
                        childtitles4,
                        c,
                        position,
                        queue
                );
                break;
        }
        if(oo==1) {
            holder.recycler.setLayoutManager(holder.linearlay);
            holder.recycler.getRecycledViewPool().setMaxRecycledViews(0, 0);
            holder.recycler.setAdapter(holder.readapt);
        }
        convertView.setTag(holder);
        return convertView;
    }
}*/
