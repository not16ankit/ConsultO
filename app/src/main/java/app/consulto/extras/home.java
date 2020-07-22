package app.consulto.extras;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.toolbox.ImageLoader;

import app.consulto.R;
import app.consulto.grids;

public class home extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.home,container,false);
        ImageLoader loader = new ImageLoader(grids.queue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> c = new LruCache<String,Bitmap>(10);
            @Override
            public Bitmap getBitmap(String url) {
                return c.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                c.put(url,bitmap);
            }
        });
        GridView list = (GridView)view.findViewById(R.id.list);
        list_adapter adapter = new list_adapter(grids.ratings,grids.enrollments,grids.itemhandler,grids.con,loader,R.layout.listview,grids.parenttitles,grids.cat1,grids.cat2,grids.cat3,grids.cat4,grids.name);
        list.setAdapter(adapter);
        return view;
    }
}