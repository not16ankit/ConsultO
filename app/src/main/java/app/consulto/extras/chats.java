package app.consulto.extras;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.toolbox.ImageLoader;

import org.jetbrains.annotations.NotNull;

import app.consulto.R;
import app.consulto.TalkActivity;
import app.consulto.griddetails;
import app.consulto.gridet;
import app.consulto.grids;

public class chats extends Fragment
{
    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = getLayoutInflater().inflate(R.layout.chats,container,false);
        get_chats();
        return view;
    }
    private void get_chats()
    {
        grids.imageLoader = new ImageLoader(grids.queue, new ImageLoader.ImageCache() {
            LruCache<String, Bitmap> cahce=  new LruCache<String,Bitmap>(grids.mysubscriptions.length);
            @Override
            public Bitmap getBitmap(String url) {
                return cahce.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cahce.put(url,bitmap);
            }
        });
        final Intent i = new Intent(grids.con, TalkActivity.class);
        Handler h = new Handler()
        {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                i.putExtras(msg.getData());
                startActivity(i);
            }
        };
        grids.bun = new Bundle[grids.mysubscriptions.length];
        ListView ls = (ListView)view.findViewById(R.id.chat_list);
        grids.c = new mysubsadapter(grids.con,R.layout.enrolled_listview,grids.mysubscriptions,h,grids.imageLoader,grids.bun);
        get_bundles();
        ls.setAdapter(grids.c);
    }
    private void get_bundles()
    {
        gridet ob = new gridet();
        ob.gri = new griddetails() {
            @Override
            public void griddetails(@NotNull String name, @NotNull String dess, @NotNull String rating, @NotNull String[][] reviews) {
                Bundle b = new Bundle();
                b.putString("title",name);
                b.putString("id", String.valueOf(grids.buncount));
                b.putString("email", grids.email);
                b.putString("password", grids.password);
                b.putString("pubkey", grids.pubkey);
                b.putString("privkey", grids.privkey);
                grids.bun[grids.buncount-1] = b;
                grids.buncount++;
                if(grids.buncount<=grids.mysubscriptions.length)
                {
                    get_bundles();
                }
                else
                {
                    grids.c.notifyDataSetChanged();
                    grids.buncount = 1;
                }
            }
        };
        ob.get_details(String.valueOf(grids.buncount),grids.email,grids.password, grids.queue,grids.con);
    }
}

